package com.hoocons.hoocons_android.Tasks.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.hoocons.hoocons_android.EventBus.JobFailureEvBusRequest;
import com.hoocons.hoocons_android.EventBus.TaskCompleteRequest;
import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Requests.ChannelRequest;
import com.hoocons.hoocons_android.Networking.Responses.IdResponse;
import com.hoocons.hoocons_android.Networking.Services.ChannelServices;
import com.hoocons.hoocons_android.Tasks.JobProperties.JobGroup;
import com.hoocons.hoocons_android.Tasks.JobProperties.Priority;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hungnguyen on 8/16/17.
 */

public class NewChannelJob extends Job {
    private String channelName;
    private String channelSubName;
    private String channelAbout;
    private String profileUrl;
    private List<String> topics;
    private String privacy;
    private String wallpaperUrl;

    public NewChannelJob(String channelName, String channelSubName,
                         String channelAbout, String profileUrl, List<String> topics,
                         String privacy, String wallpaperUrl) {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy(JobGroup.channel));
        this.channelName = channelName;
        this.channelSubName = channelSubName;
        this.channelAbout = channelAbout;
        this.profileUrl = profileUrl;
        this.topics = topics;
        this.privacy = privacy;
        this.wallpaperUrl = wallpaperUrl;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        final ChannelRequest request = new ChannelRequest(channelName, "subname", channelAbout,
                profileUrl, topics, "Public", wallpaperUrl);

        ChannelServices services = NetContext.instance.create(ChannelServices.class);
        services.postNewChannel(request).enqueue(new Callback<IdResponse>() {
            @Override
            public void onResponse(Call<IdResponse> call, Response<IdResponse> response) {
                if (response.code() == 200) {
                    EventBus.getDefault().post(new TaskCompleteRequest(AppConstant.CREATE_NEW_CHANNEL,
                            response.body().getId()));
                }
            }

            @Override
            public void onFailure(Call<IdResponse> call, Throwable t) {
                EventBus.getDefault().post(new JobFailureEvBusRequest());
            }
        });
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable,
                                                     int runCount, int maxRunCount) {
        return null;
    }
}
