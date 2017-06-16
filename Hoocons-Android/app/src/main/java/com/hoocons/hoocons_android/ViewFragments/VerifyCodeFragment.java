package com.hoocons.hoocons_android.ViewFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hoocons.hoocons_android.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class VerifyCodeFragment extends Fragment implements View.OnClickListener{
    @BindView(R.id.action_close)
    ImageButton mCloseButton;
    @BindView(R.id.action_back)
    ImageButton mBackButton;
    @BindView(R.id.submit_button)
    Button mSubmitBtn;
    @BindView(R.id.send_again)
    TextView mResendTxt;
    @BindView(R.id.code_input)
    EditText mCodeInput;

    private final String TAG = VerifyCodeFragment.class.getSimpleName();

    private static final String VERIFICATION_CODE = "VERIFICATION_CODE";
    private static final String PHONE_NUMBER = "PHONE_NUMBER";

    private static final String STATE_ARG = "STATE_ARG";

    private static final String STATE_RESET_PASSWORD = "RESET_PASSWORD";
    private static final String STATE_REGISTER = "REGISTER";

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private String mPhoneNumber;
    private String mStateArg;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mFirebaseAuth;
    private boolean isFirstTime = true;

    private SweetAlertDialog pDialog;

    public VerifyCodeFragment() {
        // Required empty public constructor
    }

    public static VerifyCodeFragment newInstance(String mVerificationId, String mPhoneNumber,
                                                 String mState) {
        VerifyCodeFragment fragment = new VerifyCodeFragment();
        Bundle args = new Bundle();
        args.putString(VERIFICATION_CODE, mVerificationId);
        args.putString(PHONE_NUMBER, mPhoneNumber);
        args.putString(STATE_ARG, mState);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVerificationId = getArguments().getString(VERIFICATION_CODE);
            mPhoneNumber = getArguments().getString(PHONE_NUMBER);
            mStateArg = getArguments().getString(STATE_ARG);
        }

        mFirebaseAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                pDialog.dismiss();
                Log.d(TAG, "onVerificationCompleted:" + credential);
                mVerificationInProgress = false;
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                mVerificationInProgress = false;
                pDialog.dismiss();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {

                } else if (e instanceof FirebaseTooManyRequestsException) {

                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                pDialog.dismiss();
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationInProgress = false;
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_verify_code, container, false);
        ButterKnife.bind(this, rootView);

        mBackButton.setOnClickListener(this);
        mCloseButton.setOnClickListener(this);
        mSubmitBtn.setOnClickListener(this);
        mResendTxt.setOnClickListener(this);

        return rootView;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mFirebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = task.getResult().getUser();
                        requestPasswordScreen();
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            mCodeInput.setError("Invalid code.");
                        }
                        Toast.makeText(getContext(), "Login Failed! Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private void resendFirstTimeVerificationCode(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),      // Activity (for callback binding)
                mCallbacks);        // Calls back

        mVerificationInProgress = true;
        isFirstTime = false;
    }

    private void resendVerificationCode(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),      // Activity (for callback binding)
                mCallbacks,         // Calls back
                mResendToken);

        mVerificationInProgress = true;
    }

    private void showProcessDialog() {
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getContext().getResources().getColor(R.color.colorPrimary));
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void sendPhoneCodeAgain() {
        if (!mVerificationInProgress) {
            if (isFirstTime) {
                resendFirstTimeVerificationCode(mPhoneNumber);
            } else {
                resendVerificationCode(mPhoneNumber);
            }
        }
    }

    private void showResendDialog() {
        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this file!")
                .setConfirmText("Yes,delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sendPhoneCodeAgain();
                        sDialog.dismiss();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
    }

    private void requestPasswordScreen() {
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in_from_right, R.anim.fade_out_to_left);
        ft.replace(R.id.login_container,
                VerifyPasswordFragment.newInstance(mPhoneNumber, mStateArg),
                "verify_phone_fragment");
        ft.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.action_back:
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.fade_in_from_left, R.anim.fade_out_to_right);
                ft.replace(R.id.login_container, new VerifyPhoneFragment(), "verify_phone_fragment");
                ft.commit();
                break;
            case R.id.action_close:
                break;
            case R.id.submit_button:
                if (!mVerificationInProgress) {
                    if (mCodeInput.getText().toString().length() < 6) {
                        mCodeInput.setError("Invalid Code");
                    } else {
                        showProcessDialog();
                        verifyPhoneNumberWithCode(mVerificationId, mCodeInput.getText().toString());
                    }
                }
                break;
            case R.id.send_again:
                showResendDialog();
                break;
            default:
                break;
        }
    }
}
