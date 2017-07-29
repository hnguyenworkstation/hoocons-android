package com.hoocons.hoocons_android.Tasks.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.hoocons.hoocons_android.EventBus.PostEventSuccess;
import com.hoocons.hoocons_android.EventBus.PostingJobAddedToDisk;
import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Helpers.ImageEncoder;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Models.Media;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Requests.EventInfoRequest;
import com.hoocons.hoocons_android.Networking.Services.EventServices;
import com.hoocons.hoocons_android.Tasks.JobProperties.JobGroup;
import com.hoocons.hoocons_android.Tasks.JobProperties.Priority;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayInputStream;
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
    private String eventType;
    private String gifUrl;

    private double chkinLon;
    private double chkinLat;
    private String chkinName;
    private String chkinAddress;
    private String chkinId;

    public PostNewEventJob(String text, String gifUrl, ArrayList<String> imagePaths,
                           String privacy, String eventType, double chkinLong, double chkinLat,
                           String chkinName, String chkinAddress, String chkinId) {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy(JobGroup.event));
        localId = -System.currentTimeMillis();
        this.textContent = text;
        this.imagePaths = imagePaths;
        this.privacy = privacy;
        this.eventType = eventType;
        this.gifUrl = gifUrl;

        this.chkinLat = chkinLat;
        this.chkinId = chkinId;
        this.chkinLon = chkinLong;
        this.chkinName = chkinName;
        this.chkinAddress = chkinAddress;
    }

    @Override
    public void onAdded() {
        EventBus.getDefault().post(new PostingJobAddedToDisk());
    }

    @Override
    public void onRun() throws Throwable {
        try {
            ArrayList<Media> medias = new ArrayList<>();

            if (eventType.equals(AppConstant.EVENT_TYPE_SINGLE_GIF)) {
                medias.add(new Media(gifUrl, AppConstant.MEDIA_TYPE_GIF));
            } else {
                medias = AppUtils.uploadAllEventImage(imagePaths);
            }

            final EventInfoRequest request = new EventInfoRequest(textContent, medias, null,
                    privacy, 0, 0, eventType, true, chkinLon, chkinLat, chkinName, chkinAddress, chkinId);

            EventServices services = NetContext.instance.create(EventServices.class);
            services.postEvent(request)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.code() == 200) {
                                EventBus.getDefault().post(new PostEventSuccess());
                            }
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
}
