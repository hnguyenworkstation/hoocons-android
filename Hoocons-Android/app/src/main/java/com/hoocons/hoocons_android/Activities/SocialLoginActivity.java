package com.hoocons.hoocons_android.Activities;

import android.os.Bundle;
import android.widget.Button;

import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SocialLoginActivity extends BaseActivity {
    @BindView(R.id.register_button)
    Button mRegisterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_login);
        ButterKnife.bind(this);
    }
}
