package com.hoocons.hoocons_android.ViewFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hoocons.hoocons_android.EventBus.FetchChatRoomsComplete;
import com.hoocons.hoocons_android.Interface.OnChatRoomClickListener;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.Tasks.Jobs.FetchChatRoomsJob;
import com.vstechlab.easyfonts.EasyFonts;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessagingFragment extends Fragment implements OnChatRoomClickListener {
    @BindView(R.id.communicate_older_textview)
    TextView mRecentConvsTitle;

    @BindView(R.id.convs_recycler)
    RecyclerView mConvRecycler;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public MessagingFragment() {

    }

    public static MessagingFragment newInstance(String param1, String param2) {
        MessagingFragment fragment = new MessagingFragment();
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

        EventBus.getDefault().register(this);

        // Fetching chat rooms in background
        BaseApplication.getInstance().getJobManager().addJobInBackground(new FetchChatRoomsJob());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messaging, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initView();
        initRecyclers();
        initTypeFaces();
    }

    private void initView() {

    }

    private void initRecyclers() {

    }

    private void initTypeFaces() {
        mRecentConvsTitle.setTypeface(EasyFonts.robotoBold(getContext()));
    }


    @Override
    public void onChatRoomClickListener(int position) {
        
    }

    @Override
    public void onChatRoomLongClickListener(int position) {

    }

    /**********************************************
     * EVENTBUS CATCHING FIELDS
     *  + FetchChatRoomsComplete: received chat rooms
     ***********************************************/
    @Subscribe
    public void onEvent(FetchChatRoomsComplete request) {

    }
}
