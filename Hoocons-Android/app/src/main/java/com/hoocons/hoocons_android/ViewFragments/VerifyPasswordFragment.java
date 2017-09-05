package com.hoocons.hoocons_android.ViewFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hoocons.hoocons_android.EventBus.UserInfoRequest;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Requests.CredentialRequest;
import com.hoocons.hoocons_android.Networking.Responses.TokenResponse;
import com.hoocons.hoocons_android.Networking.Services.UserServices;
import com.hoocons.hoocons_android.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private final String PROCESS_REGISTER = "register_process";

    private String mState;
    private String mPhoneNumber;
    private SweetAlertDialog pDialog;

    public VerifyPasswordFragment() {

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
        View rootView = inflater.inflate(R.layout.fragment_verify_password, container, false);
        ButterKnife.bind(this, rootView);

        initView();

        return rootView;
    }

    private void initView() {
        if (mState.equals(PROCESS_REGISTER)) {
            mSubmitBtn.setText(getResources().getString(R.string.register));

            mSubmitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    attemptToRegister(mPassword.getText().toString(),
                            mConfirmPassword.getText().toString());
                }
            });
        }
    }

    private void attemptToRegister(final String password, String repass) {
        if (isValidPassword(password, repass)) {
            showProcessDialog();
            UserServices services = NetContext.instance.create(UserServices.class);
            services.register(new CredentialRequest(mPhoneNumber, password)).enqueue(new Callback<TokenResponse>() {
                @Override
                public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                    if (response.code() == 201) {
                        Toast.makeText(getContext(), "Register success!", Toast.LENGTH_SHORT).show();
                        registerSuccess(response.body(), mPhoneNumber, password);
                    } else {
                        Toast.makeText(getContext(), "Register failed!", Toast.LENGTH_SHORT).show();
                    }
                    pDialog.dismiss();
                }

                @Override
                public void onFailure(Call<TokenResponse> call, Throwable t) {
                    pDialog.dismiss();
                    Toast.makeText(getContext(), "Server issue! Please try again later", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void registerSuccess(TokenResponse token, String phoneNumber, String password) {
        SharedPreferencesManager.getDefault()
                .setUserToken(token.getAccessToken());
        SharedPreferencesManager.getDefault()
                .setUserId(token.getUserId());
        SharedPreferencesManager.getDefault()
                .setCredentials(new String[] {phoneNumber, password});

        requestUpdateInfoScreen();
    }

    private void requestUpdateInfoScreen() {
        EventBus.getDefault().post(new UserInfoRequest());
    }

    private boolean isValidPassword(String pass, String repass) {
        if (pass.isEmpty()) {
            mPassword.setError(getResources().getString(R.string.error_empty_password));
            return false;
        } else if (repass.isEmpty()) {
            mConfirmPassword.setError(getResources().getString(R.string.error_empty_password));
            return false;
        } else if (!pass.equals(repass)) {
            mConfirmPassword.setError(getResources().getString(R.string.error_pass_not_match));
            return false;
        }

        return true;
    }

    private void showProcessDialog() {
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText(getResources().getString(R.string.registering));
        pDialog.getProgressHelper().setBarColor(getContext().getResources().getColor(R.color.colorPrimary));
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    public void onStop() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

    }
}
