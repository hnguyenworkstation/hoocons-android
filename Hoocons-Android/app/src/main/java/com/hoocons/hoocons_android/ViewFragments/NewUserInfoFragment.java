package com.hoocons.hoocons_android.ViewFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewUserInfoFragment extends Fragment {
    @BindView(R.id.profile_image)
    ImageView mProfileImgView;
    @BindView(R.id.display_name_input)
    EditText mDisplayNameInput;
    @BindView(R.id.nickname_input)
    EditText mNicknameInput;
    @BindView(R.id.check_nickname_btn)
    BootstrapButton mCheckNicknamBtn;
    @BindView(R.id.birthday_txt)
    TextView mBirthDayText;
    @BindView(R.id.gender_male)
    RadioButton mGenderMale;
    @BindView(R.id.gender_female)
    RadioButton mGenderFemale;
    @BindView(R.id.submit_button)
    Button mSubmitBtn;

    private final String TAG = NewUserInfoFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public NewUserInfoFragment() {

    }

    public static NewUserInfoFragment newInstance(String param1, String param2) {
        NewUserInfoFragment fragment = new NewUserInfoFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_new_user_info, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }


    /**********************************************
     * EVENTBUS CATCHING FIELDS
     *  + 
     ********************************************** */
}
