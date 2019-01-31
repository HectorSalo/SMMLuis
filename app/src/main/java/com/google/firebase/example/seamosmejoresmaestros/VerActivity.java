package com.google.firebase.example.seamosmejoresmaestros;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class VerActivity extends AppCompatActivity {

    private ImageView imagenPublicador;
    private TextView tvNombre, tvApellido, tvTelefono, tvCorreo, tvfAsignacion, tvfAyudante, tvfSustitucion, tvHabilitar;
    private String idPublicador;
    private ProgressDialog progress;


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

        Bundle recibirBundle = this.getIntent().getExtras();
        idPublicador = recibirBundle.getString("idPublicador");

        FloatingActionButton fabClose = (FloatingActionButton)findViewById(R.id.fabClose);
        fabClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        progress = new ProgressDialog(VerActivity.this);
        progress.setMessage("Cargando...");
        progress.show();
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
                    tvNombre.setText(doc.getString("nombre"));
                    tvApellido.setText(doc.getString("apellido"));
                    tvCorreo.setText(doc.getString("correo"));
                    tvTelefono.setText(doc.getString("telefono"));

                    if (doc.getString("genero").equals("Hombre")) {
                        if (doc.getString("imagen") != null) {
                            Glide.with(getApplicationContext()).load(doc.getString("imagen")).into(imagenPublicador);
                        } else {
                            imagenPublicador.setImageResource(R.drawable.ic_caballero);
                        }
                    } else if (doc.getString("genero").equals("Mujer")) {
                        if (doc.getString("imagen") != null) {
                            Glide.with(getApplicationContext()).load(doc.getString("imagen")).into(imagenPublicador);
                        } else {
                            imagenPublicador.setImageResource(R.drawable.ic_dama);
                        }
                    }

                    if (!doc.getBoolean("habilitado")) {
                        tvHabilitar.setText("Inhabilitado");
                    } else if (doc.getBoolean("habilitado")) {
                        tvHabilitar.setText("");
                    }

                    progress.dismiss();

                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error al cargar. Intente nuevamente", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
