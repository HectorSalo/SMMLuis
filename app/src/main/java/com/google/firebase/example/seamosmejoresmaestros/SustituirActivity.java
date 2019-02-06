package com.google.firebase.example.seamosmejoresmaestros;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class SustituirActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, SearchView.OnQueryTextListener {

    private RecyclerView recyclerSustituciones;
    private ArrayList<ConstructorPublicadores> listSelecSust;
    private AdapterEditSalas adapterSust;
    private Spinner spinnerEncargados;
    private Integer semana, idSala;
    private Date fechaRecibir, fechaRecienteSust;
    private String sustSeleccionado, nombreSustSelec, apellidoSustSelec, sustSeleccionadoId, idEncargado;
    private String spinnerSeleccion;
    private ArrayList<String> listEncargados;
    private ProgressDialog progress;
    private boolean encargado, ayudante;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sustituir);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        fechaRecienteSust = new Date();
        fechaRecibir = new Date();
        fechaRecibir.setTime(fecha);

        if (idSala == 1) {
            cargarEncargadosSala1(semana);
        } else if (idSala == 2) {
            cargarEncargadosSala2(semana);
        }

        encargado = false;
        ayudante = false;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editar_sust, menu);
        MenuItem menuItem = menu.findItem(R.id.action_buscar);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_buscar) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                        Utilidades.lectorSust = doc.getString(UtilidadesStatic.BD_LECTOR);
                        Utilidades.idlectorSust = doc.getString(UtilidadesStatic.BD_IDLECTOR);
                    }
                    if (doc.getString(UtilidadesStatic.BD_ENCARGADO1) != null) {
                        listEncargados.add(doc.getString(UtilidadesStatic.BD_ENCARGADO1));
                        Utilidades.encargado1Sust = doc.getString(UtilidadesStatic.BD_ENCARGADO1);
                        Utilidades.idencargado1Sust = doc.getString(UtilidadesStatic.BD_IDENCARGADO1);
                    }
                    if (doc.getString(UtilidadesStatic.BD_AYUDANTE1) != null) {
                        listEncargados.add(doc.getString(UtilidadesStatic.BD_AYUDANTE1));
                        Utilidades.ayudante1Sust = doc.getString(UtilidadesStatic.BD_AYUDANTE1);
                        Utilidades.idayudante1Sust = doc.getString(UtilidadesStatic.BD_IDAYUDANTE1);
                    }
                    if (doc.getString(UtilidadesStatic.BD_ENCARGADO2) != null) {
                        listEncargados.add(doc.getString(UtilidadesStatic.BD_ENCARGADO2));
                        Utilidades.encargado2Sust = doc.getString(UtilidadesStatic.BD_ENCARGADO2);
                        Utilidades.idencargado2Sust = doc.getString(UtilidadesStatic.BD_IDENCARGADO2);
                    }
                    if (doc.getString(UtilidadesStatic.BD_AYUDANTE2) != null) {
                        listEncargados.add(doc.getString(UtilidadesStatic.BD_AYUDANTE2));
                        Utilidades.ayudante2Sust = doc.getString(UtilidadesStatic.BD_AYUDANTE2);
                        Utilidades.idayudante2Sust = doc.getString(UtilidadesStatic.BD_IDAYUDANTE2);
                    }
                    if (doc.getString(UtilidadesStatic.BD_ENCARGADO3) != null) {
                        listEncargados.add(doc.getString(UtilidadesStatic.BD_ENCARGADO3));
                        Utilidades.encargado3Sust = doc.getString(UtilidadesStatic.BD_ENCARGADO3);
                        Utilidades.idencargado3Sust = doc.getString(UtilidadesStatic.BD_IDENCARGADO3);
                    }
                    if (doc.getString(UtilidadesStatic.BD_AYUDANTE3) != null) {
                        listEncargados.add(doc.getString(UtilidadesStatic.BD_AYUDANTE3));
                        Utilidades.ayudante3Sust = doc.getString(UtilidadesStatic.BD_AYUDANTE3);
                        Utilidades.idayudante3Sust = doc.getString(UtilidadesStatic.BD_IDAYUDANTE3);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_selec, listEncargados);
                    spinnerEncargados.setAdapter(adapter);

                }
            }
        });
    }

    public void cargarEncargadosSala2(int i) {
        String idSemana = String.valueOf(i);
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference reference = dbFirestore.collection("sala2");

        reference.document(idSemana).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();

                    listEncargados = new ArrayList<>();
                    listEncargados.add("Publicador a cambiar");
                    if (doc.getString(UtilidadesStatic.BD_LECTOR) != null) {
                        listEncargados.add(doc.getString(UtilidadesStatic.BD_LECTOR));
                        Utilidades.lectorSust = doc.getString(UtilidadesStatic.BD_LECTOR);
                        Utilidades.idlectorSust = doc.getString(UtilidadesStatic.BD_IDLECTOR);
                    }
                    if (doc.getString(UtilidadesStatic.BD_ENCARGADO1) != null) {
                        listEncargados.add(doc.getString(UtilidadesStatic.BD_ENCARGADO1));
                        Utilidades.encargado1Sust = doc.getString(UtilidadesStatic.BD_ENCARGADO1);
                        Utilidades.idencargado1Sust = doc.getString(UtilidadesStatic.BD_IDENCARGADO1);
                    }
                    if (doc.getString(UtilidadesStatic.BD_AYUDANTE1) != null) {
                        listEncargados.add(doc.getString(UtilidadesStatic.BD_AYUDANTE1));
                        Utilidades.ayudante1Sust = doc.getString(UtilidadesStatic.BD_AYUDANTE1);
                        Utilidades.idayudante1Sust = doc.getString(UtilidadesStatic.BD_IDAYUDANTE1);
                    }
                    if (doc.getString(UtilidadesStatic.BD_ENCARGADO2) != null) {
                        listEncargados.add(doc.getString(UtilidadesStatic.BD_ENCARGADO2));
                        Utilidades.encargado2Sust = doc.getString(UtilidadesStatic.BD_ENCARGADO2);
                        Utilidades.idencargado2Sust = doc.getString(UtilidadesStatic.BD_IDENCARGADO2);
                    }
                    if (doc.getString(UtilidadesStatic.BD_AYUDANTE2) != null) {
                        listEncargados.add(doc.getString(UtilidadesStatic.BD_AYUDANTE2));
                        Utilidades.ayudante2Sust = doc.getString(UtilidadesStatic.BD_AYUDANTE2);
                        Utilidades.idayudante2Sust = doc.getString(UtilidadesStatic.BD_IDAYUDANTE2);
                    }
                    if (doc.getString(UtilidadesStatic.BD_ENCARGADO3) != null) {
                        listEncargados.add(doc.getString(UtilidadesStatic.BD_ENCARGADO3));
                        Utilidades.encargado3Sust = doc.getString(UtilidadesStatic.BD_ENCARGADO3);
                        Utilidades.idencargado3Sust = doc.getString(UtilidadesStatic.BD_IDENCARGADO3);
                    }
                    if (doc.getString(UtilidadesStatic.BD_AYUDANTE3) != null) {
                        listEncargados.add(doc.getString(UtilidadesStatic.BD_AYUDANTE3));
                        Utilidades.ayudante3Sust = doc.getString(UtilidadesStatic.BD_AYUDANTE3);
                        Utilidades.idayudante3Sust = doc.getString(UtilidadesStatic.BD_IDAYUDANTE3);
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
        adapterSust = new AdapterEditSalas(listSelecSust, getApplicationContext());
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

                    adapterSust.updateListSelec(listSelecSust);

                    recyclerSustituciones.setAdapter(adapterSust);
                    progress.dismiss();
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        adapterSust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombreSustSelec = listSelecSust.get(recyclerSustituciones.getChildAdapterPosition(v)).getNombrePublicador();
                apellidoSustSelec = listSelecSust.get(recyclerSustituciones.getChildAdapterPosition(v)).getApellidoPublicador();
                fechaRecienteSust = listSelecSust.get(recyclerSustituciones.getChildAdapterPosition(v)).getUltSustitucion();
                sustSeleccionadoId = listSelecSust.get(recyclerSustituciones.getChildAdapterPosition(v)).getIdPublicador();
                sustSeleccionado =  nombreSustSelec + " " + apellidoSustSelec;
                if (!spinnerSeleccion.equals("Publicador a cambiar")) {
                    if (!spinnerSeleccion.equals(sustSeleccionado)) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(SustituirActivity.this);
                        dialog.setTitle("Confirmar");
                        dialog.setMessage("¿Desea sustituir a " + spinnerSeleccion + " por " + sustSeleccionado + "?");
                        dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                hacerSust();
                                actFechaSust();
                                actFechaViejaSust();
                                buscarEncAyu();

                            }
                        });
                        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    } else {
                        Toast.makeText(getApplicationContext(), "No puede sustituir por la misma persona", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Debe escoger a quién va a sustituir", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void hacerSust() {
        String id = "";
        String idSemana = String.valueOf(semana);
        String asignacion = "";

        if (idSala == 1) {
            id = "sala1";
        } else if (idSala == 2) {
            id = "sala2";
        }

        if (spinnerSeleccion.equals(Utilidades.lectorSust)){
            asignacion = UtilidadesStatic.BD_LECTOR;
            idEncargado = Utilidades.idlectorSust;
            encargado = true;
            ayudante = false;
        } else if (spinnerSeleccion.equals(Utilidades.encargado1Sust)) {
            asignacion = UtilidadesStatic.BD_ENCARGADO1;
            idEncargado = Utilidades.idencargado1Sust;
            encargado = true;
            ayudante = false;
        } else if (spinnerSeleccion.equals(Utilidades.ayudante1Sust)) {
            asignacion = UtilidadesStatic.BD_AYUDANTE1;
            idEncargado = Utilidades.idayudante1Sust;
            ayudante = true;
            encargado = false;
        } else if (spinnerSeleccion.equals(Utilidades.encargado2Sust)) {
            asignacion = UtilidadesStatic.BD_ENCARGADO2;
            idEncargado = Utilidades.idencargado2Sust;
            encargado = true;
            ayudante = false;
        } else if (spinnerSeleccion.equals(Utilidades.ayudante2Sust)) {
            asignacion = UtilidadesStatic.BD_AYUDANTE2;
            idEncargado = Utilidades.idayudante2Sust;
            ayudante = true;
            encargado = false;
        } else if (spinnerSeleccion.equals(Utilidades.encargado3Sust)) {
            asignacion = UtilidadesStatic.BD_ENCARGADO3;
            idEncargado = Utilidades.idencargado3Sust;
            encargado = true;
            ayudante = false;
        } else if (spinnerSeleccion.equals(Utilidades.ayudante3Sust)) {
            asignacion = UtilidadesStatic.BD_AYUDANTE3;
            idEncargado = Utilidades.idayudante3Sust;
            ayudante = true;
            encargado = false;
        }
        FirebaseFirestore dbEditar = FirebaseFirestore.getInstance();

        dbEditar.collection(id).document(idSemana).update(asignacion, sustSeleccionado).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "Error al guardar. Intente nuevamente", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void actFechaSust () {
        FirebaseFirestore dbEditar = FirebaseFirestore.getInstance();

        dbEditar.collection("publicadores").document(sustSeleccionadoId).update(UtilidadesStatic.BD_SUSTRECIENTE, fechaRecibir).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "Error al guardar. Intente nuevamente", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void actFechaViejaSust() {
        FirebaseFirestore dbEditar = FirebaseFirestore.getInstance();

        dbEditar.collection("publicadores").document(sustSeleccionadoId).update(UtilidadesStatic.BD_SUSTVIEJO, fechaRecienteSust).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "Error al guardar. Intente nuevamente", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void buscarEncAyu () {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");


        reference.document(idEncargado).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        Utilidades.idPubCambiado = doc.getId();
                        Utilidades.fechaPubCambiado = doc.getDate(UtilidadesStatic.BD_DISVIEJO);

                        if (encargado) {
                            actFechaEnc();
                        }  else if (ayudante) {
                            actFechaAyu();
                        }

                        Intent myIntent = new Intent(getApplicationContext(), AsignacionesActivity.class);
                        startActivity(myIntent);

                }
            }
        });
    }

    public void actFechaEnc() {
        String idPub = Utilidades.idPubCambiado;
        Date fecha = new Date();
        fecha = Utilidades.fechaPubCambiado;
        FirebaseFirestore dbEditar = FirebaseFirestore.getInstance();

        dbEditar.collection("publicadores").document(idPub).update(UtilidadesStatic.BD_DISRECIENTE, fecha).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "Error al guardar. Intente nuevamente", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void actFechaAyu() {
        String idPub = Utilidades.idPubCambiado;
        Date fecha = new Date();
        fecha = Utilidades.fechaPubCambiado;
        FirebaseFirestore dbEditar = FirebaseFirestore.getInstance();

        dbEditar.collection("publicadores").document(idPub).update(UtilidadesStatic.BD_AYURECIENTE, fecha).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "Error al guardar. Intente nuevamente", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (listSelecSust.isEmpty()) {
            Toast.makeText(this, "No hay lista cargada", Toast.LENGTH_SHORT).show();
        } else {
            String userInput = newText.toLowerCase();
            ArrayList<ConstructorPublicadores> newList = new ArrayList<>();

            for (ConstructorPublicadores name : listSelecSust) {

                if (name.getNombrePublicador().toLowerCase().contains(userInput) || name.getApellidoPublicador().toLowerCase().contains(userInput)) {

                    newList.add(name);
                }
            }

            adapterSust.updateListSelec(newList);

        }
        return false;
    }
}
