package com.hoocons.hoocons_android.ViewHolders;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.font.FontAwesome;
import com.birbit.android.jobqueue.JobManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.hoocons.hoocons_android.Adapters.MediaImagesAdapter;
import com.hoocons.hoocons_android.CustomUI.AdjustableImageView;
import com.hoocons.hoocons_android.CustomUI.GlideCircleTransformation;
import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Helpers.MapUtils;
import com.hoocons.hoocons_android.Interface.EventAdapterListener;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.Networking.Responses.EventResponse;
import com.hoocons.hoocons_android.Networking.Responses.MediaResponse;
import com.hoocons.hoocons_android.Networking.Responses.UserInfoResponse;
import com.hoocons.hoocons_android.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

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
    ImageView mSingleMediaView;

    @Nullable
    @BindView(R.id.single_content_progressbar)
    ProgressBar mSingleContentProgressBar;

    /* EVENT WITH VIDEO MEDIA */
    @Nullable
    @BindView(R.id.event_video_player)
    JCVideoPlayerStandard mVideoPlayer;

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
    @BindView(R.id.event_location_map)
    AdjustableImageView mLocationMapView;

    @Nullable
    @BindView(R.id.load_map_view_progress)
    ProgressBar mLocMapProgress;

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

    private Spring mLikeBtnScaleSpring;
    private Spring mCommentBtnScaleSpring;
    private final BaseSpringSystem mSpringSystem = SpringSystem.create();

    public UserInfoAndEventViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void initViewHolder(final Context context, final EventResponse response,
                               final EventAdapterListener listener, final int position) {
        initEventHeader(context, response);
        initEventContent(context, response);
        initEventFooter(context, response);
        initOnClickListener(listener, position);
    }

    private void initOnClickListener(final EventAdapterListener listener, final int position) {
        assert mLikeBtn != null;
        mLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onLikeClicked(position);
            }
        });
    }

    private void initEventFooter(Context context, final EventResponse eventResponse) {
        assert mLikeBtn != null;
        assert mLikeCount != null;
        assert mCommentCount != null;
        assert mCommentBtn != null;

        mCommentBtnScaleSpring = mSpringSystem.createSpring();
        mCommentBtnScaleSpring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.5);

                mCommentBtn.setScaleX(mappedValue);
                mCommentBtn.setScaleY(mappedValue);
            }
        });

        String likeCount = String.valueOf(eventResponse.getLikesCount()) + " likes";
        String commentCount = String.valueOf(eventResponse.getCommentsCount() + " comments");
        mLikeCount.setText(likeCount);
        mCommentCount.setText(commentCount);

        if (eventResponse.getIsLiked()) {
            mLikeBtn.setBootstrapBrand(DefaultBootstrapBrand.PRIMARY);
            mLikeBtn.setBootstrapText(new BootstrapText.Builder(context)
                    .addFontAwesomeIcon(FontAwesome.FA_THUMBS_UP)
                    .addText(" " + context.getResources().getText(R.string.liked))
                    .build());
        } else {
            mLikeBtn.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
            mLikeBtn.setBootstrapText(new BootstrapText.Builder(context)
                    .addFontAwesomeIcon(FontAwesome.FA_THUMBS_O_UP)
                    .addText(" " + context.getResources().getText(R.string.like))
                    .build());
        }

        // Init Spring
        mLikeBtnScaleSpring = mSpringSystem.createSpring();
        mLikeBtnScaleSpring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.5);

                mLikeBtn.setScaleX(mappedValue);
                mLikeBtn.setScaleY(mappedValue);
            }
        });

        mLikeBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mLikeBtnScaleSpring.setEndValue(0.3);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mLikeBtnScaleSpring.setEndValue(0);
                        break;
                }
                return false;
            }
        });

        mCommentBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mCommentBtnScaleSpring.setEndValue(0.3);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mCommentBtnScaleSpring.setEndValue(0);
                        break;
                }
                return false;
            }
        });
    }

    private void initEventHeader(final Context context, final EventResponse eventResponse) {
        loadUserProfileImage(context, eventResponse.getUserInfo().getProfileUrl());
        mUserDisplayName.setText(eventResponse.getUserInfo().getDisplayName());
        mTimeFrame.setText(eventResponse.getCreateAt());
    }

    private void initEventContent(Context context, final EventResponse eventResponse) {
        if (eventResponse.getTextContent() != null) {
            mTextContent.setText(eventResponse.getTextContent());

            if (eventResponse.getTextContent().length() < 50) {
                mTextContent.setTextSize(20);
            } else {
                mTextContent.setTextSize(16);
            }
        }

        if (eventResponse.getEventType().equals(AppConstant.EVENT_TYPE_TEXT)) {

        } else if (eventResponse.getEventType().equals(AppConstant.EVENT_TYPE_SINGLE_IMAGE)) {
            loadSingleImage(context, eventResponse.getMedias().get(0).getUrl());
        } else if (eventResponse.getEventType().equals(AppConstant.EVENT_TYPE_SINGLE_GIF)) {
            loadSingleGif(context, eventResponse.getMedias().get(0).getUrl());
        } else if (eventResponse.getEventType().equals(AppConstant.EVENT_TYPE_MULT_IMAGE)) {
            loadMultipleImages(eventResponse.getMedias());
        } else if (eventResponse.getEventType().equals(AppConstant.EVENT_TYPE_SINGLE_VIDEO)) {
            loadVideoView(eventResponse.getMedias().get(0));
        } else if (eventResponse.getEventType().equals(AppConstant.EVENT_TYPE_CHECK_IN)) {
            loadCheckinMapView(eventResponse);
        }
    }

    private void loadCheckinMapView(final EventResponse eventResponse) {
        loadLocationMapView(MapUtils.getMapLocationUrl(String.valueOf(eventResponse.getCheckInLocation().getCoordinates()[1]),
                String.valueOf(eventResponse.getCheckInLocation().getCoordinates()[0])));

        mCheckinName.setText(eventResponse.getCheckinName());
        mCheckinType.setText(eventResponse.getCheckinAddress());
    }

    private void loadLocationMapView(String url) {
        Glide.with(mLocationMapView.getContext())
                .load(url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        mLocMapProgress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(mLocationMapView);
    }


    private void loadVideoView(MediaResponse mediaResponse) {
        assert mVideoPlayer != null;
        mVideoPlayer.setUp(mediaResponse.getUrl(), JCVideoPlayer.SCREEN_LAYOUT_LIST, "Testing");
        Glide.with(mVideoPlayer.getContext())
                .load(AppUtils.getDefaultProfileUrl())
                .into(mVideoPlayer.thumbImageView);
    }

    private void loadMultipleImages(List<MediaResponse> mediaList) {
        assert mMultiMediaRecycler != null;
        mMultiImageAdapter = new MediaImagesAdapter(mMultiMediaRecycler.getContext(), mediaList);

        if (mediaList.size() % 2 == 0 && mediaList.size() <= 4) {
            mMultiMediaRecycler.setLayoutManager(new GridLayoutManager(mMultiMediaRecycler.getContext(), 2,
                    LinearLayoutManager.VERTICAL, false));
        } else {
            mMultiMediaRecycler.setLayoutManager(new GridLayoutManager(mMultiMediaRecycler.getContext(), 3,
                    LinearLayoutManager.VERTICAL, false));
        }

        mMultiMediaRecycler.setAdapter(mMultiImageAdapter);
        mMultiMediaRecycler.setItemAnimator(new DefaultItemAnimator());
        mMultiMediaRecycler.setNestedScrollingEnabled(false);
    }

    private void loadSingleGif(final Context context, String url) {
        Glide.with(context)
                .load(url)
                .asGif()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .dontAnimate()
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

    private void loadSingleImage(final Context context, String url) {
        assert mSingleMediaView != null;
        Glide.with(context)
                .load(url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
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
