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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.skysam.firebase.seamosmejoresmaestros.luis.R;
import com.skysam.firebase.seamosmejoresmaestros.luis.Variables.VariablesEstaticas;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddPublicador extends AppCompatActivity {

    private TextInputEditText editNombrePub, editApellidoPub, editTelefonoPub, editCorreoPub, etGrupo;
    private TextInputLayout layoutNombre, layoutApellido, layoutCorreo, layoutGrupo;
    private RadioButton radioMasculino, radioFemenino;
    private ProgressBar progressBarAdd;
    private CheckBox cbAnciano, cbMinisterial, cbPrecursor;
    private int grupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_publicador);

        editNombrePub = findViewById(R.id.et_nombre);
        editApellidoPub = findViewById(R.id.et_apellido);
        editTelefonoPub = findViewById(R.id.et_telefono);
        editCorreoPub = findViewById(R.id.et_correo);
        etGrupo = findViewById(R.id.et_grupo);
        layoutGrupo = findViewById(R.id.outlined_grupo);
        layoutNombre = findViewById(R.id.outlined_nombre);
        layoutApellido = findViewById(R.id.outlined_apellido);
        layoutCorreo = findViewById(R.id.outlined_correo);
        radioMasculino = findViewById(R.id.radioButtonMasculino);
        radioFemenino = findViewById(R.id.radioButtonFemenino);
        cbAnciano = findViewById(R.id.cbAnciano);
        cbMinisterial = findViewById(R.id.cbSiervoM);
        cbPrecursor = findViewById(R.id.cbPrecursor);
        progressBarAdd = findViewById(R.id.progressBarAddPub);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean temaOscuro = sharedPreferences.getBoolean("activarOscuro", false);
        if (temaOscuro) {

            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }

        radioMasculino.setChecked(true);

        Button buttonAgregar = findViewById(R.id.bottomAgregar);
        Button buttonCancelar = findViewById(R.id.bottomCancelar);

        buttonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarAdd.setVisibility(View.VISIBLE);
                layoutNombre.setError(null);
                layoutApellido.setError(null);
                layoutCorreo.setError(null);
                layoutGrupo.setError(null);
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
        String grupoS = etGrupo.getText().toString();

        boolean nombreValido;
        boolean apellidoValido;
        boolean grupoValido;
        boolean anciano = false;
        boolean ministerial = false;
        boolean precursor;

        if (NombrePub.isEmpty()) {
            progressBarAdd.setVisibility(View.GONE);
            layoutNombre.setError("Este campo no puede estar vacío");
            nombreValido = false;
        } else {
            nombreValido = true;
        }

        if (ApellidoPub.isEmpty()) {
            progressBarAdd.setVisibility(View.GONE);
            layoutApellido.setError("Este campo no puede estar vacío");
            apellidoValido = false;
        } else {
            apellidoValido = true;
        }

        if (grupoS.isEmpty()) {
            progressBarAdd.setVisibility(View.GONE);
            layoutGrupo.setError("Este campo no puede estar vacío");
            grupoValido  = false;
        } else {
            grupo = Integer.parseInt(grupoS);
            if (grupo == 0) {
                progressBarAdd.setVisibility(View.GONE);
                layoutGrupo.setError("El grupo debe ser mayor a cero");
                grupoValido  = false;
            } else {
                grupoValido = true;
            }
        }

        if (cbAnciano.isChecked() && cbMinisterial.isChecked()) {
            Toast.makeText(getApplicationContext(), "No puede ser Anciano y Ministerial", Toast.LENGTH_SHORT).show();
            progressBarAdd.setVisibility(View.GONE);
            grupoValido = false;
        } else if (cbAnciano.isChecked() && !cbMinisterial.isChecked()) {
            anciano = true;
            ministerial = false;
        } else if (!cbAnciano.isChecked() && cbMinisterial.isChecked()) {
            anciano = false;
            ministerial = true;
        } else if (!cbAnciano.isChecked() && !cbMinisterial.isChecked()) {
            anciano = false;
            ministerial = false;
        }

        if (radioFemenino.isChecked() && (cbMinisterial.isChecked() || cbAnciano.isChecked())) {
            Toast.makeText(getApplicationContext(), "Este nombramiento no aplica para una mujer", Toast.LENGTH_SHORT).show();
            progressBarAdd.setVisibility(View.GONE);
            grupoValido = false;
        }

        precursor = cbPrecursor.isChecked();

        if (!Correo.isEmpty()) {
            if (!Correo.contains("@")) {
                layoutCorreo.setError("Formato inválido para correo");
                progressBarAdd.setVisibility(View.GONE);
                grupoValido = false;
            }
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (nombreValido && apellidoValido && grupoValido) {

                Map<String, Object> publicador = new HashMap<>();
                publicador.put(VariablesEstaticas.BD_NOMBRE, NombrePub);
                publicador.put(VariablesEstaticas.BD_APELLIDO, ApellidoPub);
                publicador.put(VariablesEstaticas.BD_TELEFONO, Telefono);
                publicador.put(VariablesEstaticas.BD_CORREO, Correo);
                publicador.put(VariablesEstaticas.BD_HABILITADO, true);
                publicador.put(VariablesEstaticas.BD_IMAGEN, null);
                publicador.put(VariablesEstaticas.BD_DISRECIENTE, null);
                publicador.put(VariablesEstaticas.BD_AYURECIENTE, null);
                publicador.put(VariablesEstaticas.BD_SUSTRECIENTE, null);
                publicador.put(VariablesEstaticas.BD_MINISTERIAL, ministerial);
                publicador.put(VariablesEstaticas.BD_SUPER, false);
                publicador.put(VariablesEstaticas.BD_PRECURSOR, precursor);
                publicador.put(VariablesEstaticas.BD_ANCIANO, anciano);
                publicador.put(VariablesEstaticas.BD_AUXILIAR, false);
                publicador.put(VariablesEstaticas.BD_GRUPO, grupo);


                if (radioMasculino.isChecked()) {
                    publicador.put(VariablesEstaticas.BD_GENERO, "Hombre");
                } else if (radioFemenino.isChecked()) {
                    publicador.put(VariablesEstaticas.BD_GENERO, "Mujer");
                }

                db.collection(VariablesEstaticas.BD_PUBLICADORES).add(publicador).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressBarAdd.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Guardado exitosamente", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBarAdd.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Error al guadar. Intente nuevamente", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
    }
}
