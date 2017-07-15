package com.hoocons.hoocons_android.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.hoocons.hoocons_android.CustomUI.AdjustableImageView;
import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hungnguyen on 7/15/17.
 */

public class FeaturedViewHolder extends ViewHolder {
    /* EVENT COMPLETE VIEW HOLDER*/
    @BindView(R.id.event_viewholder)
    LinearLayout mViewHolder;

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
    @BindView(R.id.event_single_media)
    LinearLayout mSingleMediaRoot;
    @BindView(R.id.event_single_media_content)
    AdjustableImageView mSingleMediaView;

    /* EVENT MULTI MEDIAS */
    @BindView(R.id.event_multi_media)
    LinearLayout mMultiMediaRoot;
    @BindView(R.id.event_multi_media_content)
    RecyclerView mMultiMediaRecycler;

    /* EVENT CHECK IN */
    @BindView(R.id.event_checkin)
    LinearLayout mCheckinRoot;
    @BindView(R.id.event_check_in_location)
    AdjustableImageView mCheckinMapView;
    @BindView(R.id.event_check_in_name)
    TextView mCheckinName;
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

    /* SUGGESTION VIEW HOLDER */
    @BindView(R.id.suggestion_viewholder)
    LinearLayout mSuggestionViewHolder;
    @BindView(R.id.suggestion_title)
    TextView mSuggestionTitle;
    @BindView(R.id.suggestion_recycler)
    RecyclerView mSuggestionRecycler;

    public FeaturedViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
