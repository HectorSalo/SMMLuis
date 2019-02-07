package com.google.firebase.example.seamosmejoresmaestros;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private ProgressDialog progress;
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
        imgEditar = (ImageButton) vista.findViewById(R.id.imgbtEditar);

        tvAviso.setVisibility(View.INVISIBLE);
        progress = new ProgressDialog(getContext());
        progress.setMessage("Cargando...");
        progress.setCancelable(false);
        progress.show();

        if (Utilidades.fechaSelec != null) {
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
        int semanaSelec = Utilidades.semanaSelec;
        tvFecha.setText(new SimpleDateFormat("EEE d MMM yyyy").format(Utilidades.fechaSelec));

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
                        if (doc.getBoolean(UtilidadesStatic.BD_ASAMBLEA)) {
                            progress.dismiss();
                            tvAviso.setText("Sin asignaciones por Asamblea");
                            tvAviso.setVisibility(View.VISIBLE);
                            linearSala.setVisibility(View.INVISIBLE);
                        } else if (doc.getBoolean(UtilidadesStatic.BD_VISITA)) {
                            progress.dismiss();
                            tvAviso.setVisibility(View.INVISIBLE);
                            linearSala.setVisibility(View.VISIBLE);
                            tvTitulo.setText("Visita");
                            if (doc.getString(UtilidadesStatic.BD_LECTOR) != null) {
                                tvLector.setText(doc.getString(UtilidadesStatic.BD_LECTOR));
                            } else {
                                tvLector.setText("");
                            }
                            if (doc.getString(UtilidadesStatic.BD_ASIGNACION1) != null) {
                                tvAsignacion1.setText(doc.getString(UtilidadesStatic.BD_ASIGNACION1));
                            } else {
                                tvAsignacion1.setText("");
                            }
                            if (doc.getString(UtilidadesStatic.BD_ENCARGADO1) != null) {
                                tvEncargado1.setText(doc.getString(UtilidadesStatic.BD_ENCARGADO1));
                            } else {
                                tvEncargado1.setText("");
                            }
                            if (doc.getString(UtilidadesStatic.BD_AYUDANTE1) != null) {
                                tvAyudante1.setText(doc.getString(UtilidadesStatic.BD_AYUDANTE1));
                            } else {
                                tvAyudante1.setText("");
                            }
                            if (doc.getString(UtilidadesStatic.BD_ASIGNACION2) != null) {
                                tvAsignacion2.setText(doc.getString(UtilidadesStatic.BD_ASIGNACION2));
                            } else {
                                tvAsignacion2.setText("");
                            }
                            if (doc.getString(UtilidadesStatic.BD_ENCARGADO2) != null) {
                                tvEncargado2.setText(doc.getString(UtilidadesStatic.BD_ENCARGADO2));
                            } else {
                                tvEncargado2.setText("");
                            }
                            if (doc.getString(UtilidadesStatic.BD_AYUDANTE2) != null) {
                                tvAyudante2.setText(doc.getString(UtilidadesStatic.BD_AYUDANTE2));
                            } else {
                                tvAyudante2.setText("");
                            }
                            if (doc.getString(UtilidadesStatic.BD_ASIGNACION3) != null) {
                                tvAsignacion3.setText(doc.getString(UtilidadesStatic.BD_ASIGNACION3));
                            } else {
                                tvAsignacion3.setText("");
                            }
                            if (doc.getString(UtilidadesStatic.BD_ENCARGADO3) != null) {
                                tvEncargado3.setText(doc.getString(UtilidadesStatic.BD_ENCARGADO3));
                            } else {
                                tvEncargado3.setText("");
                            }
                            if (doc.getString(UtilidadesStatic.BD_AYUDANTE3) != null) {
                                tvAyudante3.setText(doc.getString(UtilidadesStatic.BD_AYUDANTE3));
                            } else {
                                tvAyudante3.setText("");
                            }

                        } else if (!doc.getBoolean(UtilidadesStatic.BD_ASAMBLEA) && !doc.getBoolean(UtilidadesStatic.BD_VISITA)) {
                            tvAviso.setVisibility(View.INVISIBLE);
                            linearSala.setVisibility(View.VISIBLE);
                            if (doc.getString(UtilidadesStatic.BD_LECTOR) != null) {
                                tvLector.setText(doc.getString(UtilidadesStatic.BD_LECTOR));
                            } else {
                                tvLector.setText("");
                            }
                            if (doc.getString(UtilidadesStatic.BD_ASIGNACION1) != null) {
                                tvAsignacion1.setText(doc.getString(UtilidadesStatic.BD_ASIGNACION1));
                            } else {
                                tvAsignacion1.setText("");
                            }
                            if (doc.getString(UtilidadesStatic.BD_ENCARGADO1) != null) {
                                tvEncargado1.setText(doc.getString(UtilidadesStatic.BD_ENCARGADO1));
                            } else {
                                tvEncargado1.setText("");
                            }
                            if (doc.getString(UtilidadesStatic.BD_AYUDANTE1) != null) {
                                tvAyudante1.setText(doc.getString(UtilidadesStatic.BD_AYUDANTE1));
                            } else {
                                tvAyudante1.setText("");
                            }
                            if (doc.getString(UtilidadesStatic.BD_ASIGNACION2) != null) {
                                tvAsignacion2.setText(doc.getString(UtilidadesStatic.BD_ASIGNACION2));
                            } else {
                                tvAsignacion2.setText("");
                            }
                            if (doc.getString(UtilidadesStatic.BD_ENCARGADO2) != null) {
                                tvEncargado2.setText(doc.getString(UtilidadesStatic.BD_ENCARGADO2));
                            } else {
                                tvEncargado2.setText("");
                            }
                            if (doc.getString(UtilidadesStatic.BD_AYUDANTE2) != null) {
                                tvAyudante2.setText(doc.getString(UtilidadesStatic.BD_AYUDANTE2));
                            } else {
                                tvAyudante2.setText("");
                            }
                            if (doc.getString(UtilidadesStatic.BD_ASIGNACION3) != null) {
                                tvAsignacion3.setText(doc.getString(UtilidadesStatic.BD_ASIGNACION3));
                            } else {
                                tvAsignacion3.setText("");
                            }
                            if (doc.getString(UtilidadesStatic.BD_ENCARGADO3) != null) {
                                tvEncargado3.setText(doc.getString(UtilidadesStatic.BD_ENCARGADO3));
                            } else {
                                tvEncargado3.setText("");
                            }
                            if (doc.getString(UtilidadesStatic.BD_AYUDANTE3) != null) {
                                tvAyudante3.setText(doc.getString(UtilidadesStatic.BD_AYUDANTE3));
                            } else {
                                tvAyudante3.setText("");
                            }
                            progress.dismiss();
                        }
                    } else {
                        tvAviso.setText("No hay asignaciones programadas para esta semana");
                        tvAviso.setVisibility(View.VISIBLE);
                        linearSala.setVisibility(View.INVISIBLE);
                        progress.dismiss();
                    }

                } else {
                    Toast.makeText(getContext(), "Error al cargar. Intente nuevamente", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            }
        });
    }

    public void editSala() {
        Intent myIntent = new Intent(getContext(), SustituirActivity.class);
        Bundle myBundle = new Bundle();
        myBundle.putInt("sala", 2);
        if(Utilidades.semanaSelec != 0) {
            myBundle.putInt("semana", Utilidades.semanaSelec);
            myBundle.putLong("fecha", Utilidades.fechaSelec.getTime());

        } else {
            myBundle.putInt("semana", semanaActual);
            myBundle.putLong("fecha", fechaActual.getTime());
        }
        myIntent.putExtras(myBundle);
        startActivity(myIntent);
    }
}
