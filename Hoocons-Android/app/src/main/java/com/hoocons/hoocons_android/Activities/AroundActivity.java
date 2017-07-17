package com.hoocons.hoocons_android.Activities;

import android.app.FragmentManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.hoocons.hoocons_android.CustomUI.RippleAnimationLayout;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AroundActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.search_around_view)
    RelativeLayout mSearchAroundView;
    @BindView(R.id.screen_null_around_fragment)
    RelativeLayout mNullScreen;
    @BindView(R.id.search_ripple_anim)
    RippleAnimationLayout mRippleAnimLayout;
    @BindView(R.id.action_back)
    ImageButton mActionBack;
    @BindView(R.id.action_more)
    ImageButton mActionMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_around);
        ButterKnife.bind(this);

        runRippleAnimation();
        addListeners();
    }

    private void addListeners() {
        mActionBack.setOnClickListener(this);
        mActionMore.setOnClickListener(this);
    }

    private void runRippleAnimation() {
        startAnimation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopAnimation();
            }
        }, 8000);
    }

    private void startAnimation() {
        if (!mRippleAnimLayout.isRippleAnimationRunning()) {
            mRippleAnimLayout.startRippleAnimation();
        }
    }

    private void stopAnimation() {
        if (mRippleAnimLayout.isRippleAnimationRunning()) {
            mRippleAnimLayout.stopRippleAnimation();
        }

        mNullScreen.setVisibility(View.VISIBLE);
        mSearchAroundView.setVisibility(View.GONE);
        mRippleAnimLayout.destroyDrawingCache();
        mSearchAroundView.destroyDrawingCache();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("AroundActivity", "popping backstack");
            fm.popBackStack();
            overridePendingTransition(R.anim.fix_anim, R.anim.slide_down_out);
        } else {
            Log.i("AroundActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_back:
                onBackPressed();
                break;
            case R.id.action_more:
                break;
            default:
                break;
        }
    }
}
