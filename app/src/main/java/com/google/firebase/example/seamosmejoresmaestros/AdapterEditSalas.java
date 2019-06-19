package com.google.firebase.example.seamosmejoresmaestros;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AdapterEditSalas extends RecyclerView.Adapter<AdapterEditSalas.ViewHolderEditSalas> implements View.OnClickListener {

    ArrayList<ConstructorPublicadores> listSelecPub;
    Context ctx;
    private View.OnClickListener listenerSeleccionar;

    public AdapterEditSalas(ArrayList<ConstructorPublicadores> listSelecPub, Context ctx){
        this.listSelecPub = listSelecPub;
        this.ctx = ctx;
    }
    @NonNull
    @Override
    public AdapterEditSalas.ViewHolderEditSalas onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.seleccionar_publicadores, null, false);
        view.setOnClickListener(this);
        return new ViewHolderEditSalas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterEditSalas.ViewHolderEditSalas viewHolderEditSalas, int i) {
        viewHolderEditSalas.nombrePub.setText(listSelecPub.get(i).getNombrePublicador());
        viewHolderEditSalas.apellidoPub.setText(listSelecPub.get(i).getApellidoPublicador());

        if (listSelecPub.get(i).getUltAsignacion() != null) {
            viewHolderEditSalas.ultFechaDiscurso.setText(new SimpleDateFormat("EEE d MMM yyyy").format(listSelecPub.get(i).getUltAsignacion()));
        } else {
            viewHolderEditSalas.ultFechaDiscurso.setText("");
        }
        if (listSelecPub.get(i).getUltAyudante() != null) {
            viewHolderEditSalas.ultFechaAyudante.setText(new SimpleDateFormat("EEE d MMM yyyy").format(listSelecPub.get(i).getUltAyudante()));
        } else {
            viewHolderEditSalas.ultFechaAyudante.setText("");
        }
        if (listSelecPub.get(i).getUltSustitucion() != null) {
            viewHolderEditSalas.ultFechaSust.setText(new SimpleDateFormat("EEE d MMM yyyy").format(listSelecPub.get(i).getUltSustitucion()));
        } else {
            viewHolderEditSalas.ultFechaSust.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return listSelecPub.size();
    }

    public void setOnClickListener (View.OnClickListener listener) {
        this.listenerSeleccionar = listener;
    }

    @Override
    public void onClick(View v) {
        if (listenerSeleccionar != null) {
            listenerSeleccionar.onClick(v);
        }
    }

    public class ViewHolderEditSalas extends RecyclerView.ViewHolder {

        TextView nombrePub, apellidoPub, ultFechaDiscurso, ultFechaAyudante, ultFechaSust;

        public ViewHolderEditSalas(@NonNull View itemView) {
            super(itemView);

            nombrePub = (TextView) itemView.findViewById(R.id.nombrePub);
            apellidoPub = (TextView) itemView.findViewById(R.id.apellidoPub);
            ultFechaDiscurso = (TextView) itemView.findViewById(R.id.ultFechaDiscurso);
            ultFechaAyudante = (TextView) itemView.findViewById(R.id.ultFechaAyudante);
            ultFechaSust = (TextView) itemView.findViewById(R.id.ultFechaSust);
        }
    }

    public void updateListSelec (ArrayList<ConstructorPublicadores> newList) {
        listSelecPub = new ArrayList<>();
        listSelecPub.addAll(newList);
        notifyDataSetChanged();
    }
}
