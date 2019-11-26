package com.google.firebase.example.seamosmejoresmaestros;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

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
                        editor.putBoolean("sugerenciaInicial", false);
                        editor.commit();
                    } else {
                        editTextPreferenceGrupos.setText("1");
                        editor.putInt("numeroGrupos", 1);
                        editor.putBoolean("sugerenciaInicial", false);
                        editor.commit();
                        Toast.makeText(getContext(), "Dato no válido. Se configurará 1 Grupo por defecto", Toast.LENGTH_LONG).show();
                    }
                    break;

                case "notificaciones":
                    break;

                case "vibracion":
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
        startActivity(new Intent(this, MainActivity.class));
    }


}