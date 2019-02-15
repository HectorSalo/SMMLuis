package com.google.firebase.example.seamosmejoresmaestros;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;


import javax.annotation.Nullable;

public class NotifFragment extends PreferenceFragment {


    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference_notif);

        Preference swtich = (Preference) findPreference("notifications_new_message");


        swtich.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                boolean isEnable = (boolean) newValue;

                if (isEnable) {
                    SharedPreferences preferences = getActivity().getSharedPreferences("notificacion", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("activar", true);
                    editor.commit();
                } else {
                    SharedPreferences preferences = getActivity().getSharedPreferences("notificacion", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("activar", false);
                    editor.commit();
                }
                return true;
            }
        });
    }
}
