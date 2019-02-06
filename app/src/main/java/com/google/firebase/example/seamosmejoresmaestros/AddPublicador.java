package com.google.firebase.example.seamosmejoresmaestros;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddPublicador extends AppCompatActivity {

    private EditText editNombrePub, editApellidoPub, editTelefonoPub, editCorreoPub;
    private RadioButton radioMasculino, radioFemenino;
    private String NombrePub, ApellidoPub, Telefono, Correo;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_publicador);

        editNombrePub = (EditText)findViewById(R.id.editNombre);
        editApellidoPub = (EditText)findViewById(R.id.editApellido);
        editTelefonoPub = (EditText)findViewById(R.id.editTelefono);
        editCorreoPub = (EditText)findViewById(R.id.editCorreo);
        radioMasculino = (RadioButton)findViewById(R.id.radioButtonMasculino);
        radioFemenino = (RadioButton)findViewById(R.id.radioButtonFemenino);
        Button buttonAgregar = (Button)findViewById(R.id.bottomAgregar);
        Button buttonCancelar = (Button)findViewById(R.id.bottomCancelar);

        buttonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(AddPublicador.this);
                progress.setMessage("Guardando...");
                progress.setCancelable(false);
                progress.show();
                guardarPublicador();
            }
        });

        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void guardarPublicador() {
        NombrePub = editNombrePub.getText().toString();
        ApellidoPub = editApellidoPub.getText().toString();
        Telefono = editTelefonoPub.getText().toString();
        Correo = editCorreoPub.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (!NombrePub.isEmpty() && !ApellidoPub.isEmpty()) {
            if (radioMasculino.isChecked() || radioFemenino.isChecked()) {


                Map<String, Object> publicador = new HashMap<>();
                publicador.put(UtilidadesStatic.BD_NOMBRE, NombrePub);
                publicador.put(UtilidadesStatic.BD_APELLIDO, ApellidoPub);
                publicador.put(UtilidadesStatic.BD_TELEFONO, Telefono);
                publicador.put(UtilidadesStatic.BD_CORREO, Correo);
                publicador.put(UtilidadesStatic.BD_HABILITADO, true);
                publicador.put(UtilidadesStatic.BD_IMAGEN, null);
                publicador.put(UtilidadesStatic.BD_DISRECIENTE, null);
                publicador.put(UtilidadesStatic.BD_DISVIEJO, null);
                publicador.put(UtilidadesStatic.BD_AYURECIENTE, null);
                publicador.put(UtilidadesStatic.BD_AYUVIEJO, null);
                publicador.put(UtilidadesStatic.BD_SUSTRECIENTE, null);
                publicador.put(UtilidadesStatic.BD_SUSTVIEJO, null);


                if (radioMasculino.isChecked()) {
                    publicador.put(UtilidadesStatic.BD_GENERO, "Hombre");
                } else if (radioFemenino.isChecked()) {
                    publicador.put(UtilidadesStatic.BD_GENERO, "Mujer");
                }

                db.collection("publicadores").add(publicador).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), "Guardado exitosamente", Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(getApplicationContext(), PublicadoresActivity.class);
                        startActivity(myIntent);
                        finish();

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progress.dismiss();
                                Toast.makeText(getApplicationContext(), "Error al guadar. Intente nuevamente", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

            } else {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), "Hay campos obligatorios vacíos", Toast.LENGTH_SHORT).show();
            }
        } else {
            progress.dismiss();
            Toast.makeText(getApplicationContext(), "Hay campos obligatorios vacíos", Toast.LENGTH_SHORT).show();
        }
    }
}
