package com.skysam.firebase.seamosmejoresmaestros.luis.Asignaciones;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationManagerCompat;
import androidx.appcompat.app.AlertDialog;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentContainerView;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skysam.firebase.seamosmejoresmaestros.luis.ConfiguracionesActivity;
import com.skysam.firebase.seamosmejoresmaestros.luis.Fragments.Sala1Fragment;
import com.skysam.firebase.seamosmejoresmaestros.luis.MainActivity;
import com.skysam.firebase.seamosmejoresmaestros.luis.OrganigramaActivity;
import com.skysam.firebase.seamosmejoresmaestros.luis.Publicadores.PublicadoresActivity;
import com.skysam.firebase.seamosmejoresmaestros.luis.R;
import com.skysam.firebase.seamosmejoresmaestros.luis.SplashActivity;
import com.skysam.firebase.seamosmejoresmaestros.luis.Variables.VariablesEstaticas;
import com.skysam.firebase.seamosmejoresmaestros.luis.Variables.VariablesGenerales;

import java.util.Calendar;

public class AsignacionesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Sala1Fragment.OnFragmentInteractionListener {

    private Sala1Fragment sala1Fragment;
    private Long fechaLunesLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignaciones);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.cancel(VariablesEstaticas.NOTIFICACION_ID);

        FragmentContainerView fragmentContainerView = findViewById(R.id.containerSalas);

        sala1Fragment = new Sala1Fragment();

        Calendar calendar = Calendar.getInstance();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(AsignacionesActivity.this, EditarSalas.class);
                startActivity(myIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navHeader = navigationView.getHeaderView(0);
        TextView tvName = navHeader.findViewById(R.id.tvNameNav);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String nombrePerfil = sharedPreferences.getString("nombrePerfil", "Nombre de Perfil");
        tvName.setText(nombrePerfil);
        boolean temaOscuro = sharedPreferences.getBoolean("activarOscuro", false);
        if (temaOscuro) {

            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        cargarFecha(calendar);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent myIntent = new Intent(this, MainActivity.class);
            startActivity(myIntent);
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.asignaciones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(AsignacionesActivity.this);
            dialog.setTitle("Confirmar");
            dialog.setMessage("¿Desea cerrar la sesión actual?");
            dialog.setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseAuth.getInstance().signOut();
                    Intent myIntent = new Intent(getApplicationContext(), SplashActivity.class);
                    startActivity(myIntent);
                    finish();
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
        } else if (id == R.id.action_verMes) {
            verMes();
            return true;
        } else if (id == R.id.action_fecha) {
            selecFecha();
            return true;
        } else if (id == R.id.action_delete) {
            deleteSalas();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            Intent myIntent = new Intent(this, MainActivity.class);
            startActivity(myIntent);
        } else if (id == R.id.nav_asignaciones) {

        } else if (id == R.id.nav_publicadores) {
            Intent myIntent = new Intent(this, PublicadoresActivity.class);
            startActivity(myIntent);
        } else if (id == R.id.nav_grupoEstudio) {
            Intent myIntent = new Intent(this, OrganigramaActivity.class);
            startActivity(myIntent);
        } else if (id == R.id.nav_ajustes) {
            Intent myIntent = new Intent(this, ConfiguracionesActivity.class);
            startActivity(myIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void verMes() {
        Calendar calendar = Calendar.getInstance();
        int mes = calendar.get(Calendar.MONTH);
        int anual = calendar.get(Calendar.YEAR);
        final String [] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        LayoutInflater inflater = getLayoutInflater();
        View vista = inflater.inflate(R.layout.month_picker, null);
        final NumberPicker monthPicker = (NumberPicker) vista.findViewById(R.id.mesPicker);
        final NumberPicker yearPicker = (NumberPicker) vista.findViewById(R.id.anualPicker);
        AlertDialog.Builder dialog = new AlertDialog.Builder(AsignacionesActivity.this);

        yearPicker.setMinValue(anual - 2);
        yearPicker.setMaxValue(anual + 2);
        yearPicker.setValue(anual);
        monthPicker.setMinValue(0);
        monthPicker.setMaxValue(meses.length - 1);
        monthPicker.setDisplayedValues(meses);
        monthPicker.setValue(mes);

        dialog.setTitle("Escoja un Mes")
                .setView(vista)
                .setPositiveButton("Seleccionar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), VistaMensualActivity.class);
                        Bundle myBundle = new Bundle();

                        myBundle.putInt("mesVerMes", monthPicker.getValue());
                        myBundle.putInt("anualVerMes", yearPicker.getValue());

                        intent.putExtras(myBundle);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();

    }

    private void selecFecha() {
        final Calendar calendario = Calendar.getInstance();

        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH);
        int anual = calendario.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendario.set(year, month, dayOfMonth);
                cargarFecha(calendario);
            }
        }, anual, mes , dia);
        datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);
        datePickerDialog.show();
    }

    private void cargarFecha(Calendar calendario) {
        int horaSelec = calendario.get(Calendar.HOUR_OF_DAY);
        int minutoSelec = calendario.get(Calendar.MINUTE);
        int segundoSelec = calendario.get(Calendar.SECOND);
        int milisegundoSeec = calendario.get(Calendar.MILLISECOND);
        int diaSemana = calendario.get(Calendar.DAY_OF_WEEK);

        long horaLong = horaSelec * 60 * 60 * 1000;
        long minLong = minutoSelec * 60 * 1000;
        long segLong = segundoSelec * 1000;
        long fechaSelecLong = calendario.getTime().getTime();

        switch (diaSemana) {
            case Calendar.MONDAY:
                fechaLunesLong = fechaSelecLong - horaLong - minLong - segLong - milisegundoSeec;
                break;
            case Calendar.TUESDAY:
                fechaLunesLong = fechaSelecLong - (24 * 60 * 60 * 1000) - horaLong - minLong - segLong - milisegundoSeec;
                break;
            case Calendar.WEDNESDAY:
                fechaLunesLong = fechaSelecLong - (2 * 24 * 60 * 60 * 1000) - horaLong - minLong - segLong - milisegundoSeec;
                break;
            case Calendar.THURSDAY:
                fechaLunesLong = fechaSelecLong - (3 * 24 * 60 * 60 * 1000) - horaLong - minLong - segLong - milisegundoSeec;
                break;
            case Calendar.FRIDAY:
                fechaLunesLong = fechaSelecLong - (4 * 24 * 60 * 60 * 1000) - horaLong - minLong - segLong - milisegundoSeec;
                break;
            case Calendar.SATURDAY:
                fechaLunesLong = fechaSelecLong - (5 * 24 * 60 * 60 * 1000) - horaLong - minLong - segLong - milisegundoSeec;
                break;
            case Calendar.SUNDAY:
                fechaLunesLong = fechaSelecLong - (6 * 24 * 60 * 60 * 1000) - horaLong - minLong - segLong - milisegundoSeec;
                break;
            default:
                break;
        }

        Bundle bundleFragment = new Bundle();
        bundleFragment.putLong("fecha", fechaLunesLong);

        sala1Fragment.setArguments(bundleFragment);

        getSupportFragmentManager().beginTransaction().replace(R.id.containerSalas, sala1Fragment, "home").commit();

        getSupportFragmentManager().beginTransaction().detach(sala1Fragment).attach(sala1Fragment).commit();
    }

    public void deleteSalas() {
        String idSemana = String.valueOf(fechaLunesLong);
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference reference = dbFirestore.collection(VariablesEstaticas.BD_SALA);

        reference.document(idSemana).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (!doc.exists()) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(AsignacionesActivity.this);
                        dialog.setTitle("¡Aviso!");
                        dialog.setMessage("Esta semana no tiene datos para eliminar ya que no ha sido programada.");
                        dialog.setIcon(R.drawable.ic_nopermitido);
                        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    } else {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(AsignacionesActivity.this);
                        dialog.setTitle("¡Advertencia!");
                        dialog.setMessage("Se borrará la programación completa.\n¿Desea continuar?");
                        dialog.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                eliminarSala1();
                            }
                        });
                        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialog.setIcon(R.drawable.ic_advertencia);
                        dialog.show();
                    }
                }
            }
        });
    }

    public void eliminarSala1() {
        ActualizarFechaSalaEliminada actualizarFechaSalaEliminada = new ActualizarFechaSalaEliminada(this);

        actualizarFechaSalaEliminada.cargarSala(fechaLunesLong);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
