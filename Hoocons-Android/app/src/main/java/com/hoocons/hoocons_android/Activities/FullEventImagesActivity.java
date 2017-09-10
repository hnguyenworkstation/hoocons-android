package com.hoocons.hoocons_android.Activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hoocons.hoocons_android.Adapters.EventImagesViewpagerAdapter;
import com.hoocons.hoocons_android.CustomUI.HackyViewPager;
import com.hoocons.hoocons_android.EventBus.OnImageViewClicked;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.Parcel.EventParcel;
import com.hoocons.hoocons_android.Parcel.MultiImagesEventClickedParcel;
import com.hoocons.hoocons_android.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FullEventImagesActivity extends BaseActivity {
    @BindView(R.id.hacky_viewpager)
    HackyViewPager mViewPager;
    @BindView(R.id.event_text)
    TextView mEventTextContent;
    @BindView(R.id.tv_indicator)
    TextView mIndicator;
    @BindView(R.id.title_layout)
    RelativeLayout mTitleLayout;

    private MultiImagesEventClickedParcel parcel;
    private EventImagesViewpagerAdapter eventImagesViewpagerAdapter;
    private boolean isHiding = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_full_event_images);
        ButterKnife.bind(this);

        parcel = (MultiImagesEventClickedParcel) Parcels.unwrap(getIntent()
                .getParcelableExtra("event_images_pack"));

        mIndicator.setText(String.format("%s / %s", String.valueOf(parcel.getClickedPosition() + 1),
                String.valueOf(parcel.getResponseList().size())));

        eventImagesViewpagerAdapter = new EventImagesViewpagerAdapter(this, parcel.getResponseList());
        mViewPager.setAdapter(eventImagesViewpagerAdapter);
        mViewPager.setCurrentItem(parcel.getClickedPosition());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mIndicator.setText(String.format("%s / %s", String.valueOf(position + 1),
                        String.valueOf(parcel.getResponseList().size())));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (parcel.getTextContent() != null  && parcel.getTextContent().length() > 0)
            mEventTextContent.setText(parcel.getTextContent());
    }


    @Subscribe
    public void onEvent(OnImageViewClicked request) {
        if (isHiding) {
            mTitleLayout.setVisibility(View.VISIBLE);
            mEventTextContent.setVisibility(View.VISIBLE);
            isHiding = false;
        } else {
            isHiding = true;
            mTitleLayout.setVisibility(View.GONE);
            mEventTextContent.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
