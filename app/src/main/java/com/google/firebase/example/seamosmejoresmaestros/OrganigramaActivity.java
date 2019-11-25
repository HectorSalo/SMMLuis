package com.google.firebase.example.seamosmejoresmaestros;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.example.seamosmejoresmaestros.Adaptadores.GrupoItemAdapter;
import com.google.firebase.example.seamosmejoresmaestros.Adaptadores.OrganigramaAdapter;
import com.google.firebase.example.seamosmejoresmaestros.Constructores.PublicadoresConstructor;
import com.google.firebase.example.seamosmejoresmaestros.Variables.VariablesEstaticas;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OrganigramaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerOrg;
    private ArrayList<PublicadoresConstructor> listPublicadores;
    private OrganigramaAdapter organigramaAdapter;
    private GrupoItemAdapter grupoItemAdapter;
    private ImageView imageNav;
    private TextView tvName;
    private ProgressDialog progress;
    private ArrayList<String> gruposList;
    private LinearLayout linearGrupos, linearAncianos, linearMinisteriales, linearPrecursores;
    private int cantidadGrupos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organigrama);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listPublicadores = new ArrayList<>();
        recyclerOrg = (RecyclerView) findViewById(R.id.recyclerOrganigrama);
        recyclerOrg.setLayoutManager(new LinearLayoutManager(this));
        recyclerOrg.setHasFixedSize(true);
        organigramaAdapter = new OrganigramaAdapter(listPublicadores, this);
        recyclerOrg.setAdapter(organigramaAdapter);
        recyclerOrg.setVisibility(View.GONE);



        linearAncianos = (LinearLayout) findViewById(R.id.linearAncianos);
        linearGrupos = (LinearLayout) findViewById(R.id.linearGrupos);
        linearMinisteriales = (LinearLayout) findViewById(R.id.linearMinisteriales);
        linearPrecursores = (LinearLayout) findViewById(R.id.linearPrecursores);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerOrg.setVisibility(View.GONE);
                linearGrupos.setVisibility(View.VISIBLE);
                linearAncianos.setVisibility(View.VISIBLE);
                linearMinisteriales.setVisibility(View.VISIBLE);
                linearPrecursores.setVisibility(View.VISIBLE);
                getSupportActionBar().setTitle("Organigrama");
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

        SharedPreferences preferences = getSharedPreferences("grupos", Context.MODE_PRIVATE);
        cantidadGrupos = preferences.getInt("cantidad", 1);
        gruposList = new ArrayList<>();
        for (int i = 1; i <= cantidadGrupos; i++) {
            gruposList.add("Grupo " + i);
        }
        gruposList.add(gruposList.size(), "Ver todos");

        datosNavDrawer();

        ImageButton grupos = (ImageButton) findViewById(R.id.imageButtonGrupos);
        grupos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selecGrupo();
            }
        });

        ImageButton ancianos = (ImageButton) findViewById(R.id.imageButtonAncianos);
        ancianos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(OrganigramaActivity.this);
                progress.setMessage("Cargando...");
                progress.setCancelable(false);
                progress.show();
                cargarAncianos();
            }
        });

        ImageButton precursores = (ImageButton) findViewById(R.id.imageButtonPrecursores);
        precursores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(OrganigramaActivity.this);
                progress.setMessage("Cargando...");
                progress.setCancelable(false);
                progress.show();
                cargarPrecursores();
            }
        });

        ImageButton ministeriales = (ImageButton) findViewById(R.id.imageButtonMinisteriales);
        ministeriales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(OrganigramaActivity.this);
                progress.setMessage("Cargando...");
                progress.setCancelable(false);
                progress.show();
                cargarMinisteriales();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            recyclerOrg.setVisibility(View.GONE);
            linearGrupos.setVisibility(View.VISIBLE);
            linearAncianos.setVisibility(View.VISIBLE);
            linearMinisteriales.setVisibility(View.VISIBLE);
            linearPrecursores.setVisibility(View.VISIBLE);
            getSupportActionBar().setTitle("Organigrama");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.organigrama, menu);
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
            AlertDialog.Builder dialog = new AlertDialog.Builder(OrganigramaActivity.this);
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
            Intent myIntent = new Intent(this, MainActivity.class);
            startActivity(myIntent);
        } else if (id == R.id.nav_asignaciones) {
            Intent myIntent = new Intent(this, AsignacionesActivity.class);
            startActivity(myIntent);

        } else if (id == R.id.nav_publicadores) {
            Intent myIntent = new Intent(this, PublicadoresActivity.class);
            startActivity(myIntent);

        } else if (id == R.id.nav_grupoEstudio) {


        } else if (id == R.id.nav_ajustes) {
            Intent myIntent = new Intent(this, SettingsActivity.class);
            startActivity(myIntent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void datosNavDrawer() {
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

            if (photoUrl != null) {
                Glide.with(this).load(photoUrl).into(imageNav);
            }

        }
    }

    public void selecGrupo() {
        final CharSequence [] opciones = gruposList.toArray(new CharSequence[gruposList.size()]);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("¿Cuál Grupo desea ver?");
        dialog.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (opciones[which].equals(gruposList.get(which))) {
                    progress = new ProgressDialog(OrganigramaActivity.this);
                    progress.setMessage("Cargando...");
                    progress.setCancelable(false);
                    progress.show();
                   String valor = String.valueOf(opciones[which].charAt(6));

                   if (valor.equals("d")) {
                       cargarTodosGrupos();
                   } else {
                       cargarGrupos(valor);
                   }
                }
            }

        });
        dialog.setIcon(R.drawable.ic_select_image);
        dialog.show();
    }

    public void cargarGrupos(String grupo) {
        int grupoInt = Integer.parseInt(grupo);
        recyclerOrg.setLayoutManager(new LinearLayoutManager(this));
        recyclerOrg.setHasFixedSize(true);
        organigramaAdapter = new OrganigramaAdapter(listPublicadores, this);
        recyclerOrg.setAdapter(organigramaAdapter);
        linearGrupos.setVisibility(View.GONE);
        linearAncianos.setVisibility(View.GONE);
        linearMinisteriales.setVisibility(View.GONE);
        linearPrecursores.setVisibility(View.GONE);

        listPublicadores = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");

        Query query = reference.whereEqualTo(VariablesEstaticas.BD_GRUPO, grupoInt).orderBy(VariablesEstaticas.BD_APELLIDO, Query.Direction.ASCENDING);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        PublicadoresConstructor publi = new PublicadoresConstructor();
                        publi.setIdPublicador(doc.getId());
                        publi.setNombrePublicador(doc.getString(VariablesEstaticas.BD_NOMBRE));
                        publi.setApellidoPublicador(doc.getString(VariablesEstaticas.BD_APELLIDO));
                        publi.setGenero(doc.getString(VariablesEstaticas.BD_GENERO));
                        publi.setImagen(doc.getString(VariablesEstaticas.BD_IMAGEN));
                        publi.setAnciano(doc.getBoolean(VariablesEstaticas.BD_ANCIANO));
                        publi.setMinisterial(doc.getBoolean(VariablesEstaticas.BD_MINISTERIAL));
                        publi.setSuperintendente(doc.getBoolean(VariablesEstaticas.BD_SUPER));
                        publi.setAuxiliar(doc.getBoolean(VariablesEstaticas.BD_AUXILIAR));
                        publi.setPrecursor(doc.getBoolean(VariablesEstaticas.BD_PRECURSOR));
                        publi.setGrupo(doc.getDouble(VariablesEstaticas.BD_GRUPO));

                        listPublicadores.add(publi);

                    }
                    organigramaAdapter.updateListOrganigrama(listPublicadores);
                    recyclerOrg.setVisibility(View.VISIBLE);
                    getSupportActionBar().setTitle("Miembros: " + listPublicadores.size());
                    progress.dismiss();

                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void cargarTodosGrupos() {
        ArrayList<Integer> cantGrupos = new ArrayList<>();
        for (int i = 1; i <= cantidadGrupos; i++) {
            cantGrupos.add(i);
        }
        recyclerOrg.setLayoutManager(new LinearLayoutManager(this));
        recyclerOrg.setHasFixedSize(true);
        grupoItemAdapter = new GrupoItemAdapter(cantGrupos, this);
        recyclerOrg.setAdapter(grupoItemAdapter);
        grupoItemAdapter.updateList(cantGrupos);

        getSupportActionBar().setTitle("Grupos: " + cantGrupos.size());
        recyclerOrg.setVisibility(View.VISIBLE);
        linearGrupos.setVisibility(View.GONE);
        linearAncianos.setVisibility(View.GONE);
        linearMinisteriales.setVisibility(View.GONE);
        linearPrecursores.setVisibility(View.GONE);
        progress.dismiss();

    }

    public void cargarAncianos() {
        recyclerOrg.setLayoutManager(new LinearLayoutManager(this));
        recyclerOrg.setHasFixedSize(true);
        organigramaAdapter = new OrganigramaAdapter(listPublicadores, this);

        recyclerOrg.setAdapter(organigramaAdapter);

        linearGrupos.setVisibility(View.GONE);
        linearAncianos.setVisibility(View.GONE);
        linearMinisteriales.setVisibility(View.GONE);
        linearPrecursores.setVisibility(View.GONE);

        listPublicadores = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");

        Query query = reference.whereEqualTo(VariablesEstaticas.BD_ANCIANO, true).orderBy(VariablesEstaticas.BD_APELLIDO, Query.Direction.ASCENDING);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        PublicadoresConstructor publi = new PublicadoresConstructor();
                        publi.setIdPublicador(doc.getId());
                        publi.setNombrePublicador(doc.getString(VariablesEstaticas.BD_NOMBRE));
                        publi.setApellidoPublicador(doc.getString(VariablesEstaticas.BD_APELLIDO));
                        publi.setGenero(doc.getString(VariablesEstaticas.BD_GENERO));
                        publi.setImagen(doc.getString(VariablesEstaticas.BD_IMAGEN));
                        publi.setAnciano(doc.getBoolean(VariablesEstaticas.BD_ANCIANO));
                        publi.setMinisterial(doc.getBoolean(VariablesEstaticas.BD_MINISTERIAL));
                        publi.setSuperintendente(doc.getBoolean(VariablesEstaticas.BD_SUPER));
                        publi.setAuxiliar(doc.getBoolean(VariablesEstaticas.BD_AUXILIAR));
                        publi.setPrecursor(doc.getBoolean(VariablesEstaticas.BD_PRECURSOR));
                        publi.setGrupo(doc.getDouble(VariablesEstaticas.BD_GRUPO));

                        listPublicadores.add(publi);

                    }
                    organigramaAdapter.updateListOrganigrama(listPublicadores);
                    recyclerOrg.setVisibility(View.VISIBLE);
                    getSupportActionBar().setTitle("Ancianos: " + listPublicadores.size());
                    progress.dismiss();
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void cargarPrecursores() {
        recyclerOrg.setLayoutManager(new LinearLayoutManager(this));
        recyclerOrg.setHasFixedSize(true);
        organigramaAdapter = new OrganigramaAdapter(listPublicadores, this);

        recyclerOrg.setAdapter(organigramaAdapter);
        linearGrupos.setVisibility(View.GONE);
        linearAncianos.setVisibility(View.GONE);
        linearMinisteriales.setVisibility(View.GONE);
        linearPrecursores.setVisibility(View.GONE);

        listPublicadores = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");

        Query query = reference.whereEqualTo(VariablesEstaticas.BD_PRECURSOR, true).orderBy(VariablesEstaticas.BD_APELLIDO, Query.Direction.ASCENDING);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        PublicadoresConstructor publi = new PublicadoresConstructor();
                        publi.setIdPublicador(doc.getId());
                        publi.setNombrePublicador(doc.getString(VariablesEstaticas.BD_NOMBRE));
                        publi.setApellidoPublicador(doc.getString(VariablesEstaticas.BD_APELLIDO));
                        publi.setGenero(doc.getString(VariablesEstaticas.BD_GENERO));
                        publi.setImagen(doc.getString(VariablesEstaticas.BD_IMAGEN));
                        publi.setAnciano(doc.getBoolean(VariablesEstaticas.BD_ANCIANO));
                        publi.setMinisterial(doc.getBoolean(VariablesEstaticas.BD_MINISTERIAL));
                        publi.setSuperintendente(doc.getBoolean(VariablesEstaticas.BD_SUPER));
                        publi.setAuxiliar(doc.getBoolean(VariablesEstaticas.BD_AUXILIAR));
                        publi.setPrecursor(doc.getBoolean(VariablesEstaticas.BD_PRECURSOR));
                        publi.setGrupo(doc.getDouble(VariablesEstaticas.BD_GRUPO));

                        listPublicadores.add(publi);

                    }
                    organigramaAdapter.updateListOrganigrama(listPublicadores);
                    recyclerOrg.setVisibility(View.VISIBLE);
                    getSupportActionBar().setTitle("Precursores: " + listPublicadores.size());
                    progress.dismiss();
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void cargarMinisteriales() {
        recyclerOrg.setLayoutManager(new LinearLayoutManager(this));
        recyclerOrg.setHasFixedSize(true);
        organigramaAdapter = new OrganigramaAdapter(listPublicadores, this);

        recyclerOrg.setAdapter(organigramaAdapter);
        linearGrupos.setVisibility(View.GONE);
        linearAncianos.setVisibility(View.GONE);
        linearMinisteriales.setVisibility(View.GONE);
        linearPrecursores.setVisibility(View.GONE);

        listPublicadores = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");

        Query query = reference.whereEqualTo(VariablesEstaticas.BD_MINISTERIAL, true).orderBy(VariablesEstaticas.BD_APELLIDO, Query.Direction.ASCENDING);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        PublicadoresConstructor publi = new PublicadoresConstructor();
                        publi.setIdPublicador(doc.getId());
                        publi.setNombrePublicador(doc.getString(VariablesEstaticas.BD_NOMBRE));
                        publi.setApellidoPublicador(doc.getString(VariablesEstaticas.BD_APELLIDO));
                        publi.setGenero(doc.getString(VariablesEstaticas.BD_GENERO));
                        publi.setImagen(doc.getString(VariablesEstaticas.BD_IMAGEN));
                        publi.setAnciano(doc.getBoolean(VariablesEstaticas.BD_ANCIANO));
                        publi.setMinisterial(doc.getBoolean(VariablesEstaticas.BD_MINISTERIAL));
                        publi.setSuperintendente(doc.getBoolean(VariablesEstaticas.BD_SUPER));
                        publi.setAuxiliar(doc.getBoolean(VariablesEstaticas.BD_AUXILIAR));
                        publi.setPrecursor(doc.getBoolean(VariablesEstaticas.BD_PRECURSOR));
                        publi.setGrupo(doc.getDouble(VariablesEstaticas.BD_GRUPO));

                        listPublicadores.add(publi);

                    }
                    organigramaAdapter.updateListOrganigrama(listPublicadores);
                    recyclerOrg.setVisibility(View.VISIBLE);
                    getSupportActionBar().setTitle("Ministeriales: " + listPublicadores.size());
                    progress.dismiss();
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
