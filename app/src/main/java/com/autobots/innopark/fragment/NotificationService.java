package com.autobots.innopark.fragment;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.autobots.innopark.util.Config;
import com.autobots.innopark.R;
import com.autobots.innopark.data.Callbacks.StringCallback;
import com.autobots.innopark.util.DatabaseUtils;
import com.autobots.innopark.data.models.Tags;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
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

    public static void sendRegistrationTokenToServer(String token, String email) {
//        if (Config.current_user_email == null) Config.current_user_email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DatabaseUtils.db.collection("users_tokens")
                .whereEqualTo("email_address", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().isEmpty()){
                                Log.d(Tags.FAILURE.name(), "Email address doesn't exist. Adding new user_token");

                                Map<String, Object> token_data = new HashMap<>();

                                Map<String, Object> notif_data = new HashMap<>();

                                notif_data.put("notif_title", "");
                                notif_data.put("notif_body", "");
                                notif_data.put("notif_datetime", "");

                                ArrayList<Map<String, Object>> notif_array = new ArrayList<>();

                                token_data.put("email_address", email);
                                token_data.put("token_id", token);
                                token_data.put("notif", notif_array);

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
                            }else{
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(Tags.SUCCESS.name(), "Email address exists. Updating token");
                                    Log.d(Tags.SUCCESS.name(), document.getId() + " => " + document.getData());

                                    DatabaseUtils.updateData("users_tokens",
                                            document.getId(), "token_id", token);
                                }
                            }
                        } else {
                            Log.d(Tags.FAILURE.name(), "Failed to complete task. Email address field may not exist.");
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

        Intent intent = new Intent(this, UnpaidTariffFragment.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channel_id)
                .setSmallIcon(R.drawable.innopark_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                // Below intent will fire when the notification is tapped
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notification_id is unique
        notificationManager.notify(notification_id, builder.build());
    }
}
