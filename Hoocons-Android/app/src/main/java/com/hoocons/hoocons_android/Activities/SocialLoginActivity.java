package com.hoocons.hoocons_android.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.BinderThread;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.hoocons.hoocons_android.Helpers.PermissionUtils;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Requests.CredentialRequest;
import com.hoocons.hoocons_android.Networking.Services.UserServices;
import com.hoocons.hoocons_android.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SocialLoginActivity extends BaseActivity {
    @BindView(R.id.login_button)
    Button mLoginBtn;
    @BindView(R.id.register_btn)
    Button mRegisterBtn;

    private CallbackManager callbackManager;
    private SweetAlertDialog mAlertDialog;

    private static final int REQUEST_IMAGE_VIEW_PERMISSION = 111;
    private static final int REQUEST_LOCATION_PERMISSION = 222;
    private static final int ACCOUNT_KIT_REQUEST = 124;
    private final String PROCESS_REGISTER = "register_process";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_login);
        ButterKnife.bind(this);

        AccountKit.initialize(this);
        checkPermission();

        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SocialLoginActivity.this, LoginActivity.class));
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneRegister();
            }
        });
    }

    private void showLoadingDialog() {
        mAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mAlertDialog.setTitleText("Loading");
        mAlertDialog.setCancelable(false);
        mAlertDialog.show();
    }

    private void showLoginOptionDialog() {
        mAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        mAlertDialog.setTitleText(getResources().getString(R.string.return_user_question));
        mAlertDialog.setContentText(getResources().getString(R.string.registered_account_detected));
        mAlertDialog.setConfirmText(getResources().getString(R.string.login));
        mAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismiss();
                startActivity(new Intent(SocialLoginActivity.this, LoginActivity.class));
            }
        });

        mAlertDialog.setCancelText(getResources().getString(R.string.cancel));
        mAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });
        mAlertDialog.show();
    }

    private void phoneRegister() {
        final Intent intent = new Intent(this, AccountKitActivity.class);

        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN);

        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());

        startActivityForResult(intent, ACCOUNT_KIT_REQUEST);
    }

    private void checkPermission() {
        if (!PermissionUtils.isPermissionValid(SocialLoginActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                && !PermissionUtils.isPermissionValid(SocialLoginActivity.this,
                android.Manifest.permission.CAMERA)) {

            List<String> permissions = new ArrayList<>();
            permissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            permissions.add(android.Manifest.permission.CAMERA);

            PermissionUtils.requestPermissions(SocialLoginActivity.this,
                    REQUEST_IMAGE_VIEW_PERMISSION, permissions);
        }

        if (!PermissionUtils.isPermissionValid(SocialLoginActivity.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                && !PermissionUtils.isPermissionValid(SocialLoginActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            List<String> permissions = new ArrayList<>();
            permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);

            PermissionUtils.requestPermissions(SocialLoginActivity.this,
                    REQUEST_LOCATION_PERMISSION, permissions);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACCOUNT_KIT_REQUEST) {
            showLoadingDialog();

            final AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;

            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();

                mAlertDialog.hide();
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";

                mAlertDialog.hide();
            } else {
                checkAccount();

                if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                    loginResult.getFinalAuthorizationState();
                } else {
                    toastMessage = String.format(
                            "Success:%s...", loginResult.getAuthorizationCode().substring(0, 10));
                }
            }

            Toast.makeText(SocialLoginActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void checkAccount() {
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
                final PhoneNumber phoneNumber = account.getPhoneNumber();
                final String phoneNumberString = phoneNumber.toString();

                UserServices userServices = NetContext.instance.create(UserServices.class);
                userServices.checkUsernameAvailability(new CredentialRequest(phoneNumberString))
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                mAlertDialog.hide();

                                if (response.code() == 200) {
                                    startActivity(new Intent(SocialLoginActivity.this,
                                            LoginActivity.class)
                                        .putExtra("PHONE_NUMBER", phoneNumberString)
                                        .putExtra("REQUIRE_PASSWORD_SCREEN", true)
                                        .putExtra("PROCESS", PROCESS_REGISTER));
                                } else if (response.code() == 201) {
                                    showLoginOptionDialog();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                mAlertDialog.hide();
                                Toast.makeText(SocialLoginActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onError(final AccountKitError error) {
                mAlertDialog.hide();
                Log.e("AccountKit", error.toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
