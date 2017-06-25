package com.hoocons.hoocons_android.Managers;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.hoocons.hoocons_android.CustomUI.FontOverride;
import com.hoocons.hoocons_android.SQLite.EmotionsDB;

import org.aisen.android.common.context.GlobalContext;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by hungnguyen on 6/3/17.
 */
public class BaseApplication extends GlobalContext {
    public static final String TAG = BaseApplication.class
            .getSimpleName();
    private static BaseApplication mInstance;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = BaseApplication.this;
        context = getApplicationContext();

        SharedPreferencesManager.init(this);

        /* Fixing URI exposed */
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }

        // Override font
        TypefaceProvider.registerDefaultIconSets();
        FontOverride.setDefaultFont(this, "DEFAULT", "fonts/Roboto-Regular.ttf");
        FontOverride.setDefaultFont(this, "MONOSPACE", "fonts/Roboto-Light.ttf");

        try {
            EmotionsDB.checkEmotions();
        } catch (Exception e) {
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized BaseApplication getInstance() {
        return mInstance;
    }

    public static synchronized BaseApplication context() {
        return (BaseApplication) context;
    }

}
