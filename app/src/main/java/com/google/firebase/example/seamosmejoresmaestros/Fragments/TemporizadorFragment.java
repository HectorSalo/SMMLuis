package com.google.firebase.example.seamosmejoresmaestros.Fragments;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.example.seamosmejoresmaestros.R;
import com.google.firebase.example.seamosmejoresmaestros.Variables.VariablesTemporizador;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class TemporizadorFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private NumberPicker numberMinutos, numberSegundos;
    private FloatingActionButton fabStart, fabPause, fabStop;
    private TextView visorTiempo;
    private int intMinutos, intSegundos;
    private long tiempoRestante;
    private String FORMAT = "%02d:%02d";
    private CountDownTimer countDownTimer, countDownTimerRestante;
    private LinearLayout layoutDuracion, layoutPauseStop;
    private MediaPlayer mediaPlayer;

    public TemporizadorFragment() {
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
        View vista = inflater.inflate(R.layout.fragment_temporizador, container, false);

        layoutDuracion = (LinearLayout) vista.findViewById(R.id.layoutDuracion);
        layoutPauseStop = (LinearLayout) vista.findViewById(R.id.layoutPauseStop);
        fabStart = (FloatingActionButton) vista.findViewById(R.id.fabPlay);
        fabPause = (FloatingActionButton) vista.findViewById(R.id.fabPause);
        fabStop = (FloatingActionButton) vista.findViewById(R.id.fabStop);
        visorTiempo = (TextView) vista.findViewById(R.id.visorTiempo);
        numberMinutos = (NumberPicker) vista.findViewById(R.id.numberMinutos);
        numberSegundos = (NumberPicker) vista.findViewById(R.id.numberSegundos);
        numberMinutos.setMaxValue(59);
        numberMinutos.setMinValue(0);
        numberSegundos.setMaxValue(59);
        numberSegundos.setMinValue(0);

        Objects.requireNonNull(getActivity()).setVolumeControlStream(AudioManager.STREAM_ALARM);
        mediaPlayer = new MediaPlayer();

        layoutPauseStop.setVisibility(View.INVISIBLE);

        if (VariablesTemporizador.tiempoRestante > 0) {
            visorTiempo.setText(String.format(FORMAT,
                    TimeUnit.MILLISECONDS.toMinutes(VariablesTemporizador.tiempoRestante) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(VariablesTemporizador.tiempoRestante)),
                    TimeUnit.MILLISECONDS.toSeconds(VariablesTemporizador.tiempoRestante) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(VariablesTemporizador.tiempoRestante))));
        }


        fabStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarTiempo();
            }
        });
        fabStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VariablesTemporizador.contadorInicial) {
                    layoutDuracion.setVisibility(View.VISIBLE);
                    layoutPauseStop.setVisibility(View.INVISIBLE);
                    countDownTimer.cancel();
                    visorTiempo.setText("00:00");
                    VariablesTemporizador.tiempoCorriendo = false;
                } else if (VariablesTemporizador.contadorRestante) {
                    layoutDuracion.setVisibility(View.VISIBLE);
                    layoutPauseStop.setVisibility(View.INVISIBLE);
                    countDownTimerRestante.cancel();
                    visorTiempo.setText("00:00");
                    VariablesTemporizador.tiempoCorriendo = false;
                } else if (!VariablesTemporizador.contadorRestante && !VariablesTemporizador.contadorInicial) {
                    layoutDuracion.setVisibility(View.VISIBLE);
                    layoutPauseStop.setVisibility(View.INVISIBLE);
                    countDownTimer.cancel();
                    visorTiempo.setText("00:00");
                    VariablesTemporizador.tiempoCorriendo = false;
                }
            }
        });
        fabPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VariablesTemporizador.tiempoCorriendo) {
                    if (VariablesTemporizador.contadorInicial) {
                        fabPause.setImageResource(R.drawable.ic_action_play);
                        countDownTimer.cancel();
                        VariablesTemporizador.tiempoCorriendo = false;
                        VariablesTemporizador.contadorInicial = false;
                       } else if (VariablesTemporizador.contadorRestante) {
                        fabPause.setImageResource(R.drawable.ic_action_play);
                        countDownTimerRestante.cancel();
                        VariablesTemporizador.tiempoCorriendo = false;
                    }
                } else {
                    fabPause.setImageResource(R.drawable.ic_action_pause);
                    reanudarTiempo();
                }
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

    private void iniciarTiempo() {
        intMinutos = numberMinutos.getValue();
        intSegundos = numberSegundos.getValue();
        long longMinuto = intMinutos * 60000;
        long longSegundo = intSegundos * 1000;
        long tiempoBase = longMinuto + longSegundo;
        long intervaloDecrecer = 1000;

        if (tiempoBase == 0) {
            Toast.makeText(getContext(), "El valor mínimo para iniciar es 1 segundo", Toast.LENGTH_SHORT).show();
        } else {

            countDownTimer = new CountDownTimer(tiempoBase, intervaloDecrecer) {
                @Override
                public void onTick(long millisUntilFinished) {
                    fabPause.setImageResource(R.drawable.ic_action_pause);
                    layoutDuracion.setVisibility(View.INVISIBLE);
                    layoutPauseStop.setVisibility(View.VISIBLE);
                    VariablesTemporizador.contadorInicial = true;
                    VariablesTemporizador.tiempoCorriendo = true;
                    VariablesTemporizador.tiempoRestante = millisUntilFinished;
                    visorTiempo.setText(String.format(FORMAT,
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                }

                @Override
                public void onFinish() {
                    visorTiempo.setText("Finalizado");
                    layoutDuracion.setVisibility(View.VISIBLE);
                    layoutPauseStop.setVisibility(View.INVISIBLE);
                    VariablesTemporizador.tiempoCorriendo = false;
                    VariablesTemporizador.contadorInicial = false;

                    sonarAlarma();
                }
            }.start();
        }
    }

    private void reanudarTiempo() {
        long intervaloDecrecer = 1000;
        long tiempoBase = VariablesTemporizador.tiempoRestante;
        countDownTimerRestante = new CountDownTimer(tiempoBase, intervaloDecrecer) {
            @Override
            public void onTick(long millisUntilFinished) {
                VariablesTemporizador.contadorRestante = true;
                VariablesTemporizador.tiempoCorriendo = true;
                visorTiempo.setText(String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                VariablesTemporizador.tiempoRestante = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                VariablesTemporizador.contadorRestante = false;
                visorTiempo.setText("Finalizado");
                VariablesTemporizador.tiempoCorriendo = false;
                sonarAlarma();
            }
        }.start();
    }

    public void sonarAlarma(){
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.alarm);
        mediaPlayer.start();
    }
}
