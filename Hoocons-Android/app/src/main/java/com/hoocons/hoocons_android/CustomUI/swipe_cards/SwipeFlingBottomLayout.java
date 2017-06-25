package com.hoocons.hoocons_android.CustomUI.swipe_cards;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hungnguyen on 6/25/17.
 */

public class SwipeFlingBottomLayout extends LinearLayout {
    public final static int ANIMATION_DURATION = 50;

    @BindView(R.id.comeback)
    BottomSwitchView mComeBackView;

    @BindView(R.id.superlike)
    BottomSwitchView mSuperLikeView;

    @BindView(R.id.like)
    BottomSwitchView mLikeView;

    @BindView(R.id.unlike)
    BottomSwitchView mUnlikeView;

    private OnBottomItemClickListener mListener;

    public SwipeFlingBottomLayout(Context context) {
        super(context);
    }

    public SwipeFlingBottomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        //mComeBackView.setEnabled(false);
    }

    public void show(int delay) {
        show(mLikeView, delay);
        show(mUnlikeView, delay);
        show(mComeBackView, delay);
        show(mSuperLikeView, delay);
    }

    public void hide() {
        int delay = 0;
        hide(mLikeView, delay);
        hide(mUnlikeView, delay);
        hide(mComeBackView, delay);
        hide(mSuperLikeView, delay);
    }

    private void show(View view, int delay) {
        view.animate()
                .setDuration(ANIMATION_DURATION)
                .setStartDelay(delay)
                .alpha(1);
    }

    private void hide(View view, int delay) {
        view.animate()
                .setDuration(ANIMATION_DURATION)
                .setStartDelay(delay)
                .alpha(0);
    }

    @OnClick(R.id.comeback)
    public void onComeBackClick() {
        if (mListener != null) {
            mListener.onComeBackClick();
        }
    }

    @OnClick(R.id.superlike)
    public void onSuperLikeClick() {
        if (mListener != null) {
            mListener.onSuperLikeClick();
        }
    }

    @OnClick(R.id.like)
    public void onLikeClick() {
        if (mListener != null) {
            mListener.onLikeClick();
        }
    }

    @OnClick(R.id.unlike)
    public void onUnLikeClick() {
        if (mListener != null) {
            mListener.onUnLikeClick();
        }
    }

    public BottomSwitchView getLikeView() {
        return mLikeView;
    }

    public BottomSwitchView getUnLikeView() {
        return mUnlikeView;
    }

    public BottomSwitchView getSuperLikeView() {
        return mSuperLikeView;
    }

    public void setEnableComeback(boolean isEnable) {
        mComeBackView.setEnabled(isEnable);
    }

    public void setEnableSuperLike(boolean isEnable) {
        mSuperLikeView.setEnabled(isEnable);
    }

    public boolean isEnableComeback() {
        return mComeBackView.isEnabled();
    }

    public boolean isEnableSuperLike() {
        return mSuperLikeView.isEnabled();
    }

    public void setComebackClickable(boolean clickable) {
        mComeBackView.setClickable(clickable);
    }

    public void setOnBottomItemClickListener(OnBottomItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnBottomItemClickListener {
        public void onComeBackClick();

        public void onSuperLikeClick();

        public void onLikeClick();

        public void onUnLikeClick();
    }
}
