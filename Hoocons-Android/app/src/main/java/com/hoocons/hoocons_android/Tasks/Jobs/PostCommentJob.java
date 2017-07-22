package com.hoocons.hoocons_android.Tasks.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.hoocons.hoocons_android.EventBus.PostCommentComplete;
import com.hoocons.hoocons_android.EventBus.PostCommentFailed;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Requests.CommentRequest;
import com.hoocons.hoocons_android.Networking.Services.EventServices;
import com.hoocons.hoocons_android.Tasks.JobProperties.JobGroup;
import com.hoocons.hoocons_android.Tasks.JobProperties.Priority;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hungnguyen on 7/22/17.
 */

public class PostCommentJob extends Job {
    private int eventId;
    private String textContent;
    private String commentType;
    private String commentMediaUrl;
    private String commentTag;

    protected PostCommentJob(int eventId, String textContent, String commentType,
                             String commentMediaUrl, String commentTag) {
        super(new Params(Priority.MID).requireNetwork().persist().groupBy(JobGroup.event));

        this.eventId = eventId;
        this.textContent = textContent;
        this.commentType = commentType;
        this.commentMediaUrl = commentMediaUrl;
        this.commentTag = commentTag;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        EventServices services = NetContext.instance.create(EventServices.class);
        services.postComment(eventId, new CommentRequest(textContent, commentType, commentMediaUrl))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200) {
                            // 200 is success
                            EventBus.getDefault().post(new PostCommentComplete(commentTag));
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        EventBus.getDefault().post(new PostCommentFailed(commentTag));
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
