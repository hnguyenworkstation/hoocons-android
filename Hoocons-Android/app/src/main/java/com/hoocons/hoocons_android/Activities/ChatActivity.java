package com.hoocons.hoocons_android.Activities;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.hoocons.hoocons_android.Adapters.ChatMessagesAdapter;
import com.hoocons.hoocons_android.CustomUI.DividerItemDecoration;
import com.hoocons.hoocons_android.EventBus.SmallEmotionClicked;
import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Helpers.ChatUtils;
import com.hoocons.hoocons_android.Helpers.SystemUtils;
import com.hoocons.hoocons_android.Interface.InfiniteScrollListener;
import com.hoocons.hoocons_android.Interface.OnChatMessageClickListener;
import com.hoocons.hoocons_android.Interface.OnStickerPagerFragmentInteractionListener;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.Models.ChatMessage;
import com.hoocons.hoocons_android.Models.Emotion;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.SQLite.EmotionsDB;
import com.hoocons.hoocons_android.ViewFragments.CommonEmojiFragment;
import com.hoocons.hoocons_android.ViewFragments.StickerCombinationFragment;

import org.aisen.android.common.utils.BitmapUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends BaseActivity implements View.OnClickListener,
        OnStickerPagerFragmentInteractionListener, OnChatMessageClickListener {
    @BindView(R.id.user_state)
    TextView mUserState;
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.message_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.chatroom_send)
    ImageButton mSendButton;
    @BindView(R.id.chatroom_emoji)
    ImageButton mEmojiButton;
    @BindView(R.id.emo_container)
    View mEmoContainer;
    @BindView(R.id.chat_input_content)
    EditText mTextInput;
    @BindView(R.id.root_layout)
    ViewGroup mRootLayout;
    @BindView(R.id.root_container)
    View mRootContainer;

    private StickerCombinationFragment stickerCombinationFragment;
    private CommonEmojiFragment commonEmojiFragment;
    private int emotionHeight;
    private final LayoutTransition transitioner = new LayoutTransition();
    private String chatRoomId;
    private List<ChatMessage> chatMessageList;
    private ChatMessagesAdapter messagesAdapter;
    private boolean isLoading = false;
    private DatabaseReference messageListDataRef;
    private int lastShownNamePos;

    private final TextWatcher editContentWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mTextInput.getText().toString().length() > 0) {
                mSendButton.setEnabled(true);
            } else {
                mSendButton.setEnabled(false);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private final View.OnKeyListener keyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL)
            {
                Log.e(TAG, "onKey: DELETE");
                shouldRemoveLastIcon();
                return true;
            } else if (keyCode==KeyEvent.KEYCODE_ENTER)
            {
                // Just ignore the [Enter] key
                return true;
            }

            return  false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        chatRoomId = intent.getStringExtra("chatroom_uid");

        // Setting up
        lastShownNamePos = -1;
        messageListDataRef = BaseApplication.getInstance().getDatabase()
                .child("messages").child(chatRoomId);
        chatMessageList = new ArrayList<>();

        stickerCombinationFragment = new StickerCombinationFragment();
        commonEmojiFragment = CommonEmojiFragment.newInstance("1", "1");

        getSupportFragmentManager().beginTransaction().add(R.id.emo_container,
                commonEmojiFragment, "EmotionFragment").commit();


        mSendButton.setEnabled(false);

        initRecyclerView();
        initDataChangeListener();
        initEmotionLayout();
        initOnListener();
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

    private void initEmotionLayout() {
        if (stickerCombinationFragment == null) {
            stickerCombinationFragment = StickerCombinationFragment.newInstance();

            getSupportFragmentManager().beginTransaction().add(R.id.emo_container,
                    stickerCombinationFragment, "EmotionFragment").commit();
        }

        ObjectAnimator animIn = ObjectAnimator.ofFloat(null, "translationY",
                SystemUtils.getScreenHeight(this), emotionHeight).
                setDuration(transitioner.getDuration(LayoutTransition.APPEARING));
        transitioner.setAnimator(LayoutTransition.APPEARING, animIn);

        ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "translationY", emotionHeight,
                SystemUtils.getScreenHeight(this)).
                setDuration(transitioner.getDuration(LayoutTransition.DISAPPEARING));
        transitioner.setAnimator(LayoutTransition.DISAPPEARING, animOut);
        mRootLayout.setLayoutTransition(transitioner);

        mTextInput.setFilters(new InputFilter[] { emotionFilter });
        mTextInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideEmotionView(true);
            }
        });
    }

    private void initOnListener() {
        mSendButton.setOnClickListener(this);
        mEmojiButton.setOnClickListener(this);

        mTextInput.addTextChangedListener(editContentWatcher);
        mTextInput.setOnKeyListener(keyListener);
    }

    private void switchEmotionSoftInput() {
        if (mEmoContainer.isShown()) {
            hideEmotionView(true);
        } else {
            showEmotionView(SystemUtils.isKeyBoardShow(this));

        }
    }

    private void showEmotionView(boolean showAnimation) {
        mEmojiButton.setSelected(true);

        if (showAnimation) {
            transitioner.setDuration(200);
        } else {
            transitioner.setDuration(0);
        }

        int statusBarHeight = SystemUtils.getStatusBarHeight(ChatActivity.this);
        emotionHeight = SystemUtils.getKeyboardHeight(ChatActivity.this);

        SystemUtils.hideSoftInput(this, mTextInput);
        mEmoContainer.getLayoutParams().height = emotionHeight;
        mEmoContainer.setVisibility(View.VISIBLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        int lockHeight = SystemUtils.getAppContentHeight(this);
        lockContainerHeight(lockHeight);
    }

    private void lockContainerHeight(int paramInt) {
        LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)
                mRootContainer.getLayoutParams();
        localLayoutParams.height = paramInt;
        localLayoutParams.weight = 0.0F;
    }

    private void hideEmotionView(boolean showKeyBoard) {
        mEmojiButton.setSelected(false);

        if (mEmoContainer.isShown()) {
            if (showKeyBoard) {
                LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)
                        mRootContainer.getLayoutParams();

                localLayoutParams.height = mEmoContainer.getTop();
                localLayoutParams.weight = 0.0F;

                mEmoContainer.setVisibility(View.GONE);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                SystemUtils.showKeyBoard(this, mTextInput);
                mTextInput.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        unlockContainerHeightDelayed();
                    }
                }, 200L);
            } else {
                mEmoContainer.setVisibility(View.GONE);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                unlockContainerHeightDelayed();
            }
        }
    }

    public void unlockContainerHeightDelayed() {
        ((LinearLayout.LayoutParams) mRootContainer.getLayoutParams()).weight = 1.0F;
    }

    private void sendPlainTextMessage() {
        ChatMessage message = new ChatMessage(
                SharedPreferencesManager.getDefault().getUserId(),
                AppConstant.MESSAGE_TYPE_TEXT,
                AppUtils.getCurrentUTCTime(),
                mTextInput.getText().toString(),
                null, false, null, null, null, false);
        ChatUtils.pushMessage(chatRoomId, message);

        // Add temporary message to the list
        message.setPosted(false);
        chatMessageList.add(0, message);
        mRecyclerView.smoothScrollToPosition(0);

        mTextInput.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chatroom_emoji:
                switchEmotionSoftInput();
                break;
            case R.id.chatroom_send:
                if (mTextInput.getText().length() > 0) {
                    sendPlainTextMessage();
                }
                break;
            default:
                break;
        }
    }

    private InputFilter emotionFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if ("".equals(source)) {
                return null;
            }

            byte[] emotionBytes = EmotionsDB.getEmotion(source.toString());
            if (emotionBytes != null) {
                byte[] data = emotionBytes;
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                int size = BaseActivity.getRunningActivity().getResources()
                        .getDimensionPixelSize(R.dimen.publish_emotion_size);
                bitmap = BitmapUtil.zoomBitmap(bitmap, size);
                SpannableString emotionSpanned = new SpannableString(source.toString());
                ImageSpan span = new ImageSpan(ChatActivity.this, bitmap);
                emotionSpanned.setSpan(span, 0, source.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                return emotionSpanned;
            } else {
                return source;
            }
        }
    };

    private void insertToTextInput(Emotion emotion) {
        Editable editAble = mTextInput.getEditableText();
        int start = mTextInput.getSelectionStart();
        if ("[最右]".equals(emotion.getKey()))
            editAble.insert(start, "→_→");
        else
            editAble.insert(start, emotion.getKey());

        // now set click listener when add new fragment -> push event bus
    }

    private void shouldRemoveLastIcon() {
        String text = mTextInput.getText().toString();
        int index = 0;
        if (text.charAt(text.length() - 1) == ']') {
            for(int i = text.length() - 1; i > 0 ; i--) {
                if (text.charAt(i) == '[') {
                    index = i;
                    break;
                }
            }
        } else {
            return;
        }

        String bracket = text.substring(index, text.length() - 1);
        byte[] emotionBytes = EmotionsDB.getEmotion(bracket);

        if (emotionBytes != null) {
            if (index > 0) {
                text = text.substring(0, index - 1);
            } else {
                text = "";
            }
            mTextInput.setText(text);
        }
    }

    @Override
    public void onBackPressed() {
        if (mEmoContainer.isShown()) {
            hideEmotionView(false);
        }

        super.onBackPressed();
    }

    @Subscribe
    public void onEvent(SmallEmotionClicked request) {
        insertToTextInput(request.getEmotion());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
}
