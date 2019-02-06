package com.google.firebase.example.seamosmejoresmaestros;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.errorprone.annotations.Var;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;


public class ActualizarFechaSalaEliminada {

   private boolean encargado = false;
   private boolean ayudante = false;

    public ActualizarFechaSalaEliminada(){}

    public void cargarSala1(int i) {
        String idSemana = String.valueOf(i);
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference reference = dbFirestore.collection("sala1");

        reference.document(idSemana).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();


                    if (doc.getString(UtilidadesStatic.BD_LECTOR) != null) {
                        VariablesActualizacionFechas.lectorSala1 = doc.getString(UtilidadesStatic.BD_IDLECTOR);
                        buscarEncAyu(VariablesActualizacionFechas.lectorSala1);
                    }
                    if (doc.getString(UtilidadesStatic.BD_ENCARGADO1) != null) {
                        VariablesActualizacionFechas.encargado1Sala1 = doc.getString(UtilidadesStatic.BD_IDENCARGADO1);
                        buscarEncAyu(VariablesActualizacionFechas.encargado1Sala1);
                    }
                    if (doc.getString(UtilidadesStatic.BD_AYUDANTE1) != null) {
                        VariablesActualizacionFechas.ayudante1Sala1 = doc.getString(UtilidadesStatic.BD_IDAYUDANTE1);
                        buscarEncAyu(VariablesActualizacionFechas.ayudante1Sala1);
                    }
                    if (doc.getString(UtilidadesStatic.BD_ENCARGADO2) != null) {
                        VariablesActualizacionFechas.encargado2Sala1 = doc.getString(UtilidadesStatic.BD_IDENCARGADO2);
                        buscarEncAyu(VariablesActualizacionFechas.encargado2Sala1);
                    }
                    if (doc.getString(UtilidadesStatic.BD_AYUDANTE2) != null) {
                        VariablesActualizacionFechas.ayudante2Sala1 = doc.getString(UtilidadesStatic.BD_IDAYUDANTE2);
                        buscarEncAyu(VariablesActualizacionFechas.ayudante2Sala1);
                    }
                    if (doc.getString(UtilidadesStatic.BD_ENCARGADO3) != null) {
                        VariablesActualizacionFechas.encargado3Sala1 = doc.getString(UtilidadesStatic.BD_IDENCARGADO3);
                        buscarEncAyu(VariablesActualizacionFechas.encargado3Sala1);
                    }
                    if (doc.getString(UtilidadesStatic.BD_AYUDANTE3) != null) {
                        VariablesActualizacionFechas.ayudante3Sala1 = doc.getString(UtilidadesStatic.BD_IDAYUDANTE3);
                        buscarEncAyu(VariablesActualizacionFechas.ayudante3Sala1);
                    }

                }
            }
        });
    }


    public void cargarSala2(int i) {
        String idSemana = String.valueOf(i);
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference reference = dbFirestore.collection("sala2");

        reference.document(idSemana).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();


                    if (doc.getString(UtilidadesStatic.BD_LECTOR) != null) {
                        VariablesActualizacionFechas.lectorSala2 = doc.getString(UtilidadesStatic.BD_IDLECTOR);
                        buscarEncAyu(VariablesActualizacionFechas.lectorSala2);
                    }
                    if (doc.getString(UtilidadesStatic.BD_ENCARGADO1) != null) {
                        VariablesActualizacionFechas.encargado1Sala2 = doc.getString(UtilidadesStatic.BD_IDENCARGADO1);
                        buscarEncAyu(VariablesActualizacionFechas.encargado1Sala2);
                    }
                    if (doc.getString(UtilidadesStatic.BD_AYUDANTE1) != null) {
                        VariablesActualizacionFechas.ayudante1Sala2 = doc.getString(UtilidadesStatic.BD_IDAYUDANTE1);
                        buscarEncAyu(VariablesActualizacionFechas.ayudante1Sala2);
                    }
                    if (doc.getString(UtilidadesStatic.BD_ENCARGADO2) != null) {
                        VariablesActualizacionFechas.encargado2Sala2 = doc.getString(UtilidadesStatic.BD_IDENCARGADO2);
                        buscarEncAyu(VariablesActualizacionFechas.encargado2Sala2);
                    }
                    if (doc.getString(UtilidadesStatic.BD_AYUDANTE2) != null) {
                        VariablesActualizacionFechas.ayudante2Sala2 = doc.getString(UtilidadesStatic.BD_IDAYUDANTE2);
                        buscarEncAyu(VariablesActualizacionFechas.ayudante2Sala2);
                    }
                    if (doc.getString(UtilidadesStatic.BD_ENCARGADO3) != null) {
                        VariablesActualizacionFechas.encargado3Sala2 = doc.getString(UtilidadesStatic.BD_IDENCARGADO3);
                        buscarEncAyu(VariablesActualizacionFechas.encargado3Sala2);
                    }
                    if (doc.getString(UtilidadesStatic.BD_AYUDANTE3) != null) {
                        VariablesActualizacionFechas.ayudante3Sala2 = doc.getString(UtilidadesStatic.BD_IDAYUDANTE3);
                        buscarEncAyu(VariablesActualizacionFechas.ayudante3Sala2);
                    }

                }
            }
        });
    }

    public void buscarEncAyu (String idEncargado) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");


        reference.document(idEncargado).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    VariablesActualizacionFechas.idPubCambiado = doc.getId();
                    VariablesActualizacionFechas.fechaPubCambiado = doc.getDate(UtilidadesStatic.BD_DISVIEJO);

                    if (encargado) {
                        actFechaEnc();
                    }  else if (ayudante) {
                        actFechaAyu();
                    }



                }
            }
        });
    }

    public void actFechaEnc() {
        String idPub = VariablesActualizacionFechas.idPubCambiado;
        Date fecha = new Date();
        fecha = VariablesActualizacionFechas.fechaPubCambiado;
        FirebaseFirestore dbEditar = FirebaseFirestore.getInstance();

        dbEditar.collection("publicadores").document(idPub).update(UtilidadesStatic.BD_DISRECIENTE, fecha).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


            }
        });
    }

    public void actFechaAyu() {
        String idPub = VariablesActualizacionFechas.idPubCambiado;
        Date fecha = new Date();
        fecha = VariablesActualizacionFechas.fechaPubCambiado;
        FirebaseFirestore dbEditar = FirebaseFirestore.getInstance();

        dbEditar.collection("publicadores").document(idPub).update(UtilidadesStatic.BD_AYURECIENTE, fecha).addOnSuccessListener(new OnSuccessListener<Void>() {
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
