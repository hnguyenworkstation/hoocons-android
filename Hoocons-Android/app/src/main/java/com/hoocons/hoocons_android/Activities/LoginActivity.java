package com.hoocons.hoocons_android.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.github.ppamorim.dragger.DraggerActivity;
import com.github.ppamorim.dragger.LazyDraggerActivity;
import com.hoocons.hoocons_android.EventBus.CompleteLoginRequest;
import com.hoocons.hoocons_android.EventBus.TaskCompleteRequest;
import com.hoocons.hoocons_android.EventBus.UserInfoRequest;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Helpers.PermissionUtils;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.Manifest;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewFragments.NewUserInfoFragment;
import com.hoocons.hoocons_android.ViewFragments.PhoneLoginFragment;
import com.hoocons.hoocons_android.ViewFragments.VerifyPasswordFragment;
import com.hoocons.hoocons_android.ViewFragments.VerifyPhoneFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity {
    private FragmentTransaction mFragTransition;
    private FragmentManager mFragManager;

    private PhoneLoginFragment loginFragment;
    private VerifyPhoneFragment verifyPhoneFragment;

    private boolean updateInfo;
    private boolean skipLogin;
    private boolean requirePasswordScreen;
    private String process;
    private String phoneNumber;

    private final String PROCESS_REGISTER = "register_process";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();

        updateInfo = intent.getBooleanExtra("REQUEST_INFO", true);
        skipLogin = intent.getBooleanExtra("SKIP_LOGIN", false);
        requirePasswordScreen = intent.getBooleanExtra("REQUIRE_PASSWORD_SCREEN", false);
        process = intent.getStringExtra("PROCESS");
        phoneNumber = intent.getStringExtra("PHONE_NUMBER");

        loginFragment = new PhoneLoginFragment();
        verifyPhoneFragment = new VerifyPhoneFragment();

        mFragManager = getSupportFragmentManager();
        mFragTransition = mFragManager.beginTransaction();

        if (skipLogin) {
            mFragTransition.replace(R.id.login_container, new NewUserInfoFragment());
            mFragTransition.commit();
        } else if (requirePasswordScreen) {
            mFragTransition.replace(R.id.login_container,
                    VerifyPasswordFragment.newInstance(phoneNumber, process));
            mFragTransition.commit();
        } else {
            mFragTransition.replace(R.id.login_container, loginFragment);
            mFragTransition.commit();
        }
    }

    private void completeLoginActivity() {
        AppUtils.signInAnonymously(this);
        SharedPreferencesManager.getDefault().setRequestUpdateInfo(false);

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void collectNewUserInfo() {
        AppUtils.signInAnonymously(this);
        SharedPreferencesManager.getDefault().setRequestUpdateInfo(true);

        Intent intent = new Intent(LoginActivity.this, CollectUserInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    /* *************************************************
    *   EVENTBUS EVENTS CATCHING AREA
    ***************************************************/
    @Subscribe
    public void onEvent(CompleteLoginRequest request) {
        completeLoginActivity();
    }

    @Subscribe
    public void onEvent(UserInfoRequest request) {
        collectNewUserInfo();
    }
}
