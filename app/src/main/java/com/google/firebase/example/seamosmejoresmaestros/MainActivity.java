package com.google.firebase.example.seamosmejoresmaestros;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, EventosFragment.OnFragmentInteractionListener, TemporizadorFragment.OnFragmentInteractionListener {


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
            Intent myIntent = new Intent(this, SettingsActivity.class);
            startActivity(myIntent);
            return true;
        } else if (id == R.id.action_perfil) {
//            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
//            dialog.setTitle("¡En Fase de Desarrollo!");
//            dialog.setMessage("La opción Mi Perfil actualmente se encuentra en elaboración.\nPróximamente podrá disfrutar de sus beneficios");
//            dialog.setIcon(R.drawable.ic_proximamente);
//            dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//            dialog.show();

            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(getApplicationContext(), SplashActivity.class);
                            startActivity(myIntent);
                        }
                    });

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
            android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(MainActivity.this);
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
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
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
}
