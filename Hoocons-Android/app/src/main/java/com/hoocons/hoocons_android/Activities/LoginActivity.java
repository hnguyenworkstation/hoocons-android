package com.hoocons.hoocons_android.Activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.github.ppamorim.dragger.DraggerActivity;
import com.github.ppamorim.dragger.LazyDraggerActivity;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewFragments.PhoneLoginFragment;
import com.hoocons.hoocons_android.ViewFragments.VerifyPhoneFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class LoginActivity extends DraggerActivity {
    private FragmentTransaction mFragTransition;
    private FragmentManager mFragManager;

    private PhoneLoginFragment loginFragment;
    private VerifyPhoneFragment verifyPhoneFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginFragment = new PhoneLoginFragment();
        verifyPhoneFragment = new VerifyPhoneFragment();

        mFragManager = getSupportFragmentManager();
        mFragTransition = mFragManager.beginTransaction();
        mFragTransition.replace(R.id.login_container, loginFragment);
        mFragTransition.commit();
    }

    /* *************************************************
    *   EVENTBUS EVENTS CATCHING AREA
    ***************************************************/
}
