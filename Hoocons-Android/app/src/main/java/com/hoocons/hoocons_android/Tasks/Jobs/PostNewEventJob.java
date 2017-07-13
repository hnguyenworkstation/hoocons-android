package com.hoocons.hoocons_android.Tasks.Jobs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Helpers.ImageEncoder;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Models.Media;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Requests.EventInfoRequest;
import com.hoocons.hoocons_android.Networking.Services.EventServices;
import com.hoocons.hoocons_android.R;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hungnguyen on 7/12/17.
 */

public class PostNewEventJob extends Job implements Serializable {
    private long localId;
    private String textContent;
    private ArrayList<String> imagePaths;
    private String privacy;
    private String s3;

    public PostNewEventJob(String s3, String text,
                           ArrayList<String> imagePaths,
                           String privacy) {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy(JobGroup.event));
        localId = -System.currentTimeMillis();
        this.textContent = text;
        this.imagePaths = imagePaths;
        this.privacy = privacy;
        this.s3 = s3;
    }

    @Override
    public void onAdded() {
        Log.e("POST NEW EVENT JOB", "onAdded: added");
    }

    @Override
    public void onRun() throws Throwable {
        try {
            Log.e("POST NEW EVENT JOB", "onRun: get to run");

            ArrayList<Media> medias = uploadAllImage();

            EventServices services = NetContext.instance.create(EventServices.class);
            services.postEvent(new EventInfoRequest(textContent, medias, null, privacy, 0,0))
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            Log.e("GASDASd", "onResponse: asdasdasd");
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                        }
                    });

        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount,
                                                     int maxRunCount) {
        return null;
    }

    @Nullable
    private ArrayList<Media> uploadAllImage() {
        try {
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
}
