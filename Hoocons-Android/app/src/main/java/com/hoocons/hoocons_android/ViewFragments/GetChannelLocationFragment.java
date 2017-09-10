package com.hoocons.hoocons_android.ViewFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hoocons.hoocons_android.EventBus.LocationRequestCollected;
import com.hoocons.hoocons_android.EventBus.LocationURLRequest;
import com.hoocons.hoocons_android.EventBus.LocationUrlReady;
import com.hoocons.hoocons_android.R;
import com.vstechlab.easyfonts.EasyFonts;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GetChannelLocationFragment extends Fragment {
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.pick_location_view)
    LinearLayout mPickLocationView;
    @BindView(R.id.pick_location)
    TextView mPickLocationText;
    @BindView(R.id.pick_location_btn)
    Button mPickLocBtn;

    @BindView(R.id.address_view)
    LinearLayout mAddressView;
    @BindView(R.id.address)
    TextView mAddress;
    @BindView(R.id.gcn_next)
    Button mNextButton;

    private String locationAddress;

    public GetChannelLocationFragment() {
        // Required empty public constructor
    }

    public static GetChannelLocationFragment newInstance() {
        GetChannelLocationFragment fragment = new GetChannelLocationFragment();
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
        return inflater.inflate(R.layout.fragment_get_channel_location, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initTextAndTypeFace();
        initListener();
    }

    private void initTextAndTypeFace() {
        mNextButton.setText(getResources().getString(R.string.continue_text));
        mPickLocationText.setText(getResources().getString(R.string.pick_channel_location));
        mTitle.setText(getResources().getString(R.string.at_location));
        mPickLocBtn.setText(getResources().getString(R.string.pick_location));

        mNextButton.setTypeface(EasyFonts.robotoRegular(getContext()));
        mPickLocationText.setTypeface(EasyFonts.robotoRegular(getContext()));
        mTitle.setTypeface(EasyFonts.robotoBold(getContext()));
        mPickLocBtn.setTypeface(EasyFonts.robotoBold(getContext()));
    }

    private void initListener() {
        mPickLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new LocationURLRequest());
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new LocationRequestCollected());
            }
        });

        mAddressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new LocationURLRequest());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(LocationUrlReady locationUrlReady) {
        locationAddress = locationUrlReady.getUrl();

        mAddress.setText(locationAddress);
        mAddressView.setVisibility(View.VISIBLE);
        mPickLocationView.setVisibility(View.GONE);
    }
}
