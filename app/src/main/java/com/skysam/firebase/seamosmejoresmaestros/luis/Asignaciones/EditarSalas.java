package com.skysam.firebase.seamosmejoresmaestros.luis.Asignaciones;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EditarSalas extends AppCompatActivity {

    private RecyclerView recyclerEditSalas;
    private ArrayList<PublicadoresConstructor> listPubs;
    private EditSalasAdapter editSalasAdapter;
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
    private boolean visita, asamblea;
    private String seleccion1, seleccion2, seleccion3, idLector, idEncargado1, idAyudante1, idEncargado2, idAyudante2, idEncargado3, idAyudante3;
    private String nombreEncargado1, nombreEncargado2, nombreEncargado3, nombreAyudante1, nombreAyudante2, nombreAyudante3, nombreLector;
    private String genero, generoAyudante;
    private int semanaSelec;
    private ProgressBar progressBarEditSalas;
    private long fechaLunesLong;


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
        progressBarEditSalas = findViewById(R.id.progressBarEditSalas);

        recyclerEditSalas = (RecyclerView) findViewById(R.id.recyclerSeleccionar);
        recyclerEditSalas.setLayoutManager(new LinearLayoutManager(this));
        recyclerEditSalas.setHasFixedSize(true);
        listPubs = new ArrayList<>();
        editSalasAdapter = new EditSalasAdapter(listPubs, this);
        recyclerEditSalas.setAdapter(editSalasAdapter);

        calendarView.setFirstDayOfWeek(Calendar.MONDAY);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean temaOscuro = sharedPreferences.getBoolean("activarOscuro", false);
        if (temaOscuro) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {

            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }

        Calendar almanaque = Calendar.getInstance();
        int diaActual = almanaque.get(Calendar.DAY_OF_MONTH);
        int mesActual = almanaque.get(Calendar.MONTH);
        int anualActual = almanaque.get(Calendar.YEAR);
        almanaque.set(anualActual, mesActual, diaActual);
        fechaActual = almanaque.getTime();
        visita = false;
        asamblea = false;

        String [] spSelecccionar = {"Seleccionar Asignación", "Primera Conversación", "Primera Revisita", "Segunda Revisita","Tercera Revisita", "Curso Bíblico", "Discurso", "Sin asignación"};
        adapterSpSeleccionar = new ArrayAdapter<String>(this, R.layout.spinner_selec, spSelecccionar);
        spinnerSelec.setAdapter(adapterSpSeleccionar);

        fabBack = (FloatingActionButton) findViewById(R.id.fabBackSelec);
        FloatingActionButton fabClose = (FloatingActionButton) findViewById(R.id.fabCloseSelec);
        fabClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
                dialog.setTitle("Confirmar");
                dialog.setMessage("¿Desea salir? Se perderán las asignaciones programadas de esta semana");
                dialog.setIcon(R.drawable.ic_advertencia);
                dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
        dialog.setTitle("Confirmar");
        dialog.setMessage("¿Desea salir? Se perderán las asignaciones programadas de esta semana");
        dialog.setIcon(R.drawable.ic_advertencia);
        dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
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
                int horaSelec = calendario.get(Calendar.HOUR_OF_DAY);
                int minutoSelec = calendario.get(Calendar.MINUTE);
                int segundoSelec = calendario.get(Calendar.SECOND);
                int milisegundoSeec = calendario.get(Calendar.MILLISECOND);
                int diaSemana = calendario.get(Calendar.DAY_OF_WEEK);

                long horaLong = horaSelec * 60 * 60 * 1000;
                long minLong = minutoSelec * 60 * 1000;
                long segLong = segundoSelec * 1000;
                long fechaSelecLong = fechaSelec.getTime();

                switch (diaSemana) {
                    case Calendar.MONDAY:
                        fechaLunesLong = fechaSelecLong - horaLong - minLong - segLong - milisegundoSeec;
                        break;
                    case Calendar.TUESDAY:
                        fechaLunesLong = fechaSelecLong - (24 * 60 * 60 * 1000) - horaLong - minLong - segLong - milisegundoSeec;
                        break;
                    case Calendar.WEDNESDAY:
                        fechaLunesLong = fechaSelecLong - (2 * 24 * 60 * 60 * 1000) - horaLong - minLong - segLong - milisegundoSeec;
                        break;
                    case Calendar.THURSDAY:
                        fechaLunesLong = fechaSelecLong - (3 * 24 * 60 * 60 * 1000) - horaLong - minLong - segLong - milisegundoSeec;
                        break;
                    case Calendar.FRIDAY:
                        fechaLunesLong = fechaSelecLong - (4 * 24 * 60 * 60 * 1000) - horaLong - minLong - segLong - milisegundoSeec;
                        break;
                    case Calendar.SATURDAY:
                        fechaLunesLong = fechaSelecLong - (5 * 24 * 60 * 60 * 1000) - horaLong - minLong - segLong - milisegundoSeec;
                        break;
                    case Calendar.SUNDAY:
                        fechaLunesLong = fechaSelecLong - (6 * 24 * 60 * 60 * 1000) - horaLong - minLong - segLong - milisegundoSeec;
                        break;
                    default:
                        break;
                }

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
                fechaDisponible();
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
        tvTitle.setText("Lector");

        listPubs = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection(VariablesEstaticas.BD_PUBLICADORES);

        Query query = reference.whereEqualTo(VariablesEstaticas.BD_GENERO, "Hombre").whereEqualTo(VariablesEstaticas.BD_HABILITADO, true).orderBy(VariablesEstaticas.BD_DISRECIENTE, Query.Direction.ASCENDING);

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

                        listPubs.add(publi);

                    }
                    editSalasAdapter.updateListSelec(listPubs);
                    progressBarEditSalas.setVisibility(View.GONE);
                } else {
                    progressBarEditSalas.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
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
                String userInput = s.toString().toLowerCase();
                final ArrayList<PublicadoresConstructor> newList = new ArrayList<>();

                for (PublicadoresConstructor name : listPubs) {

                    if (name.getNombrePublicador().toLowerCase().contains(userInput) || name.getApellidoPublicador().toLowerCase().contains(userInput)) {

                        newList.add(name);
                    }
                }

                editSalasAdapter.updateListSelec(newList);
                editSalasAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nombreLector = newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                        idLector = newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                        Snackbar.make(v, "Escogió a: " + newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                        progressBarEditSalas.setVisibility(View.VISIBLE);
                        listaEncargado1Sala1();
                    }
                });
            }
        });

        editSalasAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombreLector = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                idLector = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                Snackbar.make(v, "Escogió a: " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                progressBarEditSalas.setVisibility(View.VISIBLE);
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
        tvTitle.setText("Encargado Primera Asignación");

        listPubs = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection(VariablesEstaticas.BD_PUBLICADORES);

        Query query = reference.whereEqualTo(VariablesEstaticas.BD_HABILITADO, true).orderBy(VariablesEstaticas.BD_DISRECIENTE, Query.Direction.ASCENDING);

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

                        listPubs.add(publi);

                    }
                    editSalasAdapter.updateListSelec(listPubs);
                    progressBarEditSalas.setVisibility(View.GONE);
                } else {
                    progressBarEditSalas.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinnerSelec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seleccion1 = spinnerSelec.getSelectedItem().toString();
                if (seleccion1.equals("Sin asignación")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
                    dialog.setTitle("Confirmar");
                    dialog.setMessage("¿Desea pasar por alto esta asignación?");
                    dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            nombreEncargado1 = null;
                            nombreAyudante1 = null;
                            idEncargado1 = null;
                            idAyudante1 = null;

                            progressBarEditSalas.setVisibility(View.VISIBLE);
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
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String userInput = s.toString().toLowerCase();
                final ArrayList<PublicadoresConstructor> newList = new ArrayList<>();

                for (PublicadoresConstructor name : listPubs) {

                    if (name.getNombrePublicador().toLowerCase().contains(userInput) || name.getApellidoPublicador().toLowerCase().contains(userInput)) {

                        newList.add(name);
                    }
                }

                editSalasAdapter.updateListSelec(newList);
                editSalasAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nombreEncargado1 = newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                        idEncargado1 = newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                        genero = newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getGenero();

                        if (!seleccion1.equals("Seleccionar Asignación")) {
                            if (!idEncargado1.equals(idLector)) {
                                if (seleccion1.equals("Discurso") && genero.equals("Mujer")) {
                                    Toast.makeText(getApplicationContext(), "Debe escoger un publicador masculino", Toast.LENGTH_SHORT).show();

                                } else if (seleccion1.equals("Discurso") && genero.equals("Hombre")) {
                                    idAyudante1 = null;
                                    nombreAyudante1 = null;
                                    Snackbar.make(v, "Escogió a: " + newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                                    progressBarEditSalas.setVisibility(View.VISIBLE);
                                    listaEncargado2Sala1();

                                } else if (!seleccion1.equals("Discurso")) {
                                    Snackbar.make(v, "Escogió a: " + newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                                    progressBarEditSalas.setVisibility(View.VISIBLE);
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
            }
        });

        editSalasAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombreEncargado1 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                idEncargado1 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                genero = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getGenero();

                if (!seleccion1.equals("Seleccionar Asignación")) {
                    if (!idEncargado1.equals(idLector)) {
                        if (seleccion1.equals("Discurso") && genero.equals("Mujer")) {
                            Toast.makeText(getApplicationContext(), "Debe escoger un publicador masculino", Toast.LENGTH_SHORT).show();

                        } else if (seleccion1.equals("Discurso") && genero.equals("Hombre")) {
                            idAyudante1 = null;
                            nombreAyudante1 = null;
                            Snackbar.make(v, "Escogió a: " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                            progressBarEditSalas.setVisibility(View.VISIBLE);
                            listaEncargado2Sala1();

                        } else if (!seleccion1.equals("Discurso")) {
                            Snackbar.make(v, "Escogió a: " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                            progressBarEditSalas.setVisibility(View.VISIBLE);
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
                progressBarEditSalas.setVisibility(View.VISIBLE);
                listaLecturaSala1();
            }
        });
    }

    public void listaAyudante1Sala1() {
        etBuscar.setText("");
        spinnerSelec.setVisibility(View.INVISIBLE);
        tvTitle.setText("Ayudante Primera Asignación");

        listPubs = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection(VariablesEstaticas.BD_PUBLICADORES);

        Query query = reference.whereEqualTo(VariablesEstaticas.BD_HABILITADO, true).orderBy(VariablesEstaticas.BD_AYURECIENTE, Query.Direction.ASCENDING);

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

                        listPubs.add(publi);

                    }
                    editSalasAdapter.updateListSelec(listPubs);
                    progressBarEditSalas.setVisibility(View.GONE);
                } else {
                    progressBarEditSalas.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
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
                String userInput = s.toString().toLowerCase();
                final ArrayList<PublicadoresConstructor> newList = new ArrayList<>();

                for (PublicadoresConstructor name : listPubs) {

                    if (name.getNombrePublicador().toLowerCase().contains(userInput) || name.getApellidoPublicador().toLowerCase().contains(userInput)) {

                        newList.add(name);
                    }
                }

                editSalasAdapter.updateListSelec(newList);
                editSalasAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nombreAyudante1 = newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                        idAyudante1 = newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                        generoAyudante = newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getGenero();

                        if (!idAyudante1.equals(idLector) && !idAyudante1.equals(idEncargado1)) {
                            if (!genero.equals(generoAyudante)) {
                                if (seleccion1.equals("Primera Conversación")) {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
                                    dialog.setTitle("Confirmar");
                                    dialog.setMessage("Recuerde que deben ser familiares los publicadores del sexo opuesto\n¿Está seguro?");
                                    dialog.setIcon(R.drawable.ic_advertencia);
                                    dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            progressBarEditSalas.setVisibility(View.VISIBLE);
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
                                Snackbar.make(v, "Escogió a: " + newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                                progressBarEditSalas.setVisibility(View.VISIBLE);
                                listaEncargado2Sala1();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Este publicador ya tiene asignación esta semana", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        editSalasAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombreAyudante1 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                idAyudante1 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                generoAyudante = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getGenero();

                if (!idAyudante1.equals(idLector) && !idAyudante1.equals(idEncargado1)) {
                    if (!genero.equals(generoAyudante)) {
                        if (seleccion1.equals("Primera Conversación")) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
                            dialog.setTitle("Confirmar");
                            dialog.setMessage("Recuerde que deben ser familiares los publicadores del sexo opuesto\n¿Está seguro?");
                            dialog.setIcon(R.drawable.ic_advertencia);
                            dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    progressBarEditSalas.setVisibility(View.VISIBLE);
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
                        progressBarEditSalas.setVisibility(View.VISIBLE);
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
                progressBarEditSalas.setVisibility(View.VISIBLE);
                listaEncargado1Sala1();
            }
        });
    }

    public void listaEncargado2Sala1 () {
        etBuscar.setText("");
        spinnerSelec.setVisibility(View.VISIBLE);
        spinnerSelec.setAdapter(adapterSpSeleccionar);
        tvTitle.setText("Encargado Segunda Asignación");

        listPubs = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection(VariablesEstaticas.BD_PUBLICADORES);

        Query query = reference.whereEqualTo(VariablesEstaticas.BD_HABILITADO, true).orderBy(VariablesEstaticas.BD_DISRECIENTE, Query.Direction.ASCENDING);

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

                        listPubs.add(publi);

                    }
                    editSalasAdapter.updateListSelec(listPubs);
                    progressBarEditSalas.setVisibility(View.GONE);
                } else {
                    progressBarEditSalas.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinnerSelec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seleccion2 = spinnerSelec.getSelectedItem().toString();
                if (seleccion2.equals("Sin asignación")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
                    dialog.setTitle("Confirmar");
                    dialog.setMessage("¿Desea pasar por alto esta asignación?");
                    dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            nombreEncargado2 = null;
                            nombreAyudante2 = null;
                            idEncargado2 = null;
                            idAyudante2 = null;
                            progressBarEditSalas.setVisibility(View.VISIBLE);
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

        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String userInput = s.toString().toLowerCase();
                final ArrayList<PublicadoresConstructor> newList = new ArrayList<>();

                for (PublicadoresConstructor name : listPubs) {

                    if (name.getNombrePublicador().toLowerCase().contains(userInput) || name.getApellidoPublicador().toLowerCase().contains(userInput)) {

                        newList.add(name);
                    }
                }

                editSalasAdapter.updateListSelec(newList);
                editSalasAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nombreEncargado2 = newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                        idEncargado2 = newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                        genero = newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getGenero();

                        if (!seleccion2.equals("Seleccionar Asignación")) {
                            if (!idEncargado2.equals(idLector) && !idEncargado2.equals(idEncargado1) && !idEncargado2.equals(idAyudante1)) {
                                if (seleccion2.equals("Discurso") && genero.equals("Mujer")) {
                                    Toast.makeText(getApplicationContext(), "Debe escoger un publicador masculino", Toast.LENGTH_SHORT).show();

                                } else if (seleccion2.equals("Discurso") && genero.equals("Hombre")) {
                                    idAyudante2 = null;
                                    nombreAyudante2 = null;
                                    Snackbar.make(v, "Escogió a: " + newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                                    progressBarEditSalas.setVisibility(View.VISIBLE);
                                    listaEncargado3Sala1();

                                } else if (!seleccion2.equals("Discurso")) {
                                    Snackbar.make(v, "Escogió a: " + newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                                    progressBarEditSalas.setVisibility(View.VISIBLE);
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
            }
        });

        editSalasAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombreEncargado2 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                idEncargado2 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                genero = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getGenero();

                if (!seleccion2.equals("Seleccionar Asignación")) {
                    if (!idEncargado2.equals(idLector) && !idEncargado2.equals(idEncargado1) && !idEncargado2.equals(idAyudante1)) {
                        if (seleccion2.equals("Discurso") && genero.equals("Mujer")) {
                            Toast.makeText(getApplicationContext(), "Debe escoger un publicador masculino", Toast.LENGTH_SHORT).show();

                        } else if (seleccion2.equals("Discurso") && genero.equals("Hombre")) {
                            idAyudante2 = null;
                            nombreAyudante2 = null;
                            Snackbar.make(v, "Escogió a: " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                            progressBarEditSalas.setVisibility(View.VISIBLE);
                            listaEncargado3Sala1();

                        } else if (!seleccion2.equals("Discurso")) {
                            Snackbar.make(v, "Escogió a: " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                            progressBarEditSalas.setVisibility(View.VISIBLE);
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
                progressBarEditSalas.setVisibility(View.VISIBLE);
                listaEncargado1Sala1();
            }
        });
    }

    public void listaAyudante2Sala1() {
        etBuscar.setText("");
        spinnerSelec.setVisibility(View.INVISIBLE);
        tvTitle.setText("Ayudante Segunda Asignación");

        listPubs = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection(VariablesEstaticas.BD_PUBLICADORES);

        Query query = reference.whereEqualTo(VariablesEstaticas.BD_HABILITADO, true).orderBy(VariablesEstaticas.BD_AYURECIENTE, Query.Direction.ASCENDING);

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

                        listPubs.add(publi);

                    }
                    editSalasAdapter.updateListSelec(listPubs);
                    progressBarEditSalas.setVisibility(View.GONE);
                } else {
                    progressBarEditSalas.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
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
                String userInput = s.toString().toLowerCase();
                final ArrayList<PublicadoresConstructor> newList = new ArrayList<>();

                for (PublicadoresConstructor name : listPubs) {

                    if (name.getNombrePublicador().toLowerCase().contains(userInput) || name.getApellidoPublicador().toLowerCase().contains(userInput)) {

                        newList.add(name);
                    }
                }

                editSalasAdapter.updateListSelec(newList);
                editSalasAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nombreAyudante2 = newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                        idAyudante2 = newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                        generoAyudante = newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getGenero();

                        if (!idAyudante2.equals(idLector) && !idAyudante2.equals(idEncargado1) && !idAyudante2.equals(idAyudante1) && !idAyudante2.equals(idEncargado2)) {
                            if (!genero.equals(generoAyudante)) {
                                if (seleccion2.equals("Primera Conversación")) {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
                                    dialog.setTitle("Confirmar");
                                    dialog.setMessage("Recuerde que deben ser familiares los publicadores del sexo opuesto\n¿Está seguro?");
                                    dialog.setIcon(R.drawable.ic_advertencia);
                                    dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            progressBarEditSalas.setVisibility(View.VISIBLE);
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
                                Snackbar.make(v, "Escogió a: " + newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                                progressBarEditSalas.setVisibility(View.VISIBLE);
                                listaEncargado3Sala1();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Este publicador ya tiene asignación esta semana", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        editSalasAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombreAyudante2 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                idAyudante2 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                generoAyudante = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getGenero();

                if (!idAyudante2.equals(idLector) && !idAyudante2.equals(idEncargado1) && !idAyudante2.equals(idAyudante1) && !idAyudante2.equals(idEncargado2)) {
                    if (!genero.equals(generoAyudante)) {
                        if (seleccion2.equals("Primera Conversación")) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
                            dialog.setTitle("Confirmar");
                            dialog.setMessage("Recuerde que deben ser familiares los publicadores del sexo opuesto\n¿Está seguro?");
                            dialog.setIcon(R.drawable.ic_advertencia);
                            dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    progressBarEditSalas.setVisibility(View.VISIBLE);
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
                        progressBarEditSalas.setVisibility(View.VISIBLE);
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
                progressBarEditSalas.setVisibility(View.VISIBLE);
                listaEncargado2Sala1();
            }
        });
    }

    public void listaEncargado3Sala1() {
        etBuscar.setText("");
        spinnerSelec.setVisibility(View.VISIBLE);
        spinnerSelec.setAdapter(adapterSpSeleccionar);
        tvTitle.setText("Encargado Tercera Asignación");

        listPubs = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection(VariablesEstaticas.BD_PUBLICADORES);

        Query query = reference.whereEqualTo(VariablesEstaticas.BD_HABILITADO, true).orderBy(VariablesEstaticas.BD_DISRECIENTE, Query.Direction.ASCENDING);

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

                        listPubs.add(publi);

                    }
                    editSalasAdapter.updateListSelec(listPubs);
                    progressBarEditSalas.setVisibility(View.GONE);
                } else {
                    progressBarEditSalas.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinnerSelec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seleccion3 = spinnerSelec.getSelectedItem().toString();
                if (seleccion3.equals("Sin asignación")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
                    dialog.setTitle("Confirmar");
                    dialog.setMessage("¿Desea pasar por alto esta asignación?");
                    dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            nombreEncargado3 = null;
                            nombreAyudante3 = null;
                            idEncargado3 = null;
                            idAyudante3 = null;
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

        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String userInput = s.toString().toLowerCase();
                final ArrayList<PublicadoresConstructor> newList = new ArrayList<>();

                for (PublicadoresConstructor name : listPubs) {

                    if (name.getNombrePublicador().toLowerCase().contains(userInput) || name.getApellidoPublicador().toLowerCase().contains(userInput)) {

                        newList.add(name);
                    }
                }

                editSalasAdapter.updateListSelec(newList);
                editSalasAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nombreEncargado3 = newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                        idEncargado3 = newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                        genero = newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getGenero();

                        if (!seleccion3.equals("Seleccionar Asignación")) {
                            if (!idEncargado3.equals(idLector) && !idEncargado3.equals(idEncargado1) && !idEncargado3.equals(idAyudante1)
                                    && !idEncargado3.equals(idEncargado2) && !idEncargado3.equals(idAyudante2)) {
                                if (seleccion3.equals("Discurso") && genero.equals("Mujer")) {
                                    Toast.makeText(getApplicationContext(), "Debe escoger un publicador masculino", Toast.LENGTH_SHORT).show();

                                } else if (seleccion3.equals("Discurso") && genero.equals("Hombre")) {
                                    nombreAyudante3 = null;
                                    idAyudante3 = null;
                                    Snackbar.make(v, "Escogió a: " + newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                                    llenarSalas();

                                } else if (!seleccion3.equals("Discurso")) {
                                    Snackbar.make(v, "Escogió a: " + newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                                    progressBarEditSalas.setVisibility(View.VISIBLE);
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
            }
        });

        editSalasAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombreEncargado3 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                idEncargado3 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                genero = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getGenero();

                if (!seleccion3.equals("Seleccionar Asignación")) {
                    if (!idEncargado3.equals(idLector) && !idEncargado3.equals(idEncargado1) && !idEncargado3.equals(idAyudante1)
                    && !idEncargado3.equals(idEncargado2) && !idEncargado3.equals(idAyudante2)) {
                        if (seleccion3.equals("Discurso") && genero.equals("Mujer")) {
                            Toast.makeText(getApplicationContext(), "Debe escoger un publicador masculino", Toast.LENGTH_SHORT).show();

                        } else if (seleccion3.equals("Discurso") && genero.equals("Hombre")) {
                            nombreAyudante3 = null;
                            idAyudante3 = null;
                            Snackbar.make(v, "Escogió a: " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                            llenarSalas();

                        } else if (!seleccion3.equals("Discurso")) {
                            Snackbar.make(v, "Escogió a: " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                            progressBarEditSalas.setVisibility(View.VISIBLE);
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
                progressBarEditSalas.setVisibility(View.VISIBLE);
                listaEncargado2Sala1();
            }
        });
    }

    public void listaAyudante3Sala1 () {
        etBuscar.setText("");
        spinnerSelec.setVisibility(View.INVISIBLE);
        tvTitle.setText("Ayudante Tercera Asignación");

        listPubs = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection(VariablesEstaticas.BD_PUBLICADORES);

        Query query = reference.whereEqualTo(VariablesEstaticas.BD_HABILITADO, true).orderBy(VariablesEstaticas.BD_AYURECIENTE, Query.Direction.ASCENDING);

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

                        listPubs.add(publi);

                    }
                    editSalasAdapter.updateListSelec(listPubs);
                    progressBarEditSalas.setVisibility(View.GONE);
                } else {
                    progressBarEditSalas.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
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
                String userInput = s.toString().toLowerCase();
                final ArrayList<PublicadoresConstructor> newList = new ArrayList<>();

                for (PublicadoresConstructor name : listPubs) {

                    if (name.getNombrePublicador().toLowerCase().contains(userInput) || name.getApellidoPublicador().toLowerCase().contains(userInput)) {

                        newList.add(name);
                    }
                }

                editSalasAdapter.updateListSelec(newList);
                editSalasAdapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nombreAyudante3 = newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                        idAyudante3 = newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                        generoAyudante = newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getGenero();

                        if (!idAyudante3.equals(idLector) && !idAyudante3.equals(idEncargado1) && !idAyudante3.equals(idAyudante1) && !idAyudante3.equals(idEncargado2)
                                && !idAyudante3.equals(idAyudante2) && !idAyudante3.equals(idEncargado3)) {
                            if (!genero.equals(generoAyudante)) {
                                if (seleccion3.equals("Primera Conversación")) {
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
                                Snackbar.make(v, "Escogió a: " + newList.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador(), Snackbar.LENGTH_LONG).show();
                                llenarSalas();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Este publicador ya tiene asignación esta semana", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        editSalasAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombreAyudante3 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getNombrePublicador() + " " + listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getApellidoPublicador();
                idAyudante3 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                generoAyudante = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getGenero();

                if (!idAyudante3.equals(idLector) && !idAyudante3.equals(idEncargado1) && !idAyudante3.equals(idAyudante1) && !idAyudante3.equals(idEncargado2)
                && !idAyudante3.equals(idAyudante2) && !idAyudante3.equals(idEncargado3)) {
                    if (!genero.equals(generoAyudante)) {
                        if (seleccion3.equals("Primera Conversación")) {
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
                progressBarEditSalas.setVisibility(View.VISIBLE);
                listaEncargado3Sala1();
            }
        });
    }



    public void llenarSalas() {
        Intent myIntent = new Intent(EditarSalas.this, ResumenSalas.class);
        Bundle myBundle = new Bundle();

        myBundle.putString("idLector", idLector);
        myBundle.putString("idEncargado1", idEncargado1);
        myBundle.putString("idAyudante1", idAyudante1);
        myBundle.putString("idEncargado2", idEncargado2);
        myBundle.putString("idAyudante2", idAyudante2);
        myBundle.putString("idEncargado3", idEncargado3);
        myBundle.putString("idAyudante3", idAyudante3);
        myBundle.putString("nombreLector", nombreLector);
        myBundle.putString("nombreEncargado1", nombreEncargado1);
        myBundle.putString("nombreEncargado2", nombreEncargado2);
        myBundle.putString("nombreEncargado3", nombreEncargado3);
        myBundle.putString("nombreAyudante1", nombreAyudante1);
        myBundle.putString("nombreAyudante2", nombreAyudante2);
        myBundle.putString("nombreAyudante3", nombreAyudante3);
        myBundle.putString("asignacion1", seleccion1);
        myBundle.putString("asignacion2", seleccion2);
        myBundle.putString("asignacion3", seleccion3);


        myBundle.putBoolean("visita", visita);
        myBundle.putBoolean("asamblea", asamblea);
        myBundle.putLong("fecha", fechaSelec.getTime());
        myBundle.putLong("fechaLunes", fechaLunesLong);
        myBundle.putInt("semana", semanaSelec);

        myIntent.putExtras(myBundle);

        startActivity(myIntent);
    }


    public void filtros() {
        if (fechaSelec != null) {
            if (fechaSelec.after(fechaActual)) {
               if (cbVisita.isChecked() && cbAsamblea.isChecked()) {
                   Toast.makeText(this, "No puede programarse estos dos eventos para el mismo día", Toast.LENGTH_LONG).show();
               } else {
                   if (cbAsamblea.isChecked()) {
                       idLector = null;
                       idEncargado1 = null;
                       idAyudante1 = null;
                       idEncargado2 = null;
                       idAyudante2 = null;
                       idEncargado3 = null;
                       idAyudante3 = null;
                       seleccion1 = null;
                       seleccion2 = null;
                       seleccion3 = null;
                       asamblea = true;
                       llenarSalas();
                   } else if (cbVisita.isChecked()){
                       visita = true;
                       progressBarEditSalas.setVisibility(View.VISIBLE);
                       listaLecturaSala1();
                   } else if (!cbVisita.isChecked() && !cbAsamblea.isChecked()) {
                       progressBarEditSalas.setVisibility(View.VISIBLE);
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
        ArrayList<PublicadoresConstructor> newList = new ArrayList<>();

        for (PublicadoresConstructor name : listPubs) {

            if (name.getNombrePublicador().toLowerCase().contains(userInput) || name.getApellidoPublicador().toLowerCase().contains(userInput)) {

                newList.add(name);
            }
        }

        editSalasAdapter.updateListSelec(newList);
    }

    public void fechaDisponible() {
        String idSemana = String.valueOf(fechaLunesLong);
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference reference = dbFirestore.collection(VariablesEstaticas.BD_SALA);

        reference.document(idSemana).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                                AlertDialog.Builder dialog = new AlertDialog.Builder(EditarSalas.this);
                                dialog.setTitle("¡Aviso!");
                                dialog.setMessage("Esta semana ya fue programada.\nSi desea modificarla por completo, debe eliminarla primero y luego programarla como nueva.\nEn caso de querer susituir a alguno de los publicadores, puede hacerlo desde Asignaciones directamente");
                                dialog.setIcon(R.drawable.ic_nopermitido);
                                dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                    } else {
                        filtros();
                    }
                }
            }
        });

    }

}
