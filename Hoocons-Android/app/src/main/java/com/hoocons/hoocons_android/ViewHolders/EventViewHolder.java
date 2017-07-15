package com.hoocons.hoocons_android.ViewHolders;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hoocons.hoocons_android.CustomUI.AdjustableImageView;
import com.hoocons.hoocons_android.CustomUI.GlideCircleTransformation;
import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Networking.Responses.EventResponse;
import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hungnguyen on 7/15/17.
 */

public class EventViewHolder extends ViewHolder {
    /* EVENT TOP INTRO */
    @BindView(R.id.event_top_intro)
    LinearLayout mTopIntro;
    @BindView(R.id.event_intro_info)
    TextView mIntroInfo;
    @BindView(R.id.event_intro_options)
    ImageButton mIntroButton;

    /* EVENT HEADER */
    @BindView(R.id.event_user_profile)
    ImageView mUserProfileImage;
    @BindView(R.id.event_user_name)
    TextView mUserDisplayName;
    @BindView(R.id.event_timeframe)
    TextView mTimeFrame;
    @BindView(R.id.event_location_icon)
    ImageView mHeaderLocationIcon;
    @BindView(R.id.event_posted_location)
    TextView mPostedLocation;
    @BindView(R.id.header_event_options)
    ImageButton mHeaderMoreButton;

    /* EVENT TEXT CONTENT */
    @BindView(R.id.event_text_content)
    TextView mTextContent;

    /* EVENT SINGLE MEDIA */
    @Nullable
    @BindView(R.id.event_single_media)
    LinearLayout mSingleMediaRoot;

    @Nullable
    @BindView(R.id.event_single_media_content)
    AdjustableImageView mSingleMediaView;

    /* EVENT MULTI MEDIAS */
    @Nullable
    @BindView(R.id.event_multi_media)
    LinearLayout mMultiMediaRoot;

    @Nullable
    @BindView(R.id.event_multi_media_content)
    RecyclerView mMultiMediaRecycler;

    /* EVENT CHECK IN */
    @Nullable
    @BindView(R.id.event_checkin)
    LinearLayout mCheckinRoot;

    @Nullable
    @BindView(R.id.event_check_in_location)
    AdjustableImageView mCheckinMapView;

    @Nullable
    @BindView(R.id.event_check_in_name)
    TextView mCheckinName;

    @Nullable
    @BindView(R.id.event_check_in_type)
    TextView mCheckinType;

    /* EVENT FOOTER */
    @BindView(R.id.event_likebtn)
    BootstrapButton mLikeBtn;
    @BindView(R.id.event_commentbtn)
    BootstrapButton mCommentBtn;
    @BindView(R.id.like_count)
    TextView mLikeCount;
    @BindView(R.id.comment_count)
    TextView mCommentCount;

    private EventResponse eventResponse;

    public EventViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void initViewHolder(EventResponse response) {
        this.eventResponse = response;

        initEventHeader();
        initEventContent();
    }

    private void initEventHeader() {
        loadProfileImage(eventResponse.getUserInfo().getProfileUrl());
        mUserDisplayName.setText(eventResponse.getUserInfo().getDisplayName());
        mTimeFrame.setText(eventResponse.getCreateAt());
    }

    private void initEventContent() {
        if (eventResponse.getEventType().equals(AppConstant.EVENT_TYPE_TEXT)) {
            mTextContent.setText(eventResponse.getTextContent());

            if (eventResponse.getTextContent().length() < 50) {

            }
        }
    }

    private void loadProfileImage(String url) {
        Glide.with(mUserProfileImage.getContext())
                .load(url)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .transform(new GlideCircleTransformation(mUserProfileImage.getContext()))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model,
                                               Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        mUserProfileImage.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(mUserProfileImage);
    }
}
