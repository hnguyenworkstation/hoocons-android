package com.hoocons.hoocons_android.Tasks.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.hoocons.hoocons_android.EventBus.FetchChatRoomsComplete;
import com.hoocons.hoocons_android.EventBus.ServerErrorRequest;
import com.hoocons.hoocons_android.Models.ChatRoom;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Responses.ChatRoomResponse;
import com.hoocons.hoocons_android.Networking.Services.ChatRoomServices;
import com.hoocons.hoocons_android.Tasks.JobProperties.JobGroup;
import com.hoocons.hoocons_android.Tasks.JobProperties.Priority;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hungnguyen on 8/21/17.
 */

public class FetchChatRoomsJob extends Job {
    private List<ChatRoom> chatRooms;

    public FetchChatRoomsJob() {
        super(new Params(Priority.HIGH).requireNetwork().persist().groupBy(JobGroup.chatroom));
    }

    @Override
    public void onAdded() {
        chatRooms = new ArrayList<>();
    }

    @Override
    public void onRun() throws Throwable {
        ChatRoomServices services = NetContext.instance.create(ChatRoomServices.class);
        services.getCurrentChatRooms().enqueue(new Callback<List<ChatRoomResponse>>() {
            @Override
            public void onResponse(Call<List<ChatRoomResponse>> call,
                                   Response<List<ChatRoomResponse>> response) {
                if (response.code() == 200) {
                    EventBus.getDefault().post(new FetchChatRoomsComplete(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<ChatRoomResponse>> call, Throwable t) {
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
