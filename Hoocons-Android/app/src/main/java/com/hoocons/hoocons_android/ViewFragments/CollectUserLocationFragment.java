package com.hoocons.hoocons_android.ViewFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hoocons.hoocons_android.R;

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
    }
}
