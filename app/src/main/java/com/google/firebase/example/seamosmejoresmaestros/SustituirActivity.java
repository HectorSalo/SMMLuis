package com.google.firebase.example.seamosmejoresmaestros;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.example.seamosmejoresmaestros.Adaptadores.EditSalasAdapter;
import com.google.firebase.example.seamosmejoresmaestros.Constructores.PublicadoresConstructor;
import com.google.firebase.example.seamosmejoresmaestros.Variables.VariablesEstaticas;
import com.google.firebase.example.seamosmejoresmaestros.Variables.VariablesGenerales;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class SustituirActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, SearchView.OnQueryTextListener {

    private RecyclerView recyclerSustituciones;
    private ArrayList<PublicadoresConstructor> listSelecSust;
    private EditSalasAdapter adapterSust;
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
                    if (doc.getString(VariablesEstaticas.BD_LECTOR) != null) {
                        listEncargados.add(doc.getString(VariablesEstaticas.BD_LECTOR));
                        VariablesGenerales.lectorSust = doc.getString(VariablesEstaticas.BD_LECTOR);
                        VariablesGenerales.idlectorSust = doc.getString(VariablesEstaticas.BD_IDLECTOR);
                    }
                    if (doc.getString(VariablesEstaticas.BD_ENCARGADO1) != null) {
                        listEncargados.add(doc.getString(VariablesEstaticas.BD_ENCARGADO1));
                        VariablesGenerales.encargado1Sust = doc.getString(VariablesEstaticas.BD_ENCARGADO1);
                        VariablesGenerales.idencargado1Sust = doc.getString(VariablesEstaticas.BD_IDENCARGADO1);
                    }
                    if (doc.getString(VariablesEstaticas.BD_AYUDANTE1) != null) {
                        listEncargados.add(doc.getString(VariablesEstaticas.BD_AYUDANTE1));
                        VariablesGenerales.ayudante1Sust = doc.getString(VariablesEstaticas.BD_AYUDANTE1);
                        VariablesGenerales.idayudante1Sust = doc.getString(VariablesEstaticas.BD_IDAYUDANTE1);
                    }
                    if (doc.getString(VariablesEstaticas.BD_ENCARGADO2) != null) {
                        listEncargados.add(doc.getString(VariablesEstaticas.BD_ENCARGADO2));
                        VariablesGenerales.encargado2Sust = doc.getString(VariablesEstaticas.BD_ENCARGADO2);
                        VariablesGenerales.idencargado2Sust = doc.getString(VariablesEstaticas.BD_IDENCARGADO2);
                    }
                    if (doc.getString(VariablesEstaticas.BD_AYUDANTE2) != null) {
                        listEncargados.add(doc.getString(VariablesEstaticas.BD_AYUDANTE2));
                        VariablesGenerales.ayudante2Sust = doc.getString(VariablesEstaticas.BD_AYUDANTE2);
                        VariablesGenerales.idayudante2Sust = doc.getString(VariablesEstaticas.BD_IDAYUDANTE2);
                    }
                    if (doc.getString(VariablesEstaticas.BD_ENCARGADO3) != null) {
                        listEncargados.add(doc.getString(VariablesEstaticas.BD_ENCARGADO3));
                        VariablesGenerales.encargado3Sust = doc.getString(VariablesEstaticas.BD_ENCARGADO3);
                        VariablesGenerales.idencargado3Sust = doc.getString(VariablesEstaticas.BD_IDENCARGADO3);
                    }
                    if (doc.getString(VariablesEstaticas.BD_AYUDANTE3) != null) {
                        listEncargados.add(doc.getString(VariablesEstaticas.BD_AYUDANTE3));
                        VariablesGenerales.ayudante3Sust = doc.getString(VariablesEstaticas.BD_AYUDANTE3);
                        VariablesGenerales.idayudante3Sust = doc.getString(VariablesEstaticas.BD_IDAYUDANTE3);
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
                    if (doc.getString(VariablesEstaticas.BD_LECTOR) != null) {
                        listEncargados.add(doc.getString(VariablesEstaticas.BD_LECTOR));
                        VariablesGenerales.lectorSust = doc.getString(VariablesEstaticas.BD_LECTOR);
                        VariablesGenerales.idlectorSust = doc.getString(VariablesEstaticas.BD_IDLECTOR);
                    }
                    if (doc.getString(VariablesEstaticas.BD_ENCARGADO1) != null) {
                        listEncargados.add(doc.getString(VariablesEstaticas.BD_ENCARGADO1));
                        VariablesGenerales.encargado1Sust = doc.getString(VariablesEstaticas.BD_ENCARGADO1);
                        VariablesGenerales.idencargado1Sust = doc.getString(VariablesEstaticas.BD_IDENCARGADO1);
                    }
                    if (doc.getString(VariablesEstaticas.BD_AYUDANTE1) != null) {
                        listEncargados.add(doc.getString(VariablesEstaticas.BD_AYUDANTE1));
                        VariablesGenerales.ayudante1Sust = doc.getString(VariablesEstaticas.BD_AYUDANTE1);
                        VariablesGenerales.idayudante1Sust = doc.getString(VariablesEstaticas.BD_IDAYUDANTE1);
                    }
                    if (doc.getString(VariablesEstaticas.BD_ENCARGADO2) != null) {
                        listEncargados.add(doc.getString(VariablesEstaticas.BD_ENCARGADO2));
                        VariablesGenerales.encargado2Sust = doc.getString(VariablesEstaticas.BD_ENCARGADO2);
                        VariablesGenerales.idencargado2Sust = doc.getString(VariablesEstaticas.BD_IDENCARGADO2);
                    }
                    if (doc.getString(VariablesEstaticas.BD_AYUDANTE2) != null) {
                        listEncargados.add(doc.getString(VariablesEstaticas.BD_AYUDANTE2));
                        VariablesGenerales.ayudante2Sust = doc.getString(VariablesEstaticas.BD_AYUDANTE2);
                        VariablesGenerales.idayudante2Sust = doc.getString(VariablesEstaticas.BD_IDAYUDANTE2);
                    }
                    if (doc.getString(VariablesEstaticas.BD_ENCARGADO3) != null) {
                        listEncargados.add(doc.getString(VariablesEstaticas.BD_ENCARGADO3));
                        VariablesGenerales.encargado3Sust = doc.getString(VariablesEstaticas.BD_ENCARGADO3);
                        VariablesGenerales.idencargado3Sust = doc.getString(VariablesEstaticas.BD_IDENCARGADO3);
                    }
                    if (doc.getString(VariablesEstaticas.BD_AYUDANTE3) != null) {
                        listEncargados.add(doc.getString(VariablesEstaticas.BD_AYUDANTE3));
                        VariablesGenerales.ayudante3Sust = doc.getString(VariablesEstaticas.BD_AYUDANTE3);
                        VariablesGenerales.idayudante3Sust = doc.getString(VariablesEstaticas.BD_IDAYUDANTE3);
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
        adapterSust = new EditSalasAdapter(listSelecSust, getApplicationContext());
        listSelecSust = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");

        Query query = reference.whereEqualTo(VariablesEstaticas.BD_HABILITADO, true).orderBy(VariablesEstaticas.BD_SUSTRECIENTE, Query.Direction.ASCENDING);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        PublicadoresConstructor publi = new PublicadoresConstructor();
                        publi.setIdPublicador(doc.getId());
                        publi.setNombrePublicador(doc.getString(VariablesEstaticas.BD_NOMBRE));
                        publi.setApellidoPublicador(doc.getString(VariablesEstaticas.BD_APELLIDO));
                        publi.setCorreo(doc.getString(VariablesEstaticas.BD_CORREO));
                        publi.setTelefono(doc.getString(VariablesEstaticas.BD_TELEFONO));
                        publi.setGenero(doc.getString(VariablesEstaticas.BD_GENERO));
                        publi.setImagen(doc.getString(VariablesEstaticas.BD_IMAGEN));
                        publi.setUltAsignacion(doc.getDate(VariablesEstaticas.BD_DISRECIENTE));
                        publi.setUltAyudante(doc.getDate(VariablesEstaticas.BD_AYURECIENTE));
                        publi.setUltSustitucion(doc.getDate(VariablesEstaticas.BD_SUSTRECIENTE));

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

        if (spinnerSeleccion.equals(VariablesGenerales.lectorSust)){
            asignacion = VariablesEstaticas.BD_LECTOR;
            idEncargado = VariablesGenerales.idlectorSust;
            encargado = true;
            ayudante = false;
        } else if (spinnerSeleccion.equals(VariablesGenerales.encargado1Sust)) {
            asignacion = VariablesEstaticas.BD_ENCARGADO1;
            idEncargado = VariablesGenerales.idencargado1Sust;
            encargado = true;
            ayudante = false;
        } else if (spinnerSeleccion.equals(VariablesGenerales.ayudante1Sust)) {
            asignacion = VariablesEstaticas.BD_AYUDANTE1;
            idEncargado = VariablesGenerales.idayudante1Sust;
            ayudante = true;
            encargado = false;
        } else if (spinnerSeleccion.equals(VariablesGenerales.encargado2Sust)) {
            asignacion = VariablesEstaticas.BD_ENCARGADO2;
            idEncargado = VariablesGenerales.idencargado2Sust;
            encargado = true;
            ayudante = false;
        } else if (spinnerSeleccion.equals(VariablesGenerales.ayudante2Sust)) {
            asignacion = VariablesEstaticas.BD_AYUDANTE2;
            idEncargado = VariablesGenerales.idayudante2Sust;
            ayudante = true;
            encargado = false;
        } else if (spinnerSeleccion.equals(VariablesGenerales.encargado3Sust)) {
            asignacion = VariablesEstaticas.BD_ENCARGADO3;
            idEncargado = VariablesGenerales.idencargado3Sust;
            encargado = true;
            ayudante = false;
        } else if (spinnerSeleccion.equals(VariablesGenerales.ayudante3Sust)) {
            asignacion = VariablesEstaticas.BD_AYUDANTE3;
            idEncargado = VariablesGenerales.idayudante3Sust;
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

        dbEditar.collection("publicadores").document(sustSeleccionadoId).update(VariablesEstaticas.BD_SUSTRECIENTE, fechaRecibir).addOnSuccessListener(new OnSuccessListener<Void>() {
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

        dbEditar.collection("publicadores").document(sustSeleccionadoId).update(VariablesEstaticas.BD_SUSTVIEJO, fechaRecienteSust).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                        VariablesGenerales.idPubCambiado = doc.getId();
                        VariablesGenerales.fechaDisCambiado = doc.getDate(VariablesEstaticas.BD_DISVIEJO);
                        VariablesGenerales.fechaAyuCambiado = doc.getDate(VariablesEstaticas.BD_AYUVIEJO);

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
        String idPub = VariablesGenerales.idPubCambiado;
        Date fecha = new Date();
        fecha = VariablesGenerales.fechaDisCambiado;
        FirebaseFirestore dbEditar = FirebaseFirestore.getInstance();

        dbEditar.collection("publicadores").document(idPub).update(VariablesEstaticas.BD_DISRECIENTE, fecha).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        String idPub = VariablesGenerales.idPubCambiado;
        Date fecha = new Date();
        fecha = VariablesGenerales.fechaAyuCambiado;
        FirebaseFirestore dbEditar = FirebaseFirestore.getInstance();

        dbEditar.collection("publicadores").document(idPub).update(VariablesEstaticas.BD_AYURECIENTE, fecha).addOnSuccessListener(new OnSuccessListener<Void>() {
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
            final ArrayList<PublicadoresConstructor> newList = new ArrayList<>();

            for (PublicadoresConstructor name : listSelecSust) {

                if (name.getNombrePublicador().toLowerCase().contains(userInput) || name.getApellidoPublicador().toLowerCase().contains(userInput)) {

                    newList.add(name);
                }
            }

            adapterSust.updateListSelec(newList);
            adapterSust.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nombreSustSelec = newList.get(recyclerSustituciones.getChildAdapterPosition(v)).getNombrePublicador();
                    apellidoSustSelec = newList.get(recyclerSustituciones.getChildAdapterPosition(v)).getApellidoPublicador();
                    fechaRecienteSust = newList.get(recyclerSustituciones.getChildAdapterPosition(v)).getUltSustitucion();
                    sustSeleccionadoId = newList.get(recyclerSustituciones.getChildAdapterPosition(v)).getIdPublicador();
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
        return false;
    }
}
