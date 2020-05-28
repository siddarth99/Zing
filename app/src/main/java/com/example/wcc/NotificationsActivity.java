package com.example.wcc;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationsActivity extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title= remoteMessage.getNotification().getTitle();
        String body=remoteMessage.getNotification().getBody();
        String click_action=remoteMessage.getNotification().getClickAction();
        String fromUserId=remoteMessage.getData().get("fromUserId");

        Intent intent = new Intent(click_action);
        intent.putExtra("user_id",fromUserId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"Freind_Req")
                .setSmallIcon(R.drawable.ic_person_24px)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(pendingIntent);


        int Notification_ID=(int)System.currentTimeMillis();
        NotificationManagerCompat notificationManager=NotificationManagerCompat.from(this);
        notificationManager.notify(Notification_ID,builder.build());

    }
}
