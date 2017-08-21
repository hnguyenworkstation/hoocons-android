package com.hoocons.hoocons_android.ViewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hoocons.hoocons_android.Interface.OnChatRoomClickListener;
import com.hoocons.hoocons_android.Models.ChatRoom;
import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hungnguyen on 8/20/17.
 */

public class ChatRoomViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.chatroom_profile)
    ImageView roomProfile;

    @BindView(R.id.chatroom_name)
    TextView roomName;

    @BindView(R.id.chatroom_last_message)
    TextView roomLastMessage;

    @BindView(R.id.chatroom_lastmsg_time)
    TextView roomTime;

    @BindView(R.id.chatroom_msg_state)
    ImageView roomStatus;

    @BindView(R.id.chatroom_rootlayout)
    RelativeLayout roomLayout;

    public ChatRoomViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void initView(Context context, ChatRoom chatRoom, final int position, OnChatRoomClickListener listener) {
        initOnClick(position, listener);
    }

    private void initOnClick(final int position, final OnChatRoomClickListener listener) {
        roomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onChatRoomClickListener(position);
            }
        });

        roomLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onChatRoomLongClickListener(position);
                return false;
            }
        });
    }
}
