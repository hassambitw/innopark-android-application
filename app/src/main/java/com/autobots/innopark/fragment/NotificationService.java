package com.autobots.innopark.fragment;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.autobots.innopark.Config;
import com.autobots.innopark.LoginActivity;
import com.autobots.innopark.R;
import com.autobots.innopark.data.Callbacks.StringCallback;
import com.autobots.innopark.data.DatabaseUtils;
import com.autobots.innopark.data.Tags;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class NotificationService extends FirebaseMessagingService {

    private static final String TAG = "Notification Services";

    //firebase messaging
    private static final String channel_id = Config.channel_id;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "New refreshed token: " + token);

        Config.current_user_token = token;
        Config.new_token_status = true;
    }

    public static void sendRegistrationTokenToServer(String token) {

//        if (Config.current_user_email == null) Config.current_user_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        Map<String, Object> token_data = new HashMap<>();
//        token_data.put("email_address", Config.current_user_email);
        token_data.put("email_address", "");
        token_data.put("token_id", token);
        token_data.put("notif_title", "");
        token_data.put("notif_body", "");

        DatabaseUtils.addData("users_tokens", token_data, new StringCallback(){
            @Override
            public void passStringResult(String result) {
                if(result.equals(Tags.SUCCESS.name())){
                    Log.w(Tags.SUCCESS.name(), "SUCCESSFULLY ADDED USER_TOKEN");
                }else {
                    Log.w(Tags.FAILURE.name(), "ERROR: FAILED TO ADD USER_TOKEN");
                }
            }
        });

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        displayNotification(title, body);

    }

    private void displayNotification(String title, String message){

        int notification_id = 1;

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel_id)
                .setSmallIcon(R.drawable.innopark_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Below intent will fire when the notification is tapped
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notification_id is unique
        notificationManager.notify(notification_id, builder.build());
    }
}
