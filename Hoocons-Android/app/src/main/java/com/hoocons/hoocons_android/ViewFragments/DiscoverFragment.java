package com.hoocons.hoocons_android.ViewFragments;


import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hoocons.hoocons_android.Adapters.CommentsAdapter;
import com.hoocons.hoocons_android.Adapters.DiscoverTopPanelAdapter;
import com.hoocons.hoocons_android.Adapters.EventCardViewAdapter;
import com.hoocons.hoocons_android.CustomUI.StaggeredItemDecorator;
import com.hoocons.hoocons_android.R;
import com.vstechlab.easyfonts.EasyFonts;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscoverFragment extends Fragment {
    @BindView(R.id.disco_search)
    ImageButton mSearchButton;

    @BindView(R.id.top_panel)
    RecyclerView mTopPanelRecycler;

    @BindView(R.id.top_events)
    RecyclerView mTopEventRecycler;

    @BindView(R.id.followed_topics)
    LinearLayout mFollowedTopicLayout;

    @BindView(R.id.featured_event_txt)
    TextView mFeaturedEventTxt;

    private String[] tempTopicsArray;
    private boolean isFirstTime = true;

    private DiscoverTopPanelAdapter mPanelAdapter;
    private EventCardViewAdapter mEventAdapter;

    private StaggeredItemDecorator staggeredItemDecorator;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    public static DiscoverFragment newInstance(String param1, String param2) {
        DiscoverFragment fragment = new DiscoverFragment();
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
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        tempTopicsArray = getResources().getStringArray(R.array.channel_category);
    }

    private void initFollowedTopicsView() {
        int[] androidColors = getResources().getIntArray(R.array.array_topic_color);

        for (int i = 0; i < tempTopicsArray.length; i++) {
            final LinearLayout topicView = (LinearLayout) getLayoutInflater().inflate(R.layout.general_topic_view_layout,
                    mFollowedTopicLayout, false);
            TextView topicName = (TextView) topicView.findViewById(R.id.topic_text);

            // Set topic random color
            topicView.setBackgroundResource(R.drawable.general_rounded_shape);
            GradientDrawable drawable = (GradientDrawable) topicView.getBackground();
            drawable.setColor(androidColors[new Random().nextInt(androidColors.length)]);

            // Set Topic text
            topicName.setText(tempTopicsArray[i]);
            topicName.setTypeface(EasyFonts.robotoRegular(getContext()));
            topicView.setTag(i);

            mFollowedTopicLayout.addView(topicView);
            topicView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = (int) view.getTag();
                    Toast.makeText(getContext(), "clicked: " + tempTopicsArray[i], Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initTopPanelRecycler() {
        SnapHelper snapHelper = new LinearSnapHelper();

        mPanelAdapter = new DiscoverTopPanelAdapter();
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        mTopPanelRecycler.setLayoutManager(mLayoutManager);
        mTopPanelRecycler.setItemAnimator(new DefaultItemAnimator());
        mTopPanelRecycler.setAdapter(mPanelAdapter);

        snapHelper.attachToRecyclerView(mTopPanelRecycler);
    }

    private void initTopEventRecycler() {
        mEventAdapter = new EventCardViewAdapter();
        final StaggeredGridLayoutManager mLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mTopEventRecycler.setLayoutManager(mLayoutManager);
        mTopEventRecycler.setItemAnimator(new DefaultItemAnimator());
        mTopEventRecycler.setAdapter(mEventAdapter);
        mTopEventRecycler.setNestedScrollingEnabled(false);

        if (staggeredItemDecorator != null) {
            mTopEventRecycler.removeItemDecoration(staggeredItemDecorator);
        } else {
            staggeredItemDecorator = new StaggeredItemDecorator(8);
        }
        mTopEventRecycler.addItemDecoration(staggeredItemDecorator);
    }

    private void updateFinalView() {
        if (mEventAdapter.getItemCount() > 0) {
            mFeaturedEventTxt.setTypeface(EasyFonts.robotoBold(getContext()));
        } else {
            mFeaturedEventTxt.setVisibility(View.GONE);
        }
    }

    public void onRestore() {
        if (isFirstTime) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        synchronized (this) {
                            wait(500);

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initTopPanelRecycler();
                                    initFollowedTopicsView();
                                    initTopEventRecycler();
                                    updateFinalView();
                                }
                            });

                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    isFirstTime = false;
                };
            }.start();
        }
    }

}
