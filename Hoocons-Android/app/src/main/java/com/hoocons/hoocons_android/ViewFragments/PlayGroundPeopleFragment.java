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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlayGroundPeopleFragment.
     */
    // TODO: Rename and change types and number of parameters
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
