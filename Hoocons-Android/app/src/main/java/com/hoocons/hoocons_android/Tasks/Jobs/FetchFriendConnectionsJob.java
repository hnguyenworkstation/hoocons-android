package com.hoocons.hoocons_android.Tasks.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.hoocons.hoocons_android.EventBus.FetchRelationshipComplete;
import com.hoocons.hoocons_android.EventBus.ServerErrorRequest;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Responses.RelationshipResponse;
import com.hoocons.hoocons_android.Networking.Services.RelationshipServices;
import com.hoocons.hoocons_android.Tasks.JobProperties.JobGroup;
import com.hoocons.hoocons_android.Tasks.JobProperties.Priority;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hungnguyen on 8/26/17.
 */
public class FetchFriendConnectionsJob extends Job {
    public FetchFriendConnectionsJob() {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy(JobGroup.relationship));
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        RelationshipServices services = NetContext.instance.create(RelationshipServices.class);
        services.getFriendList().enqueue(new Callback<List<RelationshipResponse>>() {
            @Override
            public void onResponse(Call<List<RelationshipResponse>> call,
                                   Response<List<RelationshipResponse>> response) {
                if (response.code() == 200) {
                    EventBus.getDefault().post(new FetchRelationshipComplete(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<RelationshipResponse>> call, Throwable t) {
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
