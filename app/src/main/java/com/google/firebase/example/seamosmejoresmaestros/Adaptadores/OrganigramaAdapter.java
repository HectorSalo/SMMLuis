package com.google.firebase.example.seamosmejoresmaestros.Adaptadores;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.example.seamosmejoresmaestros.Constructores.PublicadoresConstructor;
import com.google.firebase.example.seamosmejoresmaestros.R;

import java.util.ArrayList;

public class OrganigramaAdapter extends RecyclerView.Adapter<OrganigramaAdapter.ViewHolderOrganigrama> implements View.OnClickListener {

    private ArrayList<PublicadoresConstructor> listPublicadores;
    private View.OnClickListener listener;
    private Context mctx;

    public OrganigramaAdapter(ArrayList<PublicadoresConstructor> listPublicadores, Context mctx) {
        this.listPublicadores = listPublicadores;
        this.mctx = mctx;
    }
    @NonNull
    @Override
    public OrganigramaAdapter.ViewHolderOrganigrama onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.organigrama_list, null, false);
        view.setOnClickListener(this);
        return new OrganigramaAdapter.ViewHolderOrganigrama(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrganigramaAdapter.ViewHolderOrganigrama viewHolderOrganigrama, int i) {
        viewHolderOrganigrama.nombre.setText(listPublicadores.get(i).getNombrePublicador());
        viewHolderOrganigrama.apellido.setText(listPublicadores.get(i).getApellidoPublicador());

        double x = listPublicadores.get(i).getGrupo();
        int grupo = (int) x;
        viewHolderOrganigrama.grupo.setText("Grupo " + grupo);

        if (listPublicadores.get(i).isAnciano()) {
            viewHolderOrganigrama.nombramiento.setText("Anciano");
        } else if (listPublicadores.get(i).isMinisterial()) {
            viewHolderOrganigrama.nombramiento.setText("Ministerial");
        } else {
            viewHolderOrganigrama.nombramiento.setText("");
        }

        if (listPublicadores.get(i).isSuperintendente()) {
            viewHolderOrganigrama.puesto.setText("Superintendente");
        } else if (listPublicadores.get(i).isAuxiliar()) {
            viewHolderOrganigrama.puesto.setText("Auxiliar");
        } else {
            viewHolderOrganigrama.puesto.setText("");
        }

        if (listPublicadores.get(i).isPrecursor()) {
            viewHolderOrganigrama.precursor.setText("Precursor Regular");
        } else {
            viewHolderOrganigrama.precursor.setText("");
        }

        if (listPublicadores.get(i).getGenero().equals("Hombre")) {
            if (listPublicadores.get(i).getImagen() != null) {
                Glide.with(mctx).load(listPublicadores.get(i).getImagen()).into(viewHolderOrganigrama.imagen);
            } else {
                viewHolderOrganigrama.imagen.setImageResource(R.drawable.ic_caballero);
            }

        } else if (listPublicadores.get(i).getGenero().equals("Mujer")) {
            if (listPublicadores.get(i).getImagen() != null) {
                Glide.with(mctx).load(listPublicadores.get(i).getImagen()).into(viewHolderOrganigrama.imagen);
            } else {
                viewHolderOrganigrama.imagen.setImageResource(R.drawable.ic_dama);
            }
        }
    }

    @Override
    public int getItemCount() {
        return listPublicadores.size();
    }

    public void setOnClickListener (View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public class ViewHolderOrganigrama extends RecyclerView.ViewHolder {

        ImageView imagen;
        TextView nombre, apellido, grupo, puesto, nombramiento, precursor;

        public ViewHolderOrganigrama(@NonNull View itemView) {
            super(itemView);

            imagen = itemView.findViewById(R.id.imageOrganigrama);
            nombre = itemView.findViewById(R.id.nombrePubOrg);
            apellido = itemView.findViewById(R.id.apellidoPubOrg);
            grupo = itemView.findViewById(R.id.titleGrupo);
            puesto = itemView.findViewById(R.id.tvPuesto);
            nombramiento = itemView.findViewById(R.id.titleNombramiento);
            precursor = itemView.findViewById(R.id.titlePrecursor);
        }
    }

    public void updateListOrganigrama (ArrayList<PublicadoresConstructor> newList) {
        listPublicadores = new ArrayList<>();
        listPublicadores.addAll(newList);
        notifyDataSetChanged();
    }
}
