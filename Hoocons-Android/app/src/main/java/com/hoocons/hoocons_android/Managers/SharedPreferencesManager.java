package com.hoocons.hoocons_android.Managers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hNguyen on 6/5/2017.
 */
public class SharedPreferencesManager {
    private final String FCMTOKEN = "FCMTOKEN";
    private final String FCMTOKEN_OLD = "TOKENOLD";

    private SharedPreferences sharedPreferences;
    private int PRIVATE_MODE = 0;

    // App behavior manager
    private final String IS_FIRST_LAUNCH = "is_first_launch";

    // User management fields
    private final String KEY_PREF = "5p46h@7";
    private final String USER_TOKEN = "USER_TOKEN";
    private final String REQUEST_UPDATE_INFO = "REQUEST_UPDATE_INFO";
    private final String USER_NAME = "USERNAME";

    /* ******************************************
    * APPLICATION INITIALIZATION PREFERENCES
    * *******************************************/
    private static SharedPreferencesManager instance;
    private boolean isFirstLaunch;

    private SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(
                KEY_PREF, Context.MODE_PRIVATE
        );

        isFirstLaunch = true;
    }

    public static void init(Context context) {
        if (instance == null)
            instance = new SharedPreferencesManager(context);
    }

    public static SharedPreferencesManager getDefault() {
        return instance;
    }





    /* ******************************************
    * USER CONTENTS PREFERENCES
    * *******************************************/

    public void setUserToken(String token) {
        sharedPreferences.edit().putString(USER_TOKEN, token).apply();
    }

    public String getUserToken() {
        return sharedPreferences.getString(USER_TOKEN, null);
    }

    public void setRequestUpdateInfo(boolean isNeed) {
        sharedPreferences.edit().putBoolean(REQUEST_UPDATE_INFO, isNeed).apply();
    }

    public boolean isNeededToRequestInfo() {
        return sharedPreferences.getBoolean(REQUEST_UPDATE_INFO, true);
    }

    public void setUserName(String name){
        sharedPreferences.edit().putString(USER_NAME, name).apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(USER_NAME, "");
    }

    /* ******************************************
    * APPLICATION BEHAVIOR PREFERENCES
    * *******************************************/
    public void setIsFirstLaunch(boolean isFirstLaunch) {
        sharedPreferences.edit().putBoolean(IS_FIRST_LAUNCH, isFirstLaunch).apply();
    }

    public boolean isFirstLaunch() {
        return sharedPreferences.getBoolean(IS_FIRST_LAUNCH, true);
    }
}
