package com.hoocons.hoocons_android.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.hoocons.hoocons_android.Adapters.GeocoderAdapter;
import com.hoocons.hoocons_android.Helpers.PermissionManager;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.R;
import com.mapbox.geocoder.service.models.GeocoderFeature;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.telemetry.location.LocationEngine;
import com.mapbox.services.android.telemetry.location.LocationEngineListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapBoxPlaceSearchActivity extends BaseActivity implements LocationEngineListener {
    @BindView(R.id.query)
    AutoCompleteTextView autocomplete;
    @BindView(R.id.mapview)
    MapView mapView;

    private MapboxMap mapboxMap;
    private PermissionManager permissionManager;
    private LocationEngine locationEngine;
    private Bundle bundle;
    private Location currentLocation;

    private boolean isWaiting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_box_place_search);
        bundle = savedInstanceState;
        ButterKnife.bind(this);

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

        // Custom adapter
        final GeocoderAdapter adapter = new GeocoderAdapter(this);
        autocomplete.setLines(1);
        autocomplete.setAdapter(adapter);
        autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GeocoderFeature result = adapter.getItem(position);
                autocomplete.setText(result.getText());
                updateMap(result.getLatitude(), result.getLongitude());
            }
        });

        // Add clear button to autocomplete
        final Drawable imgClearButton = getResources().getDrawable(R.drawable.places_ic_clear);
        autocomplete.setCompoundDrawablesWithIntrinsicBounds(null, null, imgClearButton, null);
        autocomplete.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AutoCompleteTextView et = (AutoCompleteTextView) v;
                if (et.getCompoundDrawables()[2] == null)
                    return false;
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                if (event.getX() > et.getWidth() - et.getPaddingRight() - imgClearButton.getIntrinsicWidth()) {
                    autocomplete.setText("");
                }
                return false;
            }
        });

        if (isLocationPermissionAllowed()) {
            activateLocationEngine();
            initMap(savedInstanceState);
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

    private void initMap(Bundle savedInstanceState) {
        mapView.setStyleUrl(Style.MAPBOX_STREETS);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap m) {
                mapboxMap = m;
                isWaiting = true;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        permissionManager.checkResult(requestCode, permissions, grantResults);
        ArrayList<String> granted = permissionManager.getStatus().get(0).granted;

        if (granted.contains(android.Manifest.permission.ACCESS_FINE_LOCATION)
                || granted.contains(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
            activateLocationEngine();
            initMap(bundle);
        }
    }

    private void activateLocationEngine() {
        locationEngine.addLocationEngineListener(this);
        locationEngine.activate();
    }

    private void initCurrentLocation() {
        if (mapboxMap != null && currentLocation != null) {
            updateMap(currentLocation.getLatitude(), currentLocation.getLongitude());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            currentLocation = location;
            if (isWaiting) {
                initCurrentLocation();
                isWaiting = false;
            }
        }
    }

    private void updateMap(double latitude, double longitude) {
        if (mapboxMap != null) {
            mapboxMap.removeAnnotations();

            mapboxMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title("Geocoder result")
            );

            // Animate map
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(latitude, longitude))
                    .zoom(13)
                    .build();

            mapboxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                    5000, null);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
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
}
