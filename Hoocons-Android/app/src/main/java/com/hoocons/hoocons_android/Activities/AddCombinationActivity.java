package com.hoocons.hoocons_android.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.github.ppamorim.dragger.DraggerActivity;
import com.github.ppamorim.dragger.LazyDraggerActivity;
import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddCombinationActivity extends DraggerActivity
        implements View.OnClickListener{
    @BindView(R.id.new_event_btn_view)
    LinearLayout mNewEventBtn;
    @BindView(R.id.new_meetup_btn_view)
    LinearLayout mNewMeetupBtn;
    @BindView(R.id.new_checkin_btn_view)
    LinearLayout mCheckinBtn;
    @BindView(R.id.new_channel_btn_view)
    LinearLayout mNewChannelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_combination);
        ButterKnife.bind(this);

        mNewEventBtn.setOnClickListener(this);
        mNewMeetupBtn.setOnClickListener(this);
        mCheckinBtn.setOnClickListener(this);
        mNewChannelBtn.setOnClickListener(this);
    }

    private void startNewEventActivity() {
        startActivity(new Intent(AddCombinationActivity.this, NewEventActivity.class));
    }

    private void startNewMeetOutActivity() {
        startActivity(new Intent(AddCombinationActivity.this, NewMeetOutActivity.class));
    }

    private void startCustomPlacePicker() {
        startActivity(new Intent(AddCombinationActivity.this, PlacePickerActivity.class));
    }

    private void startNewChannelActivity() {
        startActivity(new Intent(AddCombinationActivity.this, ChannelActivity.class));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_event_btn_view:
                startNewEventActivity();
                break;
            case R.id.new_meetup_btn_view:
                startNewMeetOutActivity();
                break;
            case R.id.new_checkin_btn_view:
                startCustomPlacePicker();
                break;
            case R.id.new_channel_btn_view:
                startNewChannelActivity();
                break;
            default:
                break;
        }
    }
}
