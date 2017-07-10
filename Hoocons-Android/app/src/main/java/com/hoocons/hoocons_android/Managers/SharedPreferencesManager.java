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
    private final String USER_PASSWORD = "PASSWORD";
    private final String USER_NICKNAME = "USER_NICKNAME";

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

    public String getUserNickname() {
        return sharedPreferences.getString(USER_NICKNAME, "");
    }

    public void setUserNickname(String nickName){
        sharedPreferences.edit().putString(USER_NICKNAME, nickName).apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(USER_NAME, "");
    }

    public String[] getCredentials() {
        String username = sharedPreferences.getString(USER_NAME, "");
        String password = sharedPreferences.getString(USER_PASSWORD, "");
        return new String[] {username, password};
    }

    public void setCredentials(String[] credentials) {
        sharedPreferences.edit().putString(USER_NAME, credentials[0]).apply();
        sharedPreferences.edit().putString(USER_PASSWORD, credentials[1]).apply();
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
