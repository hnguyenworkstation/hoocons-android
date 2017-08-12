package com.hoocons.hoocons_android.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.R;

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
    ImageButton mActionSkip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_channel);
        ButterKnife.bind(this);
    }
}
