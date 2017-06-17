package com.hoocons.hoocons_android.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.github.ppamorim.dragger.DraggerActivity;
import com.github.ppamorim.dragger.LazyDraggerActivity;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewFragments.NewUserInfoFragment;
import com.hoocons.hoocons_android.ViewFragments.PhoneLoginFragment;
import com.hoocons.hoocons_android.ViewFragments.VerifyPhoneFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class LoginActivity extends BaseActivity {
    private FragmentTransaction mFragTransition;
    private FragmentManager mFragManager;

    private PhoneLoginFragment loginFragment;
    private VerifyPhoneFragment verifyPhoneFragment;

    private boolean updateInfo;
    private boolean skipLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();

        updateInfo = intent.getBooleanExtra("REQUEST_INFO", true);
        skipLogin = intent.getBooleanExtra("SKIP_LOGIN", false);

        loginFragment = new PhoneLoginFragment();
        verifyPhoneFragment = new VerifyPhoneFragment();

        mFragManager = getSupportFragmentManager();
        mFragTransition = mFragManager.beginTransaction();

        if (skipLogin) {
            mFragTransition.replace(R.id.login_container, new NewUserInfoFragment());
            mFragTransition.commit();
        } else {
            mFragTransition.replace(R.id.login_container, loginFragment);
            mFragTransition.commit();
        }
    }

    /* *************************************************
    *   EVENTBUS EVENTS CATCHING AREA
    ***************************************************/
}
