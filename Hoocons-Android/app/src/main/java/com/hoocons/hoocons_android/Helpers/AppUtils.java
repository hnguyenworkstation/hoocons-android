package com.hoocons.hoocons_android.Helpers;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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
}
