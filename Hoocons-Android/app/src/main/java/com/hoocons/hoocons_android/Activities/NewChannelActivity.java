package com.hoocons.hoocons_android.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hoocons.hoocons_android.EventBus.ChannelNameCollected;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewFragments.GetChannelAboutFragment;
import com.hoocons.hoocons_android.ViewFragments.GetChannelNameFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewChannelActivity extends BaseActivity {
    @BindView(R.id.create_channel_container)
    FrameLayout mFrameContainer;
    @BindView(R.id.action_back)
    ImageButton mActionBack;
    @BindView(R.id.act_title)
    TextView mTitle;
    @BindView(R.id.action_skip)
    Button mActionSkip;
    @BindView(R.id.wallpaper)
    ImageView mWallpaper;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    private FragmentManager mFragManager;
    private FragmentTransaction mFragTransition;
    private GetChannelNameFragment getChannelNameFragment;
    private GetChannelAboutFragment getChannelAboutFragment;
    private final String TAG = NewChannelActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_new_channel);
        ButterKnife.bind(this);

        getChannelNameFragment = new GetChannelNameFragment();
        getChannelAboutFragment = new GetChannelAboutFragment();

        initGeneralView();
        initGetNameView();
    }

    private void initGeneralView() {
        mFragManager = getSupportFragmentManager();
        mFragTransition = mFragManager.beginTransaction();
        AppUtils.loadCropImageWithProgressBar(this, "http://www.stanleychowillustration.com/uploads/images/MANCHESTER_LANDSCAPE_30x12.jpg", mWallpaper, mProgressBar);

        mActionBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initGetNameView() {
        mFragTransition.replace(R.id.create_channel_container, getChannelNameFragment, "get_channel_name");
        mFragTransition.commit();

        mActionSkip.setVisibility(View.GONE);
        mActionBack.setImageResource(R.drawable.ic_close_colored);
    }

    private void initGetChannelAboutView() {
        mFragTransition = mFragManager.beginTransaction();
        mFragTransition.setCustomAnimations(R.anim.fade_out_to_left, R.anim.fade_in_from_right);
        mFragTransition.replace(R.id.create_channel_container, getChannelAboutFragment, "get_channel_id");
        mFragTransition.commit();

        mActionSkip.setVisibility(View.GONE);
        mActionBack.setImageResource(R.drawable.ic_arrow_back_colored);
    }

    @Override
    public void onBackPressed(){
        if (mFragManager.findFragmentByTag("get_channel_id") != null) {
            mFragTransition = mFragManager.beginTransaction();
            mFragTransition.setCustomAnimations(R.anim.fade_out_to_right, R.anim.fade_in_from_left);
            mFragTransition.replace(R.id.create_channel_container, getChannelNameFragment, "get_channel_name");
            mFragTransition.commit();
        } else if (mFragManager.findFragmentByTag("get_channel_topic") != null) {
            mFragTransition = mFragManager.beginTransaction();
            mFragTransition.setCustomAnimations(R.anim.fade_out_to_right, R.anim.fade_in_from_left);
            mFragTransition.replace(R.id.create_channel_container, null, "get_channel_id");
            mFragTransition.commit();
        } else if (mFragManager.getBackStackEntryCount() > 0) {
            Log.i(TAG, "popping backstack");
            mFragManager.popBackStack();
            finish();
            overridePendingTransition(R.anim.fade_in_from_left, R.anim.fade_out_to_right);
        } else {
            Log.i(TAG, "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    @Subscribe
    public void onEvent(ChannelNameCollected event) {
        initGetChannelAboutView();
    }
}
