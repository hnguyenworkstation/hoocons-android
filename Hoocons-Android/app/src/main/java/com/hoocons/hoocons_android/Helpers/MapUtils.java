package com.hoocons.hoocons_android.Helpers;

import android.content.Context;
import android.net.ConnectivityManager;

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
}