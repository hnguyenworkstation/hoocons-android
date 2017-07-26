package com.hoocons.hoocons_android.Tasks.JobServices;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hoocons.hoocons_android.Activities.AddCombinationActivity;
import com.hoocons.hoocons_android.Activities.SplashActivity;
import com.hoocons.hoocons_android.R;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by hungnguyen on 7/14/17.
 */
public class FirebaseMessageService extends FirebaseMessagingService {
    private static final String TAG = FirebaseMessageService.class.toString();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "MessageFirebase data payload: " + remoteMessage.getData());
            String title, message;
            title = remoteMessage.getData().get("title");
            if (title == null) {
                title = "Hoocons";
            }

            message = remoteMessage.getData().get("message");
            Log.e(TAG, "onMessageReceived: " + remoteMessage.getData().get("code"));

            Intent intent = new Intent(this, AddCombinationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultsound)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager
                    = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, builder.build());
        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "MessageFirebase data payload: " + remoteMessage.getData());
            Intent intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setAutoCancel(true)
                    .setSound(defaultSound)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager
                    = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, builder.build());
            EventBus.getDefault().post(remoteMessage);
        }
    }
}