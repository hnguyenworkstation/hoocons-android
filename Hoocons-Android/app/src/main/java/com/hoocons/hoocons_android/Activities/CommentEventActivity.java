package com.hoocons.hoocons_android.Activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.github.ppamorim.dragger.DraggerActivity;
import com.hoocons.hoocons_android.Parcel.EventParcel;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewFragments.CommentListFragment;
import com.hoocons.hoocons_android.ViewFragments.NewUserInfoFragment;

import org.parceler.Parcels;

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

        eventParcel = (EventParcel) Parcels.unwrap(getIntent().getParcelableExtra("event"));

        mFragManager = getSupportFragmentManager();
        mFragTransition = mFragManager.beginTransaction();

        setDraggerLimit(0.8f);
        setSlideEnabled(isDraggable);

        mFragTransition.replace(R.id.frame_container,
                CommentListFragment.newInstance(eventParcel.getId(),
                                                    eventParcel.getLikeCount(),
                                                    eventParcel.isLiked()), "COMMENT_LIST").commit();
    }
}
