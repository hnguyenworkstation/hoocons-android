package com.hoocons.hoocons_android.ViewFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoocons.hoocons_android.R;

public class PlayGroundPeopleFragment extends Fragment {
    public PlayGroundPeopleFragment() {
        // Required empty public constructor
    }

    public static PlayGroundPeopleFragment newInstance(String param1, String param2) {
        PlayGroundPeopleFragment fragment = new PlayGroundPeopleFragment();
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
        return inflater.inflate(R.layout.fragment_play_ground_people, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void onRestore() {

    }
}
