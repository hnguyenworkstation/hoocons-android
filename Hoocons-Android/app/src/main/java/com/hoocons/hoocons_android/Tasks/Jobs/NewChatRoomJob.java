package com.hoocons.hoocons_android.Tasks.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.hoocons.hoocons_android.EventBus.DuplicateObjectError;
import com.hoocons.hoocons_android.EventBus.ServerErrorRequest;
import com.hoocons.hoocons_android.EventBus.TaskCompleteRequest;
import com.hoocons.hoocons_android.EventBus.UserNotFoundError;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Requests.ChatRoomRequest;
import com.hoocons.hoocons_android.Networking.Responses.IdResponse;
import com.hoocons.hoocons_android.Networking.Services.ChatRoomServices;
import com.hoocons.hoocons_android.Tasks.JobProperties.JobGroup;
import com.hoocons.hoocons_android.Tasks.JobProperties.Priority;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hungnguyen on 8/21/17.
 */

public class NewChatRoomJob extends Job {
    private int[] users;
    private String uid;

    public NewChatRoomJob(int[] users, String uid) {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy(JobGroup.chatroom));
        this.users = users;
        this.uid = uid;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        ChatRoomServices services = NetContext.instance.create(ChatRoomServices.class);
        services.createChatRoom(new ChatRoomRequest(users, uid)).enqueue(new Callback<IdResponse>() {
            @Override
            public void onResponse(Call<IdResponse> call, Response<IdResponse> response) {
                if (response.code() == 200) {
                    // success
                    EventBus.getDefault().post(new TaskCompleteRequest());
                } else if (response.code() == 404) {
                    // user not found
                    EventBus.getDefault().post(new UserNotFoundError());
                } else if (response.code() == 201) {
                    // duplicate
                    EventBus.getDefault().post(new DuplicateObjectError());
                }
            }

            @Override
            public void onFailure(Call<IdResponse> call, Throwable t) {
                EventBus.getDefault().post(new ServerErrorRequest());
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
