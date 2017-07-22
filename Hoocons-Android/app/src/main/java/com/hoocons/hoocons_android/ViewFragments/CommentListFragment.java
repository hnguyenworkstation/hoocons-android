package com.hoocons.hoocons_android.ViewFragments;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.hoocons.hoocons_android.Activities.ChatActivity;
import com.hoocons.hoocons_android.EventBus.AllowSlideDown;
import com.hoocons.hoocons_android.Helpers.SystemUtils;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.Models.Emotion;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.SQLite.EmotionsDB;

import org.aisen.android.common.utils.BitmapUtil;
import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentListFragment extends Fragment implements View.OnClickListener,
        EmotionFragment.OnEmotionSelectedListener, ObservableScrollViewCallbacks {
    @BindView(R.id.recycler)
    ObservableRecyclerView mRecycler;
    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;
    @BindView(R.id.comment_emoji)
    ImageButton mCommentEmojiBtn;
    @BindView(R.id.comment_input_content)
    EditText mCommentTextInput;
    @BindView(R.id.emo_container)
    RelativeLayout mEmoContainer;
    @BindView(R.id.root_layout)
    ViewGroup mRootLayout;
    @BindView(R.id.root_container)
    View mRootContainer;

    private EmotionFragment emotionFragment;
    private int emotionHeight;
    private final LayoutTransition transitioner = new LayoutTransition();

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
        ButterKnife.bind(this, view);

        initEmojiLayout();
        initClickListener();
    }

    private void initClickListener() {
        mCommentEmojiBtn.setOnClickListener(this);
    }

    private void initEmojiLayout() {
        emotionFragment = new EmotionFragment();
        getFragmentManager().beginTransaction().add(R.id.emo_container,
                emotionFragment, "EmotionFragment").commit();

        if (emotionFragment == null) {
            emotionFragment = EmotionFragment.newInstance();

            getFragmentManager().beginTransaction().add(R.id.emo_container,
                    emotionFragment, "EmotionFragment").commit();
        }

        emotionFragment.setOnEmotionListener(this);

        ObjectAnimator animIn = ObjectAnimator.ofFloat(null, "translationY",
                SystemUtils.getScreenHeight(getActivity()), emotionHeight).
                setDuration(transitioner.getDuration(LayoutTransition.APPEARING));
        transitioner.setAnimator(LayoutTransition.APPEARING, animIn);

        ObjectAnimator animOut = ObjectAnimator.ofFloat(null, "translationY", emotionHeight,
                SystemUtils.getScreenHeight(getActivity())).
                setDuration(transitioner.getDuration(LayoutTransition.DISAPPEARING));
        transitioner.setAnimator(LayoutTransition.DISAPPEARING, animOut);
        mRootLayout.setLayoutTransition(transitioner);

        mCommentTextInput.setFilters(new InputFilter[] {
                emotionFilter
        });

        mCommentTextInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideEmotionView(true);
            }
        });
    }

    private void lockContainerHeight(int paramInt) {
        LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)
                mRootContainer.getLayoutParams();
        localLayoutParams.height = paramInt;
        localLayoutParams.weight = 0.0F;
    }

    private void hideEmotionView(boolean showKeyBoard) {
        mCommentEmojiBtn.setSelected(false);

        if (mEmoContainer.isShown()) {
            if (showKeyBoard) {
                LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)
                        mRootContainer.getLayoutParams();

                localLayoutParams.height = mEmoContainer.getTop();
                localLayoutParams.weight = 0.0F;

                mEmoContainer.setVisibility(View.GONE);
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

                SystemUtils.showKeyBoard(getActivity(), mCommentTextInput);
                mCommentTextInput.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        unlockContainerHeightDelayed();
                    }
                }, 200L);
            } else {
                mEmoContainer.setVisibility(View.GONE);
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                unlockContainerHeightDelayed();
            }
        }
    }

    public void unlockContainerHeightDelayed() {
        ((LinearLayout.LayoutParams) mRootContainer.getLayoutParams()).weight = 1.0F;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comment_emoji:
                switchEmotionSoftInput();
                break;
            default:
                break;
        }
    }

    private void switchEmotionSoftInput() {
        if (mEmoContainer.isShown()) {
            hideEmotionView(true);
        } else {
            showEmotionView(SystemUtils.isKeyBoardShow(getActivity()));
        }
    }

    private void showEmotionView(boolean showAnimation) {
        mCommentEmojiBtn.setSelected(true);

        if (showAnimation) {
            transitioner.setDuration(200);
        } else {
            transitioner.setDuration(0);
        }

        int statusBarHeight = SystemUtils.getStatusBarHeight(getActivity());
        emotionHeight = SystemUtils.getKeyboardHeight(getActivity());

        SystemUtils.hideSoftInput(getActivity(), mCommentTextInput);
        mEmoContainer.getLayoutParams().height = emotionHeight;
        mEmoContainer.setVisibility(View.VISIBLE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        int lockHeight = SystemUtils.getAppContentHeight(getActivity());
        lockContainerHeight(lockHeight);
    }

    @Override
    public void onEmotionSelected(Emotion emotion) {
        Editable editAble = mCommentTextInput.getEditableText();
        int start = mCommentTextInput.getSelectionStart();
        editAble.insert(start, emotion.getKey());
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
                ImageSpan span = new ImageSpan(getActivity(), bitmap);
                emotionSpanned.setSpan(span, 0, source.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                return emotionSpanned;
            } else {
                return source;
            }
        }
    };

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (scrollY == 0) {
            EventBus.getDefault().post(new AllowSlideDown());
        }
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}
