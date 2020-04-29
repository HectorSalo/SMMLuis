package com.skysam.firebase.seamosmejoresmaestros.luis.Asignaciones;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.skysam.firebase.seamosmejoresmaestros.luis.Variables.VariablesActualizacionFechas;
import com.skysam.firebase.seamosmejoresmaestros.luis.Variables.VariablesEstaticas;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;


public class ActualizarFechaSalaEliminada {

   private String idSemana = "";
   private Context ctx;

    public ActualizarFechaSalaEliminada(Context ctx){
        this.ctx = ctx;
    }

    public void cargarSala1(int i) {
        idSemana = String.valueOf(i);
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference reference = dbFirestore.collection("sala1");

        reference.document(idSemana).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        Log.d("ID:", "id: " + idSemana);
                        if (doc.getString(VariablesEstaticas.BD_LECTOR) != null) {
                            VariablesActualizacionFechas.lectorSala1 = doc.getString(VariablesEstaticas.BD_IDLECTOR);
                            buscarEnc(VariablesActualizacionFechas.lectorSala1);
                        }
                        if (doc.getString(VariablesEstaticas.BD_ENCARGADO1) != null) {
                            VariablesActualizacionFechas.encargado1Sala1 = doc.getString(VariablesEstaticas.BD_IDENCARGADO1);
                            buscarEnc(VariablesActualizacionFechas.encargado1Sala1);
                        }
                        if (doc.getString(VariablesEstaticas.BD_AYUDANTE1) != null) {
                            VariablesActualizacionFechas.ayudante1Sala1 = doc.getString(VariablesEstaticas.BD_IDAYUDANTE1);
                            buscarAyu(VariablesActualizacionFechas.ayudante1Sala1);
                        }
                        if (doc.getString(VariablesEstaticas.BD_ENCARGADO2) != null) {
                            VariablesActualizacionFechas.encargado2Sala1 = doc.getString(VariablesEstaticas.BD_IDENCARGADO2);
                            buscarEnc(VariablesActualizacionFechas.encargado2Sala1);
                        }
                        if (doc.getString(VariablesEstaticas.BD_AYUDANTE2) != null) {
                            VariablesActualizacionFechas.ayudante2Sala1 = doc.getString(VariablesEstaticas.BD_IDAYUDANTE2);
                            buscarAyu(VariablesActualizacionFechas.ayudante2Sala1);
                        }
                        if (doc.getString(VariablesEstaticas.BD_ENCARGADO3) != null) {
                            VariablesActualizacionFechas.encargado3Sala1 = doc.getString(VariablesEstaticas.BD_IDENCARGADO3);
                            buscarEnc(VariablesActualizacionFechas.encargado3Sala1);
                        }
                        if (doc.getString(VariablesEstaticas.BD_AYUDANTE3) != null) {
                            VariablesActualizacionFechas.ayudante3Sala1 = doc.getString(VariablesEstaticas.BD_IDAYUDANTE3);
                            buscarAyu(VariablesActualizacionFechas.ayudante3Sala1);
                        }
                        eliminarSala1(idSemana);
                        cargarSala2(idSemana);
                    } else {
                        Toast.makeText(ctx, "No se puede eliminar una semana sin programar", Toast.LENGTH_SHORT).show();
                        Log.d("Error", "No such document");
                    }

                }
            }
        });
    }

    public void eliminarSala1(String text) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("sala1");

        reference.document(text)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }



    public void cargarSala2(String i) {

        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference reference = dbFirestore.collection("sala2");

        reference.document(i).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();

                    if (doc.exists()) {
                        if (doc.getString(VariablesEstaticas.BD_LECTOR) != null) {
                            VariablesActualizacionFechas.lectorSala2 = doc.getString(VariablesEstaticas.BD_IDLECTOR);
                            buscarEnc(VariablesActualizacionFechas.lectorSala2);
                        }
                        if (doc.getString(VariablesEstaticas.BD_ENCARGADO1) != null) {
                            VariablesActualizacionFechas.encargado1Sala2 = doc.getString(VariablesEstaticas.BD_IDENCARGADO1);
                            buscarEnc(VariablesActualizacionFechas.encargado1Sala2);
                        }
                        if (doc.getString(VariablesEstaticas.BD_AYUDANTE1) != null) {
                            VariablesActualizacionFechas.ayudante1Sala2 = doc.getString(VariablesEstaticas.BD_IDAYUDANTE1);
                            buscarAyu(VariablesActualizacionFechas.ayudante1Sala2);
                        }
                        if (doc.getString(VariablesEstaticas.BD_ENCARGADO2) != null) {
                            VariablesActualizacionFechas.encargado2Sala2 = doc.getString(VariablesEstaticas.BD_IDENCARGADO2);
                            buscarEnc(VariablesActualizacionFechas.encargado2Sala2);
                        }
                        if (doc.getString(VariablesEstaticas.BD_AYUDANTE2) != null) {
                            VariablesActualizacionFechas.ayudante2Sala2 = doc.getString(VariablesEstaticas.BD_IDAYUDANTE2);
                            buscarAyu(VariablesActualizacionFechas.ayudante2Sala2);
                        }
                        if (doc.getString(VariablesEstaticas.BD_ENCARGADO3) != null) {
                            VariablesActualizacionFechas.encargado3Sala2 = doc.getString(VariablesEstaticas.BD_IDENCARGADO3);
                            buscarEnc(VariablesActualizacionFechas.encargado3Sala2);
                        }
                        if (doc.getString(VariablesEstaticas.BD_AYUDANTE3) != null) {
                            VariablesActualizacionFechas.ayudante3Sala2 = doc.getString(VariablesEstaticas.BD_IDAYUDANTE3);
                           buscarAyu(VariablesActualizacionFechas.ayudante3Sala2);
                        }
                        eliminarSala2(idSemana);
                    }

                }
            }
        });
    }

    public void eliminarSala2(String text) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("sala2");

        reference.document(text)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ctx, "Programción eliminada", Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(ctx, AsignacionesActivity.class);
                        ctx.startActivity(myIntent);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void buscarEnc (String idEncargado) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");


        reference.document(idEncargado).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    VariablesActualizacionFechas.idPubCambiado = doc.getId();
                    VariablesActualizacionFechas.fechaDisCambiado = doc.getDate(VariablesEstaticas.BD_DISVIEJO);

                    actFechaEnc();

                }
            }
        });
    }

    public void actFechaEnc() {
        String idPub = VariablesActualizacionFechas.idPubCambiado;
        Date fecha = new Date();
        fecha = VariablesActualizacionFechas.fechaDisCambiado;
        FirebaseFirestore dbEditar = FirebaseFirestore.getInstance();

        dbEditar.collection("publicadores").document(idPub).update(VariablesEstaticas.BD_DISRECIENTE, fecha).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


            }
        });
    }

    public void buscarAyu (String idEncargado) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");


        reference.document(idEncargado).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    VariablesActualizacionFechas.idPubCambiado = doc.getId();
                    VariablesActualizacionFechas.fechaAyuCambiado = doc.getDate(VariablesEstaticas.BD_AYUVIEJO);

                    actFechaAyu();

                }
            }
        });
    }

    public void actFechaAyu() {
        String idPub = VariablesActualizacionFechas.idPubCambiado;
        Date fecha = new Date();
        fecha = VariablesActualizacionFechas.fechaAyuCambiado;
        FirebaseFirestore dbEditar = FirebaseFirestore.getInstance();

        dbEditar.collection("publicadores").document(idPub).update(VariablesEstaticas.BD_AYURECIENTE, fecha).addOnSuccessListener(new OnSuccessListener<Void>() {
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
