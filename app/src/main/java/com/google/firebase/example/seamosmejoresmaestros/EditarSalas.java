package com.google.firebase.example.seamosmejoresmaestros;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private boolean visita;
    private String seleccion1Sala1, seleccion2Sala1, seleccion3Sala1, idLectorSala1, idEncargado1Sala1, idAyudante1Sala1, idEncargado2Sala1, idAyudante2Sala1, idEncargado3Sala1, idAyudante3Sala1;
    private String seleccion1Sala2, seleccion2Sala2, seleccion3Sala2, idLectorSala2, idEncargado1Sala2, idAyudante1Sala2, idEncargado2Sala2, idAyudante2Sala2, idEncargado3Sala2, idAyudante3Sala2;
    private String genero, generoAyudante;
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
                idEncargado1Sala1 = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getIdPublicador();
                genero = listPubs.get(recyclerEditSalas.getChildAdapterPosition(v)).getGenero();

                if (!seleccion1Sala1.equals("Seleccionar Asignación")) {
                    if (!idEncargado1Sala1.equals(idLectorSala1)) {
                        if (seleccion1Sala1.equals("Discurso") && genero.equals("Mujer")) {
                            Toast.makeText(getApplicationContext(), "Debe escoger un publicador masculino", Toast.LENGTH_SHORT).show();

                        } else if (seleccion1Sala1.equals("Discurso") && genero.equals("Hombre")) {
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

    public void filtros() {
        if (fechaSelec != null) {
            if (fechaSelec.after(fechaActual)) {
               if (cbVisita.isChecked() && cbAsamblea.isChecked()) {
                   Toast.makeText(this, "No puede haber Visita y Asamblea el mismo día", Toast.LENGTH_LONG).show();
               } else {
                   if (cbAsamblea.isChecked()) {
                       Toast.makeText(this, "Asamblea", Toast.LENGTH_SHORT).show();
                   } else if (cbVisita.isChecked()){
                       Toast.makeText(this, "Visita", Toast.LENGTH_SHORT).show();
                       visita = true;
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

}
