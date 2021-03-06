package com.hoocons.hoocons_android.ViewFragments;


import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hoocons.hoocons_android.Activities.ChatActivity;
import com.hoocons.hoocons_android.Adapters.ConversationAdapter;
import com.hoocons.hoocons_android.CustomUI.DividerItemDecoration;
import com.hoocons.hoocons_android.EventBus.DuplicateObjectError;
import com.hoocons.hoocons_android.EventBus.FetchChatRoomsComplete;
import com.hoocons.hoocons_android.EventBus.ServerErrorRequest;
import com.hoocons.hoocons_android.EventBus.TaskCompleteRequest;
import com.hoocons.hoocons_android.EventBus.UserNotFoundError;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Helpers.ChatUtils;
import com.hoocons.hoocons_android.Interface.InfiniteScrollListener;
import com.hoocons.hoocons_android.Interface.OnChatRoomClickListener;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.Models.ChatMessage;
import com.hoocons.hoocons_android.Models.ChatRoom;
import com.hoocons.hoocons_android.Networking.Responses.ChatRoomResponse;
import com.hoocons.hoocons_android.Networking.Responses.SemiUserInfoResponse;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.Tasks.Jobs.FetchChatRoomsJob;
import com.hoocons.hoocons_android.Tasks.Jobs.FetchFeaturedActivityJob;
import com.vstechlab.easyfonts.EasyFonts;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessagingFragment extends Fragment implements OnChatRoomClickListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.conversation_list_layout)
    LinearLayout mConversationList;
    @BindView(R.id.communicate_older_textview)
    TextView mRecentConvsTitle;
    @BindView(R.id.convs_recycler)
    RecyclerView mConvRecycler;
    @BindView(R.id.swipe_ref)
    SwipeRefreshLayout mSwipeRefreshLayout;

    /* Empty View*/
    @BindView(R.id.empty_add_chat_btn)
    LinearLayout mEmptyAddChatBtn;
    @BindView(R.id.empty_convs_view)
    RelativeLayout mEmptyLayout;
    @BindView(R.id.title)
    TextView mEmptyTitle;
    @BindView(R.id.message)
    TextView mEmptyMessage;
    @BindView(R.id.empty_btn_text)
    TextView mEmptyButtonText;

    private final String TAG = MessagingFragment.class.getSimpleName();
    private List<ChatRoom> chatRooms;
    private List<ChatRoomResponse> chatRoomResponses;
    private DatabaseReference databaseReference;
    private ConversationAdapter mAdapter;
    private boolean isLoading = false;
    private boolean isFirstTime = true;

    public MessagingFragment() {

    }

    public static MessagingFragment newInstance(String param1, String param2) {
        MessagingFragment fragment = new MessagingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
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

        mSwipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(true);
                        BaseApplication.getInstance().getJobManager()
                                .addJobInBackground(new FetchChatRoomsJob());
                        initView();
                    }
                }
        );
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
                                    Log.e(TAG, "fetchFireBaseChatRooms: " + String.valueOf(chatRooms.size()));
                                    mAdapter.notifyDataSetChanged();
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
        mRecentConvsTitle.setTypeface(EasyFonts.robotoRegular(getContext()));
    }

    private void fetchFireBaseChatRooms(List<ChatRoomResponse> responseList) {
        chatRoomResponses = responseList;
        final List<String> uids = new ArrayList<>();

        for (final ChatRoomResponse response: responseList) {
            uids.add(response.getUid());
        }

        databaseReference.child("chatrooms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatRooms.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (uids.indexOf(snapshot.getKey()) > -1) {
                        final ChatRoom chatRoom = snapshot.getValue(ChatRoom.class);
                        assert chatRoom != null;
                        chatRoom.setUid(snapshot.getKey());

                        Query lastMessageQuery = databaseReference.child("messages").child(snapshot.getKey())
                                .orderByKey().limitToLast(1);
                        lastMessageQuery.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ChatMessage lastMessage = dataSnapshot.getValue(ChatMessage.class);
                                chatRoom.setLastMessage(lastMessage);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        chatRooms.add(chatRoom);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initEmptyScreen() {
        mConversationList.setVisibility(View.GONE);
        mEmptyLayout.setVisibility(View.VISIBLE);

        mEmptyTitle.setTypeface(EasyFonts.robotoBold(getContext()));
        mEmptyMessage.setTypeface(EasyFonts.robotoRegular(getContext()));
        mEmptyButtonText.setTypeface(EasyFonts.robotoBold(getContext()));

        mEmptyAddChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] users = {SharedPreferencesManager.getDefault().getUserId(), 2};

                if (!roomAlreadyExists(users)) {
                    ChatUtils.createNewChatRoomWithUser(users, "Hung Nguyen");
                } else {
                    Toast.makeText(getContext(), "room exists", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean roomAlreadyExists(int[] users) {
        if (chatRoomResponses == null || chatRoomResponses.size() == 0) {
            return false;
        }

        for (ChatRoomResponse response : chatRoomResponses) {
            if (response.getUsers().size() != users.length) {
                break;
            } else {
                boolean found = true;
                List<Integer> listUserIds = new ArrayList<>();
                for (SemiUserInfoResponse user: response.getUsers()) {
                    listUserIds.add(user.getUser());
                }

                for (int id: users) {
                    if (listUserIds.indexOf(id) == -1){
                        found = false;
                        break;
                    }
                }

                if (found) {
                    return true;
                }
            }
        }

        return false;
    }


    @Override
    public void onChatRoomClickListener(int position) {
        Intent chatRoomIntent = new Intent(getActivity(), ChatActivity.class);
        chatRoomIntent.putExtra("chatroom_uid", chatRooms.get(position).getUid());
        startActivity(chatRoomIntent);
    }

    @Override
    public void onChatRoomLongClickListener(int position) {

    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        BaseApplication.getInstance().getJobManager()
                .addJobInBackground(new FetchChatRoomsJob());
        initView();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    /**********************************************
     * EVENTBUS CATCHING FIELDS
     *  + FetchChatRoomsComplete: received chat rooms
     ***********************************************/
    @Subscribe
    public void onEvent(FetchChatRoomsComplete request) {
        if (request.getRoomResponseList().size() > 0) {
            fetchFireBaseChatRooms(request.getRoomResponseList());
        } else {
            initEmptyScreen();
        }

        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Subscribe
    public void onEvent(TaskCompleteRequest request) {
        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onEvent(UserNotFoundError request) {
        Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onEvent(DuplicateObjectError request) {
        Toast.makeText(getContext(), "duplicate", Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onEvent(ServerErrorRequest error) {
        mSwipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getContext(), "server error", Toast.LENGTH_SHORT).show();
    }
}
