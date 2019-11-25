package com.google.firebase.example.seamosmejoresmaestros;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.example.seamosmejoresmaestros.Adaptadores.InicioAdapter;
import com.google.firebase.example.seamosmejoresmaestros.Fragments.EventosFragment;
import com.google.firebase.example.seamosmejoresmaestros.Fragments.TemporizadorFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, EventosFragment.OnFragmentInteractionListener, TemporizadorFragment.OnFragmentInteractionListener {

    private ImageView imageNav;
    private TextView tvName;


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
        tvName = navHeader.findViewById(R.id.tvNameNav);

        SharedPreferences preferences = getSharedPreferences("sugerencia", Context.MODE_PRIVATE);
        boolean sugerencia = preferences.getBoolean("activar", true);
        if (sugerencia) {
            sugerenciaMiPerfil();
        }
        datosNavDrawer();

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
        } else if (id == R.id.action_perfil) {
            Intent myIntent = new Intent(getApplicationContext(), MiPerfilActivity.class);
            Bundle myBundle = new Bundle();
            myBundle.putInt("ir", 1);
            myIntent.putExtras(myBundle);
            startActivity(myIntent);
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
        dialog.setTitle("Sugerencia");
        dialog.setMessage("Para un mejor desempeño de la aplicación, se sugiere configurar el número de Grupos Bíblicos en la opción Mi Perfil");
        dialog.setPositiveButton("Ir Mi Perfil", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent myIntent = new Intent(MainActivity.this, MiPerfilActivity.class);
                startActivity(myIntent);
            }
        });
        dialog.setNegativeButton("Configurar luego", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setNeutralButton("Configurado. No mostrar de nuevo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences preferences = getSharedPreferences("sugerencia", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("activar", false);
                editor.commit();

            }
        });
        dialog.setIcon(R.drawable.ic_sugerencia);
        dialog.show();
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
