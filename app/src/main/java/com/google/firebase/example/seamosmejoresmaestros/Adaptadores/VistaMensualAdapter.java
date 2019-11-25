package com.google.firebase.example.seamosmejoresmaestros.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.example.seamosmejoresmaestros.Constructores.VistaMensualConstructor;
import com.google.firebase.example.seamosmejoresmaestros.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class VistaMensualAdapter extends RecyclerView.Adapter<VistaMensualAdapter.ViewHolderVistaMensual> implements View.OnClickListener{
    private ArrayList<VistaMensualConstructor> listMensual;
    private Context mctx;

    public VistaMensualAdapter(ArrayList<VistaMensualConstructor> listMensual, Context mctx) {
        this.listMensual = listMensual;
        this.mctx = mctx;
    }

    @NonNull
    @Override
    public ViewHolderVistaMensual onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_vista_mensual, null, false);
        view.setOnClickListener(this);
        return new VistaMensualAdapter.ViewHolderVistaMensual(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderVistaMensual holder, int position) {
        holder.fecha.setText(new SimpleDateFormat("EEE d MMM yyyy").format(listMensual.get(position).getFechaMensual()));

        if (listMensual.get(position).getLector() != null) {
            holder.lector.setText(listMensual.get(position).getLector());
        }
        if (listMensual.get(position).getEncargado1() != null) {
            holder.encargado1.setText(listMensual.get(position).getEncargado1());
        }
        if (listMensual.get(position).getEncargado2() != null) {
            holder.encargado2.setText(listMensual.get(position).getEncargado2());
        }
        if (listMensual.get(position).getEncargado3() != null) {
            holder.encargado3.setText(listMensual.get(position).getEncargado3());
        }
        if (listMensual.get(position).getAyudante1() != null) {
            holder.ayudante1.setText(listMensual.get(position).getAyudante1());
        }
        if (listMensual.get(position).getAyudante2() != null) {
            holder.ayudante2.setText(listMensual.get(position).getAyudante2());
        }
        if (listMensual.get(position).getAyudante3() != null) {
            holder.ayudante3.setText(listMensual.get(position).getAyudante3());
        }
        if (listMensual.get(position).getAsigancion1() != null) {
            holder.asignacion1.setText(listMensual.get(position).getAsigancion1());
        }
        if (listMensual.get(position).getAsignacion2() != null) {
            holder.asignacion2.setText(listMensual.get(position).getAsignacion2());
        }
        if (listMensual.get(position).getAsignacion3() != null) {
            holder.asignacion3.setText(listMensual.get(position).getAsignacion3());
        }

    }

    @Override
    public int getItemCount() {
        return listMensual.size();
    }

    @Override
    public void onClick(View v) {

    }

    public class ViewHolderVistaMensual extends RecyclerView.ViewHolder {
        TextView fecha, lector, encargado1, encargado2, encargado3, ayudante1, ayudante2, ayudante3, asignacion1, asignacion2, asignacion3;

        public ViewHolderVistaMensual(@NonNull View itemView) {
            super(itemView);
            fecha = itemView.findViewById(R.id.tvFechaMensual);
            lector = itemView.findViewById(R.id.tvLectorMensual);
            encargado1 = itemView.findViewById(R.id.tvEncargado1Mensual);
            encargado2 = itemView.findViewById(R.id.tvEncargado2Mensual);
            encargado3 = itemView.findViewById(R.id.tvEncargado3Mensual);
            ayudante1 = itemView.findViewById(R.id.tvAyudante1Mensual);
            ayudante2 = itemView.findViewById(R.id.tvAyudante2Mensual);
            ayudante3 = itemView.findViewById(R.id.tvAyudante3Mensual);
            asignacion1 = itemView.findViewById(R.id.tvAsignacion1Mensual);
            asignacion2 = itemView.findViewById(R.id.tvAsignacion2Mensual);
            asignacion3 = itemView.findViewById(R.id.tvAsignacion3Mensual);

        }
    }

    public void updateList (ArrayList<VistaMensualConstructor> newList) {
        listMensual = new ArrayList<>();
        listMensual.addAll(newList);
        notifyDataSetChanged();
    }
}
