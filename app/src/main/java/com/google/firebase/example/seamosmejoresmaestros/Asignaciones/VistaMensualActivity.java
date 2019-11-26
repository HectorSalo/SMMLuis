package com.google.firebase.example.seamosmejoresmaestros.Asignaciones;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.ajts.androidmads.library.SQLiteToExcel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.example.seamosmejoresmaestros.Adaptadores.VistaMensualAdapter;
import com.google.firebase.example.seamosmejoresmaestros.Constructores.VistaMensualConstructor;
import com.google.firebase.example.seamosmejoresmaestros.R;
import com.google.firebase.example.seamosmejoresmaestros.SQLite.ConectSQLiteHelper;
import com.google.firebase.example.seamosmejoresmaestros.Variables.VariablesEstaticas;
import com.google.firebase.example.seamosmejoresmaestros.Variables.VariablesGenerales;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class VistaMensualActivity extends AppCompatActivity {

    private ArrayList<VistaMensualConstructor> listMensual;
    private VistaMensualAdapter vistaMensualAdapter;
    private Date primeroMes, ultimoMes;
    private ProgressDialog progress;
    private String mesExp, carpetaPath;
    private String NOMBRE_CARPETA = "/MejoresMaestros/";
    private LottieAnimationView lottieAnimationView;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_mensual);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        switch (VariablesGenerales.verMes){
            case 0:
                mesExp = "enero";
                break;
            case 1:
                mesExp = "febrero";
                break;
            case 2:
                mesExp = "marzo";
                break;
            case 3:
                mesExp = "abril";
                break;
            case 4:
                mesExp = "mayo";
                break;
            case 5:
                mesExp = "junio";
                break;
            case 6:
                mesExp = "julio";
                break;
            case 7:
                mesExp = "agosto";
                break;
            case 8:
                mesExp = "septiembre";
                break;
            case 9:
                mesExp = "octubre";
                break;
            case 10:
                mesExp = "noviembre";
                break;
            case 11:
                mesExp = "diciembre";
                break;

        }

        carpetaPath = Environment.getExternalStorageDirectory() + this.NOMBRE_CARPETA;
        File carpeta = new File(carpetaPath);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        } else {
            Log.d("MSG", "Carpeta creada");
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean temaOscuro = sharedPreferences.getBoolean("activarOscuro", false);
        if (temaOscuro) {

            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }

        lottieAnimationView = findViewById(R.id.lottiedescarga);
        constraintLayout = findViewById(R.id.constraintVistaMensual);

        Calendar calendarInicio = Calendar.getInstance();
        Calendar calendarFinal = Calendar.getInstance();
        calendarInicio.set(VariablesGenerales.verAnual, VariablesGenerales.verMes, 1);
        calendarFinal.set(VariablesGenerales.verAnual, VariablesGenerales.verMes, 31);
        primeroMes = calendarInicio.getTime();
        ultimoMes = calendarFinal.getTime();

        listMensual = new ArrayList<>();
        RecyclerView recyclerMensual = findViewById(R.id.recyclerMensual);
        vistaMensualAdapter = new VistaMensualAdapter(listMensual, this);
        recyclerMensual.setLayoutManager(new LinearLayoutManager(this));
        recyclerMensual.setHasFixedSize(true);
        recyclerMensual.setAdapter(vistaMensualAdapter);

        progress = new ProgressDialog(this);
        progress.setMessage("Cargando...");
        progress.setCancelable(false);
        progress.show();

        borrarSQLite();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vista_mensual_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.exportarMes) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(VistaMensualActivity.this);
            dialog.setTitle("Confirmar");
            dialog.setMessage("¿Exportar este mes a un documento Excel?");
            dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    verificarPermisos();
                }
            });
            dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void borrarSQLite() {
        ConectSQLiteHelper conect = new ConectSQLiteHelper(this, "vistamensual", null, 2);
        SQLiteDatabase db = conect.getWritableDatabase();

        for (int i = 0; i<=52;i++) {
            db.delete("mesExp", "Semana=" + i, null);
        }

        db.close();
        cargarMes();
    }

    private void cargarMes() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("sala1");

        Query query = reference.whereGreaterThanOrEqualTo(VariablesEstaticas.BD_FECHA_LUNES, primeroMes).whereLessThanOrEqualTo(VariablesEstaticas.BD_FECHA_LUNES, ultimoMes);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        String semanaS = doc.getId();
                        Date fecha = doc.getDate(VariablesEstaticas.BD_FECHA);
                        String lector = doc.getString(VariablesEstaticas.BD_LECTOR);
                        String encargado1 = doc.getString(VariablesEstaticas.BD_ENCARGADO1);
                        String ayudante1 = doc.getString(VariablesEstaticas.BD_AYUDANTE1);
                        String encargado2 = doc.getString(VariablesEstaticas.BD_ENCARGADO2);
                        String ayudante2 = doc.getString(VariablesEstaticas.BD_AYUDANTE2);
                        String encargado3 = doc.getString(VariablesEstaticas.BD_ENCARGADO3);
                        String ayudante3 = doc.getString(VariablesEstaticas.BD_AYUDANTE3);

                        VistaMensualConstructor mensual = new VistaMensualConstructor();
                        mensual.setFechaMensual(fecha);
                        mensual.setLector(lector);
                        mensual.setEncargado1(encargado1);
                        mensual.setEncargado2(encargado2);
                        mensual.setEncargado3(encargado3);
                        mensual.setAyudante1(ayudante1);
                        mensual.setAyudante2(ayudante2);
                        mensual.setAyudante3(ayudante3);
                        mensual.setAsigancion1(doc.getString(VariablesEstaticas.BD_ASIGNACION1));
                        mensual.setAsignacion2(doc.getString(VariablesEstaticas.BD_ASIGNACION2));
                        mensual.setAsignacion3(doc.getString(VariablesEstaticas.BD_ASIGNACION3));

                        listMensual.add(mensual);

                        String fechaS = new SimpleDateFormat("EEE d MMM yyyy").format(fecha);
                        int semana = Integer.parseInt(semanaS);
                        guardarSqlite(semana, fechaS, lector, encargado1, ayudante1, encargado2, ayudante2, encargado3, ayudante3);
                        
                    }

                    vistaMensualAdapter.updateList(listMensual);
                    progress.dismiss();

                    if (listMensual.isEmpty()) {
                        Snackbar.make(Objects.requireNonNull(getCurrentFocus()), "Este mes no ha sido programado", Snackbar.LENGTH_INDEFINITE).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error al cargar. Intente nuevamente", Toast.LENGTH_LONG).show();
                    progress.dismiss();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al cargar. Intente nuevamente", Toast.LENGTH_LONG).show();
                progress.dismiss();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void guardarSqlite(int semana, String fecha, String lector, String encargado1, String ayudante1, String encargado2, String ayudante2, String encargado3, String ayudante3){
        ConectSQLiteHelper conect = new ConectSQLiteHelper(this, "vistamensual", null, 2);
        SQLiteDatabase db = conect.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Semana", semana);
        values.put("Fecha", fecha);
        values.put("Lector", lector);
        values.put("Encargado1", encargado1);
        values.put("Ayudante1", ayudante1);
        values.put("Encargado2", encargado2);
        values.put("Ayudante2", ayudante2);
        values.put("Encargado3", encargado3);
        values.put("Ayudante3", ayudante3);


        db.insert("mesExp", null, values);
        db.close();

        Log.d("MSG", fecha + lector + encargado1);
    }

    public void verificarPermisos() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            exportarBD();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    exportarBD();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    final Snackbar snackbar = Snackbar.make(constraintLayout, "Permiso Denegado. No se puede almacenar", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    public void exportarBD() {


        SQLiteToExcel sqLiteToExcel = new SQLiteToExcel(this, "vistamensual", carpetaPath);

        sqLiteToExcel.exportSingleTable("mesExp", mesExp + ".xls", new SQLiteToExcel.ExportListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted(String filePath) {
                final Snackbar snackbar = Snackbar.make(constraintLayout, "Descargado en: " + filePath, Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
                lottieAnimationView.setVisibility(View.VISIBLE);
                lottieAnimationView.setAnimation("success.json");
                lottieAnimationView.playAnimation();

                lottieAnimationView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lottieAnimationView.setVisibility(View.GONE);
                        snackbar.dismiss();
                    }
                });
                Log.d("MSG", filePath);

            }

            @Override
            public void onError(Exception e) {
                final Snackbar snackbar = Snackbar.make(constraintLayout, "Error: " + e, Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
                lottieAnimationView.setVisibility(View.VISIBLE);
                lottieAnimationView.setAnimation("error.json");
                lottieAnimationView.playAnimation();

                lottieAnimationView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lottieAnimationView.setVisibility(View.GONE);
                        snackbar.dismiss();
                    }
                });
                Log.d("MSG", " " + e);
            }
        });
    }



}
