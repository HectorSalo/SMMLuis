package com.skysam.firebase.seamosmejoresmaestros.luis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.regex.Pattern;

public class ConfiguracionesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean temaOscuro = sharedPreferences.getBoolean("activarOscuro", false);
        if (temaOscuro) {

            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            EditTextPreference editTextPreferenceGrupos = findPreference("numero_grupos");
            int numeroGrupos = sharedPreferences.getInt("numeroGrupos", 1);
            editTextPreferenceGrupos.setText(String.valueOf(numeroGrupos));
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = sharedPreferences.edit();
            SwitchPreferenceCompat switchPreferenceCompatNotif = findPreference("notificaciones");
            SwitchPreferenceCompat switchPreferenceCompatVibrar = findPreference("vibracion");
            EditTextPreference editTextPreferenceNombre = findPreference("nombre_perfil");
            EditTextPreference editTextPreferenceGrupos = findPreference("numero_grupos");


            switch (key) {
                case "nombre_perfil":
                    String nombrePerfil = editTextPreferenceNombre.getText();
                    editor.putString("nombrePerfil", nombrePerfil);
                    editor.commit();
                    break;

                case "numero_grupos":
                    String datos = editTextPreferenceGrupos.getText();
                    boolean isNumber = Pattern.matches("[0-9]+", datos);
                    if (isNumber) {
                        int numeroGrupos = Integer.parseInt(editTextPreferenceGrupos.getText());
                        editor.putInt("numeroGrupos", numeroGrupos);
                        editor.commit();
                    } else {
                        editTextPreferenceGrupos.setText("1");
                        editor.putInt("numeroGrupos", 1);
                        editor.commit();
                        Toast.makeText(getContext(), "Dato no válido. Se configurará 1 Grupo por defecto", Toast.LENGTH_LONG).show();
                    }
                    break;

                case "notificaciones":
                    boolean activarNotif = sharedPreferences.getBoolean("activarNotif", true);
                    if (activarNotif) {
                        switchPreferenceCompatNotif.setIcon(R.drawable.ic_conf_notif_off);
                        editor.putBoolean("activarNotif", false);
                        editor.commit();
                        anularFCM();
                    } else {
                        switchPreferenceCompatNotif.setIcon(R.drawable.ic_conf_notif_on);
                        editor.putBoolean("activarNotif", true);
                        editor.commit();
                        suscribirseFCM();
                    }
                    break;

                case "vibracion":
                    boolean activarVibrar = sharedPreferences.getBoolean("activarVibrar", false);
                    if (activarVibrar) {
                        switchPreferenceCompatVibrar.setIcon(R.drawable.ic_conf_vibrar_off);
                        editor.putBoolean("activarVibrar", false);
                        editor.commit();
                    } else {
                        switchPreferenceCompatVibrar.setIcon(R.drawable.ic_conf_vibrar_on);
                        editor.putBoolean("activarVibrar", true);
                        editor.commit();
                    }
                    break;

                case "tema":
                    boolean temaOscuro = sharedPreferences.getBoolean("activarOscuro", false);
                    if (temaOscuro) {
                        editor.putBoolean("activarOscuro", false);
                        editor.commit();
                        getActivity().recreate();
                    } else {
                        editor.putBoolean("activarOscuro", true);
                        editor.commit();
                        getActivity().recreate();
                    }
                    break;

            }
        }


        private void suscribirseFCM() {
            FirebaseMessaging.getInstance().subscribeToTopic("notif")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = "Subscripcion exitosa";
                            if (!task.isSuccessful()) {
                                msg = "Failed";
                            }
                            Log.d("suscrito", msg);

                        }
                    });
        }

        private void anularFCM() {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("notif").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    String msg = "Anulacion exitosa";
                    if (!task.isSuccessful()) {
                        msg = "Failed anulacion";
                    }
                    Log.d("anulado", msg);

                }
            });
        }


        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }


}