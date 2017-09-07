package com.hoocons.hoocons_android.Tasks.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.hoocons.hoocons_android.EventBus.PostEventSuccess;
import com.hoocons.hoocons_android.EventBus.PostingJobAddedToDisk;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.Models.Topic;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Requests.EventInfoRequest;
import com.hoocons.hoocons_android.Networking.Requests.LocationRequest;
import com.hoocons.hoocons_android.Networking.Services.EventServices;
import com.hoocons.hoocons_android.Tasks.JobProperties.JobGroup;
import com.hoocons.hoocons_android.Tasks.JobProperties.Priority;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hungnguyen on 7/24/17.
 */

public class ShareNewEventJob extends Job implements Serializable {
    private String textContent;
    private String privacy;
    private String eventType;
    private int shareEventId;
    private List<String> tags;

    private LocationRequest postedLocation;
    private LocationRequest taggedLocation;
    private LocationRequest checkinLocation;

    public ShareNewEventJob(String textContent, String privacy,
                            String eventType, int shareEventId, LocationRequest postedLocation,
                            LocationRequest taggedLocation,
                            LocationRequest checkinLocation, List<String> tags) {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy(JobGroup.event));
        this.textContent = textContent;
        this.privacy = privacy;
        this.eventType = eventType;
        this.shareEventId = shareEventId;

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
        ArrayList<Topic> topics = new ArrayList<>();
        for (String tag: tags) {
            topics.add(new Topic(tag));
        }

        final EventInfoRequest request = new EventInfoRequest(
                textContent, null, topics, privacy, eventType, -1, -1, -1,
                SharedPreferencesManager.getDefault().getUserId(),
                postedLocation, taggedLocation, checkinLocation
        );

        EventServices services = NetContext.instance.create(EventServices.class);
        services.shareEvent(shareEventId, request).enqueue(new Callback<Void>() {
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
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}
