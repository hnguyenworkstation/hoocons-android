package com.hoocons.hoocons_android.ViewHolders;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hoocons.hoocons_android.Adapters.ImageLoaderAdapter;
import com.hoocons.hoocons_android.Adapters.MediaImagesAdapter;
import com.hoocons.hoocons_android.CustomUI.AdjustableImageView;
import com.hoocons.hoocons_android.CustomUI.GlideCircleTransformation;
import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.Models.Media;
import com.hoocons.hoocons_android.Networking.Responses.EventResponse;
import com.hoocons.hoocons_android.Networking.Responses.MediaResponse;
import com.hoocons.hoocons_android.Networking.Responses.UserInfoResponse;
import com.hoocons.hoocons_android.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hungnguyen on 7/15/17.
 */

public class UserInfoAndEventViewHolder extends ViewHolder {
    /* EVENT HEADER */
    @Nullable
    @BindView(R.id.event_user_profile)
    ImageView mUserProfileImage;

    @Nullable
    @BindView(R.id.event_user_name)
    TextView mUserDisplayName;

    @Nullable
    @BindView(R.id.event_timeframe)
    TextView mTimeFrame;

    @Nullable
    @BindView(R.id.event_location_icon)
    ImageView mHeaderLocationIcon;

    @Nullable
    @BindView(R.id.event_posted_location)
    TextView mPostedLocation;

    @Nullable
    @BindView(R.id.header_event_options)
    ImageButton mHeaderMoreButton;

    /* EVENT TEXT CONTENT */
    @Nullable
    @BindView(R.id.event_text_content)
    TextView mTextContent;

    /* EVENT SINGLE MEDIA */
    @Nullable
    @BindView(R.id.event_single_media)
    LinearLayout mSingleMediaRoot;

    @Nullable
    @BindView(R.id.event_single_media_content)
    AdjustableImageView mSingleMediaView;

    @Nullable
    @BindView(R.id.single_content_progressbar)
    ProgressBar mSingleContentProgressBar;

    /* EVENT MULTI MEDIAS */
    @Nullable
    @BindView(R.id.event_multi_media)
    LinearLayout mMultiMediaRoot;

    @Nullable
    @BindView(R.id.event_multi_media_content)
    RecyclerView mMultiMediaRecycler;

    private MediaImagesAdapter mMultiImageAdapter;

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
    @Nullable
    @BindView(R.id.event_likebtn)
    BootstrapButton mLikeBtn;

    @Nullable
    @BindView(R.id.event_commentbtn)
    BootstrapButton mCommentBtn;

    @Nullable
    @BindView(R.id.like_count)
    TextView mLikeCount;

    @Nullable
    @BindView(R.id.comment_count)
    TextView mCommentCount;

    /* */
    @Nullable
    @BindView(R.id.profile_header)
    ImageView mProfileImage;

    @Nullable
    @BindView(R.id.display_name)
    TextView mDisplayName;

    @Nullable
    @BindView(R.id.nick_name)
    TextView mNickname;

    @Nullable
    @BindView(R.id.action_friend_status)
    BootstrapButton mAddFriendBtn;

    @Nullable
    @BindView(R.id.action_send_message)
    BootstrapButton mSendMessageBtn;

    @Nullable
    @BindView(R.id.profile_progress_bar)
    ProgressBar mProfileProgress;

    private EventResponse eventResponse;

    public UserInfoAndEventViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void initViewHolder(Context context, EventResponse response) {
        this.eventResponse = response;

        initEventHeader(context);
        initEventContent(context);
    }

    private void initEventHeader(Context context) {
        loadUserProfileImage(context, eventResponse.getUserInfo().getProfileUrl());
        mUserDisplayName.setText(eventResponse.getUserInfo().getDisplayName());
        mTimeFrame.setText(eventResponse.getCreateAt());
    }

    private void initEventContent(Context context) {
        if (eventResponse.getTextContent() != null) {
            mTextContent.setText(eventResponse.getTextContent());

            if (eventResponse.getTextContent().length() < 50) {
                mTextContent.setTextSize(24);
            } else {
                mTextContent.setTextSize(18);
            }
        }

        if (eventResponse.getEventType().equals(AppConstant.EVENT_TYPE_TEXT)) {

        } else if (eventResponse.getEventType().equals(AppConstant.EVENT_TYPE_SINGLE_IMAGE)) {
            loadSingleImage(eventResponse.getMedias().get(0).getUrl());
        } else if (eventResponse.getEventType().equals(AppConstant.EVENT_TYPE_SINGLE_GIF)) {
            loadSingleGif(eventResponse.getMedias().get(0).getUrl());
        } else if (eventResponse.getEventType().equals(AppConstant.EVENT_TYPE_MULT_IMAGE)) {
            loadMultipleImages(context, eventResponse.getMedias());
        }
    }

    private void loadMultipleImages(Context context, List<MediaResponse> mediaList) {
        mMultiImageAdapter = new MediaImagesAdapter(context, mediaList);

        assert mMultiMediaRecycler != null;
        if (mediaList.size() % 2 == 0 && mediaList.size() <= 4) {
            mMultiMediaRecycler.setLayoutManager(new GridLayoutManager(context, 2,
                    LinearLayoutManager.VERTICAL, false));
        } else {
            mMultiMediaRecycler.setLayoutManager(new GridLayoutManager(context, 3,
                    LinearLayoutManager.VERTICAL, false));
        }

        mMultiMediaRecycler.setAdapter(mMultiImageAdapter);
        mMultiMediaRecycler.setItemAnimator(new DefaultItemAnimator());
        mMultiMediaRecycler.setNestedScrollingEnabled(false);
    }

    private void loadSingleGif(String url) {
        Glide.with(mSingleMediaView.getContext())
                .load(url)
                .asGif()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .listener(new RequestListener<String, GifDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GifDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GifDrawable resource, String model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        mSingleContentProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(mSingleMediaView);
    }

    private void loadSingleImage(String url) {
        assert mSingleMediaView != null;
        Glide.with(mSingleMediaView.getContext())
                .load(url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
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
                        assert mSingleContentProgressBar != null;
                        mSingleContentProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(mSingleMediaView);
    }

    public void initUserInfo(Context context, boolean isMySelf) {
        if (isMySelf) {
            UserInfoResponse response = SharedPreferencesManager.getDefault().getUserKeyInfo();

            // Load profile to both side
            loadProfileImage(context, response.getProfileUrl());

            String nickname = "@" + response.getNickname();

            assert mDisplayName != null;
            mDisplayName.setText(response.getDisplayName());
            assert mNickname != null;
            mNickname.setText(nickname);
        }
    }

    private void loadProfileImage(Context context, String url) {
        Glide.with(context)
                .load(url)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
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
                        mProfileImage.setVisibility(View.VISIBLE);
                        assert mProfileProgress != null;
                        mProfileProgress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(mProfileImage);
    }

    private void loadUserProfileImage(Context context, String url) {
        Glide.with(context)
                .load(url)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .transform(new GlideCircleTransformation(context))
                .into(mUserProfileImage);
    }
}
