package com.hoocons.hoocons_android.ViewHolders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.hoocons.hoocons_android.CustomUI.AdjustableImageView;
import com.hoocons.hoocons_android.CustomUI.CustomTextView;
import com.hoocons.hoocons_android.Networking.Responses.MeetOutResponse;
import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hungnguyen on 7/29/17.
 */

public class MeetOutCardViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.meetout_image)
    AdjustableImageView mImageView;
    @BindView(R.id.image_progress)
    ProgressBar mProgressBar;

    @BindView(R.id.meetout_name)
    TextView mMeetOutName;
    @BindView(R.id.meetout_desc)
    CustomTextView mMeetOutDesc;
    @BindView(R.id.meetout_owner)
    TextView mMeetOutOwner;
    @BindView(R.id.meetout_location_name)
    TextView mMeetOutLocName;
    @BindView(R.id.meetout_location_address)
    TextView mMeetOutLocAddress;
    @BindView(R.id.meetout_time)
    TextView mMeetOutTimeFrame;

    @BindView(R.id.meetout_love)
    LinearLayout mMeetOutLoveBtn;
    @BindView(R.id.love_icon)
    ImageView mLoveIcon;
    @BindView(R.id.meetout_details)
    LinearLayout mMeetOutDetails;

    @BindView(R.id.action_join)
    BootstrapButton mActionJoin;


    public MeetOutCardViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void initCardViewHolder(final Context context, final MeetOutResponse response,
                                   final int position) {

        // Load Image View
        Glide.with(context)
                .load(response.getPromotedMedias().get(0))
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.noAnimation())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(mImageView);

        // Load Text Content
        mMeetOutName.setText(response.getName());
        mMeetOutDesc.setContent(response.getDescription());
        mMeetOutLocName.setText(response.getMeetupLocationName());
        mMeetOutLocAddress.setText(response.getMeetupLocationAddress());
    }
}
