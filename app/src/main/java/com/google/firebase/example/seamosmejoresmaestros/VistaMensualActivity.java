package com.google.firebase.example.seamosmejoresmaestros;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.example.seamosmejoresmaestros.Adaptadores.VistaMensualAdapter;
import com.google.firebase.example.seamosmejoresmaestros.Constructores.VistaMensualConstructor;
import com.google.firebase.example.seamosmejoresmaestros.Variables.VariablesEstaticas;
import com.google.firebase.example.seamosmejoresmaestros.Variables.VariablesGenerales;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class VistaMensualActivity extends AppCompatActivity {

    private ArrayList<VistaMensualConstructor> listMensual;
    private VistaMensualAdapter vistaMensualAdapter;
    private Date primeroMes, ultimoMes;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_mensual);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Calendar calendarInicio = Calendar.getInstance();
        Calendar calendarFinal = Calendar.getInstance();
        calendarInicio.set(VariablesGenerales.verAnual, VariablesGenerales.verMes, 1);
        calendarFinal.set(VariablesGenerales.verAnual, VariablesGenerales.verMes, 31);
        primeroMes = calendarInicio.getTime();
        ultimoMes = calendarFinal.getTime();

        listMensual = new ArrayList<>();
        RecyclerView recyclerMensual = (RecyclerView) findViewById(R.id.recyclerMensual);
        vistaMensualAdapter = new VistaMensualAdapter(listMensual, this);
        recyclerMensual.setLayoutManager(new LinearLayoutManager(this));
        recyclerMensual.setHasFixedSize(true);
        recyclerMensual.setAdapter(vistaMensualAdapter);

        progress = new ProgressDialog(this);
        progress.setMessage("Cargando...");
        progress.setCancelable(false);
        progress.show();

        cargarMes();

    }

    private void cargarMes() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("sala1");

        Query query = reference.whereGreaterThanOrEqualTo(VariablesEstaticas.BD_FECHA_LUNES, primeroMes).whereLessThanOrEqualTo(VariablesEstaticas.BD_FECHA_LUNES, ultimoMes);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        VistaMensualConstructor mensual = new VistaMensualConstructor();
                        mensual.setFechaMensual(doc.getDate(VariablesEstaticas.BD_FECHA));
                        mensual.setLector(doc.getString(VariablesEstaticas.BD_LECTOR));
                        mensual.setEncargado1(doc.getString(VariablesEstaticas.BD_ENCARGADO1));
                        mensual.setEncargado2(doc.getString(VariablesEstaticas.BD_ENCARGADO2));
                        mensual.setEncargado3(doc.getString(VariablesEstaticas.BD_ENCARGADO3));
                        mensual.setAyudante1(doc.getString(VariablesEstaticas.BD_AYUDANTE1));
                        mensual.setAyudante2(doc.getString(VariablesEstaticas.BD_AYUDANTE2));
                        mensual.setAyudante3(doc.getString(VariablesEstaticas.BD_AYUDANTE3));
                        mensual.setAsigancion1(doc.getString(VariablesEstaticas.BD_ASIGNACION1));
                        mensual.setAsignacion2(doc.getString(VariablesEstaticas.BD_ASIGNACION2));
                        mensual.setAsignacion3(doc.getString(VariablesEstaticas.BD_ASIGNACION3));

                        listMensual.add(mensual);
                        
                    }

                    vistaMensualAdapter.updateList(listMensual);
                    progress.dismiss();

                    if (listMensual.isEmpty()) {
                        Snackbar.make(Objects.requireNonNull(getCurrentFocus()), "Este mes no ha sido programado", Snackbar.LENGTH_INDEFINITE).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error al cargar. Intente nuevamente", Toast.LENGTH_LONG).show();
                    progress.dismiss();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al cargar. Intente nuevamente", Toast.LENGTH_LONG).show();
                progress.dismiss();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
