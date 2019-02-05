package com.google.firebase.example.seamosmejoresmaestros;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SustituirActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerSustituciones;
    private ArrayList<ConstructorPublicadores> listSelecSust;
    private AdapterEditSalas adapterSust;
    private Spinner spinnerEncargados;
    private EditText etBuscar;
    private Integer semana, idSala;
    private Date fechaRecibir;
    private String spinnerSeleccion, lector1, encargado1Sala1, ayudante1Sala1, encargado2Sala1, ayudante2Sala1, encargado3Sala1, ayudante3Sala1, lector2, encargado1Sala2, ayudante1Sala2, encargado2Sala2, ayudante2Sala2, encargado3Sala2, ayudante3Sala2;
    private ArrayList<String> listEncargados;
    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sustituir);

        recyclerSustituciones = (RecyclerView) findViewById(R.id.recyclerSustituciones);
        LinearLayoutManager llM = new LinearLayoutManager(this);
        recyclerSustituciones.setLayoutManager(llM);
        recyclerSustituciones.setHasFixedSize(true);
        listSelecSust = new ArrayList<>();
        spinnerEncargados = (Spinner)findViewById(R.id.spinnerEncargados);

        Bundle bundleRecibir = this.getIntent().getExtras();
        idSala = bundleRecibir.getInt("sala");
        semana = bundleRecibir.getInt("semana");
        Long fecha = bundleRecibir.getLong("fecha");

        fechaRecibir = new Date();
        fechaRecibir.setTime(fecha);

        if (idSala == 1) {
            cargarEncargadosSala1(semana);
        } else if (idSala == 2) {
            //cargarEncargadosSala2(semana);
        }

        cargarSustitutos();


        spinnerEncargados.setOnItemSelectedListener(this);

        FloatingActionButton fabClose = (FloatingActionButton) findViewById(R.id.fabCloseedit);
        fabClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerSeleccion = spinnerEncargados.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void cargarEncargadosSala1(int i) {
        String idSemana = String.valueOf(i);
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference reference = dbFirestore.collection("sala1");

        reference.document(idSemana).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();

                    listEncargados = new ArrayList<>();
                    listEncargados.add("Publicador a cambiar");
                    if (doc.getString(UtilidadesStatic.BD_LECTOR) != null) {
                        listEncargados.add(doc.getString(UtilidadesStatic.BD_LECTOR));
                    }
                    if (doc.getString(UtilidadesStatic.BD_ENCARGADO1) != null) {
                        listEncargados.add(doc.getString(UtilidadesStatic.BD_ENCARGADO1));
                    }
                    if (doc.getString(UtilidadesStatic.BD_AYUDANTE1) != null) {
                        listEncargados.add(doc.getString(UtilidadesStatic.BD_AYUDANTE1));
                    }
                    if (doc.getString(UtilidadesStatic.BD_ENCARGADO2) != null) {
                        listEncargados.add(doc.getString(UtilidadesStatic.BD_ENCARGADO2));
                    }
                    if (doc.getString(UtilidadesStatic.BD_AYUDANTE2) != null) {
                        listEncargados.add(doc.getString(UtilidadesStatic.BD_AYUDANTE2));
                    }
                    if (doc.getString(UtilidadesStatic.BD_ENCARGADO3) != null) {
                        listEncargados.add(doc.getString(UtilidadesStatic.BD_ENCARGADO3));
                    }
                    if (doc.getString(UtilidadesStatic.BD_AYUDANTE3) != null) {
                        listEncargados.add(doc.getString(UtilidadesStatic.BD_AYUDANTE3));
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_selec, listEncargados);
                    spinnerEncargados.setAdapter(adapter);

                }
            }
        });
    }

    public void cargarSustitutos() {
        progress = new ProgressDialog(SustituirActivity.this);
        progress.setMessage("Cargando...");
        progress.setCancelable(false);
        progress.show();
        listSelecSust = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");

        Query query = reference.whereEqualTo(UtilidadesStatic.BD_HABILITADO, true).orderBy(UtilidadesStatic.BD_SUSTRECIENTE, Query.Direction.ASCENDING);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        ConstructorPublicadores publi = new ConstructorPublicadores();
                        publi.setIdPublicador(doc.getId());
                        publi.setNombrePublicador(doc.getString(UtilidadesStatic.BD_NOMBRE));
                        publi.setApellidoPublicador(doc.getString(UtilidadesStatic.BD_APELLIDO));
                        publi.setCorreo(doc.getString(UtilidadesStatic.BD_CORREO));
                        publi.setTelefono(doc.getString(UtilidadesStatic.BD_TELEFONO));
                        publi.setGenero(doc.getString(UtilidadesStatic.BD_GENERO));
                        publi.setImagen(doc.getString(UtilidadesStatic.BD_IMAGEN));
                        publi.setUltAsignacion(doc.getDate(UtilidadesStatic.BD_DISRECIENTE));
                        publi.setUltAyudante(doc.getDate(UtilidadesStatic.BD_AYURECIENTE));
                        publi.setUltSustitucion(doc.getDate(UtilidadesStatic.BD_SUSTRECIENTE));

                        listSelecSust.add(publi);

                    }
                    adapterSust = new AdapterEditSalas(listSelecSust, getApplicationContext());
                    adapterSust.updateListSelec(listSelecSust);
                    
                    recyclerSustituciones.setAdapter(adapterSust);
                    progress.dismiss();
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
