package com.hoocons.hoocons_android.Helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.hoocons.hoocons_android.CustomUI.CustomTextView;
import com.hoocons.hoocons_android.CustomUI.GlideBlurTransformation;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Models.Media;
import com.hoocons.hoocons_android.Networking.Responses.EventResponse;
import com.hoocons.hoocons_android.Parcel.EventParcel;
import com.hoocons.hoocons_android.R;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkBuilder;
import com.yalantis.ucrop.UCrop;

import org.aisen.android.common.utils.DateUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.CountDownLatch;
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

    public static void startCropActivity(@NonNull Activity activity, @NonNull Uri uri, String resultFileName) {
        String destinationFileName = resultFileName;
        destinationFileName += ".png";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(activity.getCacheDir(), destinationFileName)));

        uCrop.withAspectRatio(1, 1);

        // Init options
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        options.setHideBottomControls(true);

        uCrop.withOptions(options);

        uCrop.start(activity);
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
        parcel.setUserInfo(response.getAuthor());
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

    public static void loadCircleImage(final Context context, final String url, ImageView imageView, final ProgressBar progressBar) {
        Glide.with(context)
                .load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.noAnimation())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                        return false;
                    }
                })
                .into(imageView);
    }

    public static String getCurrentTimeStringFromDateTime(final int year, final int month, final int date,
                                                          final int hour, final int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, date, hour, minutes);
        Date date1 = cal.getTime();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getDefault());

        return dateFormat.format(date1);
    }

    public static String getUTCTimeStringFromDateTime(final int year, final int month, final int date,
                                                      final int hour, final int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, date, hour, minutes);
        Date date1 = cal.getTime();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("gmt"));

        return dateFormat.format(date1);
    };

    @Nullable
    public static ArrayList<Media> uploadAllEventImage(final List<String> imagePaths) {
        try {
            String s3 = BaseApplication.getInstance().getS3AWS();
            String timeStamp = String.valueOf(new Date().getTime());

            final ArrayList<Media> _uploadedImages = new ArrayList<>();
            final CountDownLatch uploadDone = new CountDownLatch(imagePaths.size());
            AmazonS3Client s3Client = BaseApplication.getInstance().getAwsS3Client();

            for (int i = 0; i < imagePaths.size(); i++) {
                String fileName = timeStamp + "_" + i + ".png";

                byte[] encodedImage = ImageEncoder.encodeImage(imagePaths.get(i));
                InputStream inputStream = new ByteArrayInputStream(encodedImage);

                ObjectMetadata meta = new ObjectMetadata();
                meta.setContentLength(encodedImage.length);
                meta.setContentType("image/png");

                PutObjectRequest por = new PutObjectRequest(s3 + "/medias",
                        fileName, inputStream, meta);
                por.setCannedAcl(CannedAccessControlList.PublicRead);

                s3Client.putObject(por);
                String _finalUrl = "https://s3-ap-southeast-1.amazonaws.com/"
                        + s3 + "/medias/" + fileName;

                uploadDone.countDown();
                _uploadedImages.add(new Media(_finalUrl, AppConstant.MEDIA_TYPE_IMAGE));
            }
            uploadDone.await();

            return _uploadedImages;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Nullable
    public static ArrayList<Media> uploadAllMeetOutImages(final List<String> imagePaths) {
        try {
            String s3 = BaseApplication.getInstance().getS3AWS();
            String timeStamp = String.valueOf(new Date().getTime());

            final ArrayList<Media> _uploadedImages = new ArrayList<>();
            final CountDownLatch uploadDone = new CountDownLatch(imagePaths.size());
            AmazonS3Client s3Client = BaseApplication.getInstance().getAwsS3Client();

            for (int i = 0; i < imagePaths.size(); i++) {
                String fileName = timeStamp + "_" + i + ".png";

                byte[] encodedImage = ImageEncoder.encodeImage(imagePaths.get(i));
                InputStream inputStream = new ByteArrayInputStream(encodedImage);

                ObjectMetadata meta = new ObjectMetadata();
                meta.setContentLength(encodedImage.length);
                meta.setContentType("image/png");

                PutObjectRequest por = new PutObjectRequest(s3 + "/meetouts",
                        fileName, inputStream, meta);
                por.setCannedAcl(CannedAccessControlList.PublicRead);

                s3Client.putObject(por);
                String _finalUrl = "https://s3-ap-southeast-1.amazonaws.com/"
                        + s3 + "/meetouts/" + fileName;

                uploadDone.countDown();
                _uploadedImages.add(new Media(_finalUrl, AppConstant.MEDIA_TYPE_IMAGE));
            }
            uploadDone.await();

            return _uploadedImages;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getSimpleMeetOutTimeFrame(final String fromDateTime, final String toDateTime) {
        return fromDateTime;
    }

    public static void loadCropImageWithProgressBar(final Context context, final String url,
                                                    final ImageView image, final ProgressBar progressBar) {
        Glide.with(context)
                .load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.noAnimation())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                        return false;
                    }
                }).into(image);
    }

    public static void loadCropBlurImageWithProgressBar(final Context context, final String url,
                                                    final ImageView image, final ProgressBar progressBar) {
        Glide.with(context)
                .load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.bitmapTransform(new MultiTransformation<Bitmap>(new CenterCrop(),
                        new GlideBlurTransformation(context, 248))))
                .apply(RequestOptions.noAnimation())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                        return false;
                    }
                }).into(image);
    }

    public static void loadCropSquareImageFromUri(final Context context, final Uri uri,
                                                  final ImageView imageView, final ProgressBar progressBar) {
        Glide.with(context)
                .load(new File(uri.getPath()))
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                        return false;
                    }
                })
                .into(imageView);
    }
}
