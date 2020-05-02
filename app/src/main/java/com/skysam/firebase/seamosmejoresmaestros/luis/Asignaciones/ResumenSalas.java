package com.skysam.firebase.seamosmejoresmaestros.luis.Asignaciones;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.skysam.firebase.seamosmejoresmaestros.luis.R;
import com.skysam.firebase.seamosmejoresmaestros.luis.Variables.VariablesEstaticas;
import com.skysam.firebase.seamosmejoresmaestros.luis.Variables.VariablesGenerales;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ResumenSalas extends AppCompatActivity {

    private String seleccion1, seleccion2, seleccion3, idLector, idEncargado1, idAyudante1, idEncargado2, idAyudante2, idEncargado3, idAyudante3;
    private String nombreLector, nombreEncargado1, nombreAyudante1, nombreEncargado2, nombreAyudante2, nombreEncargado3, nombreAyudante3;
    private boolean visita, asamblea;
    private int semanaSelec;
    private TextView tvFecha, tvLectura, tvAsignacion1, tvAsignacion2, tvAsignacion3, titulo;
    private TextView tvLector, tvEncargado1, tvAyudante1, tvEncargado2, tvAyudante2, tvEncargado3, tvAyudante3;
    private Date fechaDate, fechaLunesDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_salas);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSalas);
        setSupportActionBar(toolbar);

        tvFecha = (TextView) findViewById(R.id.fechaSala);
        titulo = (TextView) findViewById(R.id.titleSala1);
        tvLectura = (TextView) findViewById(R.id.tvAsigLecturaSala1);
        tvAsignacion1 = (TextView) findViewById(R.id.asignacion1Sala1);
        tvAsignacion2 = (TextView) findViewById(R.id.asignacion2Sala1);
        tvAsignacion3 = (TextView) findViewById(R.id.asignacion3Sala1);
        tvLector = (TextView) findViewById(R.id.lecturaSala1);
        tvEncargado1 = (TextView) findViewById(R.id.encargado1Sala1);
        tvEncargado2 = (TextView) findViewById(R.id.encargado2Sala1);
        tvEncargado3 = (TextView) findViewById(R.id.encargado3Sala1);
        tvAyudante1 = (TextView) findViewById(R.id.ayudante1Sala1);
        tvAyudante2 = (TextView) findViewById(R.id.ayudante2Sala1);
        tvAyudante3 = (TextView) findViewById(R.id.ayudante3Sala1);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean temaOscuro = sharedPreferences.getBoolean("activarOscuro", false);
        if (temaOscuro) {

            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }

        Bundle bundleRecibir = this.getIntent().getExtras();
        idLector = bundleRecibir.getString("idLectorSala1");
        idEncargado1 = bundleRecibir.getString("idEncargado1Sala1");
        idAyudante1 = bundleRecibir.getString("idAyudante1Sala1");
        idEncargado2 = bundleRecibir.getString("idEncargado2Sala1");
        idAyudante2 = bundleRecibir.getString("idAyudante2Sala1");
        idEncargado3 = bundleRecibir.getString("idEncargado3Sala1");
        idAyudante3 = bundleRecibir.getString("idAyudante3Sala1");
        nombreLector = bundleRecibir.getString("nombreLector");
        nombreEncargado1 = bundleRecibir.getString("nombreEncargado1");
        nombreEncargado2 = bundleRecibir.getString("nombreEncargado2");
        nombreEncargado3 = bundleRecibir.getString("nombreEncargado3");
        nombreAyudante1 = bundleRecibir.getString("nombreAyudante1");
        nombreAyudante2 = bundleRecibir.getString("nombreAyudante2");
        nombreAyudante3 = bundleRecibir.getString("nombreAyudante3");
        seleccion1 = bundleRecibir.getString("asignacion1Sala1");
        seleccion2 = bundleRecibir.getString("asignacion2Sala1");
        seleccion3 = bundleRecibir.getString("asignacion3Sala1");

        visita = bundleRecibir.getBoolean("visita");
        asamblea = bundleRecibir.getBoolean("asamblea");
        long fecha = bundleRecibir.getLong("fecha");
        long fechaLunes = bundleRecibir.getLong("fechaLunes");
        semanaSelec = bundleRecibir.getInt("semana");

        fechaDate = new Date();
        fechaLunesDate = new Date();
        fechaDate.setTime(fecha);
        fechaLunesDate.setTime(fechaLunes);

        llenarSala();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_editar_pub, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_save_pub) {
            guardarSala();
            return true;
        } else if (id == R.id.menu_cancel_pub) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void llenarSala() {
        if (!asamblea) {
                    tvFecha.setText(new SimpleDateFormat("EEE d MMM yyyy").format(fechaDate));

                    if (idLector != null) {
                        tvLector.setText(nombreLector);
                    }

                    if (idEncargado1 != null) {
                        tvAsignacion1.setText(seleccion1);
                        tvEncargado1.setText(nombreEncargado1);
                    }

                    if (idAyudante1 != null) {
                        tvAyudante1.setText(nombreAyudante1);
                    }

                    if (idEncargado2 != null) {
                        tvAsignacion2.setText(seleccion2);
                        tvEncargado2.setText(nombreEncargado2);
                    }

                    if (idAyudante2 != null) {
                        tvAyudante2.setText(nombreAyudante2);
                    }

                    if (idEncargado3 != null) {
                        tvAsignacion3.setText(seleccion3);
                        tvEncargado3.setText(nombreEncargado3);
                    }

                    if (idAyudante3 != null) {
                        tvAyudante3.setText(nombreAyudante3);
                    }
        } else {
            tvFecha.setText(new SimpleDateFormat("EEE d MMM yyyy").format(fechaDate));
            titulo.setText("ASAMBLEA");
        }
    }


    public void guardarSala() {
        String semana = String.valueOf(semanaSelec);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference reference = db.collection(VariablesEstaticas.BD_SALA).document(semana);

                Map<String, Object> publicador = new HashMap<>();
                publicador.put(VariablesEstaticas.BD_IDSEMANA, semanaSelec);
                publicador.put(VariablesEstaticas.BD_FECHA, fechaDate);
                publicador.put(VariablesEstaticas.BD_FECHA_LUNES, fechaLunesDate);
                publicador.put(VariablesEstaticas.BD_LECTOR, nombreLector);
                publicador.put(VariablesEstaticas.BD_ENCARGADO1, nombreEncargado1);
                publicador.put(VariablesEstaticas.BD_AYUDANTE1, nombreAyudante1);
                publicador.put(VariablesEstaticas.BD_ENCARGADO2, nombreEncargado2);
                publicador.put(VariablesEstaticas.BD_AYUDANTE2, nombreAyudante2);
                publicador.put(VariablesEstaticas.BD_ENCARGADO3, nombreEncargado3);
                publicador.put(VariablesEstaticas.BD_AYUDANTE3, nombreAyudante3);
                publicador.put(VariablesEstaticas.BD_IDLECTOR, idLector);
                publicador.put(VariablesEstaticas.BD_IDENCARGADO1, idEncargado1);
                publicador.put(VariablesEstaticas.BD_IDAYUDANTE1, idAyudante1);
                publicador.put(VariablesEstaticas.BD_IDENCARGADO2, idEncargado2);
                publicador.put(VariablesEstaticas.BD_IDAYUDANTE2, idAyudante2);
                publicador.put(VariablesEstaticas.BD_IDENCARGADO3, idEncargado3);
                publicador.put(VariablesEstaticas.BD_IDAYUDANTE3, idAyudante3);
                publicador.put(VariablesEstaticas.BD_ASIGNACION1, seleccion1);
                publicador.put(VariablesEstaticas.BD_ASIGNACION2, seleccion2);
                publicador.put(VariablesEstaticas.BD_ASIGNACION3, seleccion3);
                publicador.put(VariablesEstaticas.BD_ASAMBLEA, asamblea);
                publicador.put(VariablesEstaticas.BD_VISITA, visita);


         reference.set(publicador).addOnSuccessListener(new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void aVoid) {

             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {

             }
         });

    }


}
