package com.google.firebase.example.seamosmejoresmaestros;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.Objects;

public class VistaMensualActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_mensual);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        String fecha = Utilidades.verMes + ", " + Utilidades.verAnual;

        TextView textView = (TextView) findViewById(R.id.tvFechaMensual);
        textView.setText(fecha);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
