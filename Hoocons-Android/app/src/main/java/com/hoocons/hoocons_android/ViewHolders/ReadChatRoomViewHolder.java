package com.hoocons.hoocons_android.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hungnguyen on 8/20/17.
 */

public class ReadChatRoomViewHolder extends RecyclerView.ViewHolder {
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

    @BindView(R.id.chatroom_read_rootlayout)
    RelativeLayout roomLayout;

    public ReadChatRoomViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
