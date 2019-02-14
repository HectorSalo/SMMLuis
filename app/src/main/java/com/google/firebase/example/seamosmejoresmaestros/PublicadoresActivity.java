package com.google.firebase.example.seamosmejoresmaestros;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PublicadoresActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerPublicadores;
    private ArrayList<ConstructorPublicadores> listPublicadores;
    private AdapterPublicadores adapterPublicadores;
    private ProgressDialog progress;
    private SwipeRefreshLayout swRefresh;
    private ImageView imageNav;
    private TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicadores);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GridLayoutManager gM = new GridLayoutManager(this, 3);
        recyclerPublicadores = (RecyclerView) findViewById(R.id.recyclerPublicadores);
        recyclerPublicadores.setLayoutManager(gM);
        recyclerPublicadores.setHasFixedSize(true);
        listPublicadores = new ArrayList<>();
        swRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swRefresh.setOnRefreshListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getApplicationContext(), AddPublicador.class);
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
        imageNav = navHeader.findViewById(R.id.imageViewNav);
        tvName = navHeader.findViewById(R.id.tvNameNav);

        datosNavDrawer();


        progress = new ProgressDialog(this);
        progress.setMessage("Cargando...");
        progress.setCancelable(false);
        progress.show();
        adapterPublicadores = new AdapterPublicadores(listPublicadores, this);
        recyclerPublicadores.setAdapter(adapterPublicadores);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, gM.getOrientation());
        recyclerPublicadores.addItemDecoration(dividerItemDecoration);

        cargarLista ();

        adapterPublicadores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), VerActivity.class);
                Bundle myBundle = new Bundle();
                myBundle.putString("idPublicador", listPublicadores.get(recyclerPublicadores.getChildAdapterPosition(v)).getIdPublicador());
                myIntent.putExtras(myBundle);
                startActivity(myIntent);
            }
        });

    }

    private void cargarLista() {
        listPublicadores = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");

        Query query = reference.orderBy("apellido", Query.Direction.ASCENDING);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        ConstructorPublicadores publi = new ConstructorPublicadores();
                        publi.setIdPublicador(doc.getId());
                        publi.setNombrePublicador(doc.getString(UtilidadesStatic.BD_NOMBRE));
                        publi.setApellidoPublicador(doc.getString(UtilidadesStatic.BD_APELLIDO));
                        publi.setCorreo(doc.getString(UtilidadesStatic.BD_CORREO));
                        publi.setTelefono(doc.getString(UtilidadesStatic.BD_TELEFONO));
                        publi.setGenero(doc.getString(UtilidadesStatic.BD_GENERO));
                        publi.setImagen(doc.getString(UtilidadesStatic.BD_IMAGEN));
                        publi.setUltAsignacion(doc.getDate(UtilidadesStatic.BD_DISRECIENTE));
                        publi.setUltAyudante(doc.getDate(UtilidadesStatic.BD_AYURECIENTE));
                        publi.setUltSustitucion(doc.getDate(UtilidadesStatic.BD_SUSTRECIENTE));

                        listPublicadores.add(publi);

                    }
                    adapterPublicadores.updateList(listPublicadores);
                    progress.dismiss();
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            Intent myIntent = new Intent(this, MainActivity.class);
            startActivity(myIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.publicadores, menu);
        MenuItem menuItem = menu.findItem(R.id.action_buscar);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
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
            AlertDialog.Builder dialog = new AlertDialog.Builder(PublicadoresActivity.this);
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

        if (id == R.id.action_buscar) {
            return true;
        }

        if (id == R.id.action_filtro) {
            filtroList();
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


        } else if (id == R.id.nav_grupoEstudio) {
            Intent myIntent = new Intent(this, OrganigramaActivity.class);
            startActivity(myIntent);

        } else if (id == R.id.nav_ajustes) {
            Intent myIntent = new Intent(this, SettingsActivity.class);
            startActivity(myIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (listPublicadores.isEmpty()) {
            Toast.makeText(this, "No hay lista cargada", Toast.LENGTH_SHORT).show();
        } else {
            String userInput = s.toLowerCase();
            ArrayList<ConstructorPublicadores> newList = new ArrayList<>();

            for (ConstructorPublicadores name : listPublicadores) {

                if (name.getNombrePublicador().toLowerCase().contains(userInput) || name.getApellidoPublicador().toLowerCase().contains(userInput)) {

                    newList.add(name);
                }
            }

            adapterPublicadores.updateList(newList);

        }
        return false;
    }

    @Override
    public void onRefresh() {
        cargarLista();
        swRefresh.setRefreshing(false);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void filtroList () {
        final CharSequence [] opciones = {"Alfabéticamente por Nombre", "Alfabéticamente por Apellido", "Fecha de último discurso", "Cancelar"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Ordenar Lista: ");
        dialog.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (opciones[which].equals("Alfabéticamente por Nombre")) {
                    progress = new ProgressDialog(PublicadoresActivity.this);
                    progress.setMessage("Cargando...");
                    progress.setCancelable(false);
                    progress.show();
                    listNombre();

                } else if (opciones[which].equals("Alfabéticamente por Apellido")) {
                    progress = new ProgressDialog(PublicadoresActivity.this);
                    progress.setMessage("Cargando...");
                    progress.setCancelable(false);
                    progress.show();
                    listApellido();

                } else if (opciones[which].equals("Fecha de último discurso")) {
                    progress = new ProgressDialog(PublicadoresActivity.this);
                    progress.setMessage("Cargando...");
                    progress.setCancelable(false);
                    progress.show();
                    listFecha();
                } else if (opciones[which].equals("Cancelar")){
                    dialog.dismiss();
                }
            }

        });
        dialog.show();
    }

    public void listNombre () {
        listPublicadores = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");

        Query query = reference.orderBy("nombre", Query.Direction.ASCENDING);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        ConstructorPublicadores publi = new ConstructorPublicadores();
                        publi.setIdPublicador(doc.getId());
                        publi.setNombrePublicador(doc.getString(UtilidadesStatic.BD_NOMBRE));
                        publi.setApellidoPublicador(doc.getString(UtilidadesStatic.BD_APELLIDO));
                        publi.setCorreo(doc.getString(UtilidadesStatic.BD_CORREO));
                        publi.setTelefono(doc.getString(UtilidadesStatic.BD_TELEFONO));
                        publi.setGenero(doc.getString(UtilidadesStatic.BD_GENERO));
                        publi.setImagen(doc.getString(UtilidadesStatic.BD_IMAGEN));
                        publi.setUltAsignacion(doc.getDate(UtilidadesStatic.BD_DISRECIENTE));
                        publi.setUltAyudante(doc.getDate(UtilidadesStatic.BD_AYURECIENTE));
                        publi.setUltSustitucion(doc.getDate(UtilidadesStatic.BD_SUSTRECIENTE));

                        listPublicadores.add(publi);

                    }
                    adapterPublicadores.updateList(listPublicadores);
                    progress.dismiss();
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void listApellido () {
        listPublicadores = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");

        Query query = reference.orderBy("apellido", Query.Direction.ASCENDING);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        ConstructorPublicadores publi = new ConstructorPublicadores();
                        publi.setIdPublicador(doc.getId());
                        publi.setNombrePublicador(doc.getString(UtilidadesStatic.BD_NOMBRE));
                        publi.setApellidoPublicador(doc.getString(UtilidadesStatic.BD_APELLIDO));
                        publi.setCorreo(doc.getString(UtilidadesStatic.BD_CORREO));
                        publi.setTelefono(doc.getString(UtilidadesStatic.BD_TELEFONO));
                        publi.setGenero(doc.getString(UtilidadesStatic.BD_GENERO));
                        publi.setImagen(doc.getString(UtilidadesStatic.BD_IMAGEN));
                        publi.setUltAsignacion(doc.getDate(UtilidadesStatic.BD_DISRECIENTE));
                        publi.setUltAyudante(doc.getDate(UtilidadesStatic.BD_AYURECIENTE));
                        publi.setUltSustitucion(doc.getDate(UtilidadesStatic.BD_SUSTRECIENTE));

                        listPublicadores.add(publi);

                    }
                    adapterPublicadores.updateList(listPublicadores);
                    progress.dismiss();
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void listFecha() {
        listPublicadores = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");

        Query query = reference.orderBy("disReciente", Query.Direction.ASCENDING);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        ConstructorPublicadores publi = new ConstructorPublicadores();
                        publi.setIdPublicador(doc.getId());
                        publi.setNombrePublicador(doc.getString(UtilidadesStatic.BD_NOMBRE));
                        publi.setApellidoPublicador(doc.getString(UtilidadesStatic.BD_APELLIDO));
                        publi.setCorreo(doc.getString(UtilidadesStatic.BD_CORREO));
                        publi.setTelefono(doc.getString(UtilidadesStatic.BD_TELEFONO));
                        publi.setGenero(doc.getString(UtilidadesStatic.BD_GENERO));
                        publi.setImagen(doc.getString(UtilidadesStatic.BD_IMAGEN));
                        publi.setUltAsignacion(doc.getDate(UtilidadesStatic.BD_DISRECIENTE));
                        publi.setUltAyudante(doc.getDate(UtilidadesStatic.BD_AYURECIENTE));
                        publi.setUltSustitucion(doc.getDate(UtilidadesStatic.BD_SUSTRECIENTE));

                        listPublicadores.add(publi);

                    }
                    adapterPublicadores.updateList(listPublicadores);
                    progress.dismiss();
                } else {
                    progress.dismiss();
                    Toast.makeText(getApplicationContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
