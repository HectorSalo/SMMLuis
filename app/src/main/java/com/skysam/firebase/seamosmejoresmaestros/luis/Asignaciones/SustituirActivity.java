package com.skysam.firebase.seamosmejoresmaestros.luis.Asignaciones;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.skysam.firebase.seamosmejoresmaestros.luis.Adaptadores.EditSalasAdapter;
import com.skysam.firebase.seamosmejoresmaestros.luis.Constructores.PublicadoresConstructor;
import com.skysam.firebase.seamosmejoresmaestros.luis.R;
import com.skysam.firebase.seamosmejoresmaestros.luis.Variables.VariablesEstaticas;
import com.skysam.firebase.seamosmejoresmaestros.luis.Variables.VariablesGenerales;
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
    private Date fechaSustitucion;
    private String sustSeleccionado, nombreSustSelec, apellidoSustSelec, sustSeleccionadoId, idEncargado;
    private String spinnerSeleccion, nombreLector, idLector, nombreEncargado1, idEncargado1, nombreEncargado2, idEncargado2, nombreEncargado3, idEncargado3;
    private ArrayList<String> listEncargados;
    private ProgressBar progressBarSust;
    private long fechaLunes;


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
        progressBarSust = findViewById(R.id.progressBarSust);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean temaOscuro = sharedPreferences.getBoolean("activarOscuro", false);
        if (temaOscuro) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        Bundle bundleRecibir = this.getIntent().getExtras();
        fechaLunes = bundleRecibir.getLong("semana");

        fechaSustitucion = new Date();

        cargarEncargadosSala1(fechaLunes);

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

    public void cargarEncargadosSala1(long i) {
        String idSemana = String.valueOf(i);
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference reference = dbFirestore.collection(VariablesEstaticas.BD_SALA);

        reference.document(idSemana).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();

                    fechaSustitucion = doc.getDate(VariablesEstaticas.BD_FECHA);

                    listEncargados = new ArrayList<>();
                    listEncargados.add("Publicador a cambiar");
                    if (doc.getString(VariablesEstaticas.BD_LECTOR) != null) {
                        listEncargados.add(doc.getString(VariablesEstaticas.BD_LECTOR));
                        nombreLector = doc.getString(VariablesEstaticas.BD_LECTOR);
                        idLector = doc.getString(VariablesEstaticas.BD_IDLECTOR);
                    }
                    if (doc.getString(VariablesEstaticas.BD_ENCARGADO1) != null) {
                        listEncargados.add(doc.getString(VariablesEstaticas.BD_ENCARGADO1));
                        nombreEncargado1 = doc.getString(VariablesEstaticas.BD_ENCARGADO1);
                        idEncargado1 = doc.getString(VariablesEstaticas.BD_IDENCARGADO1);
                    }
                    if (doc.getString(VariablesEstaticas.BD_ENCARGADO2) != null) {
                        listEncargados.add(doc.getString(VariablesEstaticas.BD_ENCARGADO2));
                        nombreEncargado2 = doc.getString(VariablesEstaticas.BD_ENCARGADO2);
                        idEncargado2 = doc.getString(VariablesEstaticas.BD_IDENCARGADO2);
                    }
                    if (doc.getString(VariablesEstaticas.BD_ENCARGADO3) != null) {
                        listEncargados.add(doc.getString(VariablesEstaticas.BD_ENCARGADO3));
                        nombreEncargado3 = doc.getString(VariablesEstaticas.BD_ENCARGADO3);
                        idEncargado3 = doc.getString(VariablesEstaticas.BD_IDENCARGADO3);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_selec, listEncargados);
                    spinnerEncargados.setAdapter(adapter);

                }
            }
        });
    }

    public void cargarSustitutos() {
        progressBarSust.setVisibility(View.VISIBLE);
        adapterSust = new EditSalasAdapter(listSelecSust, getApplicationContext());
        listSelecSust = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection(VariablesEstaticas.BD_PUBLICADORES);

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
                        publi.setCumplirAsignacion(doc.getBoolean(VariablesEstaticas.BD_CUMPLIR_ASIGNACION));

                        listSelecSust.add(publi);
                    }

                    adapterSust.updateListSelec(listSelecSust);

                    recyclerSustituciones.setAdapter(adapterSust);
                    progressBarSust.setVisibility(View.GONE);
                } else {
                    progressBarSust.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        adapterSust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombreSustSelec = listSelecSust.get(recyclerSustituciones.getChildAdapterPosition(v)).getNombrePublicador();
                apellidoSustSelec = listSelecSust.get(recyclerSustituciones.getChildAdapterPosition(v)).getApellidoPublicador();
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
        String idSemana = String.valueOf(fechaLunes);
        String asignacion = "";
        String idEnc = "";


        if (spinnerSeleccion.equals(nombreLector)){
            asignacion = VariablesEstaticas.BD_LECTOR;
            idEncargado = idLector;
            idEnc = VariablesEstaticas.BD_IDLECTOR;
        } else if (spinnerSeleccion.equals(nombreEncargado1)) {
            asignacion = VariablesEstaticas.BD_ENCARGADO1;
            idEncargado = idEncargado1;
            idEnc = VariablesEstaticas.BD_IDENCARGADO1;
        } else if (spinnerSeleccion.equals(nombreEncargado2)) {
            asignacion = VariablesEstaticas.BD_ENCARGADO2;
            idEncargado = idEncargado2;
            idEnc = VariablesEstaticas.BD_IDENCARGADO2;
        } else if (spinnerSeleccion.equals(nombreEncargado3)) {
            asignacion = VariablesEstaticas.BD_ENCARGADO3;
            idEncargado = idEncargado3;
            idEnc = VariablesEstaticas.BD_IDENCARGADO3;
        }

        FirebaseFirestore dbEditar = FirebaseFirestore.getInstance();

        dbEditar.collection(VariablesEstaticas.BD_SALA).document(idSemana).update(asignacion, sustSeleccionado, idEnc, sustSeleccionadoId).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                actualizarEncargado();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al guardar. Intente nuevamente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void actualizarSustituto () {
        FirebaseFirestore dbEditar = FirebaseFirestore.getInstance();

        dbEditar.collection(VariablesEstaticas.BD_PUBLICADORES).document(sustSeleccionadoId).update(VariablesEstaticas.BD_SUSTRECIENTE, fechaSustitucion).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Sustitución exitosa", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), AsignacionesActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al guardar. Intente nuevamente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void actualizarEncargado() {
        FirebaseFirestore dbEditar = FirebaseFirestore.getInstance();

        dbEditar.collection(VariablesEstaticas.BD_PUBLICADORES).document(idEncargado).update(VariablesEstaticas.BD_CUMPLIR_ASIGNACION, false).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                actualizarSustituto();
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
