package com.hoocons.hoocons_android.Tasks.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.hoocons.hoocons_android.EventBus.JobAddedToDisk;
import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Services.RelationshipServices;
import com.hoocons.hoocons_android.Tasks.JobProperties.JobGroup;
import com.hoocons.hoocons_android.Tasks.JobProperties.Priority;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by hungnguyen on 8/25/17.
 */
public class CancelFriendRequestJob extends Job {
    private int userId;

    public  CancelFriendRequestJob(int userId) {
        super(new Params(Priority.HIGH).requireNetwork().persist()
                .groupBy(JobGroup.relationship));
        this.userId = userId;
    }

    @Override
    public void onAdded() {
        EventBus.getDefault().post(new JobAddedToDisk(AppConstant.CANCEL_FRIEND_REQUEST_TAG));
    }

    @Override
    public void onRun() throws Throwable {
        RelationshipServices services = NetContext.instance.create(RelationshipServices.class);
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}
