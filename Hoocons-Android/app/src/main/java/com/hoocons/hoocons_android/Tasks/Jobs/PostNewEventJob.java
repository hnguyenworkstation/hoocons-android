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
import com.hoocons.hoocons_android.Models.Topic;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Requests.EventInfoRequest;
import com.hoocons.hoocons_android.Networking.Requests.LocationRequest;
import com.hoocons.hoocons_android.Networking.Services.EventServices;
import com.hoocons.hoocons_android.Tasks.JobProperties.JobGroup;
import com.hoocons.hoocons_android.Tasks.JobProperties.Priority;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hungnguyen on 7/12/17.
 */

public class PostNewEventJob extends Job {
    private long localId;
    private String textContent;
    private ArrayList<String> imagePaths;
    private String privacy;
    private String eventType;
    private String gifUrl;
    private int toUser;

    private LocationRequest postedLocation;
    private LocationRequest taggedLocation;
    private LocationRequest checkinLocation;

    private List<String> tags;

    public PostNewEventJob(int toUser, String text, String gifUrl, ArrayList<String> imagePaths,
                           String privacy, String eventType, LocationRequest postedLocation,
                           LocationRequest taggedLocation, LocationRequest checkinLocation,
                           List<String> tags) {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy(JobGroup.event));
        this.toUser = toUser;
        localId = -System.currentTimeMillis();
        this.textContent = text;
        this.imagePaths = imagePaths;
        this.privacy = privacy;
        this.eventType = eventType;
        this.gifUrl = gifUrl;

        this.postedLocation = postedLocation;
        this.taggedLocation = taggedLocation;
        this.checkinLocation = checkinLocation;

        this.tags = tags;
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

            ArrayList<Topic> topics = new ArrayList<>();

            for (String tag: tags) {
                topics.add(new Topic(tag));
            }

            final EventInfoRequest request = new EventInfoRequest(
                textContent, medias, topics, privacy, eventType, -1, -1, -1, toUser,
                    postedLocation, taggedLocation, checkinLocation
            );

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
