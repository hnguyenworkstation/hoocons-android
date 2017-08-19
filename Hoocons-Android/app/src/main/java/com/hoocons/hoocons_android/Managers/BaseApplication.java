package com.hoocons.hoocons_android.Managers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.log.CustomLogger;
import com.birbit.android.jobqueue.scheduling.FrameworkJobSchedulerService;
import com.birbit.android.jobqueue.scheduling.GcmJobSchedulerService;
import com.facebook.FacebookSdk;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hoocons.hoocons_android.CustomUI.FontOverride;
import com.hoocons.hoocons_android.EventBus.MeetOutPostFailed;
import com.hoocons.hoocons_android.EventBus.MeetOutPostedSuccess;
import com.hoocons.hoocons_android.EventBus.PostEventSuccess;
import com.hoocons.hoocons_android.EventBus.PostingJobAddedToDisk;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.SQLite.EmotionsDB;
import com.hoocons.hoocons_android.SQLite.StickerDB;
import com.hoocons.hoocons_android.Tasks.JobServices.HooconsGCMJobService;
import com.hoocons.hoocons_android.Tasks.JobServices.HooconsJobService;

import org.aisen.android.common.context.GlobalContext;
import org.aisen.android.component.bitmaploader.BitmapLoader;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hungnguyen on 6/3/17.
 */
public class BaseApplication extends GlobalContext {
    public static final String TAG = BaseApplication.class
            .getSimpleName();
    private static BaseApplication mInstance;
    private static Context context;
    private JobManager jobManager;
    private AmazonS3Client s3Client;
    private DatabaseReference mDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);

        mInstance = BaseApplication.this;
        context = getApplicationContext();

        SharedPreferencesManager.init(this);

        /* Fixing URI exposed */
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }

        // Initialize Stickers object list
        StickerDB.initMonkeyStickerHashMap();

        // Initializing facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        FirebaseApp.initializeApp(this);

        // Init bitmap
        BitmapLoader.newInstance(this, getImagePath());

        // Override font
        TypefaceProvider.registerDefaultIconSets();
        FontOverride.setDefaultFont(this, "DEFAULT", "fonts/Roboto-Regular.ttf");
        FontOverride.setDefaultFont(this, "MONOSPACE", "fonts/Roboto-Light.ttf");

        try {
            EmotionsDB.checkEmotions();
        } catch (Exception e) {
        }

        getJobManager();
    }

    public static String getImagePath() {
        return GlobalContext.getInstance()
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator;
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

    private void configureJobManager() {
        Configuration.Builder builder = new Configuration.Builder(this)
                .customLogger(new CustomLogger() {
                    private static final String TAG = "JOBS";
                    @Override
                    public boolean isDebugEnabled() {
                        return true;
                    }

                    @Override
                    public void d(String text, Object... args) {
                        Log.d(TAG, String.format(text, args));
                    }

                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        Log.e(TAG, String.format(text, args), t);
                    }

                    @Override
                    public void e(String text, Object... args) {
                        Log.e(TAG, String.format(text, args));
                    }

                    @Override
                    public void v(String text, Object... args) {

                    }
                })
                .minConsumerCount(1) //always keep at least one consumer alive
                .maxConsumerCount(3) //up to 3 consumers at a time
                .loadFactor(3) //3 jobs per consumer
                .consumerKeepAlive(120);//wait 2 minute
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.scheduler(FrameworkJobSchedulerService.createSchedulerFor(this,
                    HooconsJobService.class), true);
        } else {
            int enableGcm = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
            if (enableGcm == ConnectionResult.SUCCESS) {
                builder.scheduler(GcmJobSchedulerService.createSchedulerFor(this,
                        HooconsGCMJobService.class), true);
            }
        }

        jobManager = new JobManager(builder.build());
    }

    public synchronized JobManager getJobManager() {
        if (jobManager == null) {
            configureJobManager();
        }
        return jobManager;
    }

    public synchronized DatabaseReference getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
        }

        return mDatabase;
    }

    public synchronized String getGoogleServiceKey() {
        return getBaseContext().getResources().getString(R.string.google_web_service);
    }

    public synchronized AmazonS3Client getAwsS3Client() {
        if (s3Client == null) {
            s3Client = new AmazonS3Client( new BasicAWSCredentials(
                    getApplicationContext().getResources().getString(R.string.aws_key_id),
                    getApplicationContext().getResources().getString(R.string.aws_secret)));
            s3Client.setRegion(com.amazonaws.regions.Region.getRegion(Regions.AP_SOUTHEAST_1));
        }

        return s3Client;
    }

    public static synchronized BaseApplication getInstance() {
        return mInstance;
    }

    public static synchronized BaseApplication context() {
        return (BaseApplication) context;
    }

    public String getS3AWS() {
        return getResources().getString(R.string.aws_s3);
    }

    /**/
    @Subscribe
    public void onEvent(PostEventSuccess task) {
        Toast.makeText(mInstance, "Upload event complete", Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onEvent(PostingJobAddedToDisk task) {
        Toast.makeText(mInstance, getResources().getString(R.string.posting), Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onEvent(MeetOutPostedSuccess request) {
        Toast.makeText(mInstance, getResources().getString(R.string.post_meetout_success), Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onEvent(MeetOutPostFailed request) {
        Toast.makeText(mInstance, getResources().getString(R.string.post_meetout_failed),
                Toast.LENGTH_SHORT).show();

        // Todo: Handle data in MeetOutPostFailed to allow user to modify and post again
    }
}
