package com.google.firebase.example.seamosmejoresmaestros;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EditarSalas extends AppCompatActivity {

    private RecyclerView recyclerEditSalas;
    private ArrayList<ConstructorPublicadores> listPubs;
    private AdapterEditSalas adapterEditSalas;
    private CalendarView calendarView;
    private Spinner spinnerSelec;
    private ArrayAdapter<String> adapterSpSeleccionar;
    private FloatingActionButton fabBack;
    private EditText etBuscar;
    private TextView tvTitle;
    private CheckBox cbVisita, cbAsamblea;
    private Button btnIr;
    private RadioGroup grupoCb;
    private Date fechaSelec, fechaActual;
    private ProgressDialog progress;
    private boolean visita, asamblea, activarSala2;
    private String seleccion1Sala1, seleccion2Sala1, seleccion3Sala1, idLectorSala1, idEncargado1Sala1, idAyudante1Sala1, idEncargado2Sala1, idAyudante2Sala1, idEncargado3Sala1, idAyudante3Sala1;
    private String seleccion1Sala2, seleccion2Sala2, seleccion3Sala2, idLectorSala2, idEncargado1Sala2, idAyudante1Sala2, idEncargado2Sala2, idAyudante2Sala2, idEncargado3Sala2, idAyudante3Sala2;
    private String genero, generoAyudante;
    private int semanaSelec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_salas);

        calendarView = (CalendarView) findViewById(R.id.calendarSelec);
        spinnerSelec = (Spinner) findViewById(R.id.spinnerSelec);
        etBuscar = (EditText) findViewById(R.id.etBuscar);
        tvTitle = (TextView) findViewById(R.id.tvTitulo);
        cbAsamblea = (CheckBox) findViewById(R.id.cbAsamblea);
        cbVisita = (CheckBox) findViewById(R.id.cbVisita);
        btnIr = (Button) findViewById(R.id.buttonIr);
        grupoCb = (RadioGroup) findViewById(R.id.groupcb);



        recyclerEditSalas = (RecyclerView) findViewById(R.id.recyclerSeleccionar);
        recyclerEditSalas.setLayoutManager(new LinearLayoutManager(this));
        recyclerEditSalas.setHasFixedSize(true);
        listPubs = new ArrayList<>();
        adapterEditSalas = new AdapterEditSalas(listPubs, this);
        recyclerEditSalas.setAdapter(adapterEditSalas);

        Calendar almanaque = Calendar.getInstance();
        int diaActual = almanaque.get(Calendar.DAY_OF_MONTH);
        int mesActual = almanaque.get(Calendar.MONTH);
        int anualActual = almanaque.get(Calendar.YEAR);
        almanaque.set(anualActual, mesActual, diaActual);
        fechaActual = almanaque.getTime();
        visita = false;
        asamblea = false;
        activarSala2 = true;

        String [] spSelecccionar = {"Seleccionar Asignación", "Primera Conversación", "Primera Revisita", "Segunda Revisita", "Curso Bíblico", "Discurso", "Sin asignación"};
        adapterSpSeleccionar = new ArrayAdapter<String>(this, R.layout.spinner_selec, spSelecccionar);
        spinnerSelec.setAdapter(adapterSpSeleccionar);

        fabBack = (FloatingActionButton) findViewById(R.id.fabBackSelec);
        FloatingActionButton fabClose = (FloatingActionButton) findViewById(R.id.fabCloseSelec);
        fabClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                listaBuscar(s.toString());
            }
        });

        selecFecha();
    }

    public void selecFecha () {
        etBuscar.setVisibility(View.INVISIBLE);
        spinnerSelec.setVisibility(View.INVISIBLE);
        recyclerEditSalas.setVisibility(View.INVISIBLE);
        btnIr.setVisibility(View.VISIBLE);
        grupoCb.setVisibility(View.VISIBLE);
        calendarView.setVisibility(View.VISIBLE);
        tvTitle.setText("Seleccione el día de las asignaciones");

        final Calendar calendario = Calendar.getInstance();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                calendario.set(year, month, dayOfMonth);
                fechaSelec = calendario.getTime();
                semanaSelec = calendario.get(Calendar.WEEK_OF_YEAR);
                tvTitle.setText(new SimpleDateFormat("EEE d MMM yyyy").format(fechaSelec));
            }
        });
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnIr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtros();
            }
        });
    }

    public void listaLecturaSala1() {
        etBuscar.setVisibility(View.VISIBLE);
        etBuscar.setText("");
        spinnerSelec.setVisibility(View.INVISIBLE);
        recyclerEditSalas.setVisibility(View.VISIBLE);
        btnIr.setVisibility(View.INVISIBLE);
        grupoCb.setVisibility(View.INVISIBLE);
        calendarView.setVisibility(View.INVISIBLE);
        tvTitle.setText("Lector: Sala 1");

        listPubs = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");

        Query query = reference.whereEqualTo(UtilidadesStatic.BD_GENERO, "Hombre").whereEqualTo(UtilidadesStatic.BD_HABILITADO, true).orderBy(UtilidadesStatic.BD_DISRECIENTE, Query.Direction.ASCENDING);

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

                        listPubs.add(publi);

                    }
                    adapterEditSalas.updateListSelec(listPubs);
                    progress.dismiss();
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        adapterEditSalas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilidades.lectorSala1Date = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getUltAsignacion();
                Utilidades.lectorSala1 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                idLectorSala1 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                Snackbar.make(v, "Escogió a: " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                progress = new ProgressDialog(EditarSalas.this);
                progress.setMessage("Cargando...");
                progress.setCancelable(false);
                progress.show();
                listaEncargado1Sala1();
            }
        });

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecFecha();
            }
        });
    }

    public void listaEncargado1Sala1() {
        spinnerSelec.setVisibility(View.VISIBLE);
        spinnerSelec.setAdapter(adapterSpSeleccionar);
        etBuscar.setText("");
        tvTitle.setText("Encargado Primera Asignación: Sala 1");

        listPubs = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");

        Query query = reference.whereEqualTo(UtilidadesStatic.BD_HABILITADO, true).orderBy(UtilidadesStatic.BD_DISRECIENTE, Query.Direction.ASCENDING);

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

                        listPubs.add(publi);

                    }
                    adapterEditSalas.updateListSelec(listPubs);
                    progress.dismiss();
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinnerSelec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seleccion1Sala1 = spinnerSelec.getSelectedItem().toString();
                if (seleccion1Sala1.equals("Sin asignación")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
                    dialog.setTitle("Confirmar");
                    dialog.setMessage("¿Desea pasar por alto esta asignación?");
                    dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Utilidades.encargado1Sala1 = null;
                            Utilidades.ayudante1Sala1 = null;
                            idEncargado1Sala1 = null;
                            idAyudante1Sala1 = null;

                            progress = new ProgressDialog(EditarSalas.this);
                            progress.setMessage("Cargando...");
                            progress.setCancelable(false);
                            progress.show();
                            listaEncargado2Sala1();
                            dialog.dismiss();
                        }
                    });
                    dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapterEditSalas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilidades.encargado1Sala1Date = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getUltAsignacion();
                Utilidades.encargado1Sala1 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                idEncargado1Sala1 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                genero = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getGenero();

                if (!seleccion1Sala1.equals("Seleccionar Asignación")) {
                    if (!idEncargado1Sala1.equals(idLectorSala1)) {
                        if (seleccion1Sala1.equals("Discurso") && genero.equals("Mujer")) {
                            Toast.makeText(getApplicationContext(), "Debe escoger un publicador masculino", Toast.LENGTH_SHORT).show();

                        } else if (seleccion1Sala1.equals("Discurso") && genero.equals("Hombre")) {
                            idAyudante1Sala1 = null;
                            Utilidades.ayudante1Sala1 = null;
                            Snackbar.make(v, "Escogió a: " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                            progress = new ProgressDialog(EditarSalas.this);
                            progress.setMessage("Cargando...");
                            progress.setCancelable(false);
                            progress.show();
                            listaEncargado2Sala1();

                        } else if (!seleccion1Sala1.equals("Discurso")) {
                            Snackbar.make(v, "Escogió a: " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                            progress = new ProgressDialog(EditarSalas.this);
                            progress.setMessage("Cargando...");
                            progress.setCancelable(false);
                            progress.show();
                            listaAyudante1Sala1();

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Este publicador ya tiene asignación esta semana", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Debe escoger el tipo de asignación", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(EditarSalas.this);
                progress.setMessage("Cargando...");
                progress.setCancelable(false);
                progress.show();
                listaLecturaSala1();
            }
        });
    }

    public void listaAyudante1Sala1() {
        etBuscar.setText("");
        spinnerSelec.setVisibility(View.INVISIBLE);
        tvTitle.setText("Ayudante Primera Asignación: Sala 1");

        listPubs = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");

        Query query = reference.whereEqualTo(UtilidadesStatic.BD_HABILITADO, true).orderBy(UtilidadesStatic.BD_AYURECIENTE, Query.Direction.ASCENDING);

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

                        listPubs.add(publi);

                    }
                    adapterEditSalas.updateListSelec(listPubs);
                    progress.dismiss();
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });



        adapterEditSalas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilidades.ayudante1Sala1Date = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getUltAyudante();
                Utilidades.ayudante1Sala1 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                idAyudante1Sala1 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                generoAyudante = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getGenero();

                if (!idAyudante1Sala1.equals(idLectorSala1) && !idAyudante1Sala1.equals(idEncargado1Sala1)) {
                    if (!genero.equals(generoAyudante)) {
                        if (seleccion1Sala1.equals("Primera Conversación")) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
                            dialog.setTitle("Confirmar");
                            dialog.setMessage("Recuerde que deben ser familiares los publicadores del sexo opuesto\n¿Está seguro?");
                            dialog.setIcon(R.drawable.ic_advertencia);
                            dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    progress = new ProgressDialog(EditarSalas.this);
                                    progress.setMessage("Cargando...");
                                    progress.setCancelable(false);
                                    progress.show();
                                    listaEncargado2Sala1();
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
                            AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
                            dialog.setTitle("¡Aviso!");
                            dialog.setMessage("No puede colocar publicadores del sexo opuesto para este tipo de asignación");
                            dialog.setIcon(R.drawable.ic_nopermitido);
                            dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    } else {
                        Snackbar.make(v, "Escogió a: " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                        progress = new ProgressDialog(EditarSalas.this);
                        progress.setMessage("Cargando...");
                        progress.setCancelable(false);
                        progress.show();
                        listaEncargado2Sala1();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Este publicador ya tiene asignación esta semana", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(EditarSalas.this);
                progress.setMessage("Cargando...");
                progress.setCancelable(false);
                progress.show();
                listaEncargado1Sala1();
            }
        });
    }

    public void listaEncargado2Sala1 () {
        etBuscar.setText("");
        spinnerSelec.setVisibility(View.VISIBLE);
        spinnerSelec.setAdapter(adapterSpSeleccionar);
        tvTitle.setText("Encargado Segunda Asignación: Sala 1");

        listPubs = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");

        Query query = reference.whereEqualTo(UtilidadesStatic.BD_HABILITADO, true).orderBy(UtilidadesStatic.BD_DISRECIENTE, Query.Direction.ASCENDING);

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

                        listPubs.add(publi);

                    }
                    adapterEditSalas.updateListSelec(listPubs);
                    progress.dismiss();
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinnerSelec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seleccion2Sala1 = spinnerSelec.getSelectedItem().toString();
                if (seleccion2Sala1.equals("Sin asignación")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
                    dialog.setTitle("Confirmar");
                    dialog.setMessage("¿Desea pasar por alto esta asignación?");
                    dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Utilidades.encargado2Sala1 = null;
                            Utilidades.ayudante2Sala1 = null;
                            idEncargado2Sala1 = null;
                            idAyudante2Sala1 = null;
                            progress = new ProgressDialog(EditarSalas.this);
                            progress.setMessage("Cargando...");
                            progress.setCancelable(false);
                            progress.show();
                            listaEncargado3Sala1();
                            dialog.dismiss();
                        }
                    });
                    dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapterEditSalas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilidades.encargado2Sala1Date = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getUltAsignacion();
                Utilidades.encargado2Sala1 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                idEncargado2Sala1 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                genero = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getGenero();

                if (!seleccion2Sala1.equals("Seleccionar Asignación")) {
                    if (!idEncargado2Sala1.equals(idLectorSala1) && !idEncargado2Sala1.equals(idEncargado1Sala1) && !idEncargado2Sala1.equals(idAyudante1Sala1)) {
                        if (seleccion2Sala1.equals("Discurso") && genero.equals("Mujer")) {
                            Toast.makeText(getApplicationContext(), "Debe escoger un publicador masculino", Toast.LENGTH_SHORT).show();

                        } else if (seleccion2Sala1.equals("Discurso") && genero.equals("Hombre")) {
                            idAyudante2Sala1 = null;
                            Utilidades.ayudante2Sala1 = null;
                            Snackbar.make(v, "Escogió a: " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                            progress = new ProgressDialog(EditarSalas.this);
                            progress.setMessage("Cargando...");
                            progress.setCancelable(false);
                            progress.show();
                            listaEncargado3Sala1();

                        } else if (!seleccion2Sala1.equals("Discurso")) {
                            Snackbar.make(v, "Escogió a: " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                            progress = new ProgressDialog(EditarSalas.this);
                            progress.setMessage("Cargando...");
                            progress.setCancelable(false);
                            progress.show();
                            listaAyudante2Sala1();

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Este publicador ya tiene asignación esta semana", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Debe escoger el tipo de asignación", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(EditarSalas.this);
                progress.setMessage("Cargando...");
                progress.setCancelable(false);
                progress.show();
                listaEncargado1Sala1();
            }
        });
    }

    public void listaAyudante2Sala1() {
        etBuscar.setText("");
        spinnerSelec.setVisibility(View.INVISIBLE);
        tvTitle.setText("Ayudante Segunda Asignación: Sala 1");

        listPubs = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");

        Query query = reference.whereEqualTo(UtilidadesStatic.BD_HABILITADO, true).orderBy(UtilidadesStatic.BD_AYURECIENTE, Query.Direction.ASCENDING);

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

                        listPubs.add(publi);

                    }
                    adapterEditSalas.updateListSelec(listPubs);
                    progress.dismiss();
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        adapterEditSalas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilidades.ayudante2Sala1Date = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getUltAyudante();
                Utilidades.ayudante2Sala1 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                idAyudante2Sala1 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                generoAyudante = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getGenero();

                if (!idAyudante2Sala1.equals(idLectorSala1) && !idAyudante2Sala1.equals(idEncargado1Sala1) && !idAyudante2Sala1.equals(idAyudante1Sala1) && !idAyudante2Sala1.equals(idEncargado2Sala1)) {
                    if (!genero.equals(generoAyudante)) {
                        if (seleccion2Sala1.equals("Primera Conversación")) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
                            dialog.setTitle("Confirmar");
                            dialog.setMessage("Recuerde que deben ser familiares los publicadores del sexo opuesto\n¿Está seguro?");
                            dialog.setIcon(R.drawable.ic_advertencia);
                            dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    progress = new ProgressDialog(EditarSalas.this);
                                    progress.setMessage("Cargando...");
                                    progress.setCancelable(false);
                                    progress.show();
                                    listaEncargado3Sala1();
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
                            AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
                            dialog.setTitle("¡Aviso!");
                            dialog.setMessage("No puede colocar publicadores del sexo opuesto para este tipo de asignación");
                            dialog.setIcon(R.drawable.ic_nopermitido);
                            dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    } else {
                        Snackbar.make(v, "Escogió a: " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                        progress = new ProgressDialog(EditarSalas.this);
                        progress.setMessage("Cargando...");
                        progress.setCancelable(false);
                        progress.show();
                        listaEncargado3Sala1();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Este publicador ya tiene asignación esta semana", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(EditarSalas.this);
                progress.setMessage("Cargando...");
                progress.setCancelable(false);
                progress.show();
                listaEncargado2Sala1();
            }
        });
    }

    public void listaEncargado3Sala1() {
        etBuscar.setText("");
        spinnerSelec.setVisibility(View.VISIBLE);
        spinnerSelec.setAdapter(adapterSpSeleccionar);
        tvTitle.setText("Encargado Tercera Asignación: Sala 1");

        listPubs = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");

        Query query = reference.whereEqualTo(UtilidadesStatic.BD_HABILITADO, true).orderBy(UtilidadesStatic.BD_DISRECIENTE, Query.Direction.ASCENDING);

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

                        listPubs.add(publi);

                    }
                    adapterEditSalas.updateListSelec(listPubs);
                    progress.dismiss();
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinnerSelec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seleccion3Sala1 = spinnerSelec.getSelectedItem().toString();
                if (seleccion3Sala1.equals("Sin asignación")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
                    dialog.setTitle("Confirmar");
                    dialog.setMessage("¿Desea pasar por alto esta asignación?");
                    dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Utilidades.encargado3Sala1 = null;
                            Utilidades.ayudante3Sala1 = null;
                            idEncargado3Sala1 = null;
                            idAyudante3Sala1 = null;
                            confirmacionSala2();
                            dialog.dismiss();
                        }
                    });
                    dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapterEditSalas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilidades.encargado3Sala1Date = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getUltAsignacion();
                Utilidades.encargado3Sala1 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                idEncargado3Sala1 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                genero = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getGenero();

                if (!seleccion3Sala1.equals("Seleccionar Asignación")) {
                    if (!idEncargado3Sala1.equals(idLectorSala1) && !idEncargado3Sala1.equals(idEncargado1Sala1) && !idEncargado3Sala1.equals(idAyudante1Sala1)
                    && !idEncargado3Sala1.equals(idEncargado2Sala1) && !idEncargado3Sala1.equals(idAyudante2Sala1)) {
                        if (seleccion3Sala1.equals("Discurso") && genero.equals("Mujer")) {
                            Toast.makeText(getApplicationContext(), "Debe escoger un publicador masculino", Toast.LENGTH_SHORT).show();

                        } else if (seleccion3Sala1.equals("Discurso") && genero.equals("Hombre")) {
                            Utilidades.ayudante3Sala1 = null;
                            idAyudante3Sala1 = null;
                            Snackbar.make(v, "Escogió a: " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                            confirmacionSala2();

                        } else if (!seleccion3Sala1.equals("Discurso")) {
                            Snackbar.make(v, "Escogió a: " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                            progress = new ProgressDialog(EditarSalas.this);
                            progress.setMessage("Cargando...");
                            progress.setCancelable(false);
                            progress.show();
                            listaAyudante3Sala1();

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Este publicador ya tiene asignación esta semana", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Debe escoger el tipo de asignación", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(EditarSalas.this);
                progress.setMessage("Cargando...");
                progress.setCancelable(false);
                progress.show();
                listaEncargado2Sala1();
            }
        });
    }

    public void listaAyudante3Sala1 () {
        etBuscar.setText("");
        spinnerSelec.setVisibility(View.INVISIBLE);
        tvTitle.setText("Ayudante Tercera Asignación: Sala 1");

        listPubs = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");

        Query query = reference.whereEqualTo(UtilidadesStatic.BD_HABILITADO, true).orderBy(UtilidadesStatic.BD_AYURECIENTE, Query.Direction.ASCENDING);

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

                        listPubs.add(publi);

                    }
                    adapterEditSalas.updateListSelec(listPubs);
                    progress.dismiss();
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        adapterEditSalas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilidades.ayudante3Sala1Date = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getUltAyudante();
                Utilidades.ayudante3Sala1 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                idAyudante3Sala1 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                generoAyudante = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getGenero();

                if (!idAyudante3Sala1.equals(idLectorSala1) && !idAyudante3Sala1.equals(idEncargado1Sala1) && !idAyudante3Sala1.equals(idAyudante1Sala1) && !idAyudante3Sala1.equals(idEncargado2Sala1)
                && !idAyudante3Sala1.equals(idAyudante2Sala1) && !idAyudante3Sala1.equals(idEncargado3Sala1)) {
                    if (!genero.equals(generoAyudante)) {
                        if (seleccion3Sala1.equals("Primera Conversación")) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
                            dialog.setTitle("Confirmar");
                            dialog.setMessage("Recuerde que deben ser familiares los publicadores del sexo opuesto\n¿Está seguro?");
                            dialog.setIcon(R.drawable.ic_advertencia);
                            dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    confirmacionSala2();
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
                            AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
                            dialog.setTitle("¡Aviso!");
                            dialog.setMessage("No puede colocar publicadores del sexo opuesto para este tipo de asignación");
                            dialog.setIcon(R.drawable.ic_nopermitido);
                            dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    } else {
                        Snackbar.make(v, "Escogió a: " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                        confirmacionSala2();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Este publicador ya tiene asignación esta semana", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(EditarSalas.this);
                progress.setMessage("Cargando...");
                progress.setCancelable(false);
                progress.show();
                listaEncargado3Sala1();
            }
        });
    }

    public void listaLecturaSala2() {
        etBuscar.setText("");
        spinnerSelec.setVisibility(View.INVISIBLE);
        tvTitle.setText("Lector: Sala 2");

        listPubs = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");

        Query query = reference.whereEqualTo(UtilidadesStatic.BD_GENERO, "Hombre").whereEqualTo(UtilidadesStatic.BD_HABILITADO, true).orderBy(UtilidadesStatic.BD_DISRECIENTE, Query.Direction.ASCENDING);

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

                        listPubs.add(publi);

                    }
                    adapterEditSalas.updateListSelec(listPubs);
                    progress.dismiss();
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        adapterEditSalas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilidades.lectorSala2Date = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getUltAsignacion();
                Utilidades.lectorSala2 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                idLectorSala2 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();

                if (!idLectorSala2.equals(idLectorSala1) && !idLectorSala2.equals(idEncargado1Sala1) && !idLectorSala2.equals(idAyudante1Sala1) && !idLectorSala2.equals(idEncargado2Sala1)
                        && !idLectorSala2.equals(idAyudante2Sala1) && !idLectorSala2.equals(idEncargado3Sala1) && !idLectorSala2.equals(idAyudante3Sala1)) {
                    Snackbar.make(v, "Escogió a: " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                    progress = new ProgressDialog(EditarSalas.this);
                    progress.setMessage("Cargando...");
                    progress.setCancelable(false);
                    progress.show();
                    listaEncargado1Sala2();
                } else {
                    Toast.makeText(getApplicationContext(), "Este publicador ya tiene asignación esta semana", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(EditarSalas.this);
                progress.setMessage("Cargando...");
                progress.setCancelable(false);
                progress.show();
                listaEncargado3Sala1();
            }
        });
    }

    public void listaEncargado1Sala2() {
        etBuscar.setText("");
        spinnerSelec.setVisibility(View.VISIBLE);
        spinnerSelec.setAdapter(adapterSpSeleccionar);
        tvTitle.setText("Encargado Primera Asignación: Sala 2");

        listPubs = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");

        Query query = reference.whereEqualTo(UtilidadesStatic.BD_HABILITADO, true).orderBy(UtilidadesStatic.BD_DISRECIENTE, Query.Direction.ASCENDING);

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

                        listPubs.add(publi);

                    }
                    adapterEditSalas.updateListSelec(listPubs);
                    progress.dismiss();
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinnerSelec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seleccion1Sala2 = spinnerSelec.getSelectedItem().toString();
                if (seleccion1Sala2.equals("Sin asignación")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
                    dialog.setTitle("Confirmar");
                    dialog.setMessage("¿Desea pasar por alto esta asignación?");
                    dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Utilidades.encargado1Sala2 = null;
                            Utilidades.ayudante1Sala2 = null;
                            idEncargado1Sala2 = null;
                            idAyudante1Sala2 = null;
                            listaEncargado2Sala2();
                            dialog.dismiss();
                        }
                    });
                    dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapterEditSalas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilidades.encargado1Sala2Date = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getUltAsignacion();
                Utilidades.encargado1Sala2 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                idEncargado1Sala2 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                genero = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getGenero();

                if (!seleccion1Sala2.equals("Seleccionar Asignación")) {
                    if (!idEncargado1Sala2.equals(idLectorSala1) && !idEncargado1Sala2.equals(idEncargado1Sala1) && !idEncargado1Sala2.equals(idAyudante1Sala1)
                            && !idEncargado1Sala2.equals(idEncargado2Sala1) && !idEncargado1Sala2.equals(idAyudante2Sala1) && !idEncargado1Sala2.equals(idEncargado3Sala1)
                            && !idEncargado1Sala2.equals(idAyudante3Sala1) && !idEncargado1Sala2.equals(idLectorSala2)) {
                        if (seleccion1Sala2.equals("Discurso") && genero.equals("Mujer")) {
                            Toast.makeText(getApplicationContext(), "Debe escoger un publicador masculino", Toast.LENGTH_SHORT).show();

                        } else if (seleccion1Sala2.equals("Discurso") && genero.equals("Hombre")) {
                            idAyudante1Sala2 = null;
                            Utilidades.ayudante1Sala2 = null;
                            Snackbar.make(v, "Escogió a: " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                            listaEncargado2Sala2();

                        } else if (!seleccion1Sala2.equals("Discurso")) {
                            Snackbar.make(v, "Escogió a: " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                            progress = new ProgressDialog(EditarSalas.this);
                            progress.setMessage("Cargando...");
                            progress.setCancelable(false);
                            progress.show();
                            listaAyudante1Sala2();

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Este publicador ya tiene asignación esta semana", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Debe escoger el tipo de asignación", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(EditarSalas.this);
                progress.setMessage("Cargando...");
                progress.setCancelable(false);
                progress.show();
                listaLecturaSala2();
            }
        });
    }

    public void listaAyudante1Sala2() {
        etBuscar.setText("");
        spinnerSelec.setVisibility(View.INVISIBLE);
        tvTitle.setText("Ayudante Primera Asignación: Sala 2");

        listPubs = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");

        Query query = reference.whereEqualTo(UtilidadesStatic.BD_HABILITADO, true).orderBy(UtilidadesStatic.BD_AYURECIENTE, Query.Direction.ASCENDING);

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

                        listPubs.add(publi);

                    }
                    adapterEditSalas.updateListSelec(listPubs);
                    progress.dismiss();
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        adapterEditSalas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilidades.ayudante1Sala2Date = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getUltAyudante();
                Utilidades.ayudante1Sala2 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                idAyudante1Sala2 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                generoAyudante = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getGenero();

                if (!idAyudante1Sala2.equals(idLectorSala1) && !idAyudante1Sala2.equals(idEncargado1Sala1) && !idAyudante1Sala2.equals(idAyudante1Sala1) && !idAyudante1Sala2.equals(idEncargado2Sala1)
                        && !idAyudante1Sala2.equals(idAyudante2Sala1) && !idAyudante1Sala2.equals(idEncargado3Sala1) && !idAyudante1Sala2.equals(idLectorSala2) && !idAyudante1Sala2.equals(idEncargado1Sala2)) {
                    if (!genero.equals(generoAyudante)) {
                        if (seleccion1Sala2.equals("Primera Conversación")) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
                            dialog.setTitle("Confirmar");
                            dialog.setMessage("Recuerde que deben ser familiares los publicadores del sexo opuesto\n¿Está seguro?");
                            dialog.setIcon(R.drawable.ic_advertencia);
                            dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    listaEncargado2Sala2();
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
                            AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
                            dialog.setTitle("¡Aviso!");
                            dialog.setMessage("No puede colocar publicadores del sexo opuesto para este tipo de asignación");
                            dialog.setIcon(R.drawable.ic_nopermitido);
                            dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    } else {
                        Snackbar.make(v, "Escogió a: " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                        listaEncargado2Sala2();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Este publicador ya tiene asignación esta semana", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(EditarSalas.this);
                progress.setMessage("Cargando...");
                progress.setCancelable(false);
                progress.show();
                listaEncargado1Sala2();
            }
        });
    }

    public void listaEncargado2Sala2() {
        etBuscar.setText("");
        spinnerSelec.setVisibility(View.VISIBLE);
        spinnerSelec.setAdapter(adapterSpSeleccionar);
        tvTitle.setText("Encargado Segunda Asignación: Sala 2");

        listPubs = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");

        Query query = reference.whereEqualTo(UtilidadesStatic.BD_HABILITADO, true).orderBy(UtilidadesStatic.BD_DISRECIENTE, Query.Direction.ASCENDING);

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

                        listPubs.add(publi);

                    }
                    adapterEditSalas.updateListSelec(listPubs);
                    progress.dismiss();
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinnerSelec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seleccion2Sala2 = spinnerSelec.getSelectedItem().toString();
                if (seleccion2Sala2.equals("Sin asignación")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
                    dialog.setTitle("Confirmar");
                    dialog.setMessage("¿Desea pasar por alto esta asignación?");
                    dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Utilidades.encargado2Sala2 = null;
                            Utilidades.ayudante2Sala2 = null;
                            idEncargado2Sala2 = null;
                            idAyudante2Sala2 = null;
                            listaEncargado3Sala2();
                            dialog.dismiss();
                        }
                    });
                    dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapterEditSalas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilidades.encargado2Sala2Date = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getUltAsignacion();
                Utilidades.encargado2Sala2 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                idEncargado2Sala2 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                genero = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getGenero();

                if (!seleccion2Sala2.equals("Seleccionar Asignación")) {
                    if (!idEncargado2Sala2.equals(idLectorSala1) && !idEncargado2Sala2.equals(idEncargado1Sala1) && !idEncargado2Sala2.equals(idAyudante1Sala1)
                            && !idEncargado2Sala2.equals(idEncargado2Sala1) && !idEncargado2Sala2.equals(idAyudante2Sala1) && !idEncargado2Sala2.equals(idEncargado3Sala1)
                            && !idEncargado2Sala2.equals(idAyudante3Sala1) && !idEncargado2Sala2.equals(idLectorSala2) && !idEncargado2Sala2.equals(idEncargado1Sala2)
                            && !idEncargado2Sala2.equals(idAyudante1Sala2)) {
                        if (seleccion2Sala2.equals("Discurso") && genero.equals("Mujer")) {
                            Toast.makeText(getApplicationContext(), "Debe escoger un publicador masculino", Toast.LENGTH_SHORT).show();

                        } else if (seleccion2Sala2.equals("Discurso") && genero.equals("Hombre")) {
                            idAyudante2Sala2 = null;
                            Utilidades.ayudante2Sala2 = null;
                            Snackbar.make(v, "Escogió a: " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                            listaEncargado3Sala2();

                        } else if (!seleccion2Sala2.equals("Discurso")) {
                            Snackbar.make(v, "Escogió a: " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                            progress = new ProgressDialog(EditarSalas.this);
                            progress.setMessage("Cargando...");
                            progress.setCancelable(false);
                            progress.show();
                            listaAyudante2Sala2();

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Este publicador ya tiene asignación esta semana", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Debe escoger el tipo de asignación", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(EditarSalas.this);
                progress.setMessage("Cargando...");
                progress.setCancelable(false);
                progress.show();
                listaEncargado1Sala2();
            }
        });
    }

    public void listaAyudante2Sala2() {
        etBuscar.setText("");
        spinnerSelec.setVisibility(View.INVISIBLE);
        tvTitle.setText("Ayudante Segunda Asignación: Sala 2");

        listPubs = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");

        Query query = reference.whereEqualTo(UtilidadesStatic.BD_HABILITADO, true).orderBy(UtilidadesStatic.BD_AYURECIENTE, Query.Direction.ASCENDING);

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

                        listPubs.add(publi);

                    }
                    adapterEditSalas.updateListSelec(listPubs);
                    progress.dismiss();
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        adapterEditSalas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilidades.ayudante2Sala2Date = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getUltAyudante();
                Utilidades.ayudante2Sala2 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                idAyudante2Sala2 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                generoAyudante = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getGenero();

                if (!idAyudante2Sala2.equals(idLectorSala1) && !idAyudante2Sala2.equals(idEncargado1Sala1) && !idAyudante2Sala2.equals(idAyudante1Sala1) && !idAyudante2Sala2.equals(idEncargado2Sala1)
                        && !idAyudante2Sala2.equals(idAyudante2Sala1) && !idAyudante2Sala2.equals(idEncargado3Sala1) && !idAyudante2Sala2.equals(idLectorSala2) && !idAyudante2Sala2.equals(idEncargado1Sala2)
                        && !idAyudante2Sala2.equals(idAyudante1Sala2) && !idAyudante2Sala2.equals(idEncargado2Sala2)) {
                    if (!genero.equals(generoAyudante)) {
                        if (seleccion2Sala2.equals("Primera Conversación")) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
                            dialog.setTitle("Confirmar");
                            dialog.setMessage("Recuerde que deben ser familiares los publicadores del sexo opuesto\n¿Está seguro?");
                            dialog.setIcon(R.drawable.ic_advertencia);
                            dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    listaEncargado3Sala2();
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
                            AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
                            dialog.setTitle("¡Aviso!");
                            dialog.setMessage("No puede colocar publicadores del sexo opuesto para este tipo de asignación");
                            dialog.setIcon(R.drawable.ic_nopermitido);
                            dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    } else {
                        Snackbar.make(v, "Escogió a: " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                        listaEncargado3Sala2();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Este publicador ya tiene asignación esta semana", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(EditarSalas.this);
                progress.setMessage("Cargando...");
                progress.setCancelable(false);
                progress.show();
                listaEncargado2Sala2();
            }
        });
    }

    public void listaEncargado3Sala2() {
        etBuscar.setText("");
        spinnerSelec.setVisibility(View.VISIBLE);
        spinnerSelec.setAdapter(adapterSpSeleccionar);
        tvTitle.setText("Encargado Tercera Asignación: Sala 2");

        listPubs = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");

        Query query = reference.whereEqualTo(UtilidadesStatic.BD_HABILITADO, true).orderBy(UtilidadesStatic.BD_DISRECIENTE, Query.Direction.ASCENDING);

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

                        listPubs.add(publi);

                    }
                    adapterEditSalas.updateListSelec(listPubs);
                    progress.dismiss();
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinnerSelec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seleccion3Sala2 = spinnerSelec.getSelectedItem().toString();
                if (seleccion3Sala2.equals("Sin asignación")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
                    dialog.setTitle("Confirmar");
                    dialog.setMessage("¿Desea pasar por alto esta asignación?");
                    dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Utilidades.encargado3Sala2 = null;
                            Utilidades.ayudante3Sala2 = null;
                            idEncargado3Sala2 = null;
                            idAyudante3Sala2 = null;
                            llenarSalas();
                            dialog.dismiss();
                        }
                    });
                    dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapterEditSalas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilidades.encargado3Sala2Date = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getUltAsignacion();
                Utilidades.encargado3Sala2 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                idEncargado3Sala2 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                genero = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getGenero();

                if (!seleccion3Sala2.equals("Seleccionar Asignación")) {
                    if (!idEncargado3Sala2.equals(idLectorSala1) && !idEncargado3Sala2.equals(idEncargado1Sala1) && !idEncargado3Sala2.equals(idAyudante1Sala1)
                            && !idEncargado3Sala2.equals(idEncargado2Sala1) && !idEncargado3Sala2.equals(idAyudante2Sala1) && !idEncargado3Sala2.equals(idEncargado3Sala1)
                            && !idEncargado3Sala2.equals(idAyudante3Sala1) && !idEncargado3Sala2.equals(idLectorSala2) && !idEncargado3Sala2.equals(idEncargado1Sala2)
                            && !idEncargado3Sala2.equals(idAyudante1Sala2) && !idEncargado3Sala2.equals(idEncargado2Sala2) && !idEncargado3Sala2.equals(idAyudante2Sala2)) {
                        if (seleccion3Sala2.equals("Discurso") && genero.equals("Mujer")) {
                            Toast.makeText(getApplicationContext(), "Debe escoger un publicador masculino", Toast.LENGTH_SHORT).show();

                        } else if (seleccion3Sala2.equals("Discurso") && genero.equals("Hombre")) {
                            Utilidades.ayudante3Sala2 = null;
                            idAyudante3Sala2 = null;
                            Snackbar.make(v, "Escogió a: " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                            llenarSalas();

                        } else if (!seleccion3Sala2.equals("Discurso")) {
                            Snackbar.make(v, "Escogió a: " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                            progress = new ProgressDialog(EditarSalas.this);
                            progress.setMessage("Cargando...");
                            progress.setCancelable(false);
                            progress.show();
                            listaAyudante3Sala2();

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Este publicador ya tiene asignación esta semana", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Debe escoger el tipo de asignación", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(EditarSalas.this);
                progress.setMessage("Cargando...");
                progress.setCancelable(false);
                progress.show();
                listaEncargado2Sala2();
            }
        });
    }

    public void listaAyudante3Sala2() {
        etBuscar.setText("");
        spinnerSelec.setVisibility(View.INVISIBLE);
        tvTitle.setText("Ayudante Tercera Asignación: Sala 2");

        listPubs = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");

        Query query = reference.whereEqualTo(UtilidadesStatic.BD_HABILITADO, true).orderBy(UtilidadesStatic.BD_AYURECIENTE, Query.Direction.ASCENDING);

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

                        listPubs.add(publi);

                    }
                    adapterEditSalas.updateListSelec(listPubs);
                    progress.dismiss();
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        adapterEditSalas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilidades.ayudante3Sala2Date = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getUltAyudante();
                Utilidades.ayudante3Sala2 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                idAyudante3Sala2 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                generoAyudante = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getGenero();

                if (!idAyudante3Sala2.equals(idLectorSala1) && !idAyudante3Sala2.equals(idEncargado1Sala1) && !idAyudante3Sala2.equals(idAyudante1Sala1) && !idAyudante3Sala2.equals(idEncargado2Sala1)
                        && !idAyudante3Sala2.equals(idAyudante2Sala1) && !idAyudante3Sala2.equals(idEncargado3Sala1) && !idAyudante3Sala2.equals(idLectorSala2) && !idAyudante3Sala2.equals(idEncargado1Sala2)
                        && !idAyudante3Sala2.equals(idAyudante1Sala2) && !idAyudante3Sala2.equals(idEncargado2Sala2) && !idAyudante3Sala2.equals(idAyudante2Sala2) && !idAyudante3Sala2.equals(idEncargado3Sala2)) {
                    if (!genero.equals(generoAyudante)) {
                        if (seleccion3Sala2.equals("Primera Conversación")) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
                            dialog.setTitle("Confirmar");
                            dialog.setMessage("Recuerde que deben ser familiares los publicadores del sexo opuesto\n¿Está seguro?");
                            dialog.setIcon(R.drawable.ic_advertencia);
                            dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    llenarSalas();
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
                            AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
                            dialog.setTitle("¡Aviso!");
                            dialog.setMessage("No puede colocar publicadores del sexo opuesto para este tipo de asignación");
                            dialog.setIcon(R.drawable.ic_nopermitido);
                            dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    } else {
                        Snackbar.make(v, "Escogió a: " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                        llenarSalas();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Este publicador ya tiene asignación esta semana", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(EditarSalas.this);
                progress.setMessage("Cargando...");
                progress.setCancelable(false);
                progress.show();
                listaEncargado2Sala2();
            }
        });
    }

    public void llenarSalas() {
        Intent myIntent = new Intent(EditarSalas.this, ResumenSalas.class);
        Bundle myBundle = new Bundle();

        myBundle.putString("idLectorSala1", idLectorSala1);
        myBundle.putString("idEncargado1Sala1", idEncargado1Sala1);
        myBundle.putString("idAyudante1Sala1", idAyudante1Sala1);
        myBundle.putString("idEncargado2Sala1", idEncargado2Sala1);
        myBundle.putString("idAyudante2Sala1", idAyudante2Sala1);
        myBundle.putString("idEncargado3Sala1", idEncargado3Sala1);
        myBundle.putString("idAyudante3Sala1", idAyudante3Sala1);
        myBundle.putString("asignacion1Sala1", seleccion1Sala1);
        myBundle.putString("asignacion2Sala1", seleccion2Sala1);
        myBundle.putString("asignacion3Sala1", seleccion3Sala1);

        myBundle.putString("idLectorSala2", idLectorSala2);
        myBundle.putString("idEncargado1Sala2", idEncargado1Sala2);
        myBundle.putString("idAyudante1Sala2", idAyudante1Sala2);
        myBundle.putString("idEncargado2Sala2", idEncargado2Sala2);
        myBundle.putString("idAyudante2Sala2", idAyudante2Sala2);
        myBundle.putString("idEncargado3Sala2", idEncargado3Sala2);
        myBundle.putString("idAyudante3Sala2", idAyudante3Sala2);
        myBundle.putString("asignacion1Sala2", seleccion1Sala2);
        myBundle.putString("asignacion2Sala2", seleccion2Sala2);
        myBundle.putString("asignacion3Sala2", seleccion3Sala2);

        myBundle.putBoolean("visita", visita);
        myBundle.putBoolean("asamblea", asamblea);
        myBundle.putBoolean("activarSala2", activarSala2);
        myBundle.putLong("fecha", fechaSelec.getTime());
        myBundle.putInt("semana", semanaSelec);

        myIntent.putExtras(myBundle);

        startActivity(myIntent);
    }

    public void confirmacionSala2 () {
        if (visita) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
            dialog.setTitle("Confirmar");
            dialog.setMessage("¿Desea programar la Sala 2 durante la Visita?");
            dialog.setIcon(R.drawable.ic_select_image);
            dialog.setCancelable(false);
            dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    progress = new ProgressDialog(EditarSalas.this);
                    progress.setMessage("Cargando...");
                    progress.setCancelable(false);
                    progress.show();
                    listaLecturaSala2();
                }
            });
            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    idLectorSala2 = null;
                    idEncargado1Sala2 = null;
                    idAyudante1Sala2 = null;
                    idEncargado2Sala2 = null;
                    idAyudante2Sala2 = null;
                    idEncargado3Sala2 = null;
                    idAyudante3Sala2 = null;
                    activarSala2 = false;
                    llenarSalas();
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else {
            progress = new ProgressDialog(EditarSalas.this);
            progress.setMessage("Cargando...");
            progress.setCancelable(false);
            progress.show();
            listaLecturaSala2();
        }
    }

    public void filtros() {
        if (fechaSelec != null) {
            if (fechaSelec.after(fechaActual)) {
               if (cbVisita.isChecked() && cbAsamblea.isChecked()) {
                   Toast.makeText(this, "No puede haber Visita y Asamblea el mismo día", Toast.LENGTH_LONG).show();
               } else {
                   if (cbAsamblea.isChecked()) {
                       idLectorSala1 = null;
                       idEncargado1Sala1 = null;
                       idAyudante1Sala1 = null;
                       idEncargado2Sala1 = null;
                       idAyudante2Sala1 = null;
                       idEncargado3Sala1 = null;
                       idAyudante3Sala1 = null;
                       idLectorSala2 = null;
                       idEncargado1Sala2 = null;
                       idAyudante1Sala2 = null;
                       idEncargado2Sala2 = null;
                       idAyudante2Sala2 = null;
                       idEncargado3Sala2 = null;
                       idAyudante3Sala2 = null;
                       seleccion1Sala1 = null;
                       seleccion2Sala1 = null;
                       seleccion3Sala1 = null;
                       seleccion1Sala2 = null;
                       seleccion2Sala2 = null;
                       seleccion3Sala2 = null;
                       asamblea = true;
                       Utilidades.asamblea = true;
                       llenarSalas();
                   } else if (cbVisita.isChecked()){
                       visita = true;
                       Utilidades.visita = true;
                       progress = new ProgressDialog(this);
                       progress.setMessage("Cargando...");
                       progress.setCancelable(false);
                       progress.show();
                       listaLecturaSala1();

                   } else if (!cbVisita.isChecked() && !cbAsamblea.isChecked()) {
                       progress = new ProgressDialog(this);
                       progress.setMessage("Cargando...");
                       progress.setCancelable(false);
                       progress.show();
                       listaLecturaSala1();
                   }
               }
            } else {
                Toast.makeText(this, "No puede programar una fecha anterior a la actual", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Debe escoger un día", Toast.LENGTH_SHORT).show();
        }
    }

    private void listaBuscar (String text) {
        String userInput = text.toLowerCase();
        ArrayList<ConstructorPublicadores> newList = new ArrayList<>();

        for (ConstructorPublicadores name : listPubs) {

            if (name.getNombrePublicador().toLowerCase().contains(userInput) || name.getApellidoPublicador().toLowerCase().contains(userInput)) {

                newList.add(name);
            }
        }

        adapterEditSalas.updateListSelec(newList);
    }

}
