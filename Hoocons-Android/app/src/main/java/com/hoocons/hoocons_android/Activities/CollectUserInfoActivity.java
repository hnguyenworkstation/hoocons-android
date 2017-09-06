package com.hoocons.hoocons_android.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.hoocons.hoocons_android.EventBus.LocationPermissionAllowed;
import com.hoocons.hoocons_android.EventBus.LocationPermissionDenied;
import com.hoocons.hoocons_android.EventBus.LocationURLRequest;
import com.hoocons.hoocons_android.EventBus.LocationUrlReady;
import com.hoocons.hoocons_android.EventBus.TagsCollected;
import com.hoocons.hoocons_android.EventBus.UserBasicInfoCollected;
import com.hoocons.hoocons_android.EventBus.UserNicknameCollected;
import com.hoocons.hoocons_android.Helpers.MapUtils;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewFragments.CollectUserLocationFragment;
import com.karan.churi.PermissionManager.PermissionManager;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class CollectUserInfoActivity extends BaseActivity implements LocationEngineListener{
    private FragmentTransaction mFragTransition;
    private FragmentManager mFragManager;

    private String profileUrl;
    private String gender;
    private String displayName;
    private String nickname;
    private List<String> hobbies;

    private Location currentLocation;
    private PermissionManager permissionManager;
    private LocationEngine locationEngine;
    private boolean isWaitingData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_user_info);
        EventBus.getDefault().register(this);

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

        mFragTransition.replace(R.id.info_fragment_container, new CollectUserLocationFragment());
        mFragTransition.commit();

        permissionManager.checkAndRequestPermissions(this);
    }

    private void activateLocationEngine() {
        locationEngine.addLocationEngineListener(this);
        locationEngine.activate();
    }

    @Subscribe
    public void onEvent(UserBasicInfoCollected info) {
        profileUrl = info.getProfileUrl();
        gender = info.getGender();
        displayName = info.getDisplayName();
    }

    @Subscribe
    public void onEvent(UserNicknameCollected info) {
        nickname = info.getNickname();
    }

    @Subscribe
    public void onEvent(TagsCollected topics) {
        hobbies = topics.getTags();
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

    private void requestLocationData() {
        ArrayList<String> granted = permissionManager.getStatus().get(0).granted;
        if (granted.contains(Manifest.permission.ACCESS_COARSE_LOCATION) ||
                granted.contains(Manifest.permission.ACCESS_FINE_LOCATION)) {
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
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            currentLocation = location;
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
}
