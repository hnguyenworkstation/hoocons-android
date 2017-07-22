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

/**
 * Created by hungnguyen on 7/22/17.
 */

public class FetchCommentsJob extends Job implements Serializable {
    private int eventId;
    private int start;
    private int end;
    private String commentTag;

    public FetchCommentsJob(int eventId, int start, int end, String commentTag) {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy(JobGroup.event));

        this.end = end;
        this.start = start;
        this.eventId = eventId;
        this.commentTag = commentTag;
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
