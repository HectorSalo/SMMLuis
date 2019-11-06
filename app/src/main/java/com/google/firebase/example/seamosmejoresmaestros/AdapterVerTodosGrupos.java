package com.google.firebase.example.seamosmejoresmaestros;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterVerTodosGrupos extends RecyclerView.Adapter<AdapterVerTodosGrupos.ViewHolderVerTodosGrupos> {

    private ArrayList<ConstructorPublicadores> listPublicadores;
    private Context mctx;

    public  AdapterVerTodosGrupos (ArrayList<ConstructorPublicadores> listPublicadores, Context mctx) {
        this.listPublicadores = listPublicadores;
        this.mctx = mctx;
    }

    @NonNull
    @Override
    public AdapterVerTodosGrupos.ViewHolderVerTodosGrupos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_ver_todos_grupos, null, false);

        return new ViewHolderVerTodosGrupos(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterVerTodosGrupos.ViewHolderVerTodosGrupos holder, int position) {

        //double grupoD = listPublicadores.get(position).getGrupo();
        //int grupoInt = (int) grupoD;

        holder.nombre.setText(listPublicadores.get(position).getNombrePublicador());
        holder.apellido.setText(listPublicadores.get(position).getApellidoPublicador());

        /*if (grupoInt % 2 == 0) {
            holder.linearverTodos.setBackgroundColor(Color.YELLOW);

        } else {
            holder.linearverTodos.setBackgroundColor(Color.LTGRAY);

        }*/


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

    public void updateList (ArrayList<ConstructorPublicadores> newList) {
        listPublicadores = new ArrayList<>();
        listPublicadores.addAll(newList);
        notifyDataSetChanged();
    }
}
