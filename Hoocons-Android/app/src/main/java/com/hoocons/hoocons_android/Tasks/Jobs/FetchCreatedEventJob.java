package com.hoocons.hoocons_android.Tasks.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.hoocons.hoocons_android.EventBus.FetchEventListSuccessEvBusRequest;
import com.hoocons.hoocons_android.EventBus.JobFailureEvBusRequest;
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

public class FetchCreatedEventJob extends Job implements Serializable{
    private int start;
    private int end;
    public FetchCreatedEventJob(int start, int end) {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy(JobGroup.event));

        this.start = start;
        this.end = end;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        EventServices services = NetContext.instance.create(EventServices.class);
        services.getCreatedEvent(start, end).enqueue(new Callback<List<EventResponse>>() {
            @Override
            public void onResponse(Call<List<EventResponse>> call, Response<List<EventResponse>> response) {
                if (response.code() == 200) {
                    // Get something
                    EventBus.getDefault().post(new FetchEventListSuccessEvBusRequest(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<EventResponse>> call, Throwable t) {
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
