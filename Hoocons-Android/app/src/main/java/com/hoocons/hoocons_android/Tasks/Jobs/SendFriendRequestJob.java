package com.hoocons.hoocons_android.Tasks.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.hoocons.hoocons_android.EventBus.BadRequest;
import com.hoocons.hoocons_android.EventBus.FriendRequestAddedToDisk;
import com.hoocons.hoocons_android.EventBus.TaskCompleteRequest;
import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Services.RelationshipServices;
import com.hoocons.hoocons_android.Tasks.JobProperties.JobGroup;
import com.hoocons.hoocons_android.Tasks.JobProperties.Priority;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hungnguyen on 8/25/17.
 */

public class SendFriendRequestJob extends Job {
    private static final int JOB_DELAY = 5000;
    private int userId;

    public SendFriendRequestJob(String tag, int userId) {
        super(new Params(Priority.HIGH).requireNetwork().persist()
                .groupBy(JobGroup.event)
                .addTags(tag)
                .delayInMs(JOB_DELAY));
        this.userId = userId;
    }

    @Override
    public void onAdded() {
        EventBus.getDefault().post(new FriendRequestAddedToDisk());
    }

    @Override
    public void onRun() throws Throwable {
        RelationshipServices services = NetContext.instance.create(RelationshipServices.class);
        services.sendFriendRequest(userId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    EventBus.getDefault().post(new TaskCompleteRequest(AppConstant.FRIEND_REQUEST));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
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
