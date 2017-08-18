package com.hoocons.hoocons_android.ViewFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChannelAboutFragment extends Fragment {
    @BindView(R.id.custom_toolbar)
    RelativeLayout mCustomToolbar;
    @BindView(R.id.small_profile)
    RelativeLayout mSmallProfile;
    @BindView(R.id.obs_scrollview)
    ObservableRecyclerView mRecyclerView;

    @BindView(R.id.small_profile_header)
    ImageView mSmallProfileImage;
    @BindView(R.id.wallpaper_image)
    ImageView mWallpaperImage;

    @BindView(R.id.action_back)
    ImageButton mActionBack;
    @BindView(R.id.action_more)
    ImageButton mActionMore;

    @BindView(R.id.wallpaper_progress_bar)
    ProgressBar mWallpaperProgress;
    @BindView(R.id.small_profile_progress_bar)
    ProgressBar mSmallProfileProgress;

    @BindView(R.id.linear)
    View mLinear;
    @BindView(R.id.user_display_name)
    TextView mActionBarDisplayName;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    public ChannelAboutFragment() {
        // Required empty public constructor
    }

    public static ChannelAboutFragment newInstance(String param1, String param2) {
        ChannelAboutFragment fragment = new ChannelAboutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_channel_about, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }
}
