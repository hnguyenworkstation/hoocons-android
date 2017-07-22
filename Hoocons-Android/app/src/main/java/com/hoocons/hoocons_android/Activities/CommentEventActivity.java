package com.hoocons.hoocons_android.Activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.github.ppamorim.dragger.DraggerActivity;
import com.hoocons.hoocons_android.Parcel.EventParcel;
import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentEventActivity extends DraggerActivity {
    @BindView(R.id.frame_container)
    FrameLayout mFrameContainer;

    private FragmentTransaction mFragTransition;
    private FragmentManager mFragManager;
    private boolean isDraggable = true;
    private EventParcel eventParcel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_event);
        ButterKnife.bind(this);

        mFragManager = getSupportFragmentManager();
        mFragTransition = mFragManager.beginTransaction();

        setDraggerLimit(0.8f);
        setSlideEnabled(isDraggable);
    }
}
