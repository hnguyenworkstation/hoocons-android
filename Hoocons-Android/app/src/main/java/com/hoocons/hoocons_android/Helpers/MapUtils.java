package com.hoocons.hoocons_android.Helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Networking.Requests.LocationRequest;
import com.hoocons.hoocons_android.Networking.Responses.CoordinateResponse;
import com.hoocons.hoocons_android.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.services.Constants;
import com.mapbox.services.api.staticimage.v1.MapboxStaticImage;
import com.mapbox.services.api.staticimage.v1.models.StaticMarkerAnnotation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by hungnguyen on 7/18/17.
 */

public class MapUtils {
    private static final int REQUEST_LOCATION_PERMISSION = 222;

    public static boolean verifyConnection(Context context) {
        boolean isConnected;
        ConnectivityManager conectivtyManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        isConnected = conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected();
        return isConnected;
    }

    public static LatLngBounds getCurrentLatLngBound(final Location currentLocation) {
        double longitude = currentLocation.getLongitude();
        double latitude = currentLocation.getLatitude();

        double radiusDegrees = 1.0;
        LatLng northEast = new LatLng(latitude + radiusDegrees, longitude + radiusDegrees);
        LatLng southWest = new LatLng(latitude - radiusDegrees, longitude - radiusDegrees);
        LatLngBounds bounds = LatLngBounds.builder()
                .include(northEast)
                .include(southWest)
                .build();

        return bounds;
    }

    public static String getMapLocationUrl(String latitudeFinal, String longitudeFinal) {
        return "https://maps.googleapis.com/maps/api/staticmap?center=" + latitudeFinal + ","
                + longitudeFinal + "&zoom=13&size=1290x720&markers=color:red|" + latitudeFinal + "," + longitudeFinal;
    }

    public static String getLocalPlaceQuery(String latitude, String longitude) {
        return String.format("https://maps.googleapis.com/maps/api/place/nearbysearch/" +
                        "json?location=%s,%s&radius=50000&key=%s", latitude, longitude,
                BaseApplication.getInstance().getGoogleServiceKey());
    }

    public static String getPlaceTypeQuery() {
        return "liquor_store, bar, beauty_salon, bus_station, dentist, department_store, restaurant, rv_park, " +
                "hair_care, university, taxi_stand, shopping_mall, shoe_store, school, courthouse, pharmacy, night_club, " +
                "cafe, meal_delivery, liquor_store, library";
    }

    public static Location getGpsLocation(Activity activity) {
        LocationManager lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);

        Location l = null;

        for (int i = providers.size() - 1; i >= 0; i--) {
            try {
                l = lm.getLastKnownLocation(providers.get(i));
                if (l != null) break;
            } catch (SecurityException e) {
                return null;
            }
        }

        return l;
    }

    public static String getLocalMapBoxMapImage(final Activity activity,
                                                 double longitude, double latitude) {
        MapboxStaticImage staticImage = new MapboxStaticImage.Builder()
                .setAccessToken(getMapboxAccessToken(activity))
                .setUsername(Constants.MAPBOX_USER)
                .setStyleId(Constants.MAPBOX_STYLE_STREETS)
                .setLon(longitude)
                .setLat(latitude)
                .setZoom(16)
                .setBearing(45)
                .setStaticMarkerAnnotations(new StaticMarkerAnnotation.Builder()
                        .setColor("E84545")
                        .setName(Constants.PIN_LARGE)
                        .setLon(longitude)
                        .setLat(latitude)
                        .build())
                .setWidth(SystemUtils.getScreenWidth(activity))
                .setHeight(512)
                .setRetina(false)
                .build();
        return staticImage.getUrl().toString();
    }

    public static String getMapboxAccessToken(@NonNull Context context) {
        try {
            // Read out AndroidManifest
            String token = Mapbox.getAccessToken();
            if (token == null || token.isEmpty()) {
                throw new IllegalArgumentException();
            }
            return token;
        } catch (Exception exception) {
            // Use fallback on string resource, used for development
            int tokenResId = context.getResources()
                    .getIdentifier("mapbox_access_token", "string", context.getPackageName());
            return tokenResId != 0 ? context.getString(tokenResId) : null;
        }
    }

    /**
     * Demonstrates converting any Drawable to an Icon, for use as a marker icon.
     */
    public static Icon drawableToIcon(@NonNull Context context, @DrawableRes int id) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(context.getResources(), id, context.getTheme());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return IconFactory.getInstance(context).fromBitmap(bitmap);
    }

    public static LocationRequest getLocationFromLatLong(final Context context, double LATITUDE, double LONGITUDE) {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String province = addresses.get(0).getPremises();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                LocationRequest currentLocation = new LocationRequest(new CoordinateResponse(LATITUDE, LONGITUDE),
                        knownName, city, province, state, postalCode, country, address, null, null);
                return currentLocation;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
