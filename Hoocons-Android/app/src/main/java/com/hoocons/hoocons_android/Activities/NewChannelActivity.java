package com.hoocons.hoocons_android.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewFragments.GetChannelNameFragment;

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
    private Fragment viewFragment;
    private final String TAG = NewChannelActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_channel);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        viewFragment = new GetChannelNameFragment();
        mFragManager = getSupportFragmentManager();
        mFragTransition = mFragManager.beginTransaction();
        mFragTransition.replace(R.id.create_channel_container, viewFragment, "get_channel_name");
        mFragTransition.commit();
        AppUtils.loadCropImageWithProgressBar(this, "http://www.stanleychowillustration.com/uploads/images/MANCHESTER_LANDSCAPE_30x12.jpg", mWallpaper, mProgressBar);
    }

    @Override
    public void onBackPressed(){
        if (mFragManager.findFragmentByTag("get_channel_id") != null) {
            mFragTransition = mFragManager.beginTransaction();
            mFragTransition.setCustomAnimations(R.anim.fade_out_to_right, R.anim.fade_in_from_left);
            mFragTransition.replace(R.id.create_channel_container, viewFragment, "get_channel_name");
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
}
