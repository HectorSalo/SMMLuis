package com.google.firebase.example.seamosmejoresmaestros;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificacionesRecibir extends BroadcastReceiver {

    private PendingIntent pendingIntent;
    private final static String CHANNEL_ID = "NOTIFICACION";

    @Override
    public void onReceive(Context context, Intent intent) {
        setPendingIntent(context);
        createNotfChannel(context);
        enviarNotf(context);
    }

    private void enviarNotf(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_notif);
        builder.setContentTitle("¡Inicia la semana!");
        builder.setContentText("Echa un vitazo a los discursantes de la semana");
        builder.setColor(Color.GREEN);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(UtilidadesStatic.NOTIFICACION_ID, builder.build());
    }

    private void createNotfChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notificacion";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void setPendingIntent(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
