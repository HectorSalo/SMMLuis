package com.google.firebase.example.seamosmejoresmaestros;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class NotificacionFirebase extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        SharedPreferences preferences = getSharedPreferences("notificacion", Context.MODE_PRIVATE);
        boolean  permitir = preferences.getBoolean("activar", true);

        if (permitir) {
            if (remoteMessage.getNotification() != null) {
                Log.d("Contenido", remoteMessage.getNotification().getBody());
                mostrarNotif(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            }
        } else {
            Log.d("Notificacion", "Desactivada");
        }
    }

    private void mostrarNotif(String title, String body) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "MaestrosNotif";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("VMC channel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID);

        builder.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_notif)
                .setContentTitle(title)
                .setContentText(body);

        notificationManager.notify(new Random().nextInt(), builder.build());
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.d("Token", s);
    }
}
