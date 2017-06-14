package com.hoocons.hoocons_android.Managers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hNguyen on 6/5/2017.
 */
public class SharedPreferencesManager {
    private final String TOKEN = "TOKEN";
    private final String FCMTOKEN = "FCMTOKEN";
    private final String FCMTOKEN_OLD = "TOKENOLD";
    private final String KEY_PREF = "5p46h@7";

    private SharedPreferences sharedPreferences;
    private int PRIVATE_MODE = 0;

    // App behavior manager
    private final String IS_FIRST_LAUNCH = "is_first_launch";

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

    public void setIsFirstLaunch(boolean isFirstLaunch) {
        sharedPreferences.edit().putBoolean(IS_FIRST_LAUNCH, isFirstLaunch).apply();
    }

    public boolean isFirstLaunch() {
        return sharedPreferences.getBoolean(IS_FIRST_LAUNCH, true);
    }
}
