package com.skysam.firebase.seamosmejoresmaestros.luis.Publicadores;

import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.skysam.firebase.seamosmejoresmaestros.luis.R;
import com.skysam.firebase.seamosmejoresmaestros.luis.Variables.VariablesEstaticas;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class VerActivity extends AppCompatActivity {

    private ImageView imagenPublicador;
    private TextView tvNombre, tvApellido, tvTelefono, tvCorreo, tvfAsignacion, tvfAyudante, tvfSustitucion, tvHabilitar, tvGrupo;
    private String idPublicador;
    private Date discurso, ayudante, sustitucion;
    private ProgressBar progressBarVer;
    private boolean temaOscuro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver);

        imagenPublicador = (ImageView)findViewById(R.id.imageViewPublicador);
        tvNombre = (TextView) findViewById(R.id.tvNombre);
        tvApellido = (TextView) findViewById(R.id.tvApellido);
        tvTelefono = (TextView) findViewById(R.id.tvTelefono);
        tvCorreo = (TextView) findViewById(R.id.tvCorro);
        tvfAsignacion = (TextView) findViewById(R.id.textViewfdiscurso);
        tvfAyudante = (TextView) findViewById(R.id.textViewfayudante);
        tvfSustitucion = (TextView) findViewById(R.id.textViewfsustitucion);
        tvHabilitar = (TextView) findViewById(R.id.tvHabilitar);
        tvGrupo = (TextView) findViewById(R.id.textViewGrupoVer);
        progressBarVer = findViewById(R.id.progressBarVer);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        temaOscuro = sharedPreferences.getBoolean("activarOscuro", false);
        if (temaOscuro) {

            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }

        Bundle recibirBundle = this.getIntent().getExtras();
        idPublicador = recibirBundle.getString("idPublicador");

        FloatingActionButton fabClose = (FloatingActionButton)findViewById(R.id.fabClose);
        fabClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        progressBarVer.setVisibility(View.VISIBLE);
        consultaDetalles();
    }

    public void consultaDetalles() {
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference reference = dbFirestore.collection("publicadores");

        reference.document(idPublicador).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    tvNombre.setText(doc.getString(VariablesEstaticas.BD_NOMBRE));
                    tvApellido.setText(doc.getString(VariablesEstaticas.BD_APELLIDO));
                    tvCorreo.setText(doc.getString(VariablesEstaticas.BD_CORREO));
                    tvTelefono.setText(doc.getString(VariablesEstaticas.BD_TELEFONO));
                    discurso = doc.getDate(VariablesEstaticas.BD_DISRECIENTE);
                    ayudante = doc.getDate(VariablesEstaticas.BD_AYURECIENTE);
                    sustitucion = doc.getDate(VariablesEstaticas.BD_SUSTRECIENTE);
                    double grupo = doc.getDouble(VariablesEstaticas.BD_GRUPO);
                    int x = (int)grupo;
                    tvGrupo.setText(String.valueOf(x));

                    if (doc.getString(VariablesEstaticas.BD_GENERO).equals("Hombre")) {
                        if (doc.getString(VariablesEstaticas.BD_IMAGEN) != null) {
                            Glide.with(getApplicationContext()).load(doc.getString(VariablesEstaticas.BD_IMAGEN)).into(imagenPublicador);
                        } else {
                            if (temaOscuro) {
                                imagenPublicador.setImageResource(R.drawable.ic_caballero_blanco);
                            } else {
                                imagenPublicador.setImageResource(R.drawable.ic_caballero);
                            }
                        }
                    } else if (doc.getString(VariablesEstaticas.BD_GENERO).equals("Mujer")) {
                        if (doc.getString(VariablesEstaticas.BD_IMAGEN) != null) {
                            Glide.with(getApplicationContext()).load(doc.getString(VariablesEstaticas.BD_IMAGEN)).into(imagenPublicador);
                        } else {
                            if (temaOscuro) {
                                imagenPublicador.setImageResource(R.drawable.ic_dama_blanco);
                            } else {
                                imagenPublicador.setImageResource(R.drawable.ic_dama);
                            }
                        }
                    }

                    if (!doc.getBoolean(VariablesEstaticas.BD_HABILITADO)) {
                        tvHabilitar.setText("Inhabilitado");
                    } else if (doc.getBoolean(VariablesEstaticas.BD_HABILITADO)) {
                        tvHabilitar.setText("");
                    }

                    if (discurso != null) {
                        tvfAsignacion.setText(new SimpleDateFormat("EEE d MMM yyyy").format(discurso));
                    } else {
                        tvfAsignacion.setText("");
                    }
                    if (ayudante != null) {
                        tvfAyudante.setText(new SimpleDateFormat("EEE d MMM yyyy").format(ayudante));
                    } else {
                        tvfAyudante.setText("");
                    }

                    if (sustitucion != null ) {
                        tvfSustitucion.setText(new SimpleDateFormat("EEE d MMM yyyy").format(sustitucion));
                    } else {
                        tvfSustitucion.setText("");
                    }

                    progressBarVer.setVisibility(View.GONE);

                } else {
                    progressBarVer.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Error al cargar. Intente nuevamente", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
