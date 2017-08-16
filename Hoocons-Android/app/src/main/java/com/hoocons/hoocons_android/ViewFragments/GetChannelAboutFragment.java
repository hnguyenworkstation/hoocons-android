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

import com.hoocons.hoocons_android.EventBus.ChannelDescCollected;
import com.hoocons.hoocons_android.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GetChannelAboutFragment extends Fragment {
    @BindView(R.id.input)
    EditText mInput;
    @BindView(R.id.next)
    Button mNextBtn;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    public GetChannelAboutFragment() {
        // Required empty public constructor
    }

    public static GetChannelAboutFragment newInstance(String param1, String param2) {
        GetChannelAboutFragment fragment = new GetChannelAboutFragment();
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
        return inflater.inflate(R.layout.fragment_get_channel_about, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInput.getText().length() > 0) {
                    EventBus.getDefault().post(new ChannelDescCollected(""));
                } else {
                    mInput.setError(getResources().getText(R.string.invalid_desc));
                    Toast.makeText(getContext(), getResources().getString(R.string.please_tell_channel_desc), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
