package com.hoocons.hoocons_android.ViewFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.hoocons.hoocons_android.R;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.Executor;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerifyPhoneFragment extends Fragment implements View.OnClickListener{
    @BindView(R.id.action_close)
    ImageButton mCloseButton;
    @BindView(R.id.action_back)
    ImageButton mBackButton;
    @BindView(R.id.submit_button)
    Button mSubmitBtn;
    @BindView(R.id.country_code_picker)
    CountryCodePicker mCodePicker;
    @BindView(R.id.phone_input)
    EditText mPhoneInput;

    private final String TAG = VerifyPhoneFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;

    private FirebaseAuth mFirebaseAuth;
    private String mCountryCode;

    private boolean mVerificationInProgress = false;
    private String mPhoneNumber;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    public VerifyPhoneFragment() {

    }

    public static VerifyPhoneFragment newInstance(String param1, String param2) {
        VerifyPhoneFragment fragment = new VerifyPhoneFragment();
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

        mVerificationInProgress = false;
        mFirebaseAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                mVerificationInProgress = false;
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                mVerificationInProgress = false;

                if (e instanceof FirebaseAuthInvalidCredentialsException) {

                } else if (e instanceof FirebaseTooManyRequestsException) {

                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);

                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.fade_in_from_right, R.anim.fade_out_to_left);
                ft.replace(R.id.login_container,
                        VerifyCodeFragment.newInstance(verificationId, mPhoneNumber),
                        "verify_code_fragment");
                ft.commit();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_verify_phone, container, false);
        ButterKnife.bind(this, rootView);

        mCountryCode =  mCodePicker.getDefaultCountryCodeWithPlus();
        mBackButton.setOnClickListener(this);
        mSubmitBtn.setOnClickListener(this);

        mCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                Toast.makeText(getContext(), "Updated " + mCodePicker.getSelectedCountryName(), Toast.LENGTH_SHORT).show();
                mCountryCode =  mCodePicker.getSelectedCountryCodeWithPlus();
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                java.util.concurrent.TimeUnit.SECONDS,
                getActivity(),
                mCallbacks);

        mVerificationInProgress = true;
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneInput.getText().toString();
        if (TextUtils.isEmpty(phoneNumber) && phoneNumber.length() > 8) {
            mPhoneInput.setError("Invalid phone number.");
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_back:
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.fade_in_from_left, R.anim.fade_out_to_right);
                ft.replace(R.id.login_container, new PhoneLoginFragment(), "phone_login_fragment");
                ft.commit();
                break;
            case R.id.action_close:
                break;
            case R.id.submit_button:
                if (!mVerificationInProgress && validatePhoneNumber()) {
                    mPhoneNumber = String.format("%s%s", mCountryCode, mPhoneInput.getText().toString());
                    startPhoneNumberVerification(mPhoneNumber);
                }
                break;
            default:
                break;
        }
    }
}
