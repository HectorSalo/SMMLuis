package com.skysam.firebase.seamosmejoresmaestros.luis.Asignaciones;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.errorprone.annotations.Var;
import com.skysam.firebase.seamosmejoresmaestros.luis.Fragments.Sala1Fragment;
import com.skysam.firebase.seamosmejoresmaestros.luis.R;
import com.skysam.firebase.seamosmejoresmaestros.luis.Variables.VariablesActualizacionFechas;
import com.skysam.firebase.seamosmejoresmaestros.luis.Variables.VariablesEstaticas;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;


public class ActualizarFechaSalaEliminada {

   private Context ctx;
   private ArrayList<String> listEncargados, listAyudantes;

    public ActualizarFechaSalaEliminada(Context ctx){
        this.ctx = ctx;
    }

    public void cargarSala(long i) {
        Toast.makeText(ctx, "Eliminando...", Toast.LENGTH_LONG).show();
        listEncargados = new ArrayList<>();
        listAyudantes = new ArrayList<>();

        String idSemana = String.valueOf(i);
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference reference = dbFirestore.collection(VariablesEstaticas.BD_SALA);

        reference.document(idSemana).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        Log.d("ID:", "id: " + idSemana);
                        if (doc.getString(VariablesEstaticas.BD_LECTOR) != null) {
                            listEncargados.add(doc.getString(VariablesEstaticas.BD_IDLECTOR));
                        }
                        if (doc.getString(VariablesEstaticas.BD_ENCARGADO1) != null) {
                            listEncargados.add(doc.getString(VariablesEstaticas.BD_IDENCARGADO1));
                        }
                        if (doc.getString(VariablesEstaticas.BD_AYUDANTE1) != null) {
                            listAyudantes.add(doc.getString(VariablesEstaticas.BD_IDAYUDANTE1));
                        }
                        if (doc.getString(VariablesEstaticas.BD_ENCARGADO2) != null) {
                            listEncargados.add(doc.getString(VariablesEstaticas.BD_IDENCARGADO2));
                        }
                        if (doc.getString(VariablesEstaticas.BD_AYUDANTE2) != null) {
                            listAyudantes.add(doc.getString(VariablesEstaticas.BD_IDAYUDANTE2));
                        }
                        if (doc.getString(VariablesEstaticas.BD_ENCARGADO3) != null) {
                            listEncargados.add(doc.getString(VariablesEstaticas.BD_IDENCARGADO3));
                        }
                        if (doc.getString(VariablesEstaticas.BD_AYUDANTE3) != null) {
                            listAyudantes.add(doc.getString(VariablesEstaticas.BD_IDAYUDANTE3));
                        }

                        if (!listEncargados.isEmpty()) {
                            for (int j = 0; j < listEncargados.size(); j++) {
                                actFechaEnc(listEncargados.get(j));
                            }
                        }

                        if (!listAyudantes.isEmpty()) {
                            for (int k = 0; k < listAyudantes.size(); k++) {
                                actFechaAyu(listAyudantes.get(k));
                            }
                        }

                        eliminarSala1(idSemana);
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
        CollectionReference reference = db.collection(VariablesEstaticas.BD_SALA);

        reference.document(text)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ctx, "Semana eliminada", Toast.LENGTH_SHORT).show();
                        ctx.startActivity(new Intent(ctx, AsignacionesActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ctx, "Error al eliminar. Intente nuevamente", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void actFechaEnc(String idPub) {
        FirebaseFirestore dbEditar = FirebaseFirestore.getInstance();

        dbEditar.collection(VariablesEstaticas.BD_PUBLICADORES).document(idPub).update(VariablesEstaticas.BD_DISRECIENTE, null);
    }

    public void actFechaAyu(String idPub) {
        FirebaseFirestore dbEditar = FirebaseFirestore.getInstance();

        dbEditar.collection(VariablesEstaticas.BD_PUBLICADORES).document(idPub).update(VariablesEstaticas.BD_AYURECIENTE, null);
    }
}
