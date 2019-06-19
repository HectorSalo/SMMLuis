package com.google.firebase.example.seamosmejoresmaestros;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class MiPerfilActivity extends AppCompatActivity {

    private ImageView imagen;
    private EditText nameUser;
    private FirebaseUser user;
    private String nombreUser, emailUser, urlImagen;
    private Uri imagenUrl, imagenEnviar;
    private int recibir;
    private NumberPicker cantidadGrupos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        imagen = (ImageView) findViewById(R.id.imagePerfil);
        nameUser = (EditText) findViewById(R.id.displayName);
        cantidadGrupos = (NumberPicker) findViewById(R.id.cantidadGrupos);

        cantidadGrupos.setMaxValue(30);
        cantidadGrupos.setMinValue(1);

        SharedPreferences preferences = getSharedPreferences("grupos", Context.MODE_PRIVATE);
        int gps = preferences.getInt("cantidad", 1);
        cantidadGrupos.setValue(gps);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            nombreUser = user.getDisplayName();
            emailUser = user.getEmail();
            imagenUrl = user.getPhotoUrl();
        }

        Bundle myBundle = this.getIntent().getExtras();

        if (myBundle != null) {
            recibir = myBundle.getInt("ir");
        }

        Button btnPsw = (Button) findViewById(R.id.buttonUpdatePsw);
        btnPsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarPsw();
            }
        });

        FloatingActionButton fabImagen = (FloatingActionButton) findViewById(R.id.fabImagePerfil);
        fabImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecAccion();
            }
        });

        FloatingActionButton fabUpdate = (FloatingActionButton) findViewById(R.id.fabPerfilGuardar);
        fabUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarImagen();
            }
        });

        cargarDetalles();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void cargarDetalles() {
        if (imagenUrl != null) {
            Glide.with(this).load(imagenUrl).into(imagen);
            imagenEnviar = imagenUrl;
        }
        if (nombreUser != null) {
            if (!nombreUser.isEmpty()) {
                nameUser.setText(nombreUser);
            }
        }
    }

    public void selecAccion() {
        final CharSequence [] opciones = {"Cambiar foto", "Eliminar imagen del perfil", "Cancelar"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("¿Qué desea hacer?");
        dialog.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (opciones[which].equals("Cambiar foto")) {
                    cargarImagen();
                } else if (opciones[which].equals("Eliminar imagen del perfil")) {
                    imagen.setImageResource(R.drawable.ic_menu_camera);
                    imagenEnviar = null;

                } else if (opciones[which].equals("Cancelar")){
                    dialog.dismiss();
                }
            }

        });
        dialog.setIcon(R.drawable.ic_select_image);
        dialog.show();
    }

    public void cargarImagen() {
        Intent myintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        myintent.setType("image/");
        startActivityForResult(myintent.createChooser(myintent, "Seleccione ubicación"), 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            imagenEnviar = data.getData();
            imagen.setImageURI(imagenEnviar);

        }

    }

    private void guardarImagen() {

            final ProgressDialog progressDialog = new ProgressDialog(MiPerfilActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.show();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();

            if (imagenEnviar != null && !imagenEnviar.equals(imagenUrl)) {
                final StorageReference ref = storageReference.child("images/").child(imagenEnviar.getLastPathSegment());

                ref.putFile(imagenEnviar).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                urlImagen = uri.toString();
                                actualizarPerfil();

                            }
                        });
                        Toast.makeText(getApplicationContext(), "Imagen Guardada", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error al guardar", Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Subiendo " + (int) progress + "%");
                    }
                });
            } else if (imagenEnviar != null && imagenEnviar.equals(imagenUrl)){
                urlImagen = String.valueOf(imagenEnviar);
                actualizarPerfil();
                progressDialog.dismiss();
            } else if (imagenEnviar == null) {
                urlImagen = null;
                actualizarPerfil();
                progressDialog.dismiss();
            }


    }

    public void actualizarPerfil() {
        SharedPreferences preferences = getSharedPreferences("grupos", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("cantidad", cantidadGrupos.getValue());
        editor.commit();
        String nombreNuevo = nameUser.getText().toString();
        UserProfileChangeRequest prfileUpdates = null;
        if (urlImagen != null) {
            prfileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nombreNuevo)
                    .setPhotoUri(Uri.parse(urlImagen)).build();
        } else {
            prfileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nombreNuevo)
                    .setPhotoUri(null).build();
        }

        user.updateProfile(prfileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    if (recibir == 1) {
                        Toast.makeText(getApplicationContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(myIntent);
                    } else if (recibir == 0) {
                        Toast.makeText(getApplicationContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
    }

    public void actualizarPsw() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Confirmar");
        dialog.setMessage("Se enviará un email al correo asociado a esta cuenta para cambiar la contraseña.\n¿Desea continuar?");
        dialog.setPositiveButton("Cambiar contraseña", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth auth = FirebaseAuth.getInstance();

                auth.sendPasswordResetEmail(emailUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Mensaje enviado a su correo asociado", Toast.LENGTH_LONG).show();
                        }
                    }
                });
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
