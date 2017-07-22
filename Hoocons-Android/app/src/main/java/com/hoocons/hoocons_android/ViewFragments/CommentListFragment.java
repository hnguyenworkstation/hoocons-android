package com.hoocons.hoocons_android.ViewFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoocons.hoocons_android.R;

public class CommentListFragment extends Fragment {
    private static final String ARG_EVENT_ID = "EventId";
    private static final String ARG_LIKE_COUNT = "LikeCount";
    private static final String ARG_IS_LIKED= "IsLiked";

    private boolean isLiked;
    private int likeCount;
    private int eventId;

    public CommentListFragment() {
    }

    public static CommentListFragment newInstance(int eventId, int likeCount, boolean isLiked) {
        CommentListFragment fragment = new CommentListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_EVENT_ID, eventId);
        args.putInt(ARG_LIKE_COUNT, likeCount);
        args.putBoolean(ARG_IS_LIKED, isLiked);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventId = getArguments().getInt(ARG_EVENT_ID);
            isLiked = getArguments().getBoolean(ARG_IS_LIKED);
            likeCount = getArguments().getInt(ARG_LIKE_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
