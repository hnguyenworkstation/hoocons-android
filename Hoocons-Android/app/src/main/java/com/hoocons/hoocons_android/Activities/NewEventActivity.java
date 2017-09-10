package com.hoocons.hoocons_android.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.beardedhen.androidbootstrap.font.FontAwesome;
import com.birbit.android.jobqueue.JobManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hoocons.hoocons_android.Adapters.ImageLoaderAdapter;
import com.hoocons.hoocons_android.CustomUI.AdjustableImageView;
import com.hoocons.hoocons_android.CustomUI.CustomFlowLayout;
import com.hoocons.hoocons_android.CustomUI.CustomTextView;
import com.hoocons.hoocons_android.EventBus.FriendModeRequest;
import com.hoocons.hoocons_android.EventBus.InvalidLocationRequest;
import com.hoocons.hoocons_android.EventBus.LocationRequestCollected;
import com.hoocons.hoocons_android.EventBus.PrivateModeRequest;
import com.hoocons.hoocons_android.EventBus.PublicModeRequest;
import com.hoocons.hoocons_android.EventBus.WarningContentRequest;
import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Helpers.MapUtils;
import com.hoocons.hoocons_android.Helpers.PermissionManager;
import com.hoocons.hoocons_android.Helpers.PermissionUtils;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.Networking.Requests.LocationRequest;
import com.hoocons.hoocons_android.Parcel.EventParcel;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.Tasks.Jobs.GetPlaceAttributesByLatLongJob;
import com.hoocons.hoocons_android.Tasks.Jobs.PostNewEventJob;
import com.hoocons.hoocons_android.Tasks.Jobs.ShareNewEventJob;
import com.hoocons.hoocons_android.ViewFragments.EventModeSheetFragment;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.vstechlab.easyfonts.EasyFonts;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import me.iwf.photopicker.PhotoPicker;
import xyz.klinker.giphy.Giphy;
import xyz.klinker.giphy.GiphyActivity;

public class NewEventActivity extends BaseActivity
        implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,
            GoogleApiClient.ConnectionCallbacks, LocationEngineListener{
    @BindView(R.id.action_back)
    ImageButton mBack;
    @BindView(R.id.action_post)
    Button mPostBtn;
    @BindView(R.id.display_name)
    TextView mDisplayName;
    @BindView(R.id.event_add_photo)
    ImageView mAddPhotoBtn;
    @BindView(R.id.event_add_video)
    ImageView mAddVideoBtn;
    @BindView(R.id.event_add_location)
    ImageView mAddLocationBtn;
    @BindView(R.id.event_add_sound)
    ImageView mAddSoundBtn;
    @BindView(R.id.event_add_gif)
    ImageView mAddGifBtn;

    @BindView(R.id.new_event_text_content)
    EditText mTextContentInput;
    @BindView(R.id.post_event_profile)
    ImageView mProfileImage;
    @BindView(R.id.event_privacy)
    BootstrapButton mPrivacyBtn;
    @BindView(R.id.event_location)
    BootstrapButton mEventLocation;
    @BindView(R.id.event_warning)
    BootstrapButton mWarningButton;

    @BindView(R.id.posting_as)
    TextView mPostingAsQuestion;

    @BindView(R.id.type_picker)
    RelativeLayout mTypePicker;
    @BindView(R.id.type_logo)
    ImageView mTypeLogoImage;
    @BindView(R.id.type_name)
    TextView mTypeName;

    @BindView(R.id.title_question)
    TextView mTitleQuestion;
    @BindView(R.id.title_count)
    TextView mTitleCount;
    @BindView(R.id.title_input)
    EditText mTitleInput;

    @BindView(R.id.posting_content)
    TextView mPostingContentQuestion;

    // Single Content view
    @BindView(R.id.new_event_single_content)
    RelativeLayout mSingleContentView;
    @BindView(R.id.event_single_content)
    AdjustableImageView mSingleContentImage;
    @BindView(R.id.delete_single_content)
    ImageButton mDeleteSingleContent;
    @BindView(R.id.loading_progress)
    ProgressBar mLoadingProgress;

    @BindView(R.id.video_player)
    JCVideoPlayerStandard mVideoPlayer;
    @BindView(R.id.video_layout)
    LinearLayout mVideoLayout;

    // Multiple images View
    @BindView(R.id.new_event_images_list)
    RecyclerView mImagesRecycler;

    // Checkin View
    @BindView(R.id.event_checkin_content)
    RelativeLayout mCheckinView;
    @BindView(R.id.event_location_map)
    AdjustableImageView mLocationMapView;
    @BindView(R.id.load_map_view_progress)
    ProgressBar mLocMapProgress;
    @BindView(R.id.event_check_in_name)
    TextView mCheckinName;
    @BindView(R.id.event_check_in_type)
    TextView mCheckinType;

    //Shared content view
    @BindView(R.id.new_event_shared_content)
    RelativeLayout mSharedEventLayout;
    @BindView(R.id.shared_event_profile)
    ImageView mSharedEventUserProfile;
    @BindView(R.id.shared_event_user_name)
    TextView mSharedEventUserDisplayName;
    @BindView(R.id.shared_event_type)
    TextView mSharedEventType;
    @BindView(R.id.shared_event_text_content)
    CustomTextView mSharedEventTextContent;
    @BindView(R.id.shared_event_options)
    ImageButton mSharedEventOption;

    // Tag input view
    @BindView(R.id.flow_layout)
    CustomFlowLayout mCustomFlowLayout;
    @BindView(R.id.tags_input)
    EditText mTagsInput;
    @BindView(R.id.add_topic_btn)
    BootstrapButton mAddTagBtn;

    private ArrayList<String> mImagePaths;
    private ImageLoaderAdapter mImagesAdapter;
    private SweetAlertDialog mDialog;

    private String mMode = "Public";
    private boolean isWarningContent = false;
    private FusedLocationProviderClient mFusedLocationClient;


    private final String POSTED_LOCATION = "posted_location";
    private final String TAGGED_LOCATION = "tagged_location";
    private final String CHECKIN_LOCATION = "checkin_location";

    private final int PHOTO_PICKER = 1;
    private final int CHECKIN_PLACE_PICKER_REQUEST = 2;
    private final int TAGGED_PLACE_PICKER_REQUEST = 4;
    private final int REQUEST_LOCATION_PERMISSION = 3;
    private final int VIDEO_LIBRARY_REQUEST = 5;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private final JobManager jobManager = BaseApplication.getInstance().getJobManager();
    private String gifUrl;
    private String selectedVideoPath;
    private GoogleApiClient mGoogleApiClient;
    private Location lastKnownLocation;
    private List<String> tags;

    private EventParcel eventParcel;
    private LocationRequest postedLocation;
    private LocationRequest taggedLocation;
    private LocationRequest checkinLocation;
    private boolean isRequestedPostedLocation = false;
    private GifDrawable gifDrawable;

    private PermissionManager permissionManager;
    private LocationEngine locationEngine;

    private Place checkinPlace;
    private Place taggedPlace;

    private int selectedType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        ButterKnife.bind(this);

        tags = new ArrayList<>();

        // init google location client
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        eventParcel = (EventParcel) Parcels.unwrap(getIntent().getParcelableExtra("shared_event"));

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(AppIndex.API).build();
        mGoogleApiClient.connect();
        Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();

        mImagePaths = new ArrayList<>();

        if (mightNeedLocationPermission()) {
            initLocationTracker();
        }

        locationEngine = BaseApplication.getInstance().getLocationEngine();
        permissionManager = new PermissionManager() {
            @Override
            public ArrayList<statusArray> getStatus() {
                return super.getStatus();
            }

            @Override
            public List<String> setPermission() {
                // If You Don't want to check permission automatically and check your own custom permission
                // Use super.setPermission(); or Don't override this method if not in use
                List<String> customPermission = new ArrayList<>();
                customPermission.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
                customPermission.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
                return customPermission;
            }

            @Override
            public void ifCancelledAndCanRequest(Activity activity) {
                // Do Customized operation if permission is cancelled without checking "Don't ask again"
                // Use super.ifCancelledAndCanRequest(activity); or Don't override this method if not in use
                super.ifCancelledAndCanRequest(activity);
            }

            @Override
            public void ifCancelledAndCannotRequest(Activity activity) {
                // Do Customized operation if permission is cancelled with checking "Don't ask again"
                // Use super.ifCancelledAndCannotRequest(activity); or Don't override this method if not in use
                super.ifCancelledAndCannotRequest(activity);
            }
        };
        permissionManager.checkAndRequestPermissions(this);

        if (isLocationPermissionAllowed()) {
            activateLocationEngine();
        }

        initView();
        initDefaultTextAndTypeFace();
    }

    private void initDefaultTextAndTypeFace() {
        mDisplayName.setText(SharedPreferencesManager.getDefault().getUserDisplayName());
        mPostingAsQuestion.setText(getResources().getString(R.string.posting_as));
        mTitleQuestion.setText(getResources().getString(R.string.does_have_title));
        mTitleInput.setHint(getResources().getString(R.string.add_a_title));
        mPostingContentQuestion.setText(getResources().getString(R.string.content));
        mTextContentInput.setHint(getResources().getString(R.string.write_a_message));
        mTypeName.setText(getResources().getString(R.string.story));

        mDisplayName.setTypeface(EasyFonts.robotoRegular(this));
        mPostingAsQuestion.setTypeface(EasyFonts.robotoBold(this));
        mTitleQuestion.setTypeface(EasyFonts.robotoBold(this));
        mTitleInput.setTypeface(EasyFonts.robotoRegular(this));
        mTypeName.setTypeface(EasyFonts.robotoRegular(this));
        mPostingContentQuestion.setTypeface(EasyFonts.robotoBold(this));
        mTextContentInput.setTypeface(EasyFonts.robotoRegular(this));
    }

    @Nullable
    @org.jetbrains.annotations.Contract(pure = true)
    private String getType(int selection) {
        switch (selection) {
            case 0:
                return AppConstant.TYPE_STORY;
            case 1:
                return AppConstant.TYPE_QUESTION;
            case 2:
                return AppConstant.TYPE_QUOTE;
            case 3:
                return AppConstant.TYPE_WISH;
            case 4:
                return AppConstant.TYPE_CHECKING;
            case 5:
                return AppConstant.TYPE_INVITATION;
            case 6:
                return AppConstant.TYPE_ASK;
            case 7:
                return AppConstant.TYPE_STORY;
            default:
                return null;
        }
    }

    private void initView() {
        setActivePostButton(false);
        loadProfileImage(SharedPreferencesManager.getDefault().getUserProfileUrl());

        mBack.setOnClickListener(this);
        mAddPhotoBtn.setOnClickListener(this);
        mAddGifBtn.setOnClickListener(this);
        mEventLocation.setOnClickListener(this);

        mPrivacyBtn.setOnClickListener(this);
        mAddLocationBtn.setOnClickListener(this);
        mPostBtn.setOnClickListener(this);
        mAddVideoBtn.setOnClickListener(this);

        mAddTagBtn.setOnClickListener(this);
        mTypePicker.setOnClickListener(this);

        mDeleteSingleContent.setOnClickListener(this);

        if (eventParcel != null) {
            initSharedEventView();
        }

        mTextContentInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    setActivePostButton(true);
                } else {
                    setActivePostButton(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initLocationTracker()  {
        if (mightNeedLocationPermission()) {
            try {
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        lastKnownLocation = location;
                    }
                });
            } catch (SecurityException e) {

            }
        }
    }

    private void initType(int which) {
        Toast.makeText(this, "Picked" + String.valueOf(which), Toast.LENGTH_SHORT).show();

        switch (which) {
            case 0:
                selectedType = which;
                mTypeName.setText(getResources().getString(R.string.story));
                break;
            case 1:
                selectedType = which;
                mTypeName.setText(getResources().getString(R.string.question));
                break;
            case 2:
                selectedType = which;
                mTypeName.setText(getResources().getString(R.string.quote));
                break;
            case 3:
                selectedType = which;
                mTypeName.setText(getResources().getString(R.string.wish));
                break;
            case 4:
                selectedType = which;
                mTypeName.setText(getResources().getString(R.string.checking));
                break;
            case 5:
                selectedType = which;
                mTypeName.setText(getResources().getString(R.string.invitation));
                break;
            case 6:
                selectedType = which;
                mTypeName.setText(getResources().getString(R.string.ask));
                break;
            default:
                break;
        }
    }

    private void showEventTypePickerDialog() {
        new MaterialDialog.Builder(this)
                .title(R.string.select_event_type)
                .items(R.array.event_type)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        initType(which);
                        return true;
                    }
                })
                .cancelable(true)
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        dialogInterface.dismiss();
                    }
                })
                .positiveText(R.string.select)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private boolean mightNeedLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            List<String> permissions = new ArrayList<>();
            permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);

            PermissionUtils.requestPermissions(this,
                    REQUEST_LOCATION_PERMISSION, permissions);
            return false;
        }

        return true;
    }

    private  void initSharedEventView() {
        setActivePostButton(true);
        mSharedEventLayout.setVisibility(View.VISIBLE);

        loadEventSharedUserProfile(eventParcel.getUserInfo().getProfileUrl());
        mSharedEventUserDisplayName.setText(eventParcel.getUserInfo().getDisplayName());
        mSharedEventTextContent.setContent(eventParcel.getTextContent());
    }

    private void loadEventSharedUserProfile(final String url) {
        Glide.with(this)
                .load(url)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.noAnimation())
                .into(mSharedEventUserProfile);
    }

    private void loadProfileImage(final String url) {
        Glide.with(this)
                .load(url)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.noAnimation())
                .into(mProfileImage);
    }

    private void showAlert() {
        mDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getResources().getString(R.string.delete_title))
                .setContentText(getResources().getString(R.string.delete_warning))
                .setCancelText(getResources().getString(R.string.cancel))
                .setConfirmText(getResources().getString(R.string.discard))
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        finishActivity();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                    }
                });

        mDialog.show();
    }

    @Override
    protected void onDestroy() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        super.onDestroy();
    }

    private void showMode() {
        EventModeSheetFragment fragment = EventModeSheetFragment.newInstance(mMode, isWarningContent);
        fragment.show(getSupportFragmentManager(), null);
    }

    private void finishActivity() {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("NewEventActivity", "popping backstack");
            fm.popBackStack();
            overridePendingTransition(R.anim.fix_anim, R.anim.slide_down_out);
        } else {
            Log.i("NewEventActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    private void openGooglePlacePicker(final int requestCode) {
        try {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            intentBuilder.setLatLngBounds(MapUtils.getCurrentLatLngBound(lastKnownLocation));
            Intent intent = intentBuilder.build(NewEventActivity.this);
            startActivityForResult(intent, requestCode);
        } catch (GooglePlayServicesRepairableException
                | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private boolean doesHaveContent() {
        return mTextContentInput.getText().length() > 0
                || mImagePaths.size() > 0
                || gifUrl != null
                || eventParcel != null;
    }

    @Override
    public void onBackPressed(){
        if (doesHaveContent()) {
            showAlert();
        } else {
            finishActivity();
        }
    }

    private void loadPickedImages(ArrayList<String> imageList) {
        mImagePaths.clear();
        mImagePaths.addAll(imageList);
        mImagesAdapter = new ImageLoaderAdapter(this, imageList);

        mImagesRecycler.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false));
        mImagesRecycler.setAdapter(mImagesAdapter);
        mImagesRecycler.setItemAnimator(new DefaultItemAnimator());
        mImagesRecycler.setNestedScrollingEnabled(false);
        mImagesRecycler.setVisibility(View.VISIBLE);
    }

    private void openCustomPlacePicker() {
        startActivity(new Intent(NewEventActivity.this, MapBoxPlaceSearchActivity.class));
    }

    private void addTag() {
        if (mTagsInput.getText().length() > 0) {
            String topic = mTagsInput.getText().toString();

            if (tags.contains(topic)) {
                Toast.makeText(this, getResources().getText(R.string.already_created),
                        Toast.LENGTH_SHORT).show();
            } else {
                tags.add(topic);
                initFlowLayoutView();
            }

            mTagsInput.setText("");
        }
    }

    private void initFlowLayoutView() {
        mCustomFlowLayout.removeAllViews();

        if (tags.size() == 0) {
            mCustomFlowLayout.setVisibility(View.GONE);
        } else {
            mCustomFlowLayout.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < tags.size(); i++) {
            final RelativeLayout item = (RelativeLayout) getLayoutInflater().inflate(R.layout.event_tag_item,
                    mCustomFlowLayout, false);
            TextView topic = (TextView) item.findViewById(R.id.topic_flow_text);

            topic.setText(tags.get(i));
            topic.setTypeface(EasyFonts.robotoRegular(this));
            item.setTag(i);

            mCustomFlowLayout.addView(item);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = (int) v.getTag();
                    item.setVisibility(View.GONE);
                    tags.remove(i);
                    updateTags(i);

                    if (tags.size() == 0) {
                        mCustomFlowLayout.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    private void updateTags(int i) {
        for (i = i + 1; i < tags.size(); i++) {
            RelativeLayout flowChild = (RelativeLayout) mCustomFlowLayout.getChildAt(i);
            flowChild.setTag(i - 1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.event_add_photo:
                AppUtils.startImagePicker(this, 20, PHOTO_PICKER);
                break;
            case R.id.action_back:
                onBackPressed();
                break;
            case R.id.event_add_gif:
                new Giphy.Builder(NewEventActivity.this,
                        AppConstant.GIPHY_PUBLIC_KEY)// their public BETA key
                        .maxFileSize(2 * 1024 * 1024) //2 mb
                        .start();
                break;
            case R.id.event_privacy:
                showMode();
                break;
            case R.id.event_add_video:
                showVideoLibrary();
                break;
            case R.id.event_add_location:
                openGooglePlacePicker(CHECKIN_PLACE_PICKER_REQUEST);
                break;
            case R.id.event_location:
                openGooglePlacePicker(TAGGED_PLACE_PICKER_REQUEST);
                break;
            case R.id.action_post:
                if (doesHaveContent()) {
                    postEvent();
                } else {
                    Toast.makeText(this, "No content", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.delete_single_content:
                mSingleContentView.setVisibility(View.GONE);
                mImagePaths.clear();
                gifUrl = null;
                break;
            case R.id.add_topic_btn:
                addTag();
                break;
            case R.id.type_picker:
                showEventTypePickerDialog();
                break;
            default:
                break;
        }
    }

    private void showVideoLibrary() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        i.setType("video/*");
        startActivityForResult(Intent.createChooser(i, "Select Video"), VIDEO_LIBRARY_REQUEST);
    }

    private void postEvent() {
        String eventType;

        if (mImagePaths.size() == 1) {
            eventType = AppConstant.EVENT_TYPE_SINGLE_IMAGE;
        } else if (mImagePaths.size() > 1) {
            eventType = AppConstant.EVENT_TYPE_MULT_IMAGE;
        } else if (gifUrl != null && gifUrl.length() > 5) {
            eventType = AppConstant.EVENT_TYPE_SINGLE_GIF;
        } else if (checkinLocation != null && mImagePaths.size() == 0) {
            eventType = AppConstant.EVENT_TYPE_CHECK_IN;
        } else {
            eventType = AppConstant.EVENT_TYPE_TEXT;
        }

        if (eventParcel != null) {
            ShareNewEventJob job =  new ShareNewEventJob (mTextContentInput.getText().toString(),
                    mMode,  mTitleInput.getText().toString(), getType(selectedType),
                    eventType, eventParcel.getId(), postedLocation, taggedLocation,
                    checkinLocation, tags);

            jobManager.addJobInBackground(job);
        } else {
            PostNewEventJob job =  new PostNewEventJob (SharedPreferencesManager.getDefault().getUserId(),
                    mTextContentInput.getText().toString(), gifUrl,
                    mImagePaths, mMode, mTitleInput.getText().toString(), getType(selectedType),
                    eventType, postedLocation, taggedLocation,
                    checkinLocation, tags);

            jobManager.addJobInBackground(job);
        }
        finish();
    }

    private void setActivePostButton(boolean isActive) {
        if (isActive) {
            mPostBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            mPostBtn.setTextColor(getResources().getColor(R.color.gray_alpha));
        }
    }

    private void loadGif(final String url) {
        gifUrl = url;

        Glide.with(this)
            .load(url)
            .apply(RequestOptions.centerCropTransform())
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
            .listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                            Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                               DataSource dataSource, boolean isFirstResource) {
                    if (resource instanceof GifDrawable) {
                        gifDrawable = (GifDrawable) resource;
                    } else {
                        gifDrawable = null;
                    }

                    mLoadingProgress.setVisibility(View.GONE);
                    return false;
                }
            })
            .into(mSingleContentImage);

        mSingleContentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gifDrawable != null) {
                    if (gifDrawable.isRunning()) {
                        gifDrawable.stop();
                    } else {
                        gifDrawable.start();
                    }
                }
            }
        });
    }

    private void loadPickedSingleImage(String path) {
        mImagePaths.clear();
        mImagePaths.add(path);

        Glide.with(this)
                .load(new File(path))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.noAnimation())
                .apply(RequestOptions.centerCropTransform())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mLoadingProgress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(mSingleContentImage);
    }

    public String getVideoPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor!=null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }

    private void loadVideoLayout() {
        mVideoPlayer.setUp(selectedVideoPath, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL,
                "Selected Video");
        mVideoLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Giphy.REQUEST_GIPHY) {
                String downloadUrl = data.getStringExtra(GiphyActivity.GIF_DOWNLOAD_URL);
                loadGif(downloadUrl);
            } else if (requestCode == PHOTO_PICKER) {
                if (data != null){
                    final ArrayList<String> images = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);

                    if (images.size() > 1) {
                        loadPickedImages(images);
                    } else if (images.size() == 1) {
                        loadPickedSingleImage(images.get(0));
                    }
                }
            } else if (requestCode == VIDEO_LIBRARY_REQUEST) {
                if (data != null) {
                    selectedVideoPath = getVideoPath(data.getData());
                    if (selectedVideoPath != null) {
                        loadVideoLayout();
                    }
                    Toast.makeText(this, selectedVideoPath, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == CHECKIN_PLACE_PICKER_REQUEST) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

                initCheckinPlace(place);
            } else if (requestCode == TAGGED_PLACE_PICKER_REQUEST) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

                initTaggedPlace(place);
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void initCheckinPlace(Place place) {
        BaseApplication.getInstance().getJobManager().addJobInBackground(
                new GetPlaceAttributesByLatLongJob( place.getLatLng().latitude,
                        place.getLatLng().longitude, CHECKIN_LOCATION));
        checkinPlace = place;

        mCheckinView.setVisibility(View.VISIBLE);

        LatLng ll = place.getLatLng();
        loadLocationMapView(MapUtils.getLocalMapBoxMapImage(this, ll.latitude, ll.longitude));

        mCheckinName.setText(place.getName().toString());
        mCheckinType.setText(place.getAddress().toString());
    }

    private void initTaggedPlace(Place place) {
        BaseApplication.getInstance().getJobManager().addJobInBackground(
                new GetPlaceAttributesByLatLongJob( place.getLatLng().latitude,
                        place.getLatLng().longitude, TAGGED_LOCATION));
        taggedPlace = place;
        mEventLocation.setBootstrapText(new BootstrapText.Builder(this)
                .addFontAwesomeIcon(FontAwesome.FA_PLANE)
                .addText(" " + place.getAddress())
                .build());
    }

    private void loadLocationMapView(String url) {
        Glide.with(this)
                .load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.noAnimation())
                .apply(RequestOptions.centerCropTransform())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mLocMapProgress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(mLocationMapView);
    }

    private void activateLocationEngine() {
        locationEngine.addLocationEngineListener(this);
        locationEngine.activate();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        permissionManager.checkResult(requestCode, permissions, grantResults);
        ArrayList<String> granted = permissionManager.getStatus().get(0).granted;

        if (granted.contains(android.Manifest.permission.ACCESS_FINE_LOCATION)
                || granted.contains(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
            activateLocationEngine();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (locationEngine != null && locationEngine.isConnected()) {
            locationEngine.requestLocationUpdates();
            locationEngine.addLocationEngineListener(this);
            locationEngine.activate();
        }

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private boolean isLocationPermissionAllowed() {
        ArrayList<String> granted = permissionManager.getStatus().get(0).granted;
        if (granted.contains(Manifest.permission.ACCESS_COARSE_LOCATION) ||
                granted.contains(Manifest.permission.ACCESS_FINE_LOCATION)) {
            return true;
        }

        return false;
    }

    /**********************************************
     * EVENTBUS CATCHING FIELDS
     *  + PublicModeRequest: Request Public Mode
     *  + PrivateModeRequest: Request private mode
     *  + WarningContentRequest: Request warning data
     ***********************************************/
    @Subscribe
    public void onEvent(PublicModeRequest request) {
        mMode = "Public";
        mPrivacyBtn.setBootstrapText(new BootstrapText.Builder(this)
                .addFontAwesomeIcon(FontAwesome.FA_GLOBE)
                .addText(" " + mMode + " ")
                .addFontAwesomeIcon(FontAwesome.FA_CARET_DOWN)
                .build());
    }

    @Subscribe
    public void onEvent(PrivateModeRequest request) {
        mMode = "Private";
        mPrivacyBtn.setBootstrapText(new BootstrapText.Builder(this)
                .addFontAwesomeIcon(FontAwesome.FA_USER)
                .addText(" " + mMode + " ")
                .addFontAwesomeIcon(FontAwesome.FA_CARET_DOWN)
                .build());
    }

    @Subscribe
    public void onEvent(FriendModeRequest request) {
        mMode = "Friend";
        mPrivacyBtn.setBootstrapText(new BootstrapText.Builder(this)
                .addFontAwesomeIcon(FontAwesome.FA_USERS)
                .addText(" " + mMode + " ")
                .addFontAwesomeIcon(FontAwesome.FA_CARET_DOWN)
                .build());
    }

    @Subscribe
    public void onEvent(WarningContentRequest request) {
        if (request.isRequested()) {
            mWarningButton.setVisibility(View.VISIBLE);
            isWarningContent = true;
        } else {
            mWarningButton.setVisibility(View.GONE);
            isWarningContent = false;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Cannot load google service", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended() called. Trying to reconnect.");
        Toast.makeText(this, "Trying to reconnect to Google", Toast.LENGTH_SHORT).show();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected() {
        Log.d(getClass().getSimpleName(), "Connected to engine, we can now request updates.");
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationEngine.addLocationEngineListener(this);
        locationEngine.requestLocationUpdates();
        locationEngine.activate();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            if (!isRequestedPostedLocation) {
                BaseApplication.getInstance().getJobManager().addJobInBackground(
                        new GetPlaceAttributesByLatLongJob(location.getLatitude(),
                                location.getLongitude(), POSTED_LOCATION)
                );
                isRequestedPostedLocation = true;
            }
        }
    }


    @Subscribe
    public void onEvent(InvalidLocationRequest request) {

    }

    @Subscribe
    public void onEvent(LocationRequestCollected collected) {
        if (collected.getTag().equals(POSTED_LOCATION)) {
            postedLocation = collected.getRequest();
        } else if (collected.getTag().equals(TAGGED_LOCATION)) {
            taggedLocation = collected.getRequest();
            if (taggedPlace != null) {
                taggedLocation.setAddress(taggedPlace.getAddress().toString());
                taggedLocation.setLocationName(taggedPlace.getName().toString());
            }
        } else if (collected.getTag().equals(CHECKIN_LOCATION)) {
            checkinLocation = collected.getRequest();
            if (checkinPlace != null) {
                checkinLocation.setAddress(checkinPlace.getAddress().toString());
                checkinLocation.setLocationName(checkinPlace.getName().toString());
            }
        }
    }
}
