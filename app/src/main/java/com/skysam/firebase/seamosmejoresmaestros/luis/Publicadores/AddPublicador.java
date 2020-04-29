package com.skysam.firebase.seamosmejoresmaestros.luis.Publicadores;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.skysam.firebase.seamosmejoresmaestros.luis.R;
import com.skysam.firebase.seamosmejoresmaestros.luis.Variables.VariablesEstaticas;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddPublicador extends AppCompatActivity {

    private EditText editNombrePub, editApellidoPub, editTelefonoPub, editCorreoPub;
    private RadioButton radioMasculino, radioFemenino;
    private NumberPicker numeroGrupo;
    private ProgressBar progressBarAdd;

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
        numeroGrupo = (NumberPicker) findViewById(R.id.numberGrupo);
        progressBarAdd = findViewById(R.id.progressBarAddPub);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int numeroGrupos = sharedPreferences.getInt("numeroGrupos", 1);
        boolean temaOscuro = sharedPreferences.getBoolean("activarOscuro", false);
        if (temaOscuro) {

            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }

        numeroGrupo.setMinValue(1);
        numeroGrupo.setMaxValue(numeroGrupos);

        Button buttonAgregar = (Button)findViewById(R.id.bottomAgregar);
        Button buttonCancelar = (Button)findViewById(R.id.bottomCancelar);

        buttonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarAdd.setVisibility(View.VISIBLE);
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
        String NombrePub = editNombrePub.getText().toString();
        String ApellidoPub = editApellidoPub.getText().toString();
        String Telefono = editTelefonoPub.getText().toString();
        String Correo = editCorreoPub.getText().toString();
        int grupo = numeroGrupo.getValue();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (!NombrePub.isEmpty() && !ApellidoPub.isEmpty()) {
            if (radioMasculino.isChecked() || radioFemenino.isChecked()) {


                Map<String, Object> publicador = new HashMap<>();
                publicador.put(VariablesEstaticas.BD_NOMBRE, NombrePub);
                publicador.put(VariablesEstaticas.BD_APELLIDO, ApellidoPub);
                publicador.put(VariablesEstaticas.BD_TELEFONO, Telefono);
                publicador.put(VariablesEstaticas.BD_CORREO, Correo);
                publicador.put(VariablesEstaticas.BD_HABILITADO, true);
                publicador.put(VariablesEstaticas.BD_IMAGEN, null);
                publicador.put(VariablesEstaticas.BD_DISRECIENTE, null);
                publicador.put(VariablesEstaticas.BD_DISVIEJO, null);
                publicador.put(VariablesEstaticas.BD_AYURECIENTE, null);
                publicador.put(VariablesEstaticas.BD_AYUVIEJO, null);
                publicador.put(VariablesEstaticas.BD_SUSTRECIENTE, null);
                publicador.put(VariablesEstaticas.BD_SUSTVIEJO, null);
                publicador.put(VariablesEstaticas.BD_MINISTERIAL, false);
                publicador.put(VariablesEstaticas.BD_SUPER, false);
                publicador.put(VariablesEstaticas.BD_PRECURSOR, false);
                publicador.put(VariablesEstaticas.BD_ANCIANO, false);
                publicador.put(VariablesEstaticas.BD_AUXILIAR, false);
                publicador.put(VariablesEstaticas.BD_GRUPO, grupo);


                if (radioMasculino.isChecked()) {
                    publicador.put(VariablesEstaticas.BD_GENERO, "Hombre");
                } else if (radioFemenino.isChecked()) {
                    publicador.put(VariablesEstaticas.BD_GENERO, "Mujer");
                }

                db.collection("publicadores").add(publicador).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressBarAdd.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Guardado exitosamente", Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(getApplicationContext(), PublicadoresActivity.class);
                        startActivity(myIntent);
                        finish();

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                progressBarAdd.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Error al guadar. Intente nuevamente", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

            } else {
                progressBarAdd.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Hay campos obligatorios vacíos", Toast.LENGTH_SHORT).show();
            }
        } else {
            progressBarAdd.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Hay campos obligatorios vacíos", Toast.LENGTH_SHORT).show();
        }
    }
}
