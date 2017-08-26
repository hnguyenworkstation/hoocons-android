package com.hoocons.hoocons_android.Tasks.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.hoocons.hoocons_android.EventBus.BadRequest;
import com.hoocons.hoocons_android.EventBus.FetchFriendRequestComplete;
import com.hoocons.hoocons_android.EventBus.PostEventSuccess;
import com.hoocons.hoocons_android.Networking.ApiViewSets.FriendshipRequestApiViewSet;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Services.RelationshipServices;
import com.hoocons.hoocons_android.Tasks.JobProperties.JobGroup;
import com.hoocons.hoocons_android.Tasks.JobProperties.Priority;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hungnguyen on 8/26/17.
 */

public class FetchSemiFriendRequestJob extends Job {
    public FetchSemiFriendRequestJob() {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy(JobGroup.relationship));
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        RelationshipServices services = NetContext.instance.create(RelationshipServices.class);
        services.getSemiFriendRequest().enqueue(new Callback<FriendshipRequestApiViewSet>() {
            @Override
            public void onResponse(Call<FriendshipRequestApiViewSet> call,
                                   Response<FriendshipRequestApiViewSet> response) {
                if (response.code() == 200) {
                    EventBus.getDefault().post(new FetchFriendRequestComplete(response.body()));
                }
            }

            @Override
            public void onFailure(Call<FriendshipRequestApiViewSet> call, Throwable t) {
                EventBus.getDefault().post(new BadRequest());
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
