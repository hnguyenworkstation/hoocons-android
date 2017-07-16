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

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

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

    private static SecretKey generateKey(String text)
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        return new SecretKeySpec(text.getBytes(), "AES");
    }

    public static String encryptMsg(String message)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException,
            UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException
    {
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, generateKey(message));
        byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
        return cipherText.toString();
    }

}
