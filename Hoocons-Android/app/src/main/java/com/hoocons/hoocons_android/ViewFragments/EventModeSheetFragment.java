package com.hoocons.hoocons_android.ViewFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.hoocons.hoocons_android.EventBus.FriendModeRequest;
import com.hoocons.hoocons_android.EventBus.PrivateModeRequest;
import com.hoocons.hoocons_android.EventBus.PublicModeRequest;
import com.hoocons.hoocons_android.EventBus.WarningContentRequest;
import com.hoocons.hoocons_android.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventModeSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener{
    @BindView(R.id.event_warning)
    CheckBox mWarningContent;
    @BindView(R.id.radio_public)
    RadioButton mPublicRad;
    @BindView(R.id.radio_friends)
    RadioButton mFriendsRad;
    @BindView(R.id.radio_private)
    RadioButton mPrivateRad;

    public EventModeSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_mode_sheet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mWarningContent.setOnClickListener(this);
        mPublicRad.setOnClickListener(this);
        mFriendsRad.setOnClickListener(this);
        mPrivateRad.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radio_friends:
                if (mFriendsRad.isChecked())
                    EventBus.getDefault().post(new FriendModeRequest());
                break;
            case R.id.radio_private:
                if (mPrivateRad.isChecked())
                    EventBus.getDefault().post(new PrivateModeRequest());
                break;
            case R.id.radio_public:
                if (mPublicRad.isChecked())
                    EventBus.getDefault().post(new PublicModeRequest());
                break;
            case R.id.event_warning:
                EventBus.getDefault().post(new WarningContentRequest(mWarningContent.isChecked()));
                break;
            default:
                break;
        }
    }
}
