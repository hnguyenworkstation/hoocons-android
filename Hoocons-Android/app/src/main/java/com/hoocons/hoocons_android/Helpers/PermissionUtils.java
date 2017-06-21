package com.hoocons.hoocons_android.Helpers;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hungnguyen on 6/17/17.
 */

public class PermissionUtils {
    private static final String TAG = PermissionUtils.class.getSimpleName();
    private static PermissionUtils permissionUtil = null;

    public static boolean isOverMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean isPermissionValid(Activity activity, String permissionName) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int checkCallPhonePermission = ContextCompat.checkSelfPermission(activity, permissionName);
                return checkCallPhonePermission == PackageManager.PERMISSION_GRANTED;
            } else {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static List<String> findDeniedPermissions(Activity activity, List<String> permissions) {
        if (permissions == null || permissions.size() == 0) {
            return null;
        } else {
            List<String> denyPermissions = new ArrayList<>();

            for (String value : permissions) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (activity.checkSelfPermission(value) != PackageManager.PERMISSION_GRANTED) {
                            denyPermissions.add(value);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return denyPermissions;
        }
    }


    public static void requestPermissions(Activity activity, int requestCode, List<String> mListPermissions) {
        if (mListPermissions == null || mListPermissions.size() == 0) {
            return;
        }

        if (!isOverMarshmallow()) {
            // Android 6.0以下不用申请.
            return;

        } else {
            List<String> deniedPermissionList = findDeniedPermissions(activity, mListPermissions);

            if (deniedPermissionList != null && deniedPermissionList.size() > 0) {
                Log.i(TAG, "requestPermissions: ");
                activity.requestPermissions(deniedPermissionList.toArray(new String[deniedPermissionList.size()]), requestCode);
            }
        }

    }


}
