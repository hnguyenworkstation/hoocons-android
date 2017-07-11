package com.hoocons.hoocons_android.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.hoocons.hoocons_android.EventBus.CompleteLoginRequest;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.Networking.NetContext;
import com.hoocons.hoocons_android.Networking.Responses.UserInfoResponse;
import com.hoocons.hoocons_android.Networking.Services.UserServices;
import com.hoocons.hoocons_android.R;

import org.greenrobot.eventbus.EventBus;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkValidState();
            }
        }, SPLASH_TIME_OUT);
    }

    private void checkValidState() {
        if (SharedPreferencesManager.getDefault().getUserToken() != null &&
                SharedPreferencesManager.getDefault().getUserId() == -1) {
            UserServices service = NetContext.instance.create(UserServices.class);
            service.getUserInfo().enqueue(new Callback<UserInfoResponse>() {
                @Override
                public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                    UserInfoResponse resp = response.body();

                    if (response.code() == 200) {
                        SharedPreferencesManager.getDefault().setUserId(resp.getUserPK());
                        if (resp.getNickname().equals(resp.getUsername())){
                            SharedPreferencesManager.getDefault().setRequestUpdateInfo(true);
                        } else {
                            SharedPreferencesManager.getDefault().setRequestUpdateInfo(false);
                        }

                        commitNextStage();
                    } else if (response.code() == 404) {
                        showWarningDialog();
                    }
                }

                @Override
                public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                    showWarningDialog();
                }
            });
        }
    }

    private void showWarningDialog() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getResources().getString(R.string.warning))
                .setContentText(getResources().getString(R.string.fetch_user_info_error))
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        commitNextStage();
                    }
                })
                .show();
    }

    private void commitNextStage() {
        if (SharedPreferencesManager.getDefault().isFirstLaunch()) {
            startActivity(new Intent(SplashActivity.this, IntroActivity.class));
        } else if (SharedPreferencesManager.getDefault().getUserToken() == null) {
            startActivity(new Intent(SplashActivity.this, SocialLoginActivity.class));
        } else if (SharedPreferencesManager.getDefault().isNeededToRequestInfo()) {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class)
                    .putExtra("REQUEST_INFO", true)
                    .putExtra("SKIP_LOGIN", true));
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }

        this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .isConnectedOrConnecting();
        boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();
        if (!is3g && !isWifi) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
            builder.setMessage("Bạn đang không có muốn bật kết nối internet?")
                    .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    })
                    .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(SplashActivity.this,
                                    "Bạn không thể dùng app cho đến khi có kết nối internet", Toast.LENGTH_SHORT).show();
                        }
                    }).show();
        }
    }
}
