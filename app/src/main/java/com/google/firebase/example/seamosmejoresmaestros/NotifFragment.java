package com.google.firebase.example.seamosmejoresmaestros;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import javax.annotation.Nullable;

public class NotifFragment extends PreferenceFragment {

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference_notif);
    }
}
