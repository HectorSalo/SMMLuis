package com.google.firebase.example.seamosmejoresmaestros;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EditarPubActivity extends AppCompatActivity {

    private ImageView imagePub;
    private EditText nombrePub, apellidoPub, telefono, correo;
    private TextView fdiscurso, fayudante, fsustitucion;
    private RadioButton radioHombre, radioMujer;
    private CheckBox cbHabilitar, cbSuper, cbAnciano, cbPrecursor, cbMinisterial, cbAuxiliar;
    private String idPb, urlImagen, imgRecibir, genero;
    private Integer dia, mes, anual;
    private Double grupoRecibir;
    private NumberPicker numeroGrupo;
    private Date discurso, ayudante, sustitucion, disRecibir, ayuRecibir, sustRecibir;
    private ProgressDialog progress;
    private Uri mipath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_pub);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEditPub);
        setSupportActionBar(toolbar);

        imagePub = (ImageView) findViewById(R.id.imageBtPublicador);
        nombrePub = (EditText) findViewById(R.id.etNombre);
        apellidoPub = (EditText) findViewById(R.id.etApellido);
        telefono = (EditText) findViewById(R.id.etTelefono);
        correo = (EditText) findViewById(R.id.etCorreo);
        radioHombre = (RadioButton) findViewById(R.id.radioHombre);
        radioMujer = (RadioButton) findViewById(R.id.radioMujer);
        cbHabilitar = (CheckBox) findViewById(R.id.cbHabilitar);
        cbAnciano = (CheckBox) findViewById(R.id.cbAnciano);
        cbMinisterial = (CheckBox) findViewById(R.id.cbSiervoM);
        cbSuper = (CheckBox) findViewById(R.id.cbSuperintendente);
        cbPrecursor = (CheckBox) findViewById(R.id.cbPrecursor);
        cbAuxiliar = (CheckBox) findViewById(R.id.cbAuxiliar);
        fdiscurso = (TextView) findViewById(R.id.etfdiscurso);
        fayudante = (TextView) findViewById(R.id.etfayudante);
        fsustitucion = (TextView) findViewById(R.id.etfsustitucion);
        numeroGrupo = (NumberPicker) findViewById(R.id.numberGrupoEditar);

        SharedPreferences preferences = getSharedPreferences("grupos", Context.MODE_PRIVATE);
        int gps = preferences.getInt("cantidad", 1);
        numeroGrupo.setMinValue(1);
        numeroGrupo.setMaxValue(gps);


        Bundle myBundle = this.getIntent().getExtras();
        idPb = myBundle.getString("idEditar");

        progress = new ProgressDialog(EditarPubActivity.this);
        progress.setMessage("Cargando...");
        progress.setCancelable(false);
        progress.show();
        cargarDetalles ();

        FloatingActionButton fabImagen = (FloatingActionButton)findViewById(R.id.fabEditImage);
        fabImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecAccion();
            }
        });

        fdiscurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fechaDiscurso();
            }
        });

        fayudante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fechaAyudante();
            }
        });

        fsustitucion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fechaSustitucion();
            }
        });
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
            guardarPublicador();
            return true;
        } else if (id == R.id.menu_cancel_pub) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fechaDiscurso() {
        final Calendar calendario = Calendar.getInstance();
        final Calendar dateDiscurso = Calendar.getInstance();

        dia = calendario.get(Calendar.DAY_OF_MONTH);
        mes = calendario.get(Calendar.MONTH);
        anual = calendario.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateDiscurso.set(year, month, dayOfMonth);
                discurso = dateDiscurso.getTime();
                fdiscurso.setText(new SimpleDateFormat("EEE d MMM yyyy").format(discurso));
            }
        }, anual, mes , dia);
        datePickerDialog.show();
    }

    private void fechaAyudante() {
        final Calendar calendario = Calendar.getInstance();
        final Calendar dateAyudante = Calendar.getInstance();

        dia = calendario.get(Calendar.DAY_OF_MONTH);
        mes = calendario.get(Calendar.MONTH);
        anual = calendario.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateAyudante.set(year, month, dayOfMonth);
                ayudante = dateAyudante.getTime();
                fayudante.setText(new SimpleDateFormat("EEE d MMM yyyy").format(ayudante));
            }
        }, anual, mes , dia);
        datePickerDialog.show();
    }

    private void fechaSustitucion() {
        final Calendar calendario = Calendar.getInstance();
        final Calendar dateSustitucion = Calendar.getInstance();

        dia = calendario.get(Calendar.DAY_OF_MONTH);
        mes = calendario.get(Calendar.MONTH);
        anual = calendario.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateSustitucion.set(year, month, dayOfMonth);
                sustitucion = dateSustitucion.getTime();
                fsustitucion.setText(new SimpleDateFormat("EEE d MMM yyyy").format(sustitucion));
            }
        }, anual, mes , dia);
        datePickerDialog.show();
    }

    private void cargarDetalles() {
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference reference = dbFirestore.collection("publicadores");

        reference.document(idPb).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    nombrePub.setText(doc.getString(UtilidadesStatic.BD_NOMBRE));
                    apellidoPub.setText(doc.getString(UtilidadesStatic.BD_APELLIDO));
                    correo.setText(doc.getString(UtilidadesStatic.BD_CORREO));
                    telefono.setText(doc.getString(UtilidadesStatic.BD_TELEFONO));
                    imgRecibir = doc.getString(UtilidadesStatic.BD_IMAGEN);
                    genero = doc.getString(UtilidadesStatic.BD_GENERO);
                    disRecibir = doc.getDate(UtilidadesStatic.BD_DISRECIENTE);
                    ayuRecibir = doc.getDate(UtilidadesStatic.BD_AYURECIENTE);
                    sustRecibir = doc.getDate(UtilidadesStatic.BD_SUSTRECIENTE);
                    grupoRecibir = doc.getDouble(UtilidadesStatic.BD_GRUPO);
                    int x = (int)grupoRecibir.doubleValue();
                    numeroGrupo.setValue(x);


                    if (genero.equals("Hombre")) {
                        radioHombre.setChecked(true);
                        if (imgRecibir != null) {
                            Glide.with(getApplicationContext()).load(imgRecibir).into(imagePub);
                            urlImagen = imgRecibir;
                        } else {
                            imagePub.setImageResource(R.drawable.ic_caballero);
                        }
                    } else if (genero.equals("Mujer")) {
                        if (imgRecibir != null) {
                            Glide.with(getApplicationContext()).load(imgRecibir).into(imagePub);
                            urlImagen = imgRecibir;
                        } else {
                            imagePub.setImageResource(R.drawable.ic_dama);
                        }
                        radioMujer.setChecked(true);
                    }

                    if (!doc.getBoolean(UtilidadesStatic.BD_HABILITADO)) {
                        cbHabilitar.setChecked(true);
                    }
                    if (doc.getBoolean(UtilidadesStatic.BD_SUPER)){
                        cbSuper.setChecked(true);
                    }
                    if (doc.getBoolean(UtilidadesStatic.BD_PRECURSOR)) {
                        cbPrecursor.setChecked(true);
                    }
                    if (doc.getBoolean(UtilidadesStatic.BD_MINISTERIAL)) {
                        cbMinisterial.setChecked(true);
                    }
                    if (doc.getBoolean(UtilidadesStatic.BD_AUXILIAR)) {
                        cbAuxiliar.setChecked(true);
                    }
                    if (doc.getBoolean(UtilidadesStatic.BD_ANCIANO)) {
                        cbAnciano.setChecked(true);
                    }

                    if (disRecibir != null) {
                        fdiscurso.setText(new SimpleDateFormat("EEE d MMM yyyy").format(disRecibir));
                        discurso = disRecibir;
                    }
                    if (ayuRecibir != null) {
                        fayudante.setText(new SimpleDateFormat("EEE d MMM yyyy").format(ayuRecibir));
                        ayudante = ayuRecibir;
                    }
                    if (sustRecibir != null) {
                        fsustitucion.setText(new SimpleDateFormat("EEE d MMM yyyy").format(sustRecibir));
                        sustitucion = sustRecibir;
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

    private void guardarImagen() {
        final ProgressDialog progressDialog = new ProgressDialog(EditarPubActivity.this);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        final StorageReference ref = storageReference.child("images/").child(mipath.getLastPathSegment());
        if (mipath != null) {

            ref.putFile(mipath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            urlImagen = uri.toString();

                        }
                    });
                    Toast.makeText(getApplicationContext(), "Imagen Guardada", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Error al guardar", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Subiendo " + (int)progress+ "%");
                }
            });
        }

    }

    private void cargarImagen() {
        Intent myintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        myintent.setType("image/");
        startActivityForResult(myintent.createChooser(myintent, "Seleccione ubicación"), 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            mipath = data.getData();
            imagePub.setImageURI(mipath);

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Confirmar");
            dialog.setMessage("¿Desea subir esta foto al perfil del publicador?");

            dialog.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   guardarImagen();
                }
            });

            dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(false);
            dialog.setIcon(R.drawable.ic_upload_image);
            dialog.show();
        }

    }

    private void selecAccion() {
            final CharSequence [] opciones = {"Cambiar foto del publicador", "Usar imagen por defecto", "Cancelar"};
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("¿Qué desea hacer?");
            dialog.setItems(opciones, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (opciones[which].equals("Cambiar foto del publicador")) {
                        cargarImagen();
                    } else if (opciones[which].equals("Usar imagen por defecto")) {
                        if (genero.equals("Hombre")) {
                            imagePub.setImageResource(R.drawable.ic_caballero);
                            urlImagen = null;
                            dialog.dismiss();
                        } else if (genero.equals("Mujer")) {
                            imagePub.setImageResource(R.drawable.ic_dama);
                            urlImagen = null;
                            dialog.dismiss();
                        }
                    } else if (opciones[which].equals("Cancelar")){
                        dialog.dismiss();
                    }
                }

            });
            dialog.setIcon(R.drawable.ic_select_image);
            dialog.show();

    }

    private void guardarPublicador() {

        FirebaseFirestore dbEditar = FirebaseFirestore.getInstance();

        int grupo = numeroGrupo.getValue();
        String NombrePub = nombrePub.getText().toString();
        String ApellidoPub = apellidoPub.getText().toString();
        String Telefono = telefono.getText().toString();
        String Correo = correo.getText().toString();
        String imagen = urlImagen;

        if (!NombrePub.isEmpty() && !ApellidoPub.isEmpty()) {
            if (radioHombre.isChecked() || radioMujer.isChecked()) {
                if (cbMinisterial.isChecked() && cbAnciano.isChecked()) {

                    Toast.makeText(getApplicationContext(), "No puede ser anciano y ministerial", Toast.LENGTH_SHORT).show();

                } else {
                    Map<String, Object> publicador = new HashMap<>();
                    publicador.put(UtilidadesStatic.BD_NOMBRE, NombrePub);
                    publicador.put(UtilidadesStatic.BD_APELLIDO, ApellidoPub);
                    publicador.put(UtilidadesStatic.BD_TELEFONO, Telefono);
                    publicador.put(UtilidadesStatic.BD_CORREO, Correo);
                    publicador.put(UtilidadesStatic.BD_IMAGEN, imagen);
                    publicador.put(UtilidadesStatic.BD_GRUPO, grupo);

                    if (discurso != null) {
                        publicador.put(UtilidadesStatic.BD_DISRECIENTE, discurso);
                        publicador.put(UtilidadesStatic.BD_DISVIEJO, disRecibir);
                    } else {
                        publicador.put(UtilidadesStatic.BD_DISRECIENTE, null);
                        publicador.put(UtilidadesStatic.BD_DISVIEJO, null);
                    }
                    if (ayudante != null) {
                        publicador.put(UtilidadesStatic.BD_AYURECIENTE, ayudante);
                        publicador.put(UtilidadesStatic.BD_AYUVIEJO, ayuRecibir);
                    } else {
                        publicador.put(UtilidadesStatic.BD_AYURECIENTE, null);
                        publicador.put(UtilidadesStatic.BD_AYUVIEJO, null);
                    }
                    if (sustitucion != null) {
                        publicador.put(UtilidadesStatic.BD_SUSTRECIENTE, sustitucion);
                        publicador.put(UtilidadesStatic.BD_SUSTVIEJO, sustRecibir);
                    } else {
                        publicador.put(UtilidadesStatic.BD_SUSTRECIENTE, null);
                        publicador.put(UtilidadesStatic.BD_SUSTVIEJO, null);
                    }


                    if (radioHombre.isChecked()) {
                        publicador.put(UtilidadesStatic.BD_GENERO, "Hombre");
                    } else if (radioMujer.isChecked()) {
                        publicador.put(UtilidadesStatic.BD_GENERO, "Mujer");
                    }

                    if (cbHabilitar.isChecked()) {
                        publicador.put(UtilidadesStatic.BD_HABILITADO, false);
                    } else {
                        publicador.put(UtilidadesStatic.BD_HABILITADO, true);
                    }

                    if (cbAnciano.isChecked()) {
                        publicador.put(UtilidadesStatic.BD_ANCIANO, true);
                    } else {
                        publicador.put(UtilidadesStatic.BD_ANCIANO, false);
                    }

                    if (cbAuxiliar.isChecked()) {
                        publicador.put(UtilidadesStatic.BD_AUXILIAR, true);
                    } else {
                        publicador.put(UtilidadesStatic.BD_AUXILIAR, false);
                    }

                    if (cbMinisterial.isChecked()) {
                        publicador.put(UtilidadesStatic.BD_MINISTERIAL, true);
                    } else {
                        publicador.put(UtilidadesStatic.BD_MINISTERIAL, false);
                    }

                    if (cbPrecursor.isChecked()) {
                        publicador.put(UtilidadesStatic.BD_PRECURSOR, true);
                    } else {
                        publicador.put(UtilidadesStatic.BD_PRECURSOR, false);
                    }

                    if (cbSuper.isChecked()) {
                        publicador.put(UtilidadesStatic.BD_SUPER, true);
                    } else {
                        publicador.put(UtilidadesStatic.BD_SUPER, false);
                    }

                    dbEditar.collection("publicadores").document(idPb).set(publicador).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(getApplicationContext(), "Publicador modificado ", Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(getApplicationContext(), PublicadoresActivity.class);
                            startActivity(myIntent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(getApplicationContext(), "Error al guardar. Intente nuevamente", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }else {
                Toast.makeText(getApplicationContext(), "Hay campos obligatorios vacíos", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Hay campos obligatorios vacíos", Toast.LENGTH_SHORT).show();

        }
    }

}
