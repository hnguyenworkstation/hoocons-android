package com.hoocons.hoocons_android.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.beardedhen.androidbootstrap.font.FontAwesome;
import com.birbit.android.jobqueue.JobManager;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.hoocons.hoocons_android.EventBus.BadRequest;
import com.hoocons.hoocons_android.EventBus.ChannelCategoryCollected;
import com.hoocons.hoocons_android.EventBus.ChannelDescCollected;
import com.hoocons.hoocons_android.EventBus.ChannelImageCropCollected;
import com.hoocons.hoocons_android.EventBus.ChannelNameCollected;
import com.hoocons.hoocons_android.EventBus.InvalidLocationRequest;
import com.hoocons.hoocons_android.EventBus.LocationPermissionAllowed;
import com.hoocons.hoocons_android.EventBus.LocationPermissionDenied;
import com.hoocons.hoocons_android.EventBus.LocationRequestCollected;
import com.hoocons.hoocons_android.EventBus.LocationURLRequest;
import com.hoocons.hoocons_android.EventBus.LocationUrlReady;
import com.hoocons.hoocons_android.EventBus.TagsCollected;
import com.hoocons.hoocons_android.EventBus.TaskCompleteRequest;
import com.hoocons.hoocons_android.EventBus.UploadImageFailed;
import com.hoocons.hoocons_android.EventBus.UploadImageSuccess;
import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Helpers.MapUtils;
import com.hoocons.hoocons_android.Helpers.PermissionManager;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Models.Topic;
import com.hoocons.hoocons_android.Networking.Requests.LocationRequest;
import com.hoocons.hoocons_android.Networking.Responses.ChannelProfileResponse;
import com.hoocons.hoocons_android.Networking.Responses.CoordinateResponse;
import com.hoocons.hoocons_android.Networking.Responses.LocationResponse;
import com.hoocons.hoocons_android.Networking.Responses.MediaResponse;
import com.hoocons.hoocons_android.Networking.Responses.SemiUserInfoResponse;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.Tasks.Jobs.GetPlaceAttributesByLatLongJob;
import com.hoocons.hoocons_android.Tasks.Jobs.NewChannelJob;
import com.hoocons.hoocons_android.Tasks.Jobs.UploadSingleUriImageJob;
import com.hoocons.hoocons_android.ViewFragments.GetChannelAboutFragment;
import com.hoocons.hoocons_android.ViewFragments.GetChannelCategoryFragment;
import com.hoocons.hoocons_android.ViewFragments.GetChannelLocationFragment;
import com.hoocons.hoocons_android.ViewFragments.GetChannelNameFragment;
import com.hoocons.hoocons_android.ViewFragments.GetChannelProfileFragment;
import com.hoocons.hoocons_android.ViewFragments.GetChannelTagsFragment;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.iwf.photopicker.PhotoPicker;

public class NewChannelActivity extends BaseActivity  implements LocationEngineListener {
    @BindView(R.id.create_channel_container)
    FrameLayout mFrameContainer;
    @BindView(R.id.action_back)
    ImageButton mActionBack;
    @BindView(R.id.act_title)
    TextView mTitle;
    @BindView(R.id.action_skip)
    Button mActionSkip;
    @BindView(R.id.wallpaper)
    ImageView mWallpaper;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.edit_wallpaper)
    ImageView mEditWallpaper;
    @BindView(R.id.wallpaper_root)
    RelativeLayout mWallpaperRoot;

    private FragmentManager mFragManager;
    private FragmentTransaction mFragTransition;

    private GetChannelNameFragment getChannelNameFragment;
    private GetChannelAboutFragment getChannelAboutFragment;
    private GetChannelCategoryFragment getChannelCategoryFragment;
    private GetChannelTagsFragment getChannelTagsFragment;
    private GetChannelProfileFragment getChannelProfileFragment;
    private GetChannelLocationFragment getChannelLocationFragment;

    private final String TAG = NewChannelActivity.class.getSimpleName();
    private final JobManager jobManager = BaseApplication.getInstance().getJobManager();

    private final int WALLPAPER_IMG_PICKER = 10;
    private final int LOCATION_PICKER = 100;
    private static final String WALLPAPER_CROPPED_IMAGE_NAME = "ProfileCroppedImage";

    private Uri wallpaperCroppedUri;
    private Uri profileCroppedUri;

    private String wallpaperImagePath;
    private String profileUrl;
    private String wallpaperUrl;
    private String channelName;
    private String channelCat;
    private String channelAbout;
    private final String CHANNEL_LOCATION_REQUEST = "channel_location_request";

    private List<String> topics;

    private final String UPLOAD_PROFILE_TAG = "upload_profile";
    private final String UPLOAD_WALLPAPER_TAG = "upload_wallpaper";
    private MaterialDialog loadingDialog;
    private LocationRequest locationRequest;

    private Place channelPlace;
    private Location currentLocation;
    private LocationEngine locationEngine;
    private PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_channel);
        ButterKnife.bind(this);

        topics = new ArrayList<>();

        getChannelNameFragment = new GetChannelNameFragment();
        getChannelAboutFragment = new GetChannelAboutFragment();
        getChannelCategoryFragment = new GetChannelCategoryFragment();
        getChannelLocationFragment = GetChannelLocationFragment.newInstance();

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
        locationEngine = BaseApplication.getInstance().getLocationEngine();

        if (isLocationPermissionAllowed()){
            activateLocationEngine();
        }

        initGeneralView();
        initGetNameView();
    }

    private void initGeneralView() {
        mFragManager = getSupportFragmentManager();
        mFragTransition = mFragManager.beginTransaction();
        AppUtils.loadCropImageWithProgressBar(this, "http://www.stanleychowillustration.com/uploads/images/MANCHESTER_LANDSCAPE_30x12.jpg", mWallpaper, mProgressBar);

        mActionBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initGetNameView() {
        mFragTransition.replace(R.id.create_channel_container, getChannelNameFragment, "get_channel_name");
        mFragTransition.commit();

        mActionSkip.setVisibility(View.GONE);
        mEditWallpaper.setVisibility(View.GONE);
        mActionBack.setImageResource(R.drawable.ic_close_colored);
    }

    private void initGetChannelAboutView() {
        mFragTransition = mFragManager.beginTransaction();
        mFragTransition.setCustomAnimations(R.anim.fade_out_to_left, R.anim.fade_in_from_right);
        mFragTransition.replace(R.id.create_channel_container, getChannelAboutFragment, "get_channel_about");
        mFragTransition.commit();

        mActionSkip.setVisibility(View.GONE);
        mEditWallpaper.setVisibility(View.GONE);
        mActionBack.setImageResource(R.drawable.ic_arrow_back_colored);
    }

    private void initGetCategoryView() {
        mFragTransition = mFragManager.beginTransaction();
        mFragTransition.setCustomAnimations(R.anim.fade_out_to_left, R.anim.fade_in_from_right);
        mFragTransition.replace(R.id.create_channel_container, getChannelCategoryFragment, "get_channel_category");
        mFragTransition.commit();
        mEditWallpaper.setVisibility(View.GONE);
    }

    private void initGetProfileView() {
        mFragTransition = mFragManager.beginTransaction();
        mFragTransition.setCustomAnimations(R.anim.fade_out_to_left, R.anim.fade_in_from_right);
        mFragTransition.replace(R.id.create_channel_container, getChannelProfileFragment, "get_channel_profile");
        mFragTransition.commit();

        mWallpaperRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFragManager.findFragmentByTag("get_channel_profile") != null) {
                    AppUtils.startImagePicker(NewChannelActivity.this, 1, WALLPAPER_IMG_PICKER);
                }
            }
        });

        mEditWallpaper.setVisibility(View.VISIBLE);
    }

    private void initGetLocationView() {
        mFragTransition = mFragManager.beginTransaction();
        mFragTransition.setCustomAnimations(R.anim.fade_out_to_left, R.anim.fade_in_from_right);
        mFragTransition.replace(R.id.create_channel_container, getChannelLocationFragment, "get_channel_location");
        mFragTransition.commit();
        mEditWallpaper.setVisibility(View.GONE);
    }

    private void initGetTagsView() {
        mFragTransition = mFragManager.beginTransaction();
        mFragTransition.setCustomAnimations(R.anim.fade_out_to_left, R.anim.fade_in_from_right);
        mFragTransition.replace(R.id.create_channel_container, getChannelTagsFragment, "get_channel_tags");
        mFragTransition.commit();
        mEditWallpaper.setVisibility(View.GONE);
    }

    private void initChannelPlace(Place place) {
        BaseApplication.getInstance().getJobManager().addJobInBackground(
                new GetPlaceAttributesByLatLongJob( place.getLatLng().latitude,
                        place.getLatLng().longitude, CHANNEL_LOCATION_REQUEST));
        channelPlace = place;
    }

    private void hideKeyboard(final Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed(){
        if (mFragManager.findFragmentByTag("get_channel_about") != null) {
            mFragTransition = mFragManager.beginTransaction();
            mFragTransition.setCustomAnimations(R.anim.fade_out_to_right, R.anim.fade_in_from_left);
            mFragTransition.replace(R.id.create_channel_container, getChannelNameFragment, "get_channel_name");
            mFragTransition.commit();
        } else if (mFragManager.findFragmentByTag("get_channel_category") != null) {
            mFragTransition = mFragManager.beginTransaction();
            mFragTransition.setCustomAnimations(R.anim.fade_out_to_right, R.anim.fade_in_from_left);
            mFragTransition.replace(R.id.create_channel_container, getChannelAboutFragment, "get_channel_about");
            mFragTransition.commit();
        } else if (mFragManager.findFragmentByTag("get_channel_profile") != null) {
            mFragTransition = mFragManager.beginTransaction();
            mFragTransition.setCustomAnimations(R.anim.fade_out_to_right, R.anim.fade_in_from_left);
            mFragTransition.replace(R.id.create_channel_container, getChannelTagsFragment, "get_channel_tags");
            mFragTransition.commit();
        } else if (mFragManager.findFragmentByTag("get_channel_tags") != null) {
            mFragTransition = mFragManager.beginTransaction();
            mFragTransition.setCustomAnimations(R.anim.fade_out_to_right, R.anim.fade_in_from_left);
            mFragTransition.replace(R.id.create_channel_container, getChannelCategoryFragment, "get_channel_category");
            mFragTransition.commit();
        } else if (mFragManager.getBackStackEntryCount() > 0) {
            Log.i(TAG, "popping backstack");
            mFragManager.popBackStack();
            finish();
            overridePendingTransition(R.anim.fade_in_from_left, R.anim.fade_out_to_right);
        } else {
            Log.i(TAG, "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == WALLPAPER_IMG_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null){
                    final ArrayList<String> images = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);

                    if (images.size() >= 1) {
                        wallpaperImagePath = images.get(0);
                        AppUtils.startWallpaperImageCropActivity(NewChannelActivity.this, Uri.fromFile(new File(images.get(0)))
                                , WALLPAPER_CROPPED_IMAGE_NAME);
                    }
                }
            }
        } else if (requestCode == UCrop.REQUEST_CROP) {
            if (resultCode == UCrop.RESULT_ERROR) {
                handleCropError(data);
            } else {
                handleCropResult(data);
            }
        } else if (requestCode == LOCATION_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

                initChannelPlace(place);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        permissionManager.checkResult(requestCode, permissions, grantResults);

        if (isLocationPermissionAllowed()){
            activateLocationEngine();
        }
    }

    private boolean isLocationPermissionAllowed() {
        ArrayList<String> granted = permissionManager.getStatus().get(0).granted;
        if (granted.contains(Manifest.permission.ACCESS_COARSE_LOCATION) ||
                granted.contains(Manifest.permission.ACCESS_FINE_LOCATION)) {
            return true;
        }

        return false;
    }

    private void activateLocationEngine() {
        locationEngine.addLocationEngineListener(this);
        locationEngine.activate();
    }

    private void openGooglePlacePicker(final int requestCode) {
        try {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            intentBuilder.setLatLngBounds(MapUtils.getCurrentLatLngBound(currentLocation));
            Intent intent = intentBuilder.build(NewChannelActivity.this);
            startActivityForResult(intent, requestCode);
        } catch (GooglePlayServicesRepairableException
                | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            // Load uri from here
            wallpaperCroppedUri = resultUri;
            AppUtils.loadCropSquareImageFromUri(this, resultUri, mWallpaper, null);
        } else {
            Toast.makeText(this, R.string.toast_cannot_retrieve_cropped_image,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e(TAG, "handleCropError: ", cropError);
            Toast.makeText(this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.toast_unexpected_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void startUploadingChannelProfile() {
        showUploadingDialog();

        if (profileCroppedUri != null && profileUrl == null) {
            jobManager.addJobInBackground(new UploadSingleUriImageJob(profileCroppedUri.getPath(),
                            UPLOAD_PROFILE_TAG, AppConstant.CHANNEL_PATH));
        } else if (wallpaperCroppedUri != null && wallpaperUrl == null) {
            jobManager.addJobInBackground(new UploadSingleUriImageJob(wallpaperCroppedUri.getPath(),
                            UPLOAD_WALLPAPER_TAG, AppConstant.CHANNEL_PATH));
        } else {
            submitNewChannel();
        }
    }

    private void cancelDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    private void showUploadingDialog() {
        System.out.print("Get here");
        loadingDialog = new MaterialDialog.Builder(this)
                .title(R.string.creating_channel)
                .content(R.string.please_wait)
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .cancelable(false)
                .build();

        loadingDialog.show();
    }

    private void transferToNewChannelActivity(ChannelProfileResponse response) {
        Intent intent = new Intent(NewChannelActivity.this, ChannelActivity.class);
        intent.putExtra("channel_profile",
                Parcels.wrap(AppUtils.getChannelProfileParcelFromResponse(response)));
        startActivity(intent);
        finish();
    }

    private void submitNewChannel() {
        jobManager.addJobInBackground(new NewChannelJob(channelName, "subname", channelAbout,
                profileUrl, topics, "Public", wallpaperUrl, locationRequest));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates();
            locationEngine.removeLocationEngineListener(this);
            locationEngine.deactivate();
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
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
            currentLocation = location;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates();
            locationEngine.removeLocationEngineListener(this);
            locationEngine.deactivate();
        }
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(ChannelNameCollected event) {
        channelName = event.getName();
        initGetChannelAboutView();
        hideKeyboard(this);
    }

    @Subscribe
    public void onEvent(ChannelDescCollected desc) {
        channelAbout = desc.getDesc();
        initGetCategoryView();
        hideKeyboard(this);
    }

    @Subscribe
    public void onEvent(ChannelCategoryCollected cat) {
        channelCat = cat.getCategory();
        getChannelTagsFragment = GetChannelTagsFragment.newInstance(channelCat);
        initGetTagsView();
        hideKeyboard(this);
    }

    @Subscribe
    public void onEvent(TagsCollected ev) {
        getChannelProfileFragment = GetChannelProfileFragment.newInstance(channelName, channelCat,
                (ArrayList<String>) ev.getTags());
        initGetLocationView();
    }

    @Subscribe
    public void onEvent(ChannelImageCropCollected ev) {
        profileCroppedUri = ev.getUri();
        startUploadingChannelProfile();
    }

    @Subscribe
    public void onEvent(UploadImageSuccess job) {
        /*
        * Scenarios:
        * 1. If uploaded channel profile => check if channel wallpaper need to uploaded or not
        *  => upload wallpaper if needed
        *
        * 2. If wallpaper is uploaded (means that profile already uploaded or null)
        *  => submit new channel to server
        * */

        if (job.getTag().equals(UPLOAD_PROFILE_TAG)) {
            profileUrl = job.getUrl();

            if (wallpaperCroppedUri != null && wallpaperUrl == null) {
                jobManager.addJobInBackground(new UploadSingleUriImageJob(wallpaperCroppedUri.getPath(),
                                UPLOAD_WALLPAPER_TAG, AppConstant.CHANNEL_PATH));
            } else {
                submitNewChannel();
            }
        } else if (job.getTag().equals(UPLOAD_WALLPAPER_TAG)) {
            wallpaperUrl = job.getUrl();

            submitNewChannel();
        }
    }

    @Subscribe
    public void onEvent(UploadImageFailed job) {
        cancelDialog();
        Toast.makeText(this, getResources().getString(R.string.task_failed), Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onEvent(TaskCompleteRequest task) {
        if (task.getTag().equals(AppConstant.CREATE_NEW_CHANNEL_TAG)) {
            cancelDialog();
            Toast.makeText(this, getResources().getString(R.string.created_channel) + String.valueOf(task.getId()),
                    Toast.LENGTH_SHORT).show();

            List<Topic> listTopics = new ArrayList<>();
            for (String topName: topics) {
                listTopics.add(new Topic(topName));
            }

            LocationResponse locationResponse = null;

            if (locationRequest != null) {
                locationResponse = new LocationResponse(
                        new CoordinateResponse(4326, locationRequest.getResponse().getLatitude(),
                                locationRequest.getResponse().getLongitude()),
                        locationRequest.getLocationName(),
                        locationRequest.getCity(),
                        locationRequest.getProvince(),
                        locationRequest.getState(),
                        Integer.valueOf(locationRequest.getZipcode()),
                        locationRequest.getCountry(),
                        locationRequest.getAddress(),
                        locationRequest.getPlaceId(),
                        locationRequest.getPlaceApiType()
                );
            }

            ChannelProfileResponse response = new ChannelProfileResponse(task.getId(), channelName, "subname",
                    channelAbout, profileUrl, wallpaperUrl,
                    "Public", listTopics, null, locationResponse,
                    null,0 ,0 ,0 ,0,
                    false, false, false, true, 0, false);
            transferToNewChannelActivity(response);
        }
    }

    @Subscribe
    public void onEvent(BadRequest request) {
        cancelDialog();
        Toast.makeText(this, getResources().getString(R.string.task_failed), Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onEvent(LocationURLRequest request) {
        openGooglePlacePicker(LOCATION_PICKER);
    }

    @Subscribe
    public void onEvent(LocationRequestCollected request) {
        if (request.getTag() == null && request.getRequest() == null) {
            // This is a request to move up to next fragment
            if (channelPlace != null) {
                initGetProfileView();
            } else {
                Toast.makeText(this, getResources().getString(R.string.toast_pick_loc), Toast.LENGTH_SHORT).show();
            }
        } else if (request.getTag().equals(CHANNEL_LOCATION_REQUEST)) {
            locationRequest = request.getRequest();
            if (channelPlace != null) {
                locationRequest.setAddress(channelPlace.getAddress().toString());
                locationRequest.setLocationName(channelPlace.getName().toString());
            }

            String address;
            if (locationRequest.getAddress() != null) {
                address = locationRequest.getAddress();
            } else if (locationRequest.getCity() != null && locationRequest.getCountry() != null) {
                address = String.format("%s, %s", locationRequest.getCity(), locationRequest.getCountry());
            } else if (locationRequest.getProvince() != null && locationRequest.getCountry() != null) {
                address = String.format("%s, %s", locationRequest.getProvince(), locationRequest.getCountry());
            } else if (locationRequest.getState() != null && locationRequest.getCountry() != null) {
                address = String.format("%s, %s", locationRequest.getState(), locationRequest.getCountry());
            } else {
                address = String.format("%s, %s", String.valueOf(locationRequest.getResponse().getLatitude()),
                        String.valueOf(locationRequest.getResponse().getLongitude()));
            }

            EventBus.getDefault().post(new LocationUrlReady(address));
        }
    }

}
