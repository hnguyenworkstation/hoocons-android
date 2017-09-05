package com.hoocons.hoocons_android.ViewFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.font.FontAwesome;
import com.hoocons.hoocons_android.EventBus.FieldAvailableRequest;
import com.hoocons.hoocons_android.EventBus.FieldUnavailableRequest;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.Tasks.CheckNicknameAvailabilityTask;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectNicknameFragment extends Fragment {
    @BindView(R.id.nickname_input)
    EditText mNicknameInput;
    @BindView(R.id.check_nickname_btn)
    BootstrapButton mCheckNicknameBtn;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    public CollectNicknameFragment() {
        // Required empty public constructor
    }

    public static CollectNicknameFragment newInstance(String param1, String param2) {
        CollectNicknameFragment fragment = new CollectNicknameFragment();
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
        return inflater.inflate(R.layout.fragment_collect_nickname, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mCheckNicknameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateNicknameField()) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    new CheckNicknameAvailabilityTask(mNicknameInput.getText().toString()).execute();
                }
            }
        });
    }

    private boolean validateNicknameField() {
        String nickname = mNicknameInput.getText().toString();
        if (nickname.isEmpty()) {
            mNicknameInput.setError(getResources().getString(R.string.error_empty_nickname));
            return false;
        }
        if (nickname.matches("[a-zA-Z0-9]*") && nickname.length() >= 5) {
            return true;
        } else {
            mNicknameInput.setError(getResources().getString(R.string.nickname_error));
            return false;
        }
    }


    @Subscribe
    public void onEvent(FieldAvailableRequest request) {
        mProgressBar.setVisibility(View.GONE);
        mCheckNicknameBtn.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
        mCheckNicknameBtn.setBootstrapText(new BootstrapText.Builder(getContext())
                .addFontAwesomeIcon(FontAwesome.FA_CHECK_CIRCLE)
                .addText(" " + getContext().getResources().getText(R.string.checked))
                .build());
    }


    @Subscribe
    public void onEvent(FieldUnavailableRequest request) {
        mProgressBar.setVisibility(View.GONE);
        mNicknameInput.setError(getResources().getString(R.string.nickname_unavailable));
    }

}
