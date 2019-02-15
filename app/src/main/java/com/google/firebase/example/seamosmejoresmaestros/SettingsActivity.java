package com.google.firebase.example.seamosmejoresmaestros;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        lista = (ListView) findViewById(R.id.listAjustes);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.ajustes, android.R.layout.simple_list_item_1);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String seleccion = parent.getItemAtPosition(position).toString();

                if (seleccion.equals("Mi Perfil")) {
                    Intent myIntent = new Intent(getApplicationContext(), MiPerfilActivity.class);
                    Bundle myBundle = new Bundle();
                    myBundle.putInt("ir", 0);
                    myIntent.putExtras(myBundle);
                    startActivity(myIntent);

                } else if (seleccion.equals("Notificaciones")) {
                    Intent myInten = new Intent(getApplicationContext(), NotificacionesActivity.class);
                    startActivity(myInten);
                }
            }
        });
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

}
