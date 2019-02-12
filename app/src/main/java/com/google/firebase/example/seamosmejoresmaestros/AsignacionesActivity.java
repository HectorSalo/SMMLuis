package com.google.firebase.example.seamosmejoresmaestros;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AsignacionesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Sala1Fragment.OnFragmentInteractionListener, Sala2Fragment.OnFragmentInteractionListener {

    private boolean advertencia;
    private ViewPager vpSalas;
    private SalasAdapter salasAdapter;
    private ImageView imageNav;
    private TextView tvName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignaciones);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        salasAdapter = new SalasAdapter(getSupportFragmentManager());

        vpSalas = (ViewPager) findViewById(R.id.viewpagerSalas);
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
        tvName = navHeader.findViewById(R.id.tvNameNav);

        datosNavDrawer();

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
            android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(AsignacionesActivity.this);
            dialog.setTitle("¡En Fase de Desarrollo!");
            dialog.setMessage("La opción Grupo de Estudio actualmente se encuentra en elaboración.\nPróximamente podrá disfrutar de sus beneficios");
            dialog.setIcon(R.drawable.ic_proximamente);
            dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();

        } else if (id == R.id.nav_perfil) {
            android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(AsignacionesActivity.this);
            dialog.setTitle("¡En Fase de Desarrollo!");
            dialog.setMessage("La opción Mi Perfil actualmente se encuentra en elaboración.\nPróximamente podrá disfrutar de sus beneficios");
            dialog.setIcon(R.drawable.ic_proximamente);
            dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();

        } else if (id == R.id.nav_ajustes) {
            Intent myIntent = new Intent(this, SettingsActivity.class);
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
                Utilidades.semanaSelec = dateDiscurso.get(Calendar.WEEK_OF_YEAR);
                Date discurso = dateDiscurso.getTime();
                Utilidades.fechaSelec = discurso;
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

        if (Utilidades.semanaSelec != 0) {
            actualizarFechaSalaEliminada.cargarSala1(Utilidades.semanaSelec);
        } else {
            actualizarFechaSalaEliminada.cargarSala1(semanaActual);

        }


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void datosNavDrawer () {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            if (name != null) {
                if (name.isEmpty()) {
                    tvName.setText(email);
                } else {
                    tvName.setText(name);
                }
            } else {
                tvName.setText("");
            }

            if(photoUrl != null) {
                Glide.with(this).load(photoUrl).into(imageNav);
            }

        }
    }
}
