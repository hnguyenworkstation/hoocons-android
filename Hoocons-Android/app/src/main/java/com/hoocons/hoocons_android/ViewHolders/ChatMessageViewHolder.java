package com.hoocons.hoocons_android.ViewHolders;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hoocons.hoocons_android.CustomUI.CustomTextView;
import com.hoocons.hoocons_android.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hungnguyen on 8/6/17.
 */

public class ChatMessageViewHolder extends RecyclerView.ViewHolder {
    @Nullable
    @BindView(R.id.time_title)
    TextView mTimeTitle;

    @Nullable
    @BindView(R.id.time_frame_title)
    LinearLayout mTimeFrameLayout;

    /* MESSAGE HEADER LAYOUT */
    @Nullable
    @BindView(R.id.message_header_layout)
    LinearLayout mHeaderLayout;

    @Nullable
    @BindView(R.id.friend_name)
    TextView mFriendName;

    @Nullable
    @BindView(R.id.friend_avatar)
    ImageView mFriendAvatar;

    @Nullable
    @BindView(R.id.friend_avatar_progress)
    ProgressBar mFriendAvatarProgress;

    /* MESSAGE FOOTER LAYOUT */
    @Nullable
    @BindView(R.id.message_state_image)
    ImageView mMessageStateImage;

    @Nullable
    @BindView(R.id.send_message_progress)
    ProgressBar mMessageProgress;

    @Nullable
    @BindView(R.id.message_state)
    TextView mMessageState;

    @Nullable
    @BindView(R.id.message_time_frame)
    TextView mMessageTimeFrame;

    @Nullable
    @BindView(R.id.message_footer)
    LinearLayout mMessageFooterLayout;

    /* MESSAGE BODY CONTENT */
    @Nullable
    @BindView(R.id.message_text_content)
    CustomTextView mMessageTextContent;


    public ChatMessageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void initMessage() {

    }
}
