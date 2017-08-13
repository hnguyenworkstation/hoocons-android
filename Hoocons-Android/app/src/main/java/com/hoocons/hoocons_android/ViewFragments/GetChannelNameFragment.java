package com.hoocons.hoocons_android.ViewFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hoocons.hoocons_android.EventBus.ChannelNameCollected;
import com.hoocons.hoocons_android.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GetChannelNameFragment extends Fragment {
    @BindView(R.id.gcn_next)
    Button mNextBtn;
    @BindView(R.id.gcn_put_name)
    EditText mChannelInputName;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    public GetChannelNameFragment() {
        // Required empty public constructor
    }

    public static GetChannelNameFragment newInstance(String param1, String param2) {
        GetChannelNameFragment fragment = new GetChannelNameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get_channel_name, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChannelInputName.getText().toString().length() == 0) {
                    Toast.makeText(getContext(), getContext().getResources()
                                    .getString(R.string.please_pick_a_name),
                            Toast.LENGTH_SHORT).show();
                } else {
                    EventBus.getDefault().post(new ChannelNameCollected(mChannelInputName.getText().toString()));
                }
            }
        });
    }
}
