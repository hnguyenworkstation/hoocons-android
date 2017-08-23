package com.hoocons.hoocons_android.CustomUI;

import android.content.Context;
import android.media.Image;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.hoocons.hoocons_android.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import sj.keyboard.XhsEmoticonsKeyBoard;
import sj.keyboard.adpater.PageSetAdapter;
import sj.keyboard.data.PageSetEntity;
import sj.keyboard.utils.EmoticonsKeyboardUtils;
import sj.keyboard.widget.AutoHeightLayout;
import sj.keyboard.widget.EmoticonsEditText;
import sj.keyboard.widget.EmoticonsFuncView;
import sj.keyboard.widget.EmoticonsIndicatorView;
import sj.keyboard.widget.EmoticonsToolBarView;
import sj.keyboard.widget.FuncLayout;

/**
 * Created by hungnguyen on 8/22/17.
 */

public class ChatWrapperKeyboard extends AutoHeightLayout implements
        EmoticonsFuncView.OnEmoticonsPageViewListener,
        EmoticonsToolBarView.OnToolBarItemClickListener,
        EmoticonsEditText.OnBackKeyClickListener,
        FuncLayout.OnFuncChangeListener, View.OnClickListener{
    @BindView(R.id.camera_btn)
    ImageButton mCameraBtn;
    @BindView(R.id.gallery_btn)
    ImageButton mGalleryBtn;
    @BindView(R.id.contact_btn)
    ImageButton mContactBtn;
    @BindView(R.id.gif_btn)
    ImageButton mGifBtn;
    @BindView(R.id.location_btn)
    ImageButton mLocBtn;
    @BindView(R.id.attach_file_btn)
    ImageButton mAttachBtn;

    @BindView(R.id.emoji_btn)
    ImageButton mEmojiBtn;

    @BindView(R.id.chatroom_send)
    ImageButton mSendBtn;

    @BindView(R.id.chat_input_content)
    EmoticonsEditText mInputEdt;

    @BindView(R.id.ly_kvml)
    FuncLayout mFuncLayout;

    public final int APPS_HEIGHT = 264;

    public static final int FUNC_TYPE_PTT = 1;
    public static final int FUNC_TYPE_PTV = 2;
    public static final int FUNC_TYPE_IMAGE = 3;
    public static final int FUNC_TYPE_CAMERA = 4;
    public static final int FUNC_TYPE_HONGBAO = 5;
    public static final int FUNC_TYPE_EMOTICON = 6;
    public static final int FUNC_TYPE_PLUG = 7;

    protected LayoutInflater mInflater;

    protected EmoticonsFuncView mEmoticonsFuncView;
    protected EmoticonsIndicatorView mEmoticonsIndicatorView;
    protected EmoticonsToolBarView mEmoticonsToolBarView;

    protected boolean mDispatchKeyEventPreImeLock = false;


    public ChatWrapperKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.chat_keyboard_wrapper, this);
        ButterKnife.bind(this, view);
        initView();
        initFuncView();
        initClickListener();
    }

    private void initClickListener() {
        mEmojiBtn.setOnClickListener(this);
    }

    protected void initView() {
        mInputEdt.setOnBackKeyClickListener(this);
    }

    protected void initFuncView() {
        initEmoticonFuncView();
        initEditView();
    }

    protected void initEmoticonFuncView() {
        View keyboardView = inflateFunc();
        mFuncLayout.addFuncView(FUNC_TYPE_EMOTICON, keyboardView);
        mEmoticonsFuncView = ((EmoticonsFuncView) findViewById(com.keyboard.view.R.id.view_epv));
        mEmoticonsIndicatorView = ((EmoticonsIndicatorView) findViewById(com.keyboard.view.R.id.view_eiv));
        mEmoticonsToolBarView = ((EmoticonsToolBarView) findViewById(com.keyboard.view.R.id.view_etv));
        mEmoticonsFuncView.setOnIndicatorListener(this);
        mEmoticonsToolBarView.setOnToolBarItemClickListener(this);
        mFuncLayout.setOnFuncChangeListener(this);
    }

    protected View inflateFunc() {
        return mInflater.inflate(R.layout.common_emoji_layout, null);
    }

    protected void initEditView() {
        mInputEdt.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!mInputEdt.isFocused()) {
                    mInputEdt.setFocusable(true);
                    mInputEdt.setFocusableInTouchMode(true);
                }
                return false;
            }
        });

        mInputEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    mSendBtn.setEnabled(true);
                } else {
                    mSendBtn.setEnabled(false);
                }
            }
        });
    }

    public void setAdapter(PageSetAdapter pageSetAdapter) {
        if (pageSetAdapter != null) {
            ArrayList<PageSetEntity> pageSetEntities = pageSetAdapter.getPageSetEntityList();
            if (pageSetEntities != null) {
                for (PageSetEntity pageSetEntity : pageSetEntities) {
                    mEmoticonsToolBarView.addToolItemView(pageSetEntity);
                }
            }
        }
        mEmoticonsFuncView.setAdapter(pageSetAdapter);
    }

    public void addFuncView(int type, View view) {
        mFuncLayout.addFuncView(type, view);
    }

    public void reset() {
        EmoticonsKeyboardUtils.closeSoftKeyboard(getContext());
        mFuncLayout.hideAllFuncView();
        resetIcon();
    }

    public void resetIcon() {
        mSendBtn.setEnabled(false);
        mEmojiBtn.setSelected(false);
    }

    protected void toggleFuncView(int key) {
        mFuncLayout.toggleFuncView(key, isSoftKeyboardPop(), mInputEdt);
    }


    @Override
    public void onBackKeyClick() {
        if (mFuncLayout.isShown()) {
            mDispatchKeyEventPreImeLock = true;
            reset();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (mDispatchKeyEventPreImeLock) {
                    mDispatchKeyEventPreImeLock = false;
                    return true;
                }
                if (mFuncLayout.isShown()) {
                    reset();
                    return true;
                } else {
                    return super.dispatchKeyEvent(event);
                }
        }
        return super.dispatchKeyEvent(event);
    }

    public EmoticonsEditText getEtChat() {
        return mInputEdt;
    }

    public EmoticonsFuncView getEmoticonsFuncView() {
        return mEmoticonsFuncView;
    }

    public EmoticonsIndicatorView getEmoticonsIndicatorView() {
        return mEmoticonsIndicatorView;
    }

    public EmoticonsToolBarView getEmoticonsToolBarView() {
        return mEmoticonsToolBarView;
    }

    @Override
    public void playTo(int position, PageSetEntity pageSetEntity) {
        mEmoticonsIndicatorView.playTo(position, pageSetEntity);
    }

    @Override
    public void playBy(int oldPosition, int newPosition, PageSetEntity pageSetEntity) {
        mEmoticonsIndicatorView.playBy(oldPosition, newPosition, pageSetEntity);
    }

    @Override
    public void onToolBarItemClick(PageSetEntity pageSetEntity) {
        mEmoticonsFuncView.setCurrentPageSet(pageSetEntity);
    }

    @Override
    public void onSoftKeyboardHeightChanged(int height) {
        mFuncLayout.updateHeight(height);
    }

    @Override
    public void onFuncChange(int key) {
        resetIcon();
        switch (key) {
            case FUNC_TYPE_PTT:
                break;
            case FUNC_TYPE_PTV:
                break;
            case FUNC_TYPE_IMAGE:
                break;
            case FUNC_TYPE_CAMERA:
                break;
            case FUNC_TYPE_HONGBAO:
                break;
            case FUNC_TYPE_EMOTICON:
                mEmojiBtn.setSelected(true);
                break;
            case FUNC_TYPE_PLUG:
                break;
        }
    }

    public ImageButton getBtnSend() {
        return mSendBtn;
    }

    @Override
    public void OnSoftPop(int height) {
        super.OnSoftPop(height);
        mFuncLayout.setVisibility(true);
        onFuncChange(mFuncLayout.DEF_KEY);
    }

    @Override
    public void OnSoftClose() {
        super.OnSoftClose();
        if (mFuncLayout.isOnlyShowSoftKeyboard()) {
            reset();
        } else {
            onFuncChange(mFuncLayout.getCurrentFuncKey());
        }
    }

    public void addOnFuncKeyBoardListener(FuncLayout.OnFuncKeyBoardListener l) {
        mFuncLayout.addOnKeyBoardListener(l);
    }

    @Override
    public void emoticonSetChanged(PageSetEntity pageSetEntity) {
        mEmoticonsToolBarView.setToolBtnSelect(pageSetEntity.getUuid());
    }


    protected void setFuncViewHeight(int height) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mFuncLayout.getLayoutParams();
        params.height = height;
        mFuncLayout.setLayoutParams(params);
        super.OnSoftPop(height);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.emoji_btn:
                toggleFuncView(FUNC_TYPE_EMOTICON);
                setFuncViewHeight(EmoticonsKeyboardUtils.dip2px(getContext(), APPS_HEIGHT));
                break;
            default:
                break;
        }
    }
}
