package com.google.firebase.example.seamosmejoresmaestros;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ResumenSalas extends AppCompatActivity {

    private String seleccion1Sala1, seleccion2Sala1, seleccion3Sala1, idLectorSala1, idEncargado1Sala1, idAyudante1Sala1, idEncargado2Sala1, idAyudante2Sala1, idEncargado3Sala1, idAyudante3Sala1;
    private String seleccion1Sala2, seleccion2Sala2, seleccion3Sala2, idLectorSala2, idEncargado1Sala2, idAyudante1Sala2, idEncargado2Sala2, idAyudante2Sala2, idEncargado3Sala2, idAyudante3Sala2;
    private long fecha;
    private boolean visita, asamblea, activarSala2;
    private int semanaSelec;
    private TextView tvFecha, tvLecturaSala1, tvLecturaSala2, tvAsignacion1Sala1, tvAsignacion2Sala1, tvAsignacion3Sala1, tvAsignacion1Sala2, tvAsignacion2Sala2, tvAsignacion3Sala2, tituloSala1, tituloSala2;
    private TextView tvLectorSala1, tvEncargado1Sala1, tvAyudante1Sala1, tvEncargado2Sala1, tvAyudante2Sala1, tvEncargado3Sala1, tvAyudante3Sala1;
    private TextView tvLectorSala2, tvEncargado1Sala2, tvAyudante1Sala2, tvEncargado2Sala2, tvAyudante2Sala2, tvEncargado3Sala2, tvAyudante3Sala2;
    private Date fechaDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_salas);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSalas);
        setSupportActionBar(toolbar);

        tvFecha = (TextView) findViewById(R.id.fechaSala);
        tituloSala1 = (TextView) findViewById(R.id.titleSala1);
        tituloSala2 = (TextView) findViewById(R.id.titleSala2);
        tvLecturaSala1 = (TextView) findViewById(R.id.tvAsigLecturaSala1);
        tvLecturaSala2 = (TextView) findViewById(R.id.tvAsigLecturaSala2);
        tvAsignacion1Sala1 = (TextView) findViewById(R.id.asignacion1Sala1);
        tvAsignacion2Sala1 = (TextView) findViewById(R.id.asignacion2Sala1);
        tvAsignacion3Sala1 = (TextView) findViewById(R.id.asignacion3Sala1);
        tvAsignacion1Sala2 = (TextView) findViewById(R.id.asignacion1Sala2);
        tvAsignacion2Sala2 = (TextView) findViewById(R.id.asignacion2Sala2);
        tvAsignacion3Sala2 = (TextView) findViewById(R.id.asignacion3Sala2);
        tvLectorSala1 = (TextView) findViewById(R.id.lecturaSala1);
        tvEncargado1Sala1 = (TextView) findViewById(R.id.encargado1Sala1);
        tvEncargado2Sala1 = (TextView) findViewById(R.id.encargado2Sala1);
        tvEncargado3Sala1 = (TextView) findViewById(R.id.encargado3Sala1);
        tvAyudante1Sala1 = (TextView) findViewById(R.id.ayudante1Sala1);
        tvAyudante2Sala1 = (TextView) findViewById(R.id.ayudante2Sala1);
        tvAyudante3Sala1 = (TextView) findViewById(R.id.ayudante3Sala1);
        tvLectorSala2 = (TextView) findViewById(R.id.lecturaSala2);
        tvEncargado1Sala2 = (TextView) findViewById(R.id.encargado1Sala2);
        tvEncargado2Sala2 = (TextView) findViewById(R.id.encargado2Sala2);
        tvEncargado3Sala2 = (TextView) findViewById(R.id.encargado3Sala2);
        tvAyudante1Sala2 = (TextView) findViewById(R.id.ayudante1Sala2);
        tvAyudante2Sala2 = (TextView) findViewById(R.id.ayudante2Sala2);
        tvAyudante3Sala2 = (TextView) findViewById(R.id.ayudante3Sala2);

        Bundle bundleRecibir = this.getIntent().getExtras();
        idLectorSala1 = bundleRecibir.getString("idLectorSala1");
        idEncargado1Sala1 = bundleRecibir.getString("idEncargado1Sala1");
        idAyudante1Sala1 = bundleRecibir.getString("idAyudante1Sala1");
        idEncargado2Sala1 = bundleRecibir.getString("idEncargado2Sala1");
        idAyudante2Sala1 = bundleRecibir.getString("idAyudante2Sala1");
        idEncargado3Sala1 = bundleRecibir.getString("idEncargado3Sala1");
        idAyudante3Sala1 = bundleRecibir.getString("idAyudante3Sala1");
        seleccion1Sala1 = bundleRecibir.getString("asignacion1Sala1");
        seleccion2Sala1 = bundleRecibir.getString("asignacion2Sala1");
        seleccion3Sala1 = bundleRecibir.getString("asignacion3Sala1");

        idLectorSala2 = bundleRecibir.getString("idLectorSala2");
        idEncargado1Sala2 = bundleRecibir.getString("idEncargado1Sala2");
        idAyudante1Sala2 = bundleRecibir.getString("idAyudante1Sala2");
        idEncargado2Sala2 = bundleRecibir.getString("idEncargado2Sala2");
        idAyudante2Sala2 = bundleRecibir.getString("idAyudante2Sala2");
        idEncargado3Sala2 = bundleRecibir.getString("idEncargado3Sala2");
        idAyudante3Sala2 = bundleRecibir.getString("idAyudante3Sala2");
        seleccion1Sala2 = bundleRecibir.getString("asignacion1Sala2");
        seleccion2Sala2 = bundleRecibir.getString("asignacion2Sala2");
        seleccion3Sala2 = bundleRecibir.getString("asignacion3Sala2");

        visita = bundleRecibir.getBoolean("visita");
        asamblea = bundleRecibir.getBoolean("asamblea");
        activarSala2 = bundleRecibir.getBoolean("activarSala2");
        fecha = bundleRecibir.getLong("fecha");
        semanaSelec = bundleRecibir.getInt("semana");

        fechaDate = new Date();
        fechaDate.setTime(fecha);


        llenarSalas();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_editar_pub, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_save_pub) {
            guardarSala1();
            guardarSala2();
            guradarDatosPublicadores();
            return true;
        } else if (id == R.id.menu_cancel_pub) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void llenarSalas() {
        if (!asamblea) {
            if (!visita) {
                llenarSalasCompleta();
            } else {
                if (activarSala2) {
                    llenarSalasCompleta();
                } else {
                    tvFecha.setText(new SimpleDateFormat("EEE d MMM yyyy").format(fechaDate));

                    if (idLectorSala1 != null) {
                        tvLectorSala1.setText(Utilidades.lectorSala1);
                    } else {
                        tvLecturaSala1.setText("");
                        tvLectorSala1.setText("");
                    }

                    if (idEncargado1Sala1 != null) {
                        tvAsignacion1Sala1.setText(seleccion1Sala1);
                        tvEncargado1Sala1.setText(Utilidades.encargado1Sala1);
                    } else {
                        tvAsignacion1Sala1.setText("");
                        tvEncargado1Sala1.setText("");
                    }

                    if (idAyudante1Sala1 != null) {
                        tvAyudante1Sala1.setText(Utilidades.ayudante1Sala1);
                    } else {
                        tvAyudante1Sala1.setText("");
                    }

                    if (idEncargado2Sala1 != null) {
                        tvAsignacion2Sala1.setText(seleccion2Sala1);
                        tvEncargado2Sala1.setText(Utilidades.encargado2Sala1);
                    } else {
                        tvAsignacion2Sala1.setText("");
                        tvEncargado2Sala1.setText("");
                    }

                    if (idAyudante2Sala1 != null) {
                        tvAyudante2Sala1.setText(Utilidades.ayudante2Sala1);
                    } else {
                        tvAyudante2Sala1.setText("");
                    }

                    if (idEncargado3Sala1 != null) {
                        tvAsignacion3Sala1.setText(seleccion3Sala1);
                        tvEncargado3Sala1.setText(Utilidades.encargado3Sala1);
                    } else {
                        tvAsignacion3Sala1.setText("");
                        tvEncargado3Sala1.setText("");
                    }

                    if (idAyudante3Sala1 != null) {
                        tvAyudante3Sala1.setText(Utilidades.ayudante3Sala1);
                    } else {
                        tvAyudante3Sala1.setText("");
                    }
                    tituloSala2.setText("Visita");
                    tvLecturaSala2.setText("");
                    tvAsignacion1Sala2.setText("");
                    tvAsignacion2Sala2.setText("");
                    tvAsignacion3Sala2.setText("");
                    tvLectorSala2.setText("");
                    tvEncargado1Sala2.setText("");
                    tvEncargado2Sala2.setText("");
                    tvEncargado3Sala2.setText("");
                    tvAyudante1Sala2.setText("");
                    tvAyudante2Sala2.setText("");
                    tvAyudante3Sala2.setText("");
                }
            }
        } else {
            tvFecha.setText(new SimpleDateFormat("EEE d MMM yyyy").format(fechaDate));
            tituloSala1.setText("ASAMBLEA");
            tvAsignacion1Sala1.setText("");
            tvAsignacion2Sala1.setText("");
            tvAsignacion3Sala1.setText("");
            tvLecturaSala1.setText("");
            tvLecturaSala2.setText("");
            tvLectorSala1.setText("");
            tvEncargado1Sala1.setText("");
            tvEncargado2Sala1.setText("");
            tvEncargado3Sala1.setText("");
            tvAyudante1Sala1.setText("");
            tvAyudante2Sala1.setText("");
            tvAyudante3Sala1.setText("");
            tituloSala2.setText("ASAMBLEA");
            tvAsignacion1Sala2.setText("");
            tvAsignacion2Sala2.setText("");
            tvAsignacion3Sala2.setText("");
            tvLectorSala2.setText("");
            tvEncargado1Sala2.setText("");
            tvEncargado2Sala2.setText("");
            tvEncargado3Sala2.setText("");
            tvAyudante1Sala2.setText("");
            tvAyudante2Sala2.setText("");
            tvAyudante3Sala2.setText("");


        }
    }

    public void llenarSalasCompleta() {
        tvFecha.setText(new SimpleDateFormat("EEE d MMM yyyy").format(fechaDate));

        if (idLectorSala1 != null) {
            tvLectorSala1.setText(Utilidades.lectorSala1);
        } else {
            tvLecturaSala1.setText("");
            tvLectorSala1.setText("");
        }

        if (idEncargado1Sala1 != null) {
            tvAsignacion1Sala1.setText(seleccion1Sala1);
            tvEncargado1Sala1.setText(Utilidades.encargado1Sala1);
        } else {
            tvAsignacion1Sala1.setText("");
            tvEncargado1Sala1.setText("");
        }

        if (idAyudante1Sala1 != null) {
            tvAyudante1Sala1.setText(Utilidades.ayudante1Sala1);
        } else {
            tvAyudante1Sala1.setText("");
        }

        if (idEncargado2Sala1 != null) {
            tvAsignacion2Sala1.setText(seleccion2Sala1);
            tvEncargado2Sala1.setText(Utilidades.encargado2Sala1);
        } else {
            tvAsignacion2Sala1.setText("");
            tvEncargado2Sala1.setText("");
        }

        if (idAyudante2Sala1 != null) {
            tvAyudante2Sala1.setText(Utilidades.ayudante2Sala1);
        } else {
            tvAyudante2Sala1.setText("");
        }

        if (idEncargado3Sala1 != null) {
            tvAsignacion3Sala1.setText(seleccion3Sala1);
            tvEncargado3Sala1.setText(Utilidades.encargado3Sala1);
        } else {
            tvAsignacion3Sala1.setText("");
            tvEncargado3Sala1.setText("");
        }

        if (idAyudante3Sala1 != null) {
            tvAyudante3Sala1.setText(Utilidades.ayudante3Sala1);
        } else {
            tvAyudante3Sala1.setText("");
        }
    //Sala 2
        if (idLectorSala2 != null) {
            tvLectorSala2.setText(Utilidades.lectorSala2);
        } else {
            tvLecturaSala2.setText("");
            tvLectorSala2.setText("");
        }

        if (idEncargado1Sala2 != null) {
            tvAsignacion1Sala2.setText(seleccion1Sala2);
            tvEncargado1Sala2.setText(Utilidades.encargado1Sala2);
        } else {
            tvAsignacion1Sala2.setText("");
            tvEncargado1Sala2.setText("");
        }

        if (idAyudante1Sala2 != null) {
            tvAyudante1Sala2.setText(Utilidades.ayudante1Sala2);
        } else {
            tvAyudante1Sala2.setText("");
        }

        if (idEncargado2Sala2 != null) {
            tvAsignacion2Sala2.setText(seleccion2Sala2);
            tvEncargado2Sala2.setText(Utilidades.encargado2Sala2);
        } else {
            tvAsignacion2Sala2.setText("");
            tvEncargado2Sala2.setText("");
        }

        if (idAyudante2Sala2 != null) {
            tvAyudante2Sala2.setText(Utilidades.ayudante2Sala2);
        } else {
            tvAyudante2Sala2.setText("");
        }

        if (idEncargado3Sala2 != null) {
            tvAsignacion3Sala2.setText(seleccion3Sala2);
            tvEncargado3Sala2.setText(Utilidades.encargado3Sala2);
        } else {
            tvAsignacion3Sala2.setText("");
            tvEncargado3Sala2.setText("");
        }

        if (idAyudante3Sala2 != null) {
            tvAyudante3Sala2.setText(Utilidades.ayudante3Sala2);
        } else {
            tvAyudante3Sala2.setText("");
        }
    }

    public void guradarDatosPublicadores() {

        if (idLectorSala1 != null) {
            actualizarFechasEncargados(idLectorSala1);
            actualizarFechaViejaEncargados(idLectorSala1, Utilidades.lectorSala1Date);
        }

        if (idEncargado1Sala1 != null) {
            actualizarFechasEncargados(idEncargado1Sala1);
            actualizarFechaViejaEncargados(idEncargado1Sala1, Utilidades.encargado1Sala1Date);
        }

        if (idAyudante1Sala1 != null) {
            actualizarFechasAyudantes(idAyudante1Sala1);
            actualizarFechaViejaAyudantes(idAyudante1Sala1, Utilidades.ayudante1Sala1Date);
        }

        if (idEncargado2Sala1 != null) {
            actualizarFechasEncargados(idEncargado2Sala1);
            actualizarFechaViejaEncargados(idEncargado2Sala1, Utilidades.encargado2Sala1Date);
        }

        if (idAyudante2Sala1 != null) {
            actualizarFechasAyudantes(idAyudante2Sala1);
            actualizarFechaViejaAyudantes(idAyudante2Sala1, Utilidades.ayudante2Sala1Date);
        }

        if (idEncargado3Sala1 != null) {
            actualizarFechasEncargados(idEncargado3Sala1);
            actualizarFechaViejaEncargados(idEncargado3Sala1, Utilidades.encargado3Sala1Date);
        }

        if (idAyudante3Sala1 != null) {
            actualizarFechasAyudantes(idAyudante3Sala1);
            actualizarFechaViejaAyudantes(idAyudante3Sala1, Utilidades.ayudante3Sala1Date);
        }
        //Sala 2
        if (idLectorSala2 != null) {
            actualizarFechasEncargados(idLectorSala2);
            actualizarFechaViejaEncargados(idLectorSala2, Utilidades.lectorSala2Date);
        }

        if (idEncargado1Sala2 != null) {
            actualizarFechasEncargados(idEncargado1Sala2);
            actualizarFechaViejaEncargados(idEncargado1Sala2, Utilidades.encargado1Sala2Date);
        }

        if (idAyudante1Sala2 != null) {
            actualizarFechasAyudantes(idAyudante1Sala2);
            actualizarFechaViejaAyudantes(idAyudante1Sala2, Utilidades.ayudante1Sala2Date);
        }

        if (idEncargado2Sala2 != null) {
            actualizarFechasEncargados(idEncargado2Sala2);
            actualizarFechaViejaEncargados(idEncargado2Sala2, Utilidades.encargado2Sala2Date);
        }

        if (idAyudante2Sala2 != null) {
            actualizarFechasAyudantes(idAyudante2Sala2);
            actualizarFechaViejaAyudantes(idAyudante2Sala2, Utilidades.ayudante2Sala2Date);
        }

        if (idEncargado3Sala2 != null) {
            actualizarFechasEncargados(idEncargado3Sala2);
            actualizarFechaViejaEncargados(idEncargado3Sala2, Utilidades.encargado3Sala2Date);
        }

        if (idAyudante3Sala2 != null) {
            actualizarFechasAyudantes(idAyudante3Sala2);
            actualizarFechaViejaAyudantes(idAyudante3Sala2, Utilidades.ayudante3Sala2Date);
        }
        Toast.makeText(this, "Proceso finalizado correctamente", Toast.LENGTH_LONG).show();
        Intent myIntent = new Intent(ResumenSalas.this, AsignacionesActivity.class);
        startActivity(myIntent);
        finish();
    }

    public void actualizarFechasEncargados(String id) {
        FirebaseFirestore dbEditar = FirebaseFirestore.getInstance();

        dbEditar.collection("publicadores").document(id).update(UtilidadesStatic.BD_DISRECIENTE, fechaDate).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void actualizarFechaViejaEncargados(String id, Date fechaVieja) {
        FirebaseFirestore dbEditar = FirebaseFirestore.getInstance();

        dbEditar.collection("publicadores").document(id).update(UtilidadesStatic.BD_DISVIEJO, fechaVieja).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void actualizarFechasAyudantes(String id) {
        FirebaseFirestore dbEditar = FirebaseFirestore.getInstance();

        dbEditar.collection("publicadores").document(id).update(UtilidadesStatic.BD_AYURECIENTE, fechaDate).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void actualizarFechaViejaAyudantes(String id, Date fechaVieja) {
        FirebaseFirestore dbEditar = FirebaseFirestore.getInstance();

        dbEditar.collection("publicadores").document(id).update(UtilidadesStatic.BD_AYUVIEJO, fechaVieja).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void guardarSala1() {
        String semana = String.valueOf(semanaSelec);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference reference = db.collection("sala1").document(semana);

                Map<String, Object> publicador = new HashMap<>();
                publicador.put(UtilidadesStatic.BD_LECTOR, Utilidades.lectorSala1);
                publicador.put(UtilidadesStatic.BD_ENCARGADO1, Utilidades.encargado1Sala1);
                publicador.put(UtilidadesStatic.BD_AYUDANTE1, Utilidades.ayudante1Sala1);
                publicador.put(UtilidadesStatic.BD_ENCARGADO2, Utilidades.encargado2Sala1);
                publicador.put(UtilidadesStatic.BD_AYUDANTE2, Utilidades.ayudante2Sala1);
                publicador.put(UtilidadesStatic.BD_ENCARGADO3, Utilidades.encargado3Sala1);
                publicador.put(UtilidadesStatic.BD_AYUDANTE3, Utilidades.ayudante3Sala1);
                publicador.put(UtilidadesStatic.BD_IDLECTOR, idLectorSala1);
                publicador.put(UtilidadesStatic.BD_IDENCARGADO1, idEncargado1Sala1);
                publicador.put(UtilidadesStatic.BD_IDAYUDANTE1, idAyudante1Sala1);
                publicador.put(UtilidadesStatic.BD_IDENCARGADO2, idEncargado2Sala1);
                publicador.put(UtilidadesStatic.BD_IDAYUDANTE2, idAyudante2Sala1);
                publicador.put(UtilidadesStatic.BD_IDENCARGADO3, idEncargado3Sala1);
                publicador.put(UtilidadesStatic.BD_IDAYUDANTE3, idAyudante3Sala1);
                publicador.put(UtilidadesStatic.BD_ASIGNACION1, seleccion1Sala1);
                publicador.put(UtilidadesStatic.BD_ASIGNACION2, seleccion2Sala1);
                publicador.put(UtilidadesStatic.BD_ASIGNACION3, seleccion3Sala1);
                publicador.put(UtilidadesStatic.BD_ASAMBLEA, Utilidades.asamblea);
                publicador.put(UtilidadesStatic.BD_VISITA, Utilidades.visita);

         reference.set(publicador).addOnSuccessListener(new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void aVoid) {

             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {

             }
         });

    }

    public void guardarSala2() {
        String semana = String.valueOf(semanaSelec);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference reference = db.collection("sala2").document(semana);

        Map<String, Object> publicador = new HashMap<>();
        publicador.put(UtilidadesStatic.BD_LECTOR, Utilidades.lectorSala2);
        publicador.put(UtilidadesStatic.BD_ENCARGADO1, Utilidades.encargado1Sala2);
        publicador.put(UtilidadesStatic.BD_AYUDANTE1, Utilidades.ayudante1Sala2);
        publicador.put(UtilidadesStatic.BD_ENCARGADO2, Utilidades.encargado2Sala2);
        publicador.put(UtilidadesStatic.BD_AYUDANTE2, Utilidades.ayudante2Sala2);
        publicador.put(UtilidadesStatic.BD_ENCARGADO3, Utilidades.encargado3Sala2);
        publicador.put(UtilidadesStatic.BD_AYUDANTE3, Utilidades.ayudante3Sala2);
        publicador.put(UtilidadesStatic.BD_IDLECTOR, idLectorSala2);
        publicador.put(UtilidadesStatic.BD_IDENCARGADO1, idEncargado1Sala2);
        publicador.put(UtilidadesStatic.BD_IDAYUDANTE1, idAyudante1Sala2);
        publicador.put(UtilidadesStatic.BD_IDENCARGADO2, idEncargado2Sala2);
        publicador.put(UtilidadesStatic.BD_IDAYUDANTE2, idAyudante2Sala2);
        publicador.put(UtilidadesStatic.BD_IDENCARGADO3, idEncargado3Sala2);
        publicador.put(UtilidadesStatic.BD_IDAYUDANTE3, idAyudante3Sala2);
        publicador.put(UtilidadesStatic.BD_ASIGNACION1, seleccion1Sala2);
        publicador.put(UtilidadesStatic.BD_ASIGNACION2, seleccion2Sala2);
        publicador.put(UtilidadesStatic.BD_ASIGNACION3, seleccion3Sala2);
        publicador.put(UtilidadesStatic.BD_ASAMBLEA, Utilidades.asamblea);
        publicador.put(UtilidadesStatic.BD_VISITA, Utilidades.visita);


        reference.set(publicador).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


}
