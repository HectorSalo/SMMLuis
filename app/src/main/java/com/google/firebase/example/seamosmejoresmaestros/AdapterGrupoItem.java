package com.google.firebase.example.seamosmejoresmaestros;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterGrupoItem extends RecyclerView.Adapter<AdapterGrupoItem.ViewHolderGrupoItem> {

    private ArrayList<String> listGrupoItem;
    private Context mctx;

    public AdapterGrupoItem(ArrayList<String> listGrupoItem, Context mctx) {
        this.listGrupoItem = listGrupoItem;
        this.mctx = mctx;
    }

    @NonNull
    @Override
    public AdapterGrupoItem.ViewHolderGrupoItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_grupo_item, null, false);
        return new ViewHolderGrupoItem(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterGrupoItem.ViewHolderGrupoItem holder, int position) {
        holder.tvTitleGrupo.setText(listGrupoItem.get(position));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mctx, LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerGrupoItem.setLayoutManager(layoutManager);
        holder.recyclerGrupoItem.setHasFixedSize(true);


    }

    @Override
    public int getItemCount() {
        return listGrupoItem.size();
    }

    public class ViewHolderGrupoItem extends RecyclerView.ViewHolder {

        TextView tvTitleGrupo;
        RecyclerView recyclerGrupoItem;

        public ViewHolderGrupoItem(@NonNull View itemView) {
            super(itemView);
            tvTitleGrupo = itemView.findViewById(R.id.titleGrupoItem);
            recyclerGrupoItem = itemView.findViewById(R.id.recyclerGrupoItem);
        }
    }
}
