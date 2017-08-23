package com.hoocons.hoocons_android.ViewHolders;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hoocons.hoocons_android.CustomUI.CustomTextView;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Interface.OnChatMessageClickListener;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.Models.ChatMessage;
import com.hoocons.hoocons_android.R;
import com.vstechlab.easyfonts.EasyFonts;

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
    @BindView(R.id.time_header)
    LinearLayout mTimeFrameHeaderLayout;

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
    @BindView(R.id.message_time_frame)
    TextView mMessageTimeFrame;

    @Nullable
    @BindView(R.id.message_footer)
    LinearLayout mMessageFooterLayout;

    /* MESSAGE BODY CONTENT */
    @Nullable
    @BindView(R.id.message_text_content)
    CustomTextView mMessageTextContent;

    /* SIDE FOOTER */
    @Nullable
    @BindView(R.id.side_state_layout)
    RelativeLayout mSideStateLayout;

    @Nullable
    @BindView(R.id.side_message_state_image)
    ImageView mSideStateImage;

    @Nullable
    @BindView(R.id.side_send_message_progress)
    ProgressBar mSideProgress;

    /* MESSAGE LAYOUT */
    @Nullable
    @BindView(R.id.message_layout)
    RelativeLayout mMessageLayout;

    @Nullable
    @BindView(R.id.bottom_margin_view)
    LinearLayout mBottomMarginView;

    public ChatMessageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void initMessage(final Context context, ChatMessage message, ChatMessage nextMessage,
                            OnChatMessageClickListener listener, final int position, final int lastPos) {
        assert mBottomMarginView != null;
        if (position == lastPos) {
            mBottomMarginView.setVisibility(View.VISIBLE);
        }

        initTimeHeader(context, message, nextMessage);
        initGeneralTextView(context, message, listener, position, lastPos);
    }

    private void initTimeHeader(final Context context, ChatMessage message, ChatMessage nextMessage) {
        assert mTimeTitle != null;
        assert mTimeFrameHeaderLayout != null;
        long diffTime = 0;
        if (nextMessage != null) {
            diffTime = AppUtils.diffTime(nextMessage.getCreatedTime(), message.getCreatedTime());
        }

        if (message.isShouldShowTimeHeader() || diffTime > 600 || diffTime < -600) {
            mTimeFrameHeaderLayout.setVisibility(View.VISIBLE);
            mTimeTitle.setText(AppUtils.convertDateTimeFromUTC(message.getCreatedTime()).toUpperCase());
            mTimeTitle.setTypeface(EasyFonts.robotoBold(context));
        }
    }

    private void initGeneralTextView(final Context context, ChatMessage message,
                                     final OnChatMessageClickListener listener,
                                     final int position, final int lastPos) {
        assert mMessageTextContent != null;
        assert mMessageProgress != null;
        assert mMessageStateImage != null;
        assert mMessageTimeFrame != null;
        assert mSideStateLayout != null;

        mMessageTextContent.setContent(message.getTextContent());
        mMessageTimeFrame.setText(AppUtils.convertDateTimeFromUTC(message.getCreatedTime()));

        if (message.getUserId() == SharedPreferencesManager.getDefault().getUserId()) {
            if (message.isPosted()) {
                mMessageTimeFrame.setText(AppUtils.convertDateTimeFromUTC(message.getCreatedTime()));
                mMessageProgress.setVisibility(View.GONE);
                mMessageStateImage.setVisibility(View.VISIBLE);
                mMessageStateImage.setImageDrawable(context.getResources()
                        .getDrawable(R.drawable.edit_doneblue));

                mSideStateLayout.setVisibility(View.INVISIBLE);
            } else {
                mMessageTimeFrame.setText(context.getResources().getText(R.string.posting));
                if (position == lastPos) {
                    mSideStateLayout.setVisibility(View.INVISIBLE);
                }
            }

            assert mMessageFooterLayout != null;
            if (message.isShownName()) {
                mMessageFooterLayout.setVisibility(View.VISIBLE);
            } else {
                mMessageFooterLayout.setVisibility(View.GONE);
            }
        } else {

        }

        assert mMessageLayout != null;
        mMessageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMessageClickListener(position);
            }
        });
    }
}
