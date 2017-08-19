package com.hoocons.hoocons_android.Helpers;

import com.google.firebase.database.DatabaseReference;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Models.ChatMessage;
import com.hoocons.hoocons_android.Models.ChatRoom;

/**
 * Created by hungnguyen on 8/19/17.
 */

public class ChatUtils {
    public static void initNewChatRoom() {
        DatabaseReference chatRoomPref = BaseApplication.getInstance()
                .getDatabase().child("chatrooms");
        String chatRoomId = chatRoomPref.push().getKey();
        chatRoomPref.child(chatRoomId)
                .setValue(new ChatRoom("Testing", AppConstant.CHATROOM_TYPE_SINGLE));
    }

    public static void pushMessage(final String chatRoomId, ChatMessage chatMessage) {
        DatabaseReference messageRef = BaseApplication.getInstance()
                .getDatabase().child("messages");

        messageRef.child(chatRoomId).setValue(chatMessage);
    }
}
