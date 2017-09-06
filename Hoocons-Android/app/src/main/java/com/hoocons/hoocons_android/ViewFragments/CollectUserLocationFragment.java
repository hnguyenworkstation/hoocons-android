package com.hoocons.hoocons_android.ViewFragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.hoocons.hoocons_android.EventBus.LocationPermissionAllowed;
import com.hoocons.hoocons_android.EventBus.LocationPermissionDenied;
import com.hoocons.hoocons_android.EventBus.LocationURLRequest;
import com.hoocons.hoocons_android.EventBus.LocationUrlReady;
import com.hoocons.hoocons_android.EventBus.StringDataCollected;
import com.hoocons.hoocons_android.EventBus.UserInfoRequest;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.R;
import com.vstechlab.easyfonts.EasyFonts;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectUserLocationFragment extends Fragment {
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.map_holder)
    CardView mMapHolder;
    @BindView(R.id.map_image)
    ImageView mMapImage;
    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;
    @BindView(R.id.lost_location_view)
    LinearLayout mLostLocationView;
    @BindView(R.id.allow_permission)
    Button mAllowPermission;
    @BindView(R.id.address_view)
    LinearLayout mAddressView;
    @BindView(R.id.address)
    TextView mAddress;
    @BindView(R.id.gcn_next)
    Button mSubmitBtn;
    @BindView(R.id.thanks)
    TextView mThankText;
    @BindView(R.id.need_permission_message)
    TextView mNeedPermissionMessage;

    public CollectUserLocationFragment() {
        // Required empty public constructor
    }

    public static CollectUserLocationFragment newInstance(String param1, String param2) {
        CollectUserLocationFragment fragment = new CollectUserLocationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collect_user_location, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        EventBus.getDefault().post(new LocationURLRequest());

        mAllowPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new LocationURLRequest());
            }
        });

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSubmitBtn.isEnabled()) {
                    EventBus.getDefault().post(new UserInfoRequest());
                }
            }
        });

        initTextAndTypeface();
    }

    private void initTextAndTypeface() {
        mThankText.setText(getResources().getString(R.string.shared_location_permission));
        mTitle.setText(getResources().getString(R.string.at_location));
        mNeedPermissionMessage.setText(getResources().getString(R.string.need_location_permission));
        mAllowPermission.setText(getResources().getString(R.string.action_allow_permission));
        mSubmitBtn.setText(getResources().getString(R.string.submit));

        mThankText.setTypeface(EasyFonts.robotoRegular(getContext()));
        mTitle.setTypeface(EasyFonts.robotoBold(getContext()));
        mNeedPermissionMessage.setTypeface(EasyFonts.robotoRegular(getContext()));
        mAllowPermission.setTypeface(EasyFonts.robotoBold(getContext()));
        mSubmitBtn.setTypeface(EasyFonts.robotoBold(getContext()));
        mAddress.setTypeface(EasyFonts.robotoBold(getContext()));
    }

    private void initPermissionAllowedLayout() {
        mMapHolder.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mLostLocationView.setVisibility(View.GONE);
        mAddressView.setVisibility(View.VISIBLE);
        mThankText.setVisibility(View.VISIBLE);
    }

    private void initMapView(String url) {
        if (url != null) {
            BaseApplication.getInstance().getGlide()
                    .load(url)
                    .apply(RequestOptions.centerCropTransform())
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            if (mProgressBar != null) {
                                mProgressBar.setVisibility(View.GONE);
                            }
                            return false;
                        }
                    })
                    .into(mMapImage);
        }
    }

    @Subscribe
    public void onEvent(LocationPermissionAllowed req) {
        initPermissionAllowedLayout();
    }

    @Subscribe
    public void onEvent(LocationPermissionDenied req) {

    }

    @Subscribe
    public void onEvent(StringDataCollected stringDataCollected) {
        mAddress.setText(stringDataCollected.getData());
    }

    @Subscribe
    public void onEvent(LocationUrlReady request) {
        initMapView(request.getUrl());
    }

}
