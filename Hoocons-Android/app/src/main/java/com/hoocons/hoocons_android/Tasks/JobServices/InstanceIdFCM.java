package com.hoocons.hoocons_android.Tasks.JobServices;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;

/**
 * Created by hungnguyen on 7/14/17.
 */
public class InstanceIdFCM extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String newToken = FirebaseInstanceId.getInstance().getToken();

        String oldToken = SharedPreferencesManager.getDefault().getFcmToken();

        if (oldToken != null) {
            //Todo: Create or add new Token
        }

        // Update with new Token
        SharedPreferencesManager.getDefault().setFcmToken(newToken);
    }
}