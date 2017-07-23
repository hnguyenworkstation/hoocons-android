package com.hoocons.hoocons_android.Helpers;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.R;

import org.aisen.android.common.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import me.iwf.photopicker.PhotoPicker;

/**
 * Created by hungnguyen on 6/17/17.
 */

public class AppUtils {
    public static void startImagePicker(Activity activity, int maxNum, int code) {
        PhotoPicker.builder()
                .setPhotoCount(maxNum)
                .setGridColumnCount(3)
                .setPreviewEnabled(false)
                .setShowCamera(true)
                .setShowGif(true)
                .start(activity, code);
    }

    public static void startImagePickerFromFragment(Context context, Fragment fragment, int maxNum, int code) {
        PhotoPicker.builder()
                .setPhotoCount(maxNum)
                .setGridColumnCount(3)
                .setPreviewEnabled(false)
                .setShowCamera(true)
                .setShowGif(true)
                .start(context, fragment, code);
    }

    public static void signInAnonymously(Activity activity) {
        // Sign in anonymously. Authentication is required to read or write from Firebase Storage.
        FirebaseAuth.getInstance().signInAnonymously()
                .addOnSuccessListener(activity, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d("Login Anonymously", "signInAnonymously:SUCCESS");
                    }
                })
                .addOnFailureListener(activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("Login Anonymously", "signInAnonymously:FAILURE", exception);
                    }
                });
    }

    public static String getDefaultProfileUrl() {
        return "http://res.cloudinary.com/dumfykuvl/image/upload/v1493749974/images_lm0sjf.jpg";
    }

    public static String getRandomSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    @SuppressWarnings("deprecation")
    public static String convDate(String time) {
        try {
            Context context = BaseApplication.getInstance().getApplicationContext();
            Resources res = context.getResources();

            StringBuffer buffer = new StringBuffer();
            Calendar createCal = Calendar.getInstance();
            if (time.length() == 13) {
                try {
                    createCal.setTimeInMillis(Long.parseLong(time));
                } catch (Exception e) {
                    createCal.setTimeInMillis(Date.parse(time));
                }
            }
            else {
                createCal.setTimeInMillis(Date.parse(time));
            }

            Calendar currentcal = Calendar.getInstance();
            currentcal.setTimeInMillis(System.currentTimeMillis());

            long diffTime = (currentcal.getTimeInMillis() - createCal.getTimeInMillis()) / 1000;

            if (currentcal.get(Calendar.MONTH) == createCal.get(Calendar.MONTH)) {
                if (currentcal.get(Calendar.DAY_OF_MONTH) == createCal.get(Calendar.DAY_OF_MONTH)) {
                    if (diffTime < 3600 && diffTime >= 60) {
                        buffer.append((diffTime / 60) + res.getString(R.string.msg_few_minutes_ago));
                    } else if (diffTime < 60) {
                        buffer.append(res.getString(R.string.msg_now));
                    } else {
                        buffer.append(res.getString(R.string.msg_today)).append(" ")
                                .append(DateUtils.formatDate(createCal.getTimeInMillis(), "HH:mm"));
                    }
                }
                else if (currentcal.get(Calendar.DAY_OF_MONTH) - createCal.get(Calendar.DAY_OF_MONTH) == 1) {
                    buffer.append(res.getString(R.string.msg_yesterday)).append(" ").append(DateUtils.formatDate(createCal.getTimeInMillis(), "HH:mm"));
                }
            }

            if (buffer.length() == 0) {
                buffer.append(DateUtils.formatDate(createCal.getTimeInMillis(), "MM-dd HH:mm"));
            }

            String timeStr = buffer.toString();
            if (currentcal.get(Calendar.YEAR) != createCal.get(Calendar.YEAR)) {
                timeStr = createCal.get(Calendar.YEAR) + " " + timeStr;
            }

            return timeStr;
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return time;
    }

    public static void copyToClipboard(String text) {
        try {
            ClipboardManager cmb = (ClipboardManager) GlobalContext.getInstance()
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setPrimaryClip(ClipData.newPlainText(null, text.trim()));
        } catch (Exception e) {
        }
    }
}
