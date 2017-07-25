package com.hoocons.hoocons_android.Helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hoocons.hoocons_android.CustomUI.CustomTextView;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Networking.Responses.EventResponse;
import com.hoocons.hoocons_android.Parcel.EventParcel;
import com.hoocons.hoocons_android.R;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;

import org.aisen.android.common.utils.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Pattern;

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

    public static void catchLinksOnTextView(final Context context, CustomTextView textView) {
        // Add the links and make the links clickable
        LinkBuilder.on(textView)
                .addLinks(getAvailableLinks(context))
                .build();
    }

    private static List<Link> getAvailableLinks(final Context context) {
        List<Link> links = new ArrayList<>();

        // create a single click link to the matched twitter profiles
        Link mentions = new Link(Pattern.compile("@\\w{1,15}"));
        mentions.setTextColor(Color.parseColor("#00BCD4"));
        mentions.setHighlightAlpha(.4f);
        mentions.setUnderlined(false);
        mentions.setOnLongClickListener(new Link.OnLongClickListener() {
            @Override
            public void onLongClick(String clickedText) {

            }
        });
        mentions.setOnClickListener(new Link.OnClickListener() {
            @Override
            public void onClick(String clickedText) {
                Toast.makeText(context, clickedText, Toast.LENGTH_SHORT).show();
            }
        });

        // Getting available tags
        Link tags = new Link(Pattern.compile("(?<=[\\s>])#(\\d*[A-Za-z0-9]+\\d*)\\b(?!;)"));
        tags.setTextColor(context.getResources().getColor(R.color.subject));
        tags.setHighlightAlpha(.4f);
        tags.setUnderlined(false);
        tags.setBold(true);
        tags.setOnLongClickListener(new Link.OnLongClickListener() {
            @Override
            public void onLongClick(String clickedText) {

            }
        });
        tags.setOnClickListener(new Link.OnClickListener() {
            @Override
            public void onClick(String clickedText) {
                Toast.makeText(context, clickedText, Toast.LENGTH_SHORT).show();
            }
        });


        // Getting available tags
        Link urls = new Link(Pattern.compile("^(http:/\\/|https:\\/\\/)?(www.)?([a-zA-Z0-9]+).[a-zA-Z0-9]*.[\u200C\u200Ba-z]{3}\\.([a-z]+)?$"));
        urls.setTextColor(context.getResources().getColor(R.color.timestamp));
        urls.setHighlightAlpha(.4f);
        urls.setUnderlined(false);
        urls.setOnLongClickListener(new Link.OnLongClickListener() {
            @Override
            public void onLongClick(String clickedText) {

            }
        });
        urls.setOnClickListener(new Link.OnClickListener() {
            @Override
            public void onClick(String clickedText) {
                Toast.makeText(context, clickedText, Toast.LENGTH_SHORT).show();
            }
        });

        links.add(mentions);
        links.add(tags);
        links.add(urls);

        return links;
    }

    public static EventParcel getEventParcel(final EventResponse response) {
        EventParcel parcel = new EventParcel();
        parcel.setId(response.getEventId());
        parcel.setTextContent(response.getTextContent());
        parcel.setUserInfo(response.getUserInfo());
        parcel.setEventType(response.getEventType());
        parcel.setReported(response.isReported());

        return parcel;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getCurrentUTCTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("gmt"));
        
        return df.format(new Date());
    }

    @SuppressLint("SimpleDateFormat")
    public static String convertDateTimeFromUTC(String time) {
        try {
            Calendar cal = Calendar.getInstance();
            TimeZone tz = cal.getTimeZone();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

            SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            output.setTimeZone(tz);

            Date d = sdf.parse(time);

            return convDate(String.valueOf(d.getTime()));
        } catch (ParseException e) {
            return BaseApplication.getInstance().getBaseContext().getResources().getString(R.string.in_the_past);
        }
    }

    @SuppressWarnings("deprecation")
    private static String convDate(String time) {
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
                        buffer.append(diffTime / 60).append(" ").append(res.getString(R.string.msg_few_minutes_ago));
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
