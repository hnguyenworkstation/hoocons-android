package com.hoocons.hoocons_android.ViewFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;
import com.beardedhen.androidbootstrap.font.FontAwesome;
import com.hoocons.hoocons_android.EventBus.FieldAvailableRequest;
import com.hoocons.hoocons_android.EventBus.FieldUnavailableRequest;
import com.hoocons.hoocons_android.EventBus.UserNicknameCollected;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.Tasks.CheckNicknameAvailabilityTask;
import com.vstechlab.easyfonts.EasyFonts;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectNicknameFragment extends Fragment {
    @BindView(R.id.nickname_input)
    EditText mNicknameInput;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.reason)
    TextView mReason;
    @BindView(R.id.check_nickname_btn)
    BootstrapButton mCheckNicknameBtn;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.gcn_next)
    Button mNextBtn;

    private boolean isChecked = false;

    // isRequesting is checking the follow that user submit nickname before checking it
    private boolean isRequesting = false;

    private FragmentTransaction mFragTransition;
    private FragmentManager mFragManager;


    public CollectNicknameFragment() {
        // Required empty public constructor
    }

    public static CollectNicknameFragment newInstance(String param1, String param2) {
        CollectNicknameFragment fragment = new CollectNicknameFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
        }

        mFragManager = getActivity().getSupportFragmentManager();
        mFragTransition = mFragManager.beginTransaction();
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

        initTypeFace();
        mNicknameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isChecked = false;
                isRequesting = false;
                mCheckNicknameBtn.setShowOutline(true);
                mCheckNicknameBtn.setBootstrapBrand(DefaultBootstrapBrand.REGULAR);
                mCheckNicknameBtn.setBootstrapText(new BootstrapText.Builder(getContext())
                        .addFontAwesomeIcon(FontAwesome.FA_CHECK_CIRCLE)
                        .addText(" " + getContext().getResources().getText(R.string.check))
                        .build());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mCheckNicknameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateNicknameField()) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    new CheckNicknameAvailabilityTask(mNicknameInput.getText().toString()).execute();
                }
            }
        });

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateNicknameField() && isChecked) {
                    EventBus.getDefault().post(new UserNicknameCollected(mNicknameInput.getText().toString()));
                    commitNextStage();
                } else if (validateNicknameField()) {
                    isRequesting = true;
                    mProgressBar.setVisibility(View.VISIBLE);
                    new CheckNicknameAvailabilityTask(mNicknameInput.getText().toString()).execute();
                }
            }
        });
    }

    private void initTypeFace() {
        mTitle.setText(getResources().getString(R.string.pick_nickname));
        mReason.setText(getResources().getString(R.string.pick_nickname_reason));

        mReason.setTypeface(EasyFonts.robotoRegular(getContext()));
        mTitle.setTypeface(EasyFonts.robotoBold(getContext()));
        mNicknameInput.setTypeface(EasyFonts.robotoThin(getContext()));
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

    private void commitNextStage() {
        mFragTransition.replace(R.id.info_fragment_container, new CollectUserHobbiesFragment());
        mFragTransition.commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(FieldAvailableRequest request) {
        mProgressBar.setVisibility(View.GONE);
        isChecked = true;

        if (isChecked && isRequesting) {
            EventBus.getDefault().post(new UserNicknameCollected(mNicknameInput.getText().toString()));
            commitNextStage();
        } else {
            mCheckNicknameBtn.setShowOutline(false);
            mCheckNicknameBtn.setBootstrapBrand(DefaultBootstrapBrand.SUCCESS);
            mCheckNicknameBtn.setBootstrapText(new BootstrapText.Builder(getContext())
                    .addFontAwesomeIcon(FontAwesome.FA_CHECK_CIRCLE)
                    .addText(" " + getContext().getResources().getText(R.string.checked))
                    .build());
        }
    }


    @Subscribe
    public void onEvent(FieldUnavailableRequest request) {
        mProgressBar.setVisibility(View.GONE);
        isRequesting = false;
        isChecked = false;
        Toast.makeText(getContext(), getResources().getString(R.string.nickname_unavailable), Toast.LENGTH_SHORT).show();
    }

}
