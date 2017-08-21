package com.hoocons.hoocons_android.EventBus;

import com.hoocons.hoocons_android.Networking.Responses.ChatRoomResponse;

import java.util.List;

/**
 * Created by hungnguyen on 8/21/17.
 */

public class FetchChatRoomsComplete {
    private List<ChatRoomResponse> roomResponseList;

    public FetchChatRoomsComplete(List<ChatRoomResponse> roomResponseList) {
        this.roomResponseList = roomResponseList;
    }

    public List<ChatRoomResponse> getRoomResponseList() {
        return roomResponseList;
    }

    public void setRoomResponseList(List<ChatRoomResponse> roomResponseList) {
        this.roomResponseList = roomResponseList;
    }
}
