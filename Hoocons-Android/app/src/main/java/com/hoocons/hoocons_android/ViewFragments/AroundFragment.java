package com.hoocons.hoocons_android.ViewFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AroundFragment extends Fragment {
    @BindView(R.id.screen_around_fragment)
    RelativeLayout mMainScreen;
    @BindView(R.id.screen_null_around_fragment)
    RelativeLayout mEmptyScreen;
    @BindView(R.id.nearme_swiperef)
    SwipeRefreshLayout mSwipeRefLayout;
    @BindView(R.id.nearme_user_recycler)
    RecyclerView mUserAroundRecycler;
    @BindView(R.id.nearme_event_recycler)
    RecyclerView mEventAroundRecycler;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AroundFragment() {
    }

    public static AroundFragment newInstance(String param1, String param2) {
        AroundFragment fragment = new AroundFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_around, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
