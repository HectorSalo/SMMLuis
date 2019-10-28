package com.google.firebase.example.seamosmejoresmaestros;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
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

    private ArrayList<ConstructorVistaMensual> listMensual;
    private AdapterVistaMensual adapterVistaMensual;
    private Date primeroMes, ultimoMes;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_mensual);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Calendar calendarInicio = Calendar.getInstance();
        Calendar calendarFinal = Calendar.getInstance();
        calendarInicio.set(Utilidades.verAnual, Utilidades.verMes, 1);
        calendarFinal.set(Utilidades.verAnual, Utilidades.verMes, 31);
        primeroMes = calendarInicio.getTime();
        ultimoMes = calendarFinal.getTime();

        listMensual = new ArrayList<>();
        RecyclerView recyclerMensual = (RecyclerView) findViewById(R.id.recyclerMensual);
        adapterVistaMensual = new AdapterVistaMensual(listMensual, this);
        recyclerMensual.setLayoutManager(new LinearLayoutManager(this));
        recyclerMensual.setHasFixedSize(true);
        recyclerMensual.setAdapter(adapterVistaMensual);

        progress = new ProgressDialog(this);
        progress.setMessage("Cargando...");
        progress.setCancelable(false);
        progress.show();

        cargarMes();

    }

    private void cargarMes() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("sala1");

        Query query = reference.whereGreaterThanOrEqualTo(UtilidadesStatic.BD_FECHA_LUNES, primeroMes).whereLessThanOrEqualTo(UtilidadesStatic.BD_FECHA_LUNES, ultimoMes);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        ConstructorVistaMensual mensual = new ConstructorVistaMensual();
                        mensual.setFechaMensual(doc.getDate(UtilidadesStatic.BD_FECHA));
                        mensual.setLector(doc.getString(UtilidadesStatic.BD_LECTOR));
                        mensual.setEncargado1(doc.getString(UtilidadesStatic.BD_ENCARGADO1));
                        mensual.setEncargado2(doc.getString(UtilidadesStatic.BD_ENCARGADO2));
                        mensual.setEncargado3(doc.getString(UtilidadesStatic.BD_ENCARGADO3));
                        mensual.setAyudante1(doc.getString(UtilidadesStatic.BD_AYUDANTE1));
                        mensual.setAyudante2(doc.getString(UtilidadesStatic.BD_AYUDANTE2));
                        mensual.setAyudante3(doc.getString(UtilidadesStatic.BD_AYUDANTE3));
                        mensual.setAsigancion1(doc.getString(UtilidadesStatic.BD_ASIGNACION1));
                        mensual.setAsignacion2(doc.getString(UtilidadesStatic.BD_ASIGNACION2));
                        mensual.setAsignacion3(doc.getString(UtilidadesStatic.BD_ASIGNACION3));

                        listMensual.add(mensual);
                        
                    }

                    adapterVistaMensual.updateList(listMensual);
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
