package com.google.firebase.example.seamosmejoresmaestros;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class NotificacionFirebase extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        SharedPreferences preferences = getSharedPreferences("notificacion", Context.MODE_PRIVATE);
        boolean  permitir = preferences.getBoolean("activar", true);

        if (permitir) {
            if (remoteMessage.getNotification() != null) {
                Log.d("Contenido", remoteMessage.getNotification().getBody());
            }
        } else {
            Log.d("Notificacion", "Desactivada");
        }
    }
}
