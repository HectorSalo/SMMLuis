package com.google.firebase.example.seamosmejoresmaestros;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ResumenSalas extends AppCompatActivity {

    private String seleccion1Sala1, seleccion2Sala1, seleccion3Sala1, idLectorSala1, idEncargado1Sala1, idAyudante1Sala1, idEncargado2Sala1, idAyudante2Sala1, idEncargado3Sala1, idAyudante3Sala1;
    private String seleccion1Sala2, seleccion2Sala2, seleccion3Sala2, idLectorSala2, idEncargado1Sala2, idAyudante1Sala2, idEncargado2Sala2, idAyudante2Sala2, idEncargado3Sala2, idAyudante3Sala2;
    private long fecha;
    private boolean visita, asamblea, activarSala2;
    private int semanaSelec;
    private TextView tvFecha, tvLecturaSala1, tvLecturaSala2, tvAsignacion1Sala1, tvAsignacion2Sala1, tvAsignacion3Sala1, tvAsignacion1Sala2, tvAsignacion2Sala2, tvAsignacion3Sala2, tituloSala1, tituloSala2;
    private TextView tvLectorSala1, tvEncargado1Sala1, tvAyudante1Sala1, tvEncargado2Sala1, tvAyudante2Sala1, tvEncargado3Sala1, tvAyudante3Sala1;
    private TextView tvLectorSala2, tvEncargado1Sala2, tvAyudante1Sala2, tvEncargado2Sala2, tvAyudante2Sala2, tvEncargado3Sala2, tvAyudante3Sala2;
    private Date fechaDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_salas);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSalas);
        setSupportActionBar(toolbar);

        tvFecha = (TextView) findViewById(R.id.fechaSala);
        tituloSala1 = (TextView) findViewById(R.id.titleSala1);
        tituloSala2 = (TextView) findViewById(R.id.titleSala2);
        tvLecturaSala1 = (TextView) findViewById(R.id.tvAsigLecturaSala1);
        tvLecturaSala2 = (TextView) findViewById(R.id.tvAsigLecturaSala2);
        tvAsignacion1Sala1 = (TextView) findViewById(R.id.asignacion1Sala1);
        tvAsignacion2Sala1 = (TextView) findViewById(R.id.asignacion2Sala1);
        tvAsignacion3Sala1 = (TextView) findViewById(R.id.asignacion3Sala1);
        tvAsignacion1Sala2 = (TextView) findViewById(R.id.asignacion1Sala2);
        tvAsignacion2Sala2 = (TextView) findViewById(R.id.asignacion2Sala2);
        tvAsignacion3Sala2 = (TextView) findViewById(R.id.asignacion3Sala2);
        tvLectorSala1 = (TextView) findViewById(R.id.lecturaSala1);
        tvEncargado1Sala1 = (TextView) findViewById(R.id.encargado1Sala1);
        tvEncargado2Sala1 = (TextView) findViewById(R.id.encargado2Sala1);
        tvEncargado3Sala1 = (TextView) findViewById(R.id.encargado3Sala1);
        tvAyudante1Sala1 = (TextView) findViewById(R.id.ayudante1Sala1);
        tvAyudante2Sala1 = (TextView) findViewById(R.id.ayudante2Sala1);
        tvAyudante3Sala1 = (TextView) findViewById(R.id.ayudante3Sala1);
        tvLectorSala2 = (TextView) findViewById(R.id.lecturaSala2);
        tvEncargado1Sala2 = (TextView) findViewById(R.id.encargado1Sala2);
        tvEncargado2Sala2 = (TextView) findViewById(R.id.encargado2Sala2);
        tvEncargado3Sala2 = (TextView) findViewById(R.id.encargado3Sala2);
        tvAyudante1Sala2 = (TextView) findViewById(R.id.ayudante1Sala2);
        tvAyudante2Sala2 = (TextView) findViewById(R.id.ayudante2Sala2);
        tvAyudante3Sala2 = (TextView) findViewById(R.id.ayudante3Sala2);

        Bundle bundleRecibir = this.getIntent().getExtras();
        idLectorSala1 = bundleRecibir.getString("idLectorSala1");
        idEncargado1Sala1 = bundleRecibir.getString("idEncargado1Sala1");
        idAyudante1Sala1 = bundleRecibir.getString("idAyudante1Sala1");
        idEncargado2Sala1 = bundleRecibir.getString("idEncargado2Sala1");
        idAyudante2Sala1 = bundleRecibir.getString("idAyudante2Sala1");
        idEncargado3Sala1 = bundleRecibir.getString("idEncargado3Sala1");
        idAyudante3Sala1 = bundleRecibir.getString("idAyudante3Sala1");
        seleccion1Sala1 = bundleRecibir.getString("asignacion1Sala1");
        seleccion2Sala1 = bundleRecibir.getString("asignacion2Sala1");
        seleccion3Sala1 = bundleRecibir.getString("asignacion3Sala1");

        idLectorSala2 = bundleRecibir.getString("idLectorSala2");
        idEncargado1Sala2 = bundleRecibir.getString("idEncargado1Sala2");
        idAyudante1Sala2 = bundleRecibir.getString("idAyudante1Sala2");
        idEncargado2Sala2 = bundleRecibir.getString("idEncargado2Sala2");
        idAyudante2Sala2 = bundleRecibir.getString("idAyudante2Sala2");
        idEncargado3Sala2 = bundleRecibir.getString("idEncargado3Sala2");
        idAyudante3Sala2 = bundleRecibir.getString("idAyudante3Sala2");
        seleccion1Sala2 = bundleRecibir.getString("asignacion1Sala2");
        seleccion2Sala2 = bundleRecibir.getString("asignacion2Sala2");
        seleccion3Sala2 = bundleRecibir.getString("asignacion3Sala2");

        visita = bundleRecibir.getBoolean("visita");
        asamblea = bundleRecibir.getBoolean("asamblea");
        activarSala2 = bundleRecibir.getBoolean("activarSala2");
        fecha = bundleRecibir.getLong("fecha");
        semanaSelec = bundleRecibir.getInt("semana");

        fechaDate = new Date();
        fechaDate.setTime(fecha);


        llenarSalas();

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
            finish();
            return true;
        } else if (id == R.id.menu_cancel_pub) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void llenarSalas() {
        if (!asamblea) {
            if (!visita) {

            } else {
                if (activarSala2) {

                } else {
                    tvFecha.setText(new SimpleDateFormat("EEE d MMM yyyy").format(fechaDate));

                    if (idLectorSala1 != null) {
                        String info = cargarPublicador(idLectorSala1);
                        tvLectorSala1.setText(info);
                    } else {
                        tvLecturaSala1.setText("");
                        tvLectorSala1.setText("");
                    }

                    if (idEncargado1Sala1 != null) {
                        String info = cargarPublicador(idEncargado1Sala1);
                        tvAsignacion1Sala1.setText(seleccion1Sala1);
                        tvEncargado1Sala1.setText(info);
                    } else {
                        tvAsignacion1Sala1.setText("");
                        tvEncargado1Sala2.setText("");
                    }
                    tituloSala2.setText("Visita");
                    tvLecturaSala2.setText("");
                    tvAsignacion1Sala2.setText("");
                    tvAsignacion2Sala2.setText("");
                    tvAsignacion3Sala2.setText("");
                    tvLectorSala2.setText("");
                    tvEncargado1Sala2.setText("");
                    tvEncargado2Sala2.setText("");
                    tvEncargado3Sala2.setText("");
                    tvAyudante1Sala2.setText("");
                    tvAyudante2Sala2.setText("");
                    tvAyudante3Sala2.setText("");
                }
            }
        } else {
            tvFecha.setText(new SimpleDateFormat("EEE d MMM yyyy").format(fechaDate));
            tituloSala1.setText("ASAMBLEA");
            tvAsignacion1Sala1.setText("");
            tvAsignacion2Sala1.setText("");
            tvAsignacion3Sala1.setText("");
            tvLecturaSala1.setText("");
            tvLecturaSala2.setText("");
            tvLectorSala1.setText("");
            tvEncargado1Sala1.setText("");
            tvEncargado2Sala1.setText("");
            tvEncargado3Sala1.setText("");
            tvAyudante1Sala1.setText("");
            tvAyudante2Sala1.setText("");
            tvAyudante3Sala1.setText("");
            tituloSala2.setText("ASAMBLEA");
            tvAsignacion1Sala2.setText("");
            tvAsignacion2Sala2.setText("");
            tvAsignacion3Sala2.setText("");
            tvLectorSala2.setText("");
            tvEncargado1Sala2.setText("");
            tvEncargado2Sala2.setText("");
            tvEncargado3Sala2.setText("");
            tvAyudante1Sala2.setText("");
            tvAyudante2Sala2.setText("");
            tvAyudante3Sala2.setText("");


        }
    }

    public String cargarPublicador(String id) {
        final String[] nombreCompleto = new String[1];
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference reference = dbFirestore.collection("publicadores");

        reference.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    String nombre = doc.getString(UtilidadesStatic.BD_NOMBRE);
                    String apellido = doc.getString(UtilidadesStatic.BD_APELLIDO);
                    nombreCompleto[0] = nombre + " " + apellido;

                } else {
                    Toast.makeText(getApplicationContext(), "Error al cargar publicador", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Toast.makeText(getApplicationContext(), "Prueba" + nombreCompleto[0], Toast.LENGTH_SHORT).show();
        return nombreCompleto[0];
    }

}
