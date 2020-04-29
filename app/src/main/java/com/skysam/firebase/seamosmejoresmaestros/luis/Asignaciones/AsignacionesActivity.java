package com.skysam.firebase.seamosmejoresmaestros.luis.Asignaciones;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager.widget.ViewPager;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.skysam.firebase.seamosmejoresmaestros.luis.Adaptadores.SalasAdapter;
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

    private boolean advertencia;
    private ImageView imageNav;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignaciones);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.cancel(VariablesEstaticas.NOTIFICACION_ID);

        SalasAdapter salasAdapter = new SalasAdapter(getSupportFragmentManager());

        ViewPager vpSalas = (ViewPager) findViewById(R.id.viewpagerSalas);
        vpSalas.setAdapter(salasAdapter);
        vpSalas.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vpSalas));

        SharedPreferences preferences = getSharedPreferences("advertencia", Context.MODE_PRIVATE);
        advertencia = preferences.getBoolean("activar", true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (advertencia) {
                    irSalas();
                } else {
                    Intent myIntent = new Intent(AsignacionesActivity.this, EditarSalas.class);
                    startActivity(myIntent);
                }
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
        imageNav = navHeader.findViewById(R.id.imageViewNav);
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

    private void irSalas() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(AsignacionesActivity.this);
        dialog.setTitle("¡Advertencia!");
        dialog.setMessage("Al empezar a programar las asignaciones de la semana, no debe salir a mitad del proceso.\nEn caso de salir antes de concluir, la información no será guardada y deberá iniciar el proceso nuevamente.\n¿Desea continuar?");
        dialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent myIntent = new Intent(AsignacionesActivity.this, EditarSalas.class);
                startActivity(myIntent);
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setNeutralButton("Continuar y no mostrar de nuevo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences preferences = getSharedPreferences("advertencia", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("activar", false);
                editor.commit();

                Intent myIntent = new Intent(AsignacionesActivity.this, EditarSalas.class);
                startActivity(myIntent);
            }
        });
        dialog.setIcon(R.drawable.ic_advertencia);
        dialog.show();

    }

    private void verMes() {
        Calendar calendar = Calendar.getInstance();
        final String [] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        LayoutInflater inflater = getLayoutInflater();
        View vista = inflater.inflate(R.layout.month_picker, null);
        final NumberPicker monthPicker = (NumberPicker) vista.findViewById(R.id.mesPicker);
        final NumberPicker yearPicker = (NumberPicker) vista.findViewById(R.id.anualPicker);
        AlertDialog.Builder dialog = new AlertDialog.Builder(AsignacionesActivity.this);

        yearPicker.setMinValue(2019);
        yearPicker.setMaxValue(2020);
        yearPicker.setValue(calendar.get(Calendar.YEAR));
        monthPicker.setMinValue(0);
        monthPicker.setMaxValue(meses.length - 1);
        monthPicker.setDisplayedValues(meses);
        monthPicker.setValue(calendar.get(Calendar.MONTH));

        dialog.setTitle("Escoja un Mes")
                .setView(vista)
                .setPositiveButton("Seleccionar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VariablesGenerales.verAnual = yearPicker.getValue();
                        VariablesGenerales.verMes = monthPicker.getValue();
                        startActivity(new Intent(AsignacionesActivity.this, VistaMensualActivity.class));
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
        final Calendar dateDiscurso = Calendar.getInstance();

        int dia = calendario.get(Calendar.DAY_OF_MONTH);
        int mes = calendario.get(Calendar.MONTH);
        int anual = calendario.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateDiscurso.set(year, month, dayOfMonth);
                VariablesGenerales.semanaSelec = dateDiscurso.get(Calendar.WEEK_OF_YEAR);
                VariablesGenerales.fechaSelec = dateDiscurso.getTime();

                recreate();

            }
        }, anual, mes , dia);
        datePickerDialog.show();
    }

    public void deleteSalas() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(AsignacionesActivity.this);
        dialog.setTitle("¡Advertencia!");
        dialog.setMessage("Se borrará la programación de ambas salas.\n¿Desea continuar?");
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

    public void eliminarSala1() {
        ActualizarFechaSalaEliminada actualizarFechaSalaEliminada = new ActualizarFechaSalaEliminada(this);
        Calendar calendario = Calendar.getInstance();
        int semanaActual = calendario.get(Calendar.WEEK_OF_YEAR);

        if (VariablesGenerales.semanaSelec != 0) {
            actualizarFechaSalaEliminada.cargarSala1(VariablesGenerales.semanaSelec);
        } else {
            actualizarFechaSalaEliminada.cargarSala1(semanaActual);

        }


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
