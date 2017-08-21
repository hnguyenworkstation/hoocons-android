package com.hoocons.hoocons_android.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoocons.hoocons_android.Interface.OnChatRoomClickListener;
import com.hoocons.hoocons_android.Models.ChatRoom;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewHolders.ChatRoomViewHolder;

import java.util.List;

/**
 * Created by hNguyen on 8/21/2017.
 */

public class ConversationAdapter extends RecyclerView.Adapter<ChatRoomViewHolder> {
    private List<ChatRoom> chatRooms;
    private Context context;
    private OnChatRoomClickListener listener;

    public ConversationAdapter(Context context, List<ChatRoom> chatRooms,
                               OnChatRoomClickListener listener) {
        this.chatRooms = chatRooms;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ChatRoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conversation_row_viewholder, parent, false);

        return new ChatRoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatRoomViewHolder holder, int position) {
        holder.initView(context, chatRooms.get(position), position, listener);
    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }
}
