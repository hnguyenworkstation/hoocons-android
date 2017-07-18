package com.hoocons.hoocons_android.Tasks.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.hoocons.hoocons_android.Tasks.JobProperties.JobGroup;
import com.hoocons.hoocons_android.Tasks.JobProperties.Priority;

import java.io.Serializable;

/**
 * Created by hungnguyen on 7/18/17.
 */

public class LikeEventJob extends Job implements Serializable {
    private int eventId;

    public LikeEventJob(String tag, int eventId) {
        super(new Params(Priority.LOW).requireNetwork().persist().groupBy(JobGroup.event).addTags(tag));
        this.eventId = eventId;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {

    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}
