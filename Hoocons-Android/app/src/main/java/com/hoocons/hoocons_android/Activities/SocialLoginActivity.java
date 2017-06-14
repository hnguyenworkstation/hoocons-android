package com.hoocons.hoocons_android.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cengalabs.flatui.views.FlatButton;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SocialLoginActivity extends BaseActivity {
    @BindView(R.id.slogin_phonelogin)
    FlatButton mPhoneLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_login);
        ButterKnife.bind(this);

        mPhoneLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SocialLoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
