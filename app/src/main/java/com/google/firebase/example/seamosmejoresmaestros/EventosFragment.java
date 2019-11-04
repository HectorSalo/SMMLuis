package com.google.firebase.example.seamosmejoresmaestros;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class EventosFragment extends Fragment {

    private TextView tvUltFecha, tvVisita, tvLector, tvAsignacion1, tvAsignacion2, tvAsignacion3, tvEncargado1, tvAyudante1, tvEncargado2, tvAyudante2, tvEncargado3, tvAyudante3;
    private ProgressDialog progress;
    private Date lunesActual;
    private OnFragmentInteractionListener mListener;

    public EventosFragment() {
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
        View vista = inflater.inflate(R.layout.fragment_eventos, container, false);

        Calendar calendario = Calendar.getInstance();
        Calendar calendarioLunes = Calendar.getInstance();
        calendarioLunes.clear();
        calendarioLunes.setFirstDayOfWeek(Calendar.MONDAY);
        int semanaActual = calendario.get(Calendar.WEEK_OF_YEAR);
        int anualActual = calendario.get(Calendar.YEAR);

        calendarioLunes.set(Calendar.WEEK_OF_YEAR, semanaActual);
        calendarioLunes.set(Calendar.YEAR, anualActual);
        lunesActual = new Date();
        lunesActual = calendarioLunes.getTime();

        tvUltFecha = (TextView) vista.findViewById(R.id.tvInicioUltSemana);
        tvVisita = (TextView) vista.findViewById(R.id.tvInicioVisita);
        tvAsignacion1 = vista.findViewById(R.id.tvEventosAsignacion1);
        tvAsignacion2 = vista.findViewById(R.id.tvEventosAsignacion2);
        tvAsignacion3 = vista.findViewById(R.id.tvEventosAsignacion3);
        tvLector = vista.findViewById(R.id.tvEventosPubsLectura);
        tvEncargado1 = vista.findViewById(R.id.tvEventosPubsAsignacion1);
        tvAyudante1 = vista.findViewById(R.id.tvEventosPubsAsignacionAyu1);
        tvEncargado2 = vista.findViewById(R.id.tvEventosPubsAsignacion2);
        tvAyudante2 = vista.findViewById(R.id.tvEventosPubsAsignacionAyu2);
        tvEncargado3 = vista.findViewById(R.id.tvEventosPubsAsignacion3);
        tvAyudante3 = vista.findViewById(R.id.tvEventosPubsAsignacionAyu3);

        progress = new ProgressDialog(getContext());
        progress.setMessage("Cargando...");
        progress.setCancelable(false);
        progress.show();
        cargarSemanaEnCurso(semanaActual);
        cargarUltSemana();
        cargarProxVisita();
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void cargarSemanaEnCurso(int i) {
        String idDocument = String.valueOf(i);
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference reference = dbFirestore.collection("sala1");

        reference.document(idDocument).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (doc.exists()) {
                        if (doc.getBoolean(UtilidadesStatic.BD_ASAMBLEA)) {
                            progress.dismiss();
                            tvLector.setText("Sin asignaciones por Asamblea");
                            tvAsignacion1.setVisibility(View.GONE);
                            tvAsignacion2.setVisibility(View.GONE);
                            tvAsignacion3.setVisibility(View.GONE);
                            tvEncargado1.setVisibility(View.GONE);
                            tvEncargado2.setVisibility(View.GONE);
                            tvEncargado3.setVisibility(View.GONE);
                            tvAyudante1.setVisibility(View.GONE);
                            tvAyudante2.setVisibility(View.GONE);
                            tvAyudante3.setVisibility(View.GONE);

                        } else {

                            if (doc.getString(UtilidadesStatic.BD_LECTOR) != null) {
                                tvLector.setText(doc.getString(UtilidadesStatic.BD_LECTOR));
                            }
                            if (doc.getString(UtilidadesStatic.BD_ASIGNACION1) != null) {
                                tvAsignacion1.setText(doc.getString(UtilidadesStatic.BD_ASIGNACION1));
                            } else {
                                tvAsignacion1.setVisibility(View.GONE);
                            }
                            if (doc.getString(UtilidadesStatic.BD_ENCARGADO1) != null) {
                                tvEncargado1.setText(doc.getString(UtilidadesStatic.BD_ENCARGADO1));
                            } else {
                                tvEncargado1.setVisibility(View.GONE);
                            }
                            if (doc.getString(UtilidadesStatic.BD_AYUDANTE1) != null) {
                                tvAyudante1.setText(doc.getString(UtilidadesStatic.BD_AYUDANTE1));
                            } else {
                                tvAyudante1.setVisibility(View.GONE);
                            }
                            if (doc.getString(UtilidadesStatic.BD_ASIGNACION2) != null) {
                                tvAsignacion2.setText(doc.getString(UtilidadesStatic.BD_ASIGNACION2));
                            } else {
                                tvAsignacion2.setVisibility(View.GONE);
                            }
                            if (doc.getString(UtilidadesStatic.BD_ENCARGADO2) != null) {
                                tvEncargado2.setText(doc.getString(UtilidadesStatic.BD_ENCARGADO2));
                            } else {
                                tvEncargado2.setVisibility(View.GONE);
                            }
                            if (doc.getString(UtilidadesStatic.BD_AYUDANTE2) != null) {
                                tvAyudante2.setText(doc.getString(UtilidadesStatic.BD_AYUDANTE2));
                            } else {
                                tvAyudante2.setVisibility(View.GONE);
                            }
                            if (doc.getString(UtilidadesStatic.BD_ASIGNACION3) != null) {
                                tvAsignacion3.setText(doc.getString(UtilidadesStatic.BD_ASIGNACION3));
                            } else {
                                tvAsignacion3.setVisibility(View.GONE);
                            }
                            if (doc.getString(UtilidadesStatic.BD_ENCARGADO3) != null) {
                                tvEncargado3.setText(doc.getString(UtilidadesStatic.BD_ENCARGADO3));
                            } else {
                                tvEncargado3.setVisibility(View.GONE);
                            }
                            if (doc.getString(UtilidadesStatic.BD_AYUDANTE3) != null) {
                                tvAyudante3.setText(doc.getString(UtilidadesStatic.BD_AYUDANTE3));
                            } else {
                                tvAyudante3.setVisibility(View.GONE);
                            }
                            progress.dismiss();
                        }
                    } else {
                        tvLector.setText("No hay asignaciones programadas para esta semana");
                        progress.dismiss();
                    }

                } else {
                    Toast.makeText(getContext(), "Error al cargar. Intente nuevamente", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            }
        });
    }

    private void cargarUltSemana() {

        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference reference = dbFirestore.collection("sala1");

        Query query = reference.orderBy(UtilidadesStatic.BD_FECHA_LUNES, Query.Direction.DESCENDING).limit(1);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        if(doc.exists()) {
                            if (doc.getDouble(UtilidadesStatic.BD_IDSEMANA) == 0) {
                                progress.dismiss();
                                tvUltFecha.setText("Sin programar");
                            } else {
                                Date fecha = doc.getDate(UtilidadesStatic.BD_FECHA);
                                tvUltFecha.setText(new SimpleDateFormat("EEE d MMM yyyy").format(fecha));
                                progress.dismiss();
                            }

                        } else {
                            progress.dismiss();
                            tvUltFecha.setText("Sin programar");
                        }

                    }

                } else {
                    progress.dismiss();
                    Toast.makeText(getContext(), "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void cargarProxVisita() {
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference reference = dbFirestore.collection("sala1");

        Query query = reference.whereEqualTo(UtilidadesStatic.BD_VISITA, true).whereGreaterThanOrEqualTo(UtilidadesStatic.BD_FECHA_LUNES, lunesActual).orderBy(UtilidadesStatic.BD_FECHA_LUNES, Query.Direction.ASCENDING).limit(1);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Date fecha = doc.getDate(UtilidadesStatic.BD_FECHA);
                        tvVisita.setText(new SimpleDateFormat("EEE d MMM yyyy").format(fecha));
                    }
                }
            }
        });
    }
}
