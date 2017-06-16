package com.hoocons.hoocons_android.ViewFragments;


import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class VerifyPasswordFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.action_back)
    ImageButton mBackBtn;
    @BindView(R.id.action_close)
    ImageButton mCloseBtn;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.confirm_password)
    EditText mConfirmPassword;
    @BindView(R.id.submit_button)
    Button mSubmitBtn;

    private static final String STATE_ARG = "STATE_ARG";
    private static final String PHONE_NUMBER = "PHONE_NUMBER";

    private static final String STATE_RESET_PASSWORD = "RESET_PASSWORD";
    private static final String STATE_REGISTER = "REGISTER";

    private String mState;
    private String mPhoneNumber;
    private SweetAlertDialog pDialog;

    public VerifyPasswordFragment() {
        // Required empty public constructor
    }

    public static VerifyPasswordFragment newInstance(String phoneNumber, String stateArg){
        VerifyPasswordFragment fragment = new VerifyPasswordFragment();
        Bundle args = new Bundle();
        args.putString(STATE_ARG, stateArg);
        args.putString(PHONE_NUMBER, phoneNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mState = getArguments().getString(STATE_ARG);
            mPhoneNumber = getArguments().getString(PHONE_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_verify_password, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    private void showProcessDialog() {
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getContext().getResources().getColor(R.color.colorPrimary));
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    public void onClick(View v) {

    }
}
