package com.skysam.firebase.seamosmejoresmaestros.luis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.skysam.firebase.seamosmejoresmaestros.luis.Adaptadores.InicioAdapter;
import com.skysam.firebase.seamosmejoresmaestros.luis.Asignaciones.AsignacionesActivity;
import com.skysam.firebase.seamosmejoresmaestros.luis.Fragments.EventosFragment;
import com.skysam.firebase.seamosmejoresmaestros.luis.Publicadores.PublicadoresActivity;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, EventosFragment.OnFragmentInteractionListener, TemporizadorFragment.OnFragmentInteractionListener {

    private ImageView imageNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        InicioAdapter inicioAdapter = new InicioAdapter(getSupportFragmentManager());
        ViewPager vpInicio = (ViewPager) findViewById(R.id.viewpagerInicio);
        vpInicio.setAdapter(inicioAdapter);
        vpInicio.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vpInicio));

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
        boolean subscripcionInicial = sharedPreferences.getBoolean("subsinicial", true);
        boolean sugerenciaInicial = sharedPreferences.getBoolean("sugerenciaInicial", true);
        tvName.setText(nombrePerfil);
        boolean temaOscuro = sharedPreferences.getBoolean("activarOscuro", false);
        if (temaOscuro) {

            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }

        if (subscripcionInicial) {
            subsInicial();
        }

        if (sugerenciaInicial) {
            sugerenciaMiPerfil();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
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
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            // Handle the camera action

        } else if (id == R.id.nav_asignaciones) {
            Intent myIntent = new Intent(this, AsignacionesActivity.class);
            startActivity(myIntent);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    public void sugerenciaMiPerfil() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        final EditText editTextGrupos = new EditText(this);
        editTextGrupos.setInputType(InputType.TYPE_CLASS_NUMBER);

        dialog.setTitle("Bienvenido");
        dialog.setMessage("¿Cuántos Grupos integran la congregación?");
        dialog.setView(editTextGrupos);
        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (editTextGrupos.getText().toString().isEmpty()){
                    editTextGrupos.setError("No puede estar vacío");
                } else {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    int numeroGrupos = Integer.parseInt(editTextGrupos.getText().toString());
                    editor.putInt("numeroGrupos", numeroGrupos);
                    editor.putBoolean("sugerenciaInicial", false);
                    editor.commit();
                }
            }
        });
        dialog.setIcon(R.drawable.ic_sugerencia);
        dialog.show();
    }

    private void subsInicial() {
        SharedPreferences sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("subsinicial", false);
        editor.commit();

        FirebaseMessaging.getInstance().subscribeToTopic("notif")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscripcion exitosa";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                        Log.d("suscrito", msg);

                    }
                });
    }


}
