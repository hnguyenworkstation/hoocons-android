package com.hoocons.hoocons_android.Helpers;

import android.content.Context;
import android.net.ConnectivityManager;

import com.hoocons.hoocons_android.Managers.BaseApplication;

/**
 * Created by hungnguyen on 7/18/17.
 */

public class MapUtils {
    public static boolean verifyConnection(Context context) {
        boolean isConnected;
        ConnectivityManager conectivtyManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        isConnected = conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected();
        return isConnected;
    }

    public static String getMapLocationUrl(String latitudeFinal, String longitudeFinal) {
        return "https://maps.googleapis.com/maps/api/staticmap?center=" + latitudeFinal + ","
                + longitudeFinal +"&zoom=13&size=1290x720&markers=color:red|" + latitudeFinal + "," + longitudeFinal;
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
}
