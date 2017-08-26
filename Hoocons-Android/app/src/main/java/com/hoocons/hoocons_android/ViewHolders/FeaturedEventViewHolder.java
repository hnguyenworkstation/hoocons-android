package com.hoocons.hoocons_android.ViewHolders;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.hoocons.hoocons_android.Adapters.MediaImagesAdapter;
import com.hoocons.hoocons_android.CustomUI.AdjustableImageView;
import com.hoocons.hoocons_android.CustomUI.CustomTextView;
import com.hoocons.hoocons_android.CustomUI.RoundedCornersTransformation;
import com.hoocons.hoocons_android.EventBus.StartEventChildImages;
import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Helpers.MapUtils;
import com.hoocons.hoocons_android.Interface.EventAdapterListener;
import com.hoocons.hoocons_android.Interface.OnChildImageClickListener;
import com.hoocons.hoocons_android.Interface.OnUserInfoClickListener;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Models.SimpleMeetout;
import com.hoocons.hoocons_android.Networking.Responses.EventResponse;
import com.hoocons.hoocons_android.Networking.Responses.MediaResponse;
import com.hoocons.hoocons_android.Networking.Responses.UserInfoResponse;
import com.hoocons.hoocons_android.Parcel.MeetOutParcel;
import com.hoocons.hoocons_android.R;
import com.vstechlab.easyfonts.EasyFonts;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by hungnguyen on 8/11/17.
 */

public class FeaturedEventViewHolder extends ViewHolder {
    /* EVENT TOP MESSAGE */
    @Nullable
    @BindView(R.id.event_top_layout)
    LinearLayout mTopMessageLayout;

    @Nullable
    @BindView(R.id.event_intro_info)
    TextView mIntroMessage;

    @Nullable
    @BindView(R.id.event_intro_options)
    ImageButton mIntroMoreButton;

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

    /* SHARED EVENT HEADER */
    @Nullable
    @BindView(R.id.shared_event_user_profile)
    ImageView mSharedUserProfileImage;

    @Nullable
    @BindView(R.id.shared_event_user_name)
    TextView mSharedUserDisplayName;

    @Nullable
    @BindView(R.id.shared_event_timeframe)
    TextView mSharedTimeFrame;

    @Nullable
    @BindView(R.id.shared_event_location_icon)
    ImageView mSharedHeaderLocationIcon;

    @Nullable
    @BindView(R.id.shared_event_posted_location)
    TextView mSharedPostedLocation;

    @Nullable
    @BindView(R.id.shared_event_text_content)
    CustomTextView mSharedEventTextContent;

    /* EVENT TEXT CONTENT */
    @Nullable
    @BindView(R.id.event_text_content)
    CustomTextView mTextContent;

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
    @BindView(R.id.love_event_action)
    LinearLayout mLoveView;

    @Nullable
    @BindView(R.id.love_icon)
    ImageView mLoveIcon;

    @Nullable
    @BindView(R.id.love_text)
    TextView mLoveText;

    @Nullable
    @BindView(R.id.comment_event_action)
    LinearLayout mCommentView;

    @Nullable
    @BindView(R.id.comment_text)
    TextView mCommentText;

    @Nullable
    @BindView(R.id.like_count)
    TextView mLikeCount;

    @Nullable
    @BindView(R.id.comment_count)
    TextView mCommentCount;

    /*  OTHER LIST VIEWs */
    @Nullable
    @BindView(R.id.empty_recycler)
    ObservableRecyclerView mEmptyRecycler;

    private Spring mLikeBtnScaleSpring;
    private Spring mCommentBtnScaleSpring;
    private final BaseSpringSystem mSpringSystem = SpringSystem.create();
    private GifDrawable gifDrawable;

    public FeaturedEventViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void initViewHolder(final Context context, final EventResponse response,
                               final EventAdapterListener listener, final int position) {
        initEventHeader(response);
        initEventFooter(context, response);
        initEventTypeFace(context);

        assert mTextContent != null;
        if (response.getTextContent() != null && response.getTextContent().length() >  1) {
            mTextContent.setContent(response.getTextContent());
            mTextContent.setVisibility(View.VISIBLE);

            if (response.getTextContent().length() < 50) {
                mTextContent.setTextSize(20);
            } else {
                mTextContent.setTextSize(16);
            }
        }

        if (response.getContainEvent() == null) {
            initEventContent(context, response, position);
        } else {
            assert mSharedEventTextContent != null;
            initSharedEventHeader(context, response.getContainEvent(), listener, position);
            mSharedEventTextContent.setVisibility(View.VISIBLE);
            mSharedEventTextContent.setContent(response.getContainEvent().getTextContent());
            initEventContent(context, response.getContainEvent(), position);

            assert mTextContent != null;
            if (response.getTextContent() != null && response.getTextContent().length() >  1) {
                mSharedEventTextContent.setContent(response.getContainEvent().getTextContent());
                mSharedEventTextContent.setVisibility(View.VISIBLE);
                mSharedEventTextContent.setTypeface(EasyFonts.robotoRegular(context));
            }
        }

        initOnClickListener(listener, position);
    }

    private void initEventTypeFace(final Context context) {
        assert mLikeCount != null;
        assert mCommentCount != null;
        assert mCommentText != null;
        assert mLoveText != null;
        assert mTextContent != null;
        assert mTimeFrame != null;
        assert mUserDisplayName != null;

        mLoveText.setTypeface(EasyFonts.robotoLight(context));
        mCommentText.setTypeface(EasyFonts.robotoLight(context));
        mTimeFrame.setTypeface(EasyFonts.robotoLight(context));

        mLikeCount.setTypeface(EasyFonts.robotoRegular(context));
        mCommentCount.setTypeface(EasyFonts.robotoRegular(context));
        mTextContent.setTypeface(EasyFonts.robotoRegular(context));

        mUserDisplayName.setTypeface(EasyFonts.robotoBold(context));
    }

    private void initSharedEventHeader(final Context context, final EventResponse response,
                                       final EventAdapterListener listener, final int position) {
        loadUserProfileImage(response.getAuthor().getProfileUrl(), mSharedUserProfileImage);

        assert mSharedTimeFrame != null;
        assert mSharedUserDisplayName != null;

        mSharedUserDisplayName.setText(response.getAuthor().getDisplayName());
        mSharedTimeFrame.setText(AppUtils.convertDateTimeFromUTC(response.getCreateAt()));

        mSharedTimeFrame.setTypeface(EasyFonts.robotoLight(context));
        mSharedUserDisplayName.setTypeface(EasyFonts.robotoBold(context));

        mSharedUserDisplayName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSharedUserInfoClicked(position);
            }
        });

        mSharedUserProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSharedUserInfoClicked(position);
            }
        });
    }

    private void initOnClickListener(final EventAdapterListener listener, final int position) {
        assert mLoveView != null;
        assert mCommentView != null;
        assert mHeaderMoreButton != null;
        assert mUserDisplayName != null;
        assert mUserProfileImage != null;
        assert mSharedUserDisplayName != null;

        mLoveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onLikeClicked(position);
            }
        });

        mCommentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCommentClicked(position);
            }
        });

        mHeaderMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onOptionClicked(mHeaderMoreButton, position);
            }
        });

        mUserDisplayName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onUserInfoClicked(position);
            }
        });
        mUserProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onUserInfoClicked(position);
            }
        });
    }

    private void initEventFooter(Context context, final EventResponse eventResponse) {
        assert mLoveView != null;
        assert mLikeCount != null;
        assert mCommentCount != null;
        assert mCommentView != null;
        assert mCommentText != null;
        assert mLoveIcon != null;
        assert mLoveText != null;

        mCommentBtnScaleSpring = mSpringSystem.createSpring();
        mCommentBtnScaleSpring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.5);

                mCommentView.setScaleX(mappedValue);
                mCommentView.setScaleY(mappedValue);
            }
        });

        String likeCount = String.valueOf(eventResponse.getLikesCount()) + " likes";
        String commentCount = String.valueOf(eventResponse.getCommentsCount() + " comments");
        mLikeCount.setText(likeCount);
        mCommentCount.setText(commentCount);

        if (eventResponse.getIsLiked()) {
            mLoveIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_color));
            mLoveText.setText(context.getResources().getText(R.string.loved));
            mLoveText.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            mLoveIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorite_inactive));
            mLoveText.setText(context.getResources().getText(R.string.love));
            mLoveText.setTextColor(context.getResources().getColor(R.color.event_timestamp));
        }

        // Init Spring
        mLikeBtnScaleSpring = mSpringSystem.createSpring();
        mLikeBtnScaleSpring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.5);

                mLoveView.setScaleX(mappedValue);
                mLoveView.setScaleY(mappedValue);
            }
        });

        mLoveView.setOnTouchListener(new View.OnTouchListener() {
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

        mCommentView.setOnTouchListener(new View.OnTouchListener() {
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

    private void initEventHeader(final EventResponse eventResponse) {
        assert mUserDisplayName != null;
        assert mTimeFrame != null;

        loadUserProfileImage(eventResponse.getAuthor().getProfileUrl(), mUserProfileImage);
        mUserDisplayName.setText(eventResponse.getAuthor().getDisplayName());
        mTimeFrame.setText(AppUtils.convertDateTimeFromUTC(eventResponse.getCreateAt()));
    }

    private void initEventContent(final Context context, final EventResponse eventResponse,
                                  final int eventPosition) {
        if (eventResponse.getEventType().equals(AppConstant.EVENT_TYPE_TEXT)) {

        } else if (eventResponse.getEventType().equals(AppConstant.EVENT_TYPE_SINGLE_IMAGE)) {
            loadSingleImage(eventResponse.getMedias().get(0).getUrl());
        } else if (eventResponse.getEventType().equals(AppConstant.EVENT_TYPE_SINGLE_GIF)) {
            loadSingleGif(eventResponse.getMedias().get(0).getUrl());
        } else if (eventResponse.getEventType().equals(AppConstant.EVENT_TYPE_MULT_IMAGE)) {
            loadMultipleImages(context, eventResponse.getMedias(), eventPosition);
        } else if (eventResponse.getEventType().equals(AppConstant.EVENT_TYPE_SINGLE_VIDEO)) {
            loadVideoView(eventResponse.getMedias().get(0));
        } else if (eventResponse.getEventType().equals(AppConstant.EVENT_TYPE_CHECK_IN)) {
            loadCheckinMapView(eventResponse);
        }
    }

    private void loadCheckinMapView(final EventResponse eventResponse) {
        assert mCheckinName != null;
        assert mCheckinType != null;
        loadLocationMapView(MapUtils.getMapLocationUrl(String.valueOf(eventResponse.getCheckInLocation().getCoordinates()[1]),
                String.valueOf(eventResponse.getCheckInLocation().getCoordinates()[0])));

        mCheckinName.setText(eventResponse.getCheckinName());
        mCheckinType.setText(eventResponse.getCheckinAddress());
    }

    private void loadLocationMapView(String url) {
        assert mLocationMapView != null;
        BaseApplication.getInstance().getGlide()
                .load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.noAnimation())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        assert mLocMapProgress != null;
                        mLocMapProgress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(mLocationMapView);
    }

    private void loadVideoView(MediaResponse mediaResponse) {
        assert mVideoPlayer != null;
        mVideoPlayer.setUp(mediaResponse.getUrl(), JCVideoPlayer.SCREEN_LAYOUT_LIST, "Testing");
        BaseApplication.getInstance().getGlide()
                .load(AppUtils.getDefaultProfileUrl())
                .into(mVideoPlayer.thumbImageView);
    }

    private void loadMultipleImages(final Context context, final List<MediaResponse> mediaList,
                                    final int eventPosition) {
        assert mMultiMediaRecycler != null;
        mMultiImageAdapter = new MediaImagesAdapter(context, mediaList, eventPosition,
                new OnChildImageClickListener() {
                    @Override
                    public void onChildImageClick(int eventPosition, int position) {
                        EventBus.getDefault().post(new StartEventChildImages(eventPosition, position));
                    }
                });

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

    private void loadSingleGif(final String url) {
        assert mSingleMediaView != null;
        BaseApplication.getInstance().getGlide()
                .load(url)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                   DataSource dataSource, boolean isFirstResource) {
                        if (resource instanceof GifDrawable) {
                            gifDrawable = (GifDrawable) resource;
                        } else {
                            gifDrawable = null;
                        }

                        assert mSingleContentProgressBar != null;
                        mSingleContentProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(mSingleMediaView);

        mSingleMediaView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gifDrawable != null) {
                    if (gifDrawable.isRunning()) {
                        gifDrawable.stop();
                    } else {
                        gifDrawable.start();
                    }
                }
            }
        });

    }

    private void loadSingleImage(String url) {
        assert mSingleMediaView != null;
        BaseApplication.getInstance().getGlide()
                .load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.noAnimation())
                .apply(RequestOptions.centerCropTransform())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        assert mSingleContentProgressBar != null;
                        mSingleContentProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(mSingleMediaView);
    }


    private void loadUserProfileImage(final String url, ImageView imageView) {
        BaseApplication.getInstance().getGlide()
                .load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.noAnimation())
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);
    }
}
