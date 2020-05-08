package com.skysam.firebase.seamosmejoresmaestros.luis.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.skysam.firebase.seamosmejoresmaestros.luis.Asignaciones.SustituirActivity;
import com.skysam.firebase.seamosmejoresmaestros.luis.R;
import com.skysam.firebase.seamosmejoresmaestros.luis.Variables.VariablesGenerales;
import com.skysam.firebase.seamosmejoresmaestros.luis.Variables.VariablesEstaticas;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class Sala1Fragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private TextView tvAviso, tvFecha, tvTitulo, tvLector, tvAsignacion1, tvEncargado1, tvAyudante1, tvAsignacion2, tvEncargado2,tvAyudante2, tvAsignacion3, tvEncargado3, tvAyudante3, tvLectura;
    private LinearLayout linearSala;
    private ProgressBar progressBarSala1;
    private Date fechaActual;
    private long fechaLunes;

    public Sala1Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_sala1, container, false);

        tvAviso = (TextView) vista.findViewById(R.id.tvAviso);
        tvFecha = (TextView) vista.findViewById(R.id.tvFechaAsig);
        tvTitulo = (TextView) vista.findViewById(R.id.tvTitulo);
        tvLectura = vista.findViewById(R.id.tvLecturaSala1);
        tvLector = (TextView) vista.findViewById(R.id.textViewPubsInicioLectura);
        tvAsignacion1 = (TextView) vista.findViewById(R.id.inicioAsignacion1);
        tvEncargado1 = (TextView) vista.findViewById(R.id.textViewPubsInicioAsignacion1);
        tvAyudante1 = (TextView) vista.findViewById(R.id.textViewPubsInicioAsignacionAyu1);
        tvAsignacion2 = (TextView) vista.findViewById(R.id.inicioAsignacion2);
        tvEncargado2 = (TextView) vista.findViewById(R.id.textViewPubsInicioAsignacion2);
        tvAyudante2 = (TextView) vista.findViewById(R.id.textViewPubsInicioAsignacionAyu2);
        tvAsignacion3 = (TextView) vista.findViewById(R.id.inicioAsignacion3);
        tvEncargado3 = (TextView) vista.findViewById(R.id.textViewPubsInicioAsignacion3);
        tvAyudante3 = (TextView) vista.findViewById(R.id.textViewPubsInicioAsignacionAyu3);
        linearSala = (LinearLayout) vista.findViewById(R.id.layoutSala1);
        progressBarSala1 = vista.findViewById(R.id.progressBarSala1);
        ImageButton imgEditar = (ImageButton) vista.findViewById(R.id.imgbtEditar);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean temaOscuro = sharedPreferences.getBoolean("activarOscuro", false);
        if (!temaOscuro) {
            tvLector.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            tvEncargado1.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            tvAyudante1.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            tvEncargado2.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            tvAyudante2.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            tvEncargado3.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            tvAyudante3.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
            tvAviso.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        }

        tvAviso.setVisibility(View.GONE);

        fechaLunes = getArguments().getLong("fecha");

        imgEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence [] opciones = {"Sustituir uno de los Publicadores", "Cancelar"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                dialog.setTitle("Seleccione una opción");
                dialog.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (opciones[which].equals("Sustituir uno de los Publicadores")) {
                            editSala();
                        } else if (opciones[which].equals("Cancelar")) {
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });

        fechaActual = new Date();

        fechaActual.setTime(fechaLunes);
        tvFecha.setText(new SimpleDateFormat("EEE d MMM yyyy").format(fechaActual));

        cargarSala(fechaLunes);

        return vista;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void cargarSala(long i) {
        String id = String.valueOf(i);

        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference reference = dbFirestore.collection(VariablesEstaticas.BD_SALA);

        reference.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        if (doc.getBoolean(VariablesEstaticas.BD_ASAMBLEA)) {
                            progressBarSala1.setVisibility(View.GONE);
                            tvAviso.setText("Sin asignaciones por Asamblea");
                            tvAviso.setVisibility(View.VISIBLE);
                            linearSala.setVisibility(View.GONE);
                        } else if (doc.getBoolean(VariablesEstaticas.BD_VISITA)) {
                            progressBarSala1.setVisibility(View.GONE);
                            tvAviso.setVisibility(View.GONE);
                            linearSala.setVisibility(View.VISIBLE);
                            tvTitulo.setText("Visita");
                            if (doc.getString(VariablesEstaticas.BD_LECTOR) != null) {
                                tvLectura.setText("Lectura Bíblica");
                                tvLector.setText(doc.getString(VariablesEstaticas.BD_LECTOR));
                            } else {
                                tvLector.setText("");
                            }
                            if (doc.getString(VariablesEstaticas.BD_ASIGNACION1) != null) {
                                tvAsignacion1.setText(doc.getString(VariablesEstaticas.BD_ASIGNACION1));
                            } else {
                                tvAsignacion1.setText("");
                            }
                            if (doc.getString(VariablesEstaticas.BD_ENCARGADO1) != null) {
                                tvEncargado1.setText(doc.getString(VariablesEstaticas.BD_ENCARGADO1));
                            } else {
                                tvEncargado1.setText("");
                            }
                            if (doc.getString(VariablesEstaticas.BD_AYUDANTE1) != null) {
                                tvAyudante1.setText(doc.getString(VariablesEstaticas.BD_AYUDANTE1));
                            } else {
                                tvAyudante1.setText("");
                            }
                            if (doc.getString(VariablesEstaticas.BD_ASIGNACION2) != null) {
                                tvAsignacion2.setText(doc.getString(VariablesEstaticas.BD_ASIGNACION2));
                            } else {
                                tvAsignacion2.setText("");
                            }
                            if (doc.getString(VariablesEstaticas.BD_ENCARGADO2) != null) {
                                tvEncargado2.setText(doc.getString(VariablesEstaticas.BD_ENCARGADO2));
                            } else {
                                tvEncargado2.setText("");
                            }
                            if (doc.getString(VariablesEstaticas.BD_AYUDANTE2) != null) {
                                tvAyudante2.setText(doc.getString(VariablesEstaticas.BD_AYUDANTE2));
                            } else {
                                tvAyudante2.setText("");
                            }
                            if (doc.getString(VariablesEstaticas.BD_ASIGNACION3) != null) {
                                tvAsignacion3.setText(doc.getString(VariablesEstaticas.BD_ASIGNACION3));
                            } else {
                                tvAsignacion3.setText("");
                            }
                            if (doc.getString(VariablesEstaticas.BD_ENCARGADO3) != null) {
                                tvEncargado3.setText(doc.getString(VariablesEstaticas.BD_ENCARGADO3));
                            } else {
                                tvEncargado3.setText("");
                            }
                            if (doc.getString(VariablesEstaticas.BD_AYUDANTE3) != null) {
                                tvAyudante3.setText(doc.getString(VariablesEstaticas.BD_AYUDANTE3));
                            } else {
                                tvAyudante3.setText("");
                            }

                        } else if (!doc.getBoolean(VariablesEstaticas.BD_ASAMBLEA) && !doc.getBoolean(VariablesEstaticas.BD_VISITA)) {
                            tvAviso.setVisibility(View.GONE);
                            linearSala.setVisibility(View.VISIBLE);
                            if (doc.getString(VariablesEstaticas.BD_LECTOR) != null) {
                                tvLectura.setText("Lectura Bíblica");
                                tvLector.setText(doc.getString(VariablesEstaticas.BD_LECTOR));
                            } else {
                                tvLector.setText("");
                            }
                            if (doc.getString(VariablesEstaticas.BD_ASIGNACION1) != null) {
                                tvAsignacion1.setText(doc.getString(VariablesEstaticas.BD_ASIGNACION1));
                            } else {
                                tvAsignacion1.setText("");
                            }
                            if (doc.getString(VariablesEstaticas.BD_ENCARGADO1) != null) {
                                tvEncargado1.setText(doc.getString(VariablesEstaticas.BD_ENCARGADO1));
                            } else {
                                tvEncargado1.setText("");
                            }
                            if (doc.getString(VariablesEstaticas.BD_AYUDANTE1) != null) {
                                tvAyudante1.setText(doc.getString(VariablesEstaticas.BD_AYUDANTE1));
                            } else {
                                tvAyudante1.setText("");
                            }
                            if (doc.getString(VariablesEstaticas.BD_ASIGNACION2) != null) {
                                tvAsignacion2.setText(doc.getString(VariablesEstaticas.BD_ASIGNACION2));
                            } else {
                                tvAsignacion2.setText("");
                            }
                            if (doc.getString(VariablesEstaticas.BD_ENCARGADO2) != null) {
                                tvEncargado2.setText(doc.getString(VariablesEstaticas.BD_ENCARGADO2));
                            } else {
                                tvEncargado2.setText("");
                            }
                            if (doc.getString(VariablesEstaticas.BD_AYUDANTE2) != null) {
                                tvAyudante2.setText(doc.getString(VariablesEstaticas.BD_AYUDANTE2));
                            } else {
                                tvAyudante2.setText("");
                            }
                            if (doc.getString(VariablesEstaticas.BD_ASIGNACION3) != null) {
                                tvAsignacion3.setText(doc.getString(VariablesEstaticas.BD_ASIGNACION3));
                            } else {
                                tvAsignacion3.setText("");
                            }
                            if (doc.getString(VariablesEstaticas.BD_ENCARGADO3) != null) {
                                tvEncargado3.setText(doc.getString(VariablesEstaticas.BD_ENCARGADO3));
                            } else {
                                tvEncargado3.setText("");
                            }
                            if (doc.getString(VariablesEstaticas.BD_AYUDANTE3) != null) {
                                tvAyudante3.setText(doc.getString(VariablesEstaticas.BD_AYUDANTE3));
                            } else {
                                tvAyudante3.setText("");
                            }
                            progressBarSala1.setVisibility(View.GONE);
                        }
                    } else {
                        tvAviso.setText("No hay asignaciones programadas para esta semana");
                        tvAviso.setVisibility(View.VISIBLE);
                        linearSala.setVisibility(View.GONE);
                        progressBarSala1.setVisibility(View.GONE);
                    }

                } else {
                    Toast.makeText(getContext(), "Error al cargar. Intente nuevamente", Toast.LENGTH_SHORT).show();
                    progressBarSala1.setVisibility(View.GONE);
                }
            }
        });
    }

    public void editSala() {
        Intent myIntent = new Intent(getContext(), SustituirActivity.class);
        Bundle myBundle = new Bundle();
        myBundle.putLong("semana", fechaLunes);

        myIntent.putExtras(myBundle);
        startActivity(myIntent);
    }
}
