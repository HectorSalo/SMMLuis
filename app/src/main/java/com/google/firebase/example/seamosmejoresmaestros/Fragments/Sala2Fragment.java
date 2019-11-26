package com.google.firebase.example.seamosmejoresmaestros.Fragments;

import android.app.ProgressDialog;
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
import com.google.firebase.example.seamosmejoresmaestros.R;
import com.google.firebase.example.seamosmejoresmaestros.Asignaciones.SustituirActivity;
import com.google.firebase.example.seamosmejoresmaestros.Variables.VariablesGenerales;
import com.google.firebase.example.seamosmejoresmaestros.Variables.VariablesEstaticas;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Sala2Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Sala2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Sala2Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TextView tvAviso, tvFecha, tvTitulo, tvLector, tvAsignacion1, tvEncargado1, tvAyudante1, tvAsignacion2, tvEncargado2,tvAyudante2, tvAsignacion3, tvEncargado3, tvAyudante3;
    private ImageButton imgEditar;
    private LinearLayout linearSala;
    private ProgressBar progressBarSala2;
    private int semanaActual;
    private Date fechaActual;

    public Sala2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Sala2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Sala2Fragment newInstance(String param1, String param2) {
        Sala2Fragment fragment = new Sala2Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View vista = inflater.inflate(R.layout.fragment_sala2, container, false);

        tvAviso = (TextView) vista.findViewById(R.id.tvAviso);
        tvFecha = (TextView) vista.findViewById(R.id.tvFechaAsig);
        tvTitulo = (TextView) vista.findViewById(R.id.tvTitulo);
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
        linearSala = (LinearLayout) vista.findViewById(R.id.layoutSala2);
        progressBarSala2 = vista.findViewById(R.id.progressBarSala2);
        ImageButton imgEditar = (ImageButton) vista.findViewById(R.id.imgbtEditar);

        tvAviso.setVisibility(View.GONE);

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

        if (VariablesGenerales.fechaSelec != null) {
            cargarFechaSelec();

        } else {
            cargarFechaActual();
        }

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
                            dialog.dismiss();
                        } else if (opciones[which].equals("Cancelar")) {
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            }
        });

        return vista;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void cargarFechaActual() {
        Calendar calendario = Calendar.getInstance();
        semanaActual = calendario.get(Calendar.WEEK_OF_YEAR);
        fechaActual = calendario.getTime();
        tvFecha.setText(new SimpleDateFormat("EEE d MMM yyyy").format(fechaActual));

        cargarSala(semanaActual);
    }

    private void cargarFechaSelec() {
        int semanaSelec = VariablesGenerales.semanaSelec;
        tvFecha.setText(new SimpleDateFormat("EEE d MMM yyyy").format(VariablesGenerales.fechaSelec));

        cargarSala(semanaSelec);
    }

    public void cargarSala(int i) {
        String id = String.valueOf(i);

        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference reference = dbFirestore.collection("sala2");

        reference.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        if (doc.getBoolean(VariablesEstaticas.BD_ASAMBLEA)) {
                            progressBarSala2.setVisibility(View.GONE);
                            tvAviso.setText("Sin asignaciones por Asamblea");
                            tvAviso.setVisibility(View.VISIBLE);
                            linearSala.setVisibility(View.GONE);
                        } else if (doc.getBoolean(VariablesEstaticas.BD_VISITA)) {
                            progressBarSala2.setVisibility(View.GONE);
                            tvAviso.setVisibility(View.GONE);
                            linearSala.setVisibility(View.VISIBLE);
                            tvTitulo.setText("Visita");
                            if (doc.getString(VariablesEstaticas.BD_LECTOR) != null) {
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
                            progressBarSala2.setVisibility(View.GONE);
                        }
                    } else {
                        tvAviso.setText("No hay asignaciones programadas para esta semana");
                        tvAviso.setVisibility(View.VISIBLE);
                        linearSala.setVisibility(View.GONE);
                        progressBarSala2.setVisibility(View.GONE);
                    }

                } else {
                    Toast.makeText(getContext(), "Error al cargar. Intente nuevamente", Toast.LENGTH_SHORT).show();
                    progressBarSala2.setVisibility(View.GONE);
                }
            }
        });
    }

    public void editSala() {
        Intent myIntent = new Intent(getContext(), SustituirActivity.class);
        Bundle myBundle = new Bundle();
        myBundle.putInt("sala", 2);
        if(VariablesGenerales.semanaSelec != 0) {
            myBundle.putInt("semana", VariablesGenerales.semanaSelec);
            myBundle.putLong("fecha", VariablesGenerales.fechaSelec.getTime());

        } else {
            myBundle.putInt("semana", semanaActual);
            myBundle.putLong("fecha", fechaActual.getTime());
        }
        myIntent.putExtras(myBundle);
        startActivity(myIntent);
    }
}
