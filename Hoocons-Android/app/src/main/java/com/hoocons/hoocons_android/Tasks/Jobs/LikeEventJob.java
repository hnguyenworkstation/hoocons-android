package com.hoocons.hoocons_android.Tasks.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Services.EventServices;
import com.hoocons.hoocons_android.Tasks.JobProperties.JobGroup;
import com.hoocons.hoocons_android.Tasks.JobProperties.Priority;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hungnguyen on 7/18/17.
 */

public class LikeEventJob extends Job implements Serializable {
    private static final int JOB_DELAY = 10000;
    private int eventId;

    public LikeEventJob(String tag, int eventId) {
        super(new Params(Priority.LOW).requireNetwork().persist()
                .groupBy(JobGroup.event).addTags(tag).delayInMs(JOB_DELAY));
        this.eventId = eventId;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        EventServices services = NetContext.instance.create(EventServices.class);
        services.likeEvent(eventId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

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
