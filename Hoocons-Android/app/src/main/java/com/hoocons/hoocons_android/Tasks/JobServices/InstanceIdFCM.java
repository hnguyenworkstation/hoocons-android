package com.hoocons.hoocons_android.Tasks.JobServices;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.Tasks.Jobs.UpdateFCMJob;
import com.hoocons.hoocons_android.Tasks.Jobs.UpdateUserInfoJob;

/**
 * Created by hungnguyen on 7/14/17.
 */
public class InstanceIdFCM extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String newToken = FirebaseInstanceId.getInstance().getToken();
        String oldToken = SharedPreferencesManager.getDefault().getFcmToken();

        // Update with new Token
        SharedPreferencesManager.getDefault().setFcmToken(newToken);

        // BaseApplication.getInstance().getJobManager().addJobInBackground(new UpdateFCMJob(oldToken, newToken));
    }
}