package com.hoocons.hoocons_android.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.hoocons.hoocons_android.Adapters.ImageLoaderAdapter;
import com.hoocons.hoocons_android.CustomUI.AdjustableImageView;
import com.hoocons.hoocons_android.EventBus.FriendModeRequest;
import com.hoocons.hoocons_android.EventBus.PrivateModeRequest;
import com.hoocons.hoocons_android.EventBus.PublicModeRequest;
import com.hoocons.hoocons_android.EventBus.WarningContentRequest;
import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Helpers.MapUtils;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.Tasks.Jobs.PostNewEventJob;
import com.hoocons.hoocons_android.ViewFragments.EventModeSheetFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import me.iwf.photopicker.PhotoPicker;
import xyz.klinker.giphy.Giphy;
import xyz.klinker.giphy.GiphyActivity;

public class NewEventActivity extends BaseActivity
        implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
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

    private ArrayList<String> mImagePaths;
    private ImageLoaderAdapter mImagesAdapter;
    private SweetAlertDialog mDialog;

    private String mMode = "Public";
    private boolean isWarningContent = false;

    private final int PHOTO_PICKER = 1;
    private final int PLACE_PICKER_REQUEST = 2;
    private final int VIDEO_LIBRARY_REQUEST = 5;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private final JobManager jobManager = BaseApplication.getInstance().getJobManager();
    private String gifUrl;
    private String selectedVideoPath;
    private GoogleApiClient mGoogleApiClient;

    private double checkinLongitude = 0;
    private double checkinLatitude = 0;
    private String checkinName;
    private String checkinAddress;
    private String checkinPlaceId;
    private GifDrawable gifDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_new_event);
        ButterKnife.bind(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(AppIndex.API).build();
        mGoogleApiClient.connect();
        Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();

        mImagePaths = new ArrayList<>();

        initView();
    }

    private void initView() {
        setActivePostButton(false);
        loadProfileImage(SharedPreferencesManager.getDefault().getUserProfileUrl());

        mBack.setOnClickListener(this);
        mAddPhotoBtn.setOnClickListener(this);
        mAddGifBtn.setOnClickListener(this);

        mPrivacyBtn.setOnClickListener(this);
        mAddLocationBtn.setOnClickListener(this);
        mPostBtn.setOnClickListener(this);
        mAddVideoBtn.setOnClickListener(this);

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

        mDisplayName.setText(SharedPreferencesManager.getDefault().getUserDisplayName());
    }

    private void loadProfileImage(String url) {
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

    private void openGooglePlacePicker() {
        try {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            intentBuilder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
            Intent intent = intentBuilder.build(NewEventActivity.this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException
                | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private boolean doesHaveContent() {
        return mTextContentInput.getText().length() > 0 || mImagePaths.size() > 0 || gifUrl != null;
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
        updateUIForMultiImageEvent();

        mImagePaths.clear();
        mImagePaths.addAll(imageList);
        mImagesAdapter = new ImageLoaderAdapter(this, imageList);

        mImagesRecycler.setLayoutManager(new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false));
        mImagesRecycler.setAdapter(mImagesAdapter);
        mImagesRecycler.setItemAnimator(new DefaultItemAnimator());
        mImagesRecycler.setNestedScrollingEnabled(false);
    }

    private void openCustomPlacePicker() {
        startActivity(new Intent(NewEventActivity.this, PlacePickerActivity.class));
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
                new Giphy.Builder(NewEventActivity.this, AppConstant.GIPHY_PUBLIC_KEY)// their public BETA key
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
                openGooglePlacePicker();
                break;
            case R.id.action_post:
                if (doesHaveContent()) {
                    postEvent();
                } else {
                    Toast.makeText(this, "No content", Toast.LENGTH_SHORT).show();
                }
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
        } else if (checkinLatitude != 0 && checkinLongitude != 0) {
            eventType = AppConstant.EVENT_TYPE_CHECK_IN;
        } else {
            eventType = AppConstant.EVENT_TYPE_TEXT;
        }

        PostNewEventJob job =  new PostNewEventJob (
                mTextContentInput.getText().toString(), gifUrl,
                mImagePaths, mMode, eventType, checkinLongitude, checkinLatitude,
                checkinName, checkinAddress, checkinPlaceId);
        jobManager.addJobInBackground(job);

        finish();
    }

    private void setActivePostButton(boolean isActive) {
        if (isActive) {
            mPostBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            mPostBtn.setTextColor(getResources().getColor(R.color.gray_alpha));
        }
    }

    private void updateUIForGifEvent() {
        // display the holder content
        mSingleContentView.setVisibility(View.VISIBLE);
        mDeleteSingleContent.bringToFront();
        mLoadingProgress.bringToFront();

        // update the add more content options
        mAddPhotoBtn.setVisibility(View.GONE);
        mAddLocationBtn.setVisibility(View.GONE);
        mAddVideoBtn.setVisibility(View.GONE);
        mAddSoundBtn.setVisibility(View.GONE);
    }

    private void updateUIForSingleImageEvent() {
        // display the holder content
        mSingleContentView.setVisibility(View.VISIBLE);
        mDeleteSingleContent.bringToFront();
        mLoadingProgress.bringToFront();

        // update the add more content options
        mAddPhotoBtn.setVisibility(View.VISIBLE);
        mAddLocationBtn.setVisibility(View.VISIBLE);
        mAddGifBtn.setVisibility(View.GONE);
        mAddVideoBtn.setVisibility(View.GONE);
        mAddSoundBtn.setVisibility(View.GONE);
    }

    private void updateUIForMultiImageEvent() {
        // display the holder content
        mSingleContentView.setVisibility(View.GONE);

        // update the add more content options
        mAddPhotoBtn.setVisibility(View.VISIBLE);
        mAddLocationBtn.setVisibility(View.VISIBLE);
        mAddGifBtn.setVisibility(View.GONE);
        mAddVideoBtn.setVisibility(View.GONE);
        mAddSoundBtn.setVisibility(View.GONE);
    }

    private void updateUIForNormalEvent() {
        mAddPhotoBtn.setVisibility(View.VISIBLE);
        mAddLocationBtn.setVisibility(View.VISIBLE);
        mAddVideoBtn.setVisibility(View.VISIBLE);
        mAddSoundBtn.setVisibility(View.VISIBLE);
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
        updateUIForSingleImageEvent();

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
                updateUIForGifEvent();
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
            } else if (requestCode == PLACE_PICKER_REQUEST) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

                initCheckinPlace(place);
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void initCheckinPlace(Place place) {
        checkinAddress = place.getAddress().toString();
        checkinName = place.getName().toString();
        checkinPlaceId = place.getId();
        checkinLatitude = place.getLatLng().latitude;
        checkinLongitude = place.getLatLng().longitude;

        mCheckinView.setVisibility(View.VISIBLE);

        LatLng ll = place.getLatLng();
        loadLocationMapView(MapUtils.getMapLocationUrl(String.valueOf(ll.latitude),
                String.valueOf(ll.longitude)));

        mCheckinName.setText(place.getName().toString());
        mCheckinType.setText(place.getAddress().toString());
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
}
