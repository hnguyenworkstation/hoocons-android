package com.hoocons.hoocons_android.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hoocons.hoocons_android.EventBus.CompleteLoginRequest;
import com.hoocons.hoocons_android.EventBus.JobFailureEvBusRequest;
import com.hoocons.hoocons_android.EventBus.LocationPermissionAllowed;
import com.hoocons.hoocons_android.EventBus.LocationPermissionDenied;
import com.hoocons.hoocons_android.EventBus.LocationRequestCollected;
import com.hoocons.hoocons_android.EventBus.LocationURLRequest;
import com.hoocons.hoocons_android.EventBus.LocationUrlReady;
import com.hoocons.hoocons_android.EventBus.ServerErrorRequest;
import com.hoocons.hoocons_android.EventBus.StringDataCollected;
import com.hoocons.hoocons_android.EventBus.TagsCollected;
import com.hoocons.hoocons_android.EventBus.UserBasicInfoCollected;
import com.hoocons.hoocons_android.EventBus.UserInfoRequest;
import com.hoocons.hoocons_android.EventBus.UserNicknameCollected;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Helpers.MapUtils;
import com.hoocons.hoocons_android.Helpers.PermissionManager;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.Networking.Requests.LocationRequest;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.Tasks.Jobs.GetPlaceAttributesByLatLongJob;
import com.hoocons.hoocons_android.Tasks.Jobs.UpdateUserInfoJob;
import com.hoocons.hoocons_android.ViewFragments.CollectUserLocationFragment;
import com.hoocons.hoocons_android.ViewFragments.NewUserInfoFragment;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CollectUserInfoActivity extends BaseActivity implements LocationEngineListener{
    private FragmentTransaction mFragTransition;
    private FragmentManager mFragManager;

    private String profileUrl;
    private String gender;
    private String displayName;
    private String nickname;
    private String birthday;
    private List<String> hobbies;

    private PermissionManager permissionManager;
    private LocationEngine locationEngine;
    private Place closestPlace;
    private LocationRequest locationRequest;
    private boolean isWaitingData = false;
    private PlaceDetectionClient mPlaceDetectionClient;
    private MaterialDialog mLoadingDialog;
    private LocationRequest currentLocation;
    private final String LOCATION_REQUEST = "location_request";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_user_info);
        EventBus.getDefault().register(this);

        mLoadingDialog = new MaterialDialog.Builder(this)
                .title(R.string.uploading_profile)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0)
                .build();

        mPlaceDetectionClient = BaseApplication.getInstance().getPlaceDetectionClient();

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

        mFragManager = getSupportFragmentManager();
        mFragTransition = mFragManager.beginTransaction();

        mFragTransition.replace(R.id.info_fragment_container, new NewUserInfoFragment());
        mFragTransition.commit();

        permissionManager.checkAndRequestPermissions(this);
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
            EventBus.getDefault().post(new LocationPermissionAllowed());
        } else {
            EventBus.getDefault().post(new LocationPermissionDenied());
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
    }

    private boolean isLocationPermissionAllowed() {
        ArrayList<String> granted = permissionManager.getStatus().get(0).granted;
        if (granted.contains(Manifest.permission.ACCESS_COARSE_LOCATION) ||
                granted.contains(Manifest.permission.ACCESS_FINE_LOCATION)) {
            return true;
        }

        return false;
    }

    private void requestLocationData() {
         if (isLocationPermissionAllowed()) {
            activateLocationEngine();
            EventBus.getDefault().post(new LocationPermissionAllowed());
        } else {
            permissionManager.checkAndRequestPermissions(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates();
            locationEngine.removeLocationEngineListener(this);
            locationEngine.deactivate();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
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

    private void sendLocation(Location location) {
        EventBus.getDefault().post(new LocationUrlReady(
                MapUtils.getLocalMapBoxMapImage(this, location.getLongitude(), location.getLatitude())
        ));

        if (locationRequest != null) {
            EventBus.getDefault().post(new StringDataCollected(
                    String.format("%s, %s, %s", locationRequest.getCity() == null? "" : locationRequest.getCity(),
                            locationRequest.getCountry() == null? "": locationRequest.getCountry(),
                            locationRequest.getZipcode() == null? "": locationRequest.getZipcode())
            ));
        }
    }

    private void showLoadingDialog() {
        mLoadingDialog.show();
    }

    private void trySubmitUserInfo() {
        if (isLocationPermissionAllowed()) {
            BaseApplication.getInstance().getJobManager().addJobInBackground(new
                    UpdateUserInfoJob(locationRequest, gender, displayName, nickname, birthday,
                        null, profileUrl, null, hobbies));

            showLoadingDialog();
        } else {
            Toast.makeText(this, getResources().getString(R.string.pls_provide_location_permission),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            if (locationRequest == null) {
                BaseApplication.getInstance().getJobManager().addJobInBackground(
                        new GetPlaceAttributesByLatLongJob(location.getLatitude(), location.getLongitude(), LOCATION_REQUEST)
                );
            }

            if (isWaitingData) {
                sendLocation(location);
                isWaitingData = false;
            }
        }
    }

    @Subscribe
    public void onEvent(LocationURLRequest request) {
        isWaitingData = true;
        requestLocationData();
    }

    @Subscribe
    public void onEvent(UserInfoRequest request) {
        trySubmitUserInfo();
    }

    @Subscribe
    public void onEvent(JobFailureEvBusRequest failure) {
        mLoadingDialog.dismiss();
        Toast.makeText(this, getResources().getString(R.string.cannot_get_address),
                Toast.LENGTH_SHORT).show();
    }


    @Subscribe
    public void onEvent(UserBasicInfoCollected info) {
        profileUrl = info.getProfileUrl();
        gender = info.getGender();
        displayName = info.getDisplayName();
        birthday = info.getBirthday();
    }

    @Subscribe
    public void onEvent(CompleteLoginRequest request) {
        mLoadingDialog.dismiss();
        AppUtils.signInAnonymously(this);
        SharedPreferencesManager.getDefault().setRequestUpdateInfo(false);

        Intent intent = new Intent(CollectUserInfoActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Subscribe
    public void onEvent(UserNicknameCollected info) {
        nickname = info.getNickname();
    }

    @Subscribe
    public void onEvent(TagsCollected topics) {
        hobbies = topics.getTags();
    }

    @Subscribe
    public void onEvent(ServerErrorRequest errorRequest) {

    }

    @Subscribe
    public void onEvent(LocationRequestCollected requestCollected) {
        if (requestCollected.getTag().equals(LOCATION_REQUEST)) {
            locationRequest = requestCollected.getRequest();
        }
    }
}
