package com.hoocons.hoocons_android.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.hoocons.hoocons_android.EventBus.UserBasicInfoCollected;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewFragments.CollectNicknameFragment;
import com.hoocons.hoocons_android.ViewFragments.NewUserInfoFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class CollectUserInfoActivity extends BaseActivity {
    private FragmentTransaction mFragTransition;
    private FragmentManager mFragManager;

    private String profileUrl;
    private String gender;
    private String displayName;
    private String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_user_info);
        EventBus.getDefault().register(this);

        mFragManager = getSupportFragmentManager();
        mFragTransition = mFragManager.beginTransaction();

        mFragTransition.replace(R.id.info_fragment_container, new NewUserInfoFragment());
        mFragTransition.commit();
    }

    @Subscribe
    public void onEvent(UserBasicInfoCollected info) {
        profileUrl = info.getProfileUrl();
        gender = info.getGender();
        displayName = info.getDisplayName();
    }
}
