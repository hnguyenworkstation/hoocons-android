package com.hoocons.hoocons_android.Activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewFragments.PhoneLoginFragment;

import butterknife.BindView;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        PhoneLoginFragment loginFragment = new PhoneLoginFragment();
        FragmentManager mFragManager = getSupportFragmentManager();
        FragmentTransaction mFragTransition = mFragManager.beginTransaction();
        mFragTransition.replace(R.id.login_container, loginFragment);
        mFragTransition.commit();
    }
}
