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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



public class EventosFragment extends Fragment {

    private TextView tvUltFecha, tvAsamblea, tvVisita;
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
        tvAsamblea = (TextView) vista.findViewById(R.id.tvInicioAsamblea);
        tvVisita = (TextView) vista.findViewById(R.id.tvInicioVisita);


        progress = new ProgressDialog(getContext());
        progress.setMessage("Cargando...");
        progress.setCancelable(false);
        progress.show();
        cargarUltSemana();
        cargarProxAsamblea();
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

    private void cargarProxAsamblea() {
        FirebaseFirestore dbFirestore = FirebaseFirestore.getInstance();
        CollectionReference reference = dbFirestore.collection("sala1");

        Query query = reference.whereEqualTo(UtilidadesStatic.BD_ASAMBLEA, true).whereGreaterThanOrEqualTo(UtilidadesStatic.BD_FECHA_LUNES, lunesActual).orderBy(UtilidadesStatic.BD_FECHA_LUNES, Query.Direction.ASCENDING).limit(1);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    Date fecha = doc.getDate(UtilidadesStatic.BD_FECHA);
                    tvAsamblea.setText(new SimpleDateFormat("EEE d MMM yyyy").format(fecha));
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
