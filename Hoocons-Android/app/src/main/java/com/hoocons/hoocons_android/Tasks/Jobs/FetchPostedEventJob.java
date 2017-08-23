package com.hoocons.hoocons_android.Tasks.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.hoocons.hoocons_android.EventBus.FetchEventListSuccessEvBusRequest;
import com.hoocons.hoocons_android.EventBus.JobFailureEvBusRequest;
import com.hoocons.hoocons_android.Networking.ApiViewSets.EventsApiViewSet;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Responses.EventResponse;
import com.hoocons.hoocons_android.Networking.Services.EventServices;
import com.hoocons.hoocons_android.Tasks.JobProperties.JobGroup;
import com.hoocons.hoocons_android.Tasks.JobProperties.Priority;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hungnguyen on 7/15/17.
 */

public class FetchPostedEventJob extends Job implements Serializable{
    private int userId;
    private int pageNum;

    public FetchPostedEventJob(int userId, int pageNum) {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy(JobGroup.event));

        this.userId = userId;
        this.pageNum = pageNum;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        EventServices services = NetContext.instance.create(EventServices.class);
        services.getEventPosted(userId, pageNum).enqueue(new Callback<EventsApiViewSet>() {
            @Override
            public void onResponse(Call<EventsApiViewSet> call, Response<EventsApiViewSet> response) {
                if (response.code() == 200) {
                    // Get something
                    EventBus.getDefault().post(new FetchEventListSuccessEvBusRequest(response.body()));
                } else if (response.code() == 404) {

                }
            }

            @Override
            public void onFailure(Call<EventsApiViewSet> call, Throwable t) {
                EventBus.getDefault().post(new JobFailureEvBusRequest());
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
