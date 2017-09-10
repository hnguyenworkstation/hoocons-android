package com.hoocons.hoocons_android.Tasks.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.hoocons.hoocons_android.EventBus.BadRequest;
import com.hoocons.hoocons_android.EventBus.FetchOwnedChannelsComplete;
import com.hoocons.hoocons_android.EventBus.ServerErrorRequest;
import com.hoocons.hoocons_android.Networking.ApiViewSets.ChannelApiViewSet;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Services.ChannelServices;
import com.hoocons.hoocons_android.Tasks.JobProperties.JobGroup;
import com.hoocons.hoocons_android.Tasks.JobProperties.Priority;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hungnguyen on 9/10/17.
 */

public class FetchOwnedChannelJob extends Job {
    public FetchOwnedChannelJob() {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy(JobGroup.channel));
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        ChannelServices services = NetContext.instance.create(ChannelServices.class);
        services.getOwnedChannel().enqueue(new Callback<ChannelApiViewSet>() {
            @Override
            public void onResponse(Call<ChannelApiViewSet> call, Response<ChannelApiViewSet> response) {
                if (response.code() == 200) {
                    EventBus.getDefault().post(new FetchOwnedChannelsComplete(response.body()));
                }
            }

            @Override
            public void onFailure(Call<ChannelApiViewSet> call, Throwable t) {
                EventBus.getDefault().post(new ServerErrorRequest());
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
