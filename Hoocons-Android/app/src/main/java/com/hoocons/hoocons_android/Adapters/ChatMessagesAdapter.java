package com.hoocons.hoocons_android.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Interface.OnChatMessageClickListener;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.Models.ChatMessage;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewHolders.ChatMessageViewHolder;

import java.util.List;

/**
 * Created by hungnguyen on 8/6/17.
 */

public class ChatMessagesAdapter extends RecyclerView.Adapter<ChatMessageViewHolder> {
    private Context context;
    private List<ChatMessage> chatMessageList;
    private OnChatMessageClickListener listener;

    private final int MESSAGE_TEXT_FROM_ME = 1;
    private final int MESSAGE_IMAGE_FROM_ME = 2;
    private final int MESSAGE_GIF_FROM_ME = 3;
    private final int MESSAGE_VIDEO_FROM_ME = 4;
    private final int MESSAGE_LOCATION_FROM_ME = 5;
    private final int MESSAGE_FILE_FROM_ME = 6;
    private final int MESSAGE_CONTACT_FROM_ME = 7;

    private final int MESSAGE_TEXT_FROM_FRIEND = 8;
    private final int MESSAGE_IMAGE_FROM_FRIEND = 9;
    private final int MESSAGE_GIF_FROM_FRIEND = 10;
    private final int MESSAGE_VIDEO_FROM_FRIEND = 11;
    private final int MESSAGE_LOCATION_FROM_FRIEND = 12;
    private final int MESSAGE_FILE_FROM_FRIEND = 13;
    private final int MESSAGE_CONTACT_FROM_FRIEND = 14;

    private final int MESSAGE_STICKER_FROM_ME = 15;
    private final int MESSAGE_VOICE_FROM_ME = 16;
    private final int MESSAGE_STICKER_FROM_FRIEND = 17;
    private final int MESSAGE_VOICE_FROM_FRIEND = 18;

    public ChatMessagesAdapter(Context context, List<ChatMessage> chatMessageList,
                               OnChatMessageClickListener listener) {
        this.context = context;
        this.chatMessageList = chatMessageList;
        this.listener = listener;
    }

    @Override
    public ChatMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case MESSAGE_TEXT_FROM_ME:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_message_text_from_me, parent, false);
                break;
            case MESSAGE_TEXT_FROM_FRIEND:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_message_text_from_friend, parent, false);
                break;
            default:
                break;
        }

        return new ChatMessageViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = chatMessageList.get(position);
        if (message.getUserId() == SharedPreferencesManager.getDefault().getUserId()) {
            switch (message.getMessageType()) {
                case AppConstant.MESSAGE_TYPE_TEXT:
                    return MESSAGE_TEXT_FROM_ME;
                case AppConstant.MESSAGE_TYPE_IMAGE:
                    return MESSAGE_IMAGE_FROM_ME;
                case AppConstant.MESSAGE_TYPE_GIF:
                    return MESSAGE_GIF_FROM_ME;
                case AppConstant.MESSAGE_TYPE_VIDEO:
                    return MESSAGE_VIDEO_FROM_ME;
                case AppConstant.MESSAGE_TYPE_LOCATION:
                    return MESSAGE_LOCATION_FROM_ME;
                case AppConstant.MESSAGE_TYPE_FILE:
                    return MESSAGE_FILE_FROM_ME;
                case AppConstant.MESSAGE_TYPE_CONTACT:
                    return MESSAGE_CONTACT_FROM_ME;
                case AppConstant.MESSAGE_TYPE_STICKER:
                    return MESSAGE_STICKER_FROM_ME;
                case AppConstant.MESSAGE_TYPE_VOICE:
                    return MESSAGE_VOICE_FROM_ME;
                default:
                    return -1;
            }
        } else {
            switch (message.getMessageType()) {
                case AppConstant.MESSAGE_TYPE_TEXT:
                    return MESSAGE_TEXT_FROM_FRIEND;
                case AppConstant.MESSAGE_TYPE_IMAGE:
                    return MESSAGE_IMAGE_FROM_FRIEND;
                case AppConstant.MESSAGE_TYPE_GIF:
                    return MESSAGE_GIF_FROM_FRIEND;
                case AppConstant.MESSAGE_TYPE_VIDEO:
                    return MESSAGE_VIDEO_FROM_FRIEND;
                case AppConstant.MESSAGE_TYPE_LOCATION:
                    return MESSAGE_LOCATION_FROM_FRIEND;
                case AppConstant.MESSAGE_TYPE_FILE:
                    return MESSAGE_FILE_FROM_FRIEND;
                case AppConstant.MESSAGE_TYPE_CONTACT:
                    return MESSAGE_CONTACT_FROM_FRIEND;
                case AppConstant.MESSAGE_TYPE_STICKER:
                    return MESSAGE_STICKER_FROM_FRIEND;
                case AppConstant.MESSAGE_TYPE_VOICE:
                    return MESSAGE_VOICE_FROM_FRIEND;
                default:
                    return -1;
            }
        }
    }

    @Override
    public void onBindViewHolder(ChatMessageViewHolder holder, int position) {
        ChatMessage currentMessage = chatMessageList.get(position);
        ChatMessage nextMessage = null;
        if (position == chatMessageList.size() - 1) {
            currentMessage.setShouldShowTimeHeader(true);
        } else {
            nextMessage = chatMessageList.get(position + 1);
        }

        holder.initMessage(context,currentMessage , nextMessage, listener, position, chatMessageList.size()-1);
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }
}
