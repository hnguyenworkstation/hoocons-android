package com.hoocons.hoocons_android.Tasks.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.hoocons.hoocons_android.EventBus.EventDeleted;
import com.hoocons.hoocons_android.EventBus.FetchCommentsComplete;
import com.hoocons.hoocons_android.EventBus.FetchCommentsFailed;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Responses.CommentResponse;
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
 * Created by hungnguyen on 7/22/17.
 */

public class FetchCommentsJob extends Job {
    private int eventId;
    private int offset;
    private int limit;

    public FetchCommentsJob(int eventId, int offset, int limit) {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy(JobGroup.event));

        this.offset = offset;
        this.limit = limit;
        this.eventId = eventId;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        EventServices services = NetContext.instance.create(EventServices.class);
        services.getComments(eventId, offset, limit).enqueue(new Callback<List<CommentResponse>>() {
            @Override
            public void onResponse(Call<List<CommentResponse>> call,
                                   Response<List<CommentResponse>> response) {
                if (response.code() == 200) {
                    // Success
                    EventBus.getDefault().post(new FetchCommentsComplete(response.body()));
                } else if (response.code() == 404) {
                    // Event already deleted
                    EventBus.getDefault().post(new EventDeleted());
                }
            }

            @Override
            public void onFailure(Call<List<CommentResponse>> call, Throwable t) {
                EventBus.getDefault().post(new FetchCommentsFailed());
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
