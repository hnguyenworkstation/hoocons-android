package com.hoocons.hoocons_android.Tasks.Jobs;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.hoocons.hoocons_android.EventBus.JobFailureEvBusRequest;
import com.hoocons.hoocons_android.EventBus.UploadImageFailed;
import com.hoocons.hoocons_android.EventBus.UploadImageSuccess;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Tasks.JobProperties.JobGroup;
import com.hoocons.hoocons_android.Tasks.JobProperties.Priority;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by hungnguyen on 8/16/17.
 */

public class UploadSingleUriImageJob extends Job {
    private Uri uri;
    private String tag;
    private String toFolder;

    public UploadSingleUriImageJob(final Uri uri, final String tag, final String toFolder) {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy(JobGroup.channel));
        this.uri = uri;
        this.tag = tag;
        this.toFolder = toFolder;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        String url = AppUtils.uploadPNGImageByUri(uri, toFolder);

        if (url != null) {
            EventBus.getDefault().post(new UploadImageSuccess(url, tag));
        } else {
            EventBus.getDefault().post(new UploadImageFailed());
        }
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return null;
    }
}
