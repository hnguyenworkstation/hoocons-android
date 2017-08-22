package com.hoocons.hoocons_android.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.hoocons.hoocons_android.Adapters.ChatMessagesAdapter;
import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Helpers.ChatUtils;
import com.hoocons.hoocons_android.Interface.InfiniteScrollListener;
import com.hoocons.hoocons_android.Interface.OnChatMessageClickListener;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.Models.ChatMessage;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewFragments.StickerCombinationFragment;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sj.keyboard.XhsEmoticonsKeyBoard;
import sj.keyboard.interfaces.EmoticonClickListener;
import sj.keyboard.widget.AutoHeightLayout;
import sj.keyboard.widget.FuncLayout;

public class ChatActivity extends BaseActivity implements View.OnClickListener,
        OnChatMessageClickListener, FuncLayout.OnFuncKeyBoardListener,
        AutoHeightLayout.OnMaxParentHeightChangeListener {
    @BindView(R.id.user_state)
    TextView mUserState;
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.message_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.ek_bar)
    XhsEmoticonsKeyBoard emotionKeyboard;

    private String chatRoomId;
    private List<ChatMessage> chatMessageList;
    private ChatMessagesAdapter messagesAdapter;
    private boolean isLoading = false;
    private DatabaseReference messageListDataRef;
    private int lastShownNamePos;

    private final TextWatcher editContentWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            if (mTextInput.getText().toString().length() > 0) {
//                mSendButton.setEnabled(true);
//            } else {
//                mSendButton.setEnabled(false);
//            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private EmoticonClickListener emoticonClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        chatRoomId = intent.getStringExtra("chatroom_uid");

        // Setting up
        lastShownNamePos = -1;
        messageListDataRef = BaseApplication.getInstance().getDatabase()
                .child("messages").child(chatRoomId);
        chatMessageList = new ArrayList<>();

        // mSendButton.setEnabled(false);

        initRecyclerView();
        initDataChangeListener();
        initEmoticonsKeyBoardBar();
        initEmoticonsListener();
        initOnListener();
    }

    private void initEmoticonsKeyBoardBar() {
    }

    private void initEmoticonsListener() {
    }

    private void initDataChangeListener() {
        messageListDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatMessageList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatMessage message = snapshot.getValue(ChatMessage.class);
                    assert message != null;
                    message.setId(snapshot.getKey());
                    message.setPosted(true);
                    chatMessageList.add(0, message);
                }

                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        messageListDataRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                handleOnChildAdded(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                handleOnChildUpdated(dataSnapshot.getKey(), dataSnapshot.getValue(ChatMessage.class));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void handleOnChildAdded(String key) {
        for (int i = 0; i < chatMessageList.size(); i++) {
            ChatMessage m = chatMessageList.get(i);

            if (m.getId().equals(key)) {
                m.setPosted(true);
                messagesAdapter.notifyItemChanged(i);
                return;
            }
        }
    }

    private void handleOnChildUpdated(String key, ChatMessage newMessage) {
        for (int i = 0; i < chatMessageList.size(); i++) {
            ChatMessage m = chatMessageList.get(i);

            if (m.getId().equals(key)) {
                m = newMessage;
                messagesAdapter.notifyItemChanged(i);
                return;
            }
        }
    }

    private void initRecyclerView() {
        messagesAdapter = new ChatMessagesAdapter(this, chatMessageList, this);

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mLayoutManager.setReverseLayout(true);

        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(messagesAdapter);
        mRecyclerView.addOnScrollListener(new InfiniteScrollListener(mLayoutManager) {

            @Override
            protected void loadMoreItems() {
            }

            @Override
            public int getTotalItems() {
                return messagesAdapter.getItemCount();
            }

            @Override
            public boolean isLastItem() {
//                return ((LinearLayoutManager) mLayoutManager).findLastCompletelyVisibleItemPosition()
//                        == (mEventsAdapter.getItemCount() - 1);
                return false;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    private void initOnListener() {
//        mSendButton.setOnClickListener(this);
//        mEmojiButton.setOnClickListener(this);
//
//        mTextInput.addTextChangedListener(editContentWatcher);
    }

//    private void sendPlainTextMessage() {
//        ChatMessage message = new ChatMessage(
//                SharedPreferencesManager.getDefault().getUserId(),
//                AppConstant.MESSAGE_TYPE_TEXT,
//                AppUtils.getCurrentUTCTime(),
//                mTextInput.getText().toString(),
//                null, false, null, null, null, false);
//        ChatUtils.pushMessage(chatRoomId, message);
//
//        // Add temporary message to the list
//        message.setPosted(false);
//        chatMessageList.add(0, message);
//        mRecyclerView.smoothScrollToPosition(0);
//
//        mTextInput.setText("");
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chatroom_emoji:
                break;
            case R.id.chatroom_send:
//                if (mTextInput.getText().length() > 0) {
//                    sendPlainTextMessage();
//                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onMessageClickListener(int position) {
        if (lastShownNamePos != -1) {
            ChatMessage oldMessage = chatMessageList.get(lastShownNamePos);
            boolean isShown = oldMessage.isShownName();
            oldMessage.setShownName(!isShown);
            messagesAdapter.notifyItemChanged(lastShownNamePos);
        }

        ChatMessage message = chatMessageList.get(position);
        boolean isShown = message.isShownName();
        message.setShownName(!isShown);
        lastShownNamePos = position;
        messagesAdapter.notifyItemChanged(position);
    }

    @Override
    public void onMessageLocationClickListener(int position) {

    }

    @Override
    public void onMessageImageClickListener(int position) {

    }

    @Override
    public void onMessageContactClickListener(int position) {

    }

    @Override
    public void onMaxParentHeightChange(int height) {

    }

    @Override
    public void OnFuncPop(int height) {

    }

    @Override
    public void OnFuncClose() {

    }
}
