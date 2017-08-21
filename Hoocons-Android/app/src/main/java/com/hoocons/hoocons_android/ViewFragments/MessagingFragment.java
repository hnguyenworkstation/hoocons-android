package com.hoocons.hoocons_android.ViewFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.hoocons.hoocons_android.Adapters.ConversationAdapter;
import com.hoocons.hoocons_android.CustomUI.DividerItemDecoration;
import com.hoocons.hoocons_android.EventBus.FetchChatRoomsComplete;
import com.hoocons.hoocons_android.Interface.InfiniteScrollListener;
import com.hoocons.hoocons_android.Interface.OnChatRoomClickListener;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Models.ChatRoom;
import com.hoocons.hoocons_android.Networking.Responses.ChatRoomResponse;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.Tasks.Jobs.FetchChatRoomsJob;
import com.hoocons.hoocons_android.Tasks.Jobs.FetchFeaturedActivityJob;
import com.vstechlab.easyfonts.EasyFonts;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessagingFragment extends Fragment implements OnChatRoomClickListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.communicate_older_textview)
    TextView mRecentConvsTitle;
    @BindView(R.id.convs_recycler)
    RecyclerView mConvRecycler;
    @BindView(R.id.swipe_ref)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private final String TAG = MessagingFragment.class.getSimpleName();
    private List<ChatRoom> chatRooms;
    private List<ChatRoomResponse> chatRoomResponses;
    private DatabaseReference databaseReference;
    private ConversationAdapter mAdapter;
    private boolean isLoading = false;

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
        chatRooms = new ArrayList<>();
        databaseReference = BaseApplication.getInstance().getDatabase();
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

        mSwipeRefreshLayout.setOnRefreshListener(this);

        // Run first init when created
        mSwipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        BaseApplication.getInstance().getJobManager()
                                .addJobInBackground(new FetchChatRoomsJob());
                        initView();
                    }
                }
        );
    }

    private void initView() {
        initRecyclers();
        initTypeFaces();
    }

    private void initRecyclers() {
        mAdapter = new ConversationAdapter(getContext(), chatRooms, this);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        ((SimpleItemAnimator) mConvRecycler.getItemAnimator()).setSupportsChangeAnimations(false);

        mConvRecycler.setFocusable(false);
        mConvRecycler.setLayoutManager(mLayoutManager);
        mConvRecycler.setHasFixedSize(false);
        mConvRecycler.setAdapter(mAdapter);
        mConvRecycler.addOnScrollListener(new InfiniteScrollListener((LinearLayoutManager) mLayoutManager) {
            @Override
            protected void loadMoreItems() {
                if (!isLoading) {

                }
            }

            @Override
            public int getTotalItems() {
                return mAdapter.getItemCount();
            }

            @Override
            public boolean isLastItem() {
                return ((LinearLayoutManager) mLayoutManager).findLastCompletelyVisibleItemPosition()
                        == (mAdapter.getItemCount() - 1);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    private void initTypeFaces() {
        mRecentConvsTitle.setTypeface(EasyFonts.robotoBold(getContext()));
    }

    private void fetchFireBaseChatRooms(List<ChatRoomResponse> responseList) {
        chatRoomResponses = responseList;

        for (final ChatRoomResponse response: responseList) {
            databaseReference.child("chatrooms").child(response.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                ChatRoom chatRoom = dataSnapshot.getValue(ChatRoom.class);

                                assert chatRoom != null;
                                chatRoom.setUid(response.getUid());

                                chatRooms.add(chatRoom);
                            } catch (Exception e) {
                                Log.e(TAG, "onDataChange: " + e.toString());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

        mAdapter.notifyDataSetChanged();
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
        fetchFireBaseChatRooms(request.getRoomResponseList());
    }

    @Override
    public void onRefresh() {

    }
}
