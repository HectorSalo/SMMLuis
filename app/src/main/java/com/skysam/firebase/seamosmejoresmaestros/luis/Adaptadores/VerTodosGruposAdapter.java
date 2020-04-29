package com.skysam.firebase.seamosmejoresmaestros.luis.Adaptadores;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skysam.firebase.seamosmejoresmaestros.luis.Constructores.PublicadoresConstructor;
import com.skysam.firebase.seamosmejoresmaestros.luis.R;

import java.util.ArrayList;

public class VerTodosGruposAdapter extends RecyclerView.Adapter<VerTodosGruposAdapter.ViewHolderVerTodosGrupos> {

    private ArrayList<PublicadoresConstructor> listPublicadores;
    private Context mctx;

    public VerTodosGruposAdapter(ArrayList<PublicadoresConstructor> listPublicadores, Context mctx) {
        this.listPublicadores = listPublicadores;
        this.mctx = mctx;
    }

    @NonNull
    @Override
    public VerTodosGruposAdapter.ViewHolderVerTodosGrupos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ver_todos_grupos, null, false);

        return new ViewHolderVerTodosGrupos(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull VerTodosGruposAdapter.ViewHolderVerTodosGrupos holder, int position) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mctx);
        boolean temaOscuro = sharedPreferences.getBoolean("activarOscuro", false);
        if (!temaOscuro) {
            holder.linearverTodos.setBackgroundColor(Color.parseColor("#E8F5E9"));

        }

            holder.nombre.setText(listPublicadores.get(position).getNombrePublicador());
            holder.apellido.setText(listPublicadores.get(position).getApellidoPublicador());

    }

    @Override
    public int getItemCount() {
        return listPublicadores.size();
    }

    public class ViewHolderVerTodosGrupos extends RecyclerView.ViewHolder {

        TextView nombre, apellido;
        LinearLayout linearverTodos;

        public ViewHolderVerTodosGrupos(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.textViewNombreVerTodos);
            apellido = itemView.findViewById(R.id.textViewApellidoVerTodos);
            linearverTodos = itemView.findViewById(R.id.linearVerTodos);
        }
    }

    public void updateList (ArrayList<PublicadoresConstructor> newList) {
        listPublicadores = new ArrayList<>();
        listPublicadores.addAll(newList);
        notifyDataSetChanged();
    }
}
