package com.hoocons.hoocons_android.ViewFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hoocons.hoocons_android.Activities.UserProfileActivity;
import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeaturedFragment extends Fragment {
    @BindView(R.id.screen_no_connection_error)
    RelativeLayout mErrorScreen;
    @BindView(R.id.screen_featured_fragment)
    RelativeLayout mMainScreen;
    @BindView(R.id.swipe_ref_layout)
    SwipeRefreshLayout mSwipeRefLayout;
    @BindView(R.id.bottom_menu)
    RelativeLayout mBottomMenuLayout;
    @BindView(R.id.header_profile)
    ImageView mImageHeader;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    public FeaturedFragment() {
        // Required empty public constructor
    }

    public static FeaturedFragment newInstance(String param1, String param2) {
        FeaturedFragment fragment = new FeaturedFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_featured, container, false);
        ButterKnife.bind(this, rootView);

        mImageHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UserProfileActivity.class));
            }
        });

        return rootView;
    }
}
