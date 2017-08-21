package com.hoocons.hoocons_android.Helpers;

import com.google.firebase.database.DatabaseReference;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Models.ChatMessage;
import com.hoocons.hoocons_android.Models.ChatRoom;
import com.hoocons.hoocons_android.Tasks.Jobs.NewChatRoomJob;

/**
 * Created by hungnguyen on 8/19/17.
 */

public class ChatUtils {
    public static void createNewChatRoomWithUser(int[] users, String roomName) {
        DatabaseReference chatRoomPref = BaseApplication.getInstance()
                .getDatabase().child("chatrooms");
        String chatRoomId = chatRoomPref.push().getKey();
        chatRoomPref.child(chatRoomId)
                .setValue(new ChatRoom(roomName, AppConstant.CHATROOM_TYPE_SINGLE));

        BaseApplication.getInstance().getJobManager()
                .addJobInBackground(new NewChatRoomJob(users, chatRoomId));
    }

    public static void pushMessage(final String chatRoomId, ChatMessage chatMessage) {
        DatabaseReference messageRef = BaseApplication.getInstance()
                .getDatabase().child("messages");

        String simpleId = messageRef.child(chatRoomId).push().getKey();
        messageRef.child(chatRoomId).child(simpleId).setValue(chatMessage);

        chatMessage.setId(simpleId);
    }
}
