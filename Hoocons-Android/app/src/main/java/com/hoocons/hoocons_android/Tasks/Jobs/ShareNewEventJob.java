package com.hoocons.hoocons_android.Tasks.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.hoocons.hoocons_android.EventBus.PostEventSuccess;
import com.hoocons.hoocons_android.EventBus.PostingJobAddedToDisk;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Requests.EventInfoRequest;
import com.hoocons.hoocons_android.Networking.Services.EventServices;
import com.hoocons.hoocons_android.Tasks.JobProperties.JobGroup;
import com.hoocons.hoocons_android.Tasks.JobProperties.Priority;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hungnguyen on 7/24/17.
 */

public class ShareNewEventJob extends Job implements Serializable {
    private String textContent;
    private String privacy;
    private String eventType;
    private int shareEventId;

    private double chkinLon;
    private double chkinLat;
    private String chkinName;
    private String chkinAddress;
    private String chkinId;

    public ShareNewEventJob(String text, int shareEventId, String privacy, String eventType,
                               double chkinLong, double chkinLat,
                               String chkinName, String chkinAddress, String chkinId) {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy(JobGroup.event));

        this.textContent = text;
        this.privacy = privacy;
        this.eventType = eventType;
        this.shareEventId = shareEventId;

        this.chkinLat = chkinLat;
        this.chkinId = chkinId;
        this.chkinLon = chkinLong;
        this.chkinName = chkinName;
        this.chkinAddress = chkinAddress;
    }

    @Override
    public void onAdded() {
        EventBus.getDefault().post(new PostingJobAddedToDisk());
    }

    @Override
    public void onRun() throws Throwable {
        final EventInfoRequest request = new EventInfoRequest(textContent, null, null,
                privacy, 0, 0, eventType, true, chkinLon, chkinLat, chkinName, chkinAddress, chkinId);

        EventServices services = NetContext.instance.create(EventServices.class);
        services.shareEvent(shareEventId, request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    EventBus.getDefault().post(new PostEventSuccess());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

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
