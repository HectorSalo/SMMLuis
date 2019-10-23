package com.google.firebase.example.seamosmejoresmaestros;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class VistaMensualActivity extends AppCompatActivity {

    private RecyclerView recyclerMensual;
    private ArrayList<ConstructorVistaMensual> listMensual;
    private AdapterVistaMensual adapterVistaMensual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_mensual);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //String fecha = Utilidades.verMes + ", " + Utilidades.verAnual;
        listMensual = new ArrayList<>();
        recyclerMensual = (RecyclerView) findViewById(R.id.recyclerMensual);
        adapterVistaMensual = new AdapterVistaMensual(listMensual, this);
        recyclerMensual.setLayoutManager(new LinearLayoutManager(this));
        recyclerMensual.setHasFixedSize(true);
        recyclerMensual.setAdapter(adapterVistaMensual);


        cargarMes();

    }

    private void cargarMes() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("sala1");

        Query query = reference.whereGreaterThanOrEqualTo(UtilidadesStatic.BD_IDSEMANA, 49);
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

                        listMensual.add(mensual);
                        
                    }

                    adapterVistaMensual.updateList(listMensual);

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
