package com.hoocons.hoocons_android.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.github.ppamorim.dragger.DraggerActivity;
import com.github.ppamorim.dragger.LazyDraggerActivity;
import com.hoocons.hoocons_android.Helpers.PermissionUtils;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.Manifest;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewFragments.NewUserInfoFragment;
import com.hoocons.hoocons_android.ViewFragments.PhoneLoginFragment;
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

    private static final int REQUEST_IMAGE_VIEW_PERMISSION = 111;
    private static final int REQUEST_LOCATION_PERMISSION = 222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();

        updateInfo = intent.getBooleanExtra("REQUEST_INFO", true);
        skipLogin = intent.getBooleanExtra("SKIP_LOGIN", false);

        if (!PermissionUtils.isPermissionValid(LoginActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                && !PermissionUtils.isPermissionValid(LoginActivity.this,
                    android.Manifest.permission.CAMERA)) {

            List<String> permissions = new ArrayList<>();
            permissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            permissions.add(android.Manifest.permission.CAMERA);

            PermissionUtils.requestPermissions(LoginActivity.this,
                    REQUEST_IMAGE_VIEW_PERMISSION, permissions);
        }

        if (!PermissionUtils.isPermissionValid(LoginActivity.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                && !PermissionUtils.isPermissionValid(LoginActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            List<String> permissions = new ArrayList<>();
            permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);

            PermissionUtils.requestPermissions(LoginActivity.this,
                    REQUEST_LOCATION_PERMISSION, permissions);
        }

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
