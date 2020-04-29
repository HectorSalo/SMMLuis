package com.skysam.firebase.seamosmejoresmaestros.luis.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.skysam.firebase.seamosmejoresmaestros.luis.Constructores.PublicadoresConstructor;
import com.skysam.firebase.seamosmejoresmaestros.luis.R;
import com.skysam.firebase.seamosmejoresmaestros.luis.Variables.VariablesEstaticas;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GrupoItemAdapter extends RecyclerView.Adapter<GrupoItemAdapter.ViewHolderGrupoItem> {

    private ArrayList<Integer> listGrupoItem;
    private Context mctx;


    public GrupoItemAdapter(ArrayList<Integer> listGrupoItem, Context mctx) {
        this.listGrupoItem = listGrupoItem;
        this.mctx = mctx;
    }

    @NonNull
    @Override
    public GrupoItemAdapter.ViewHolderGrupoItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_grupo_item, null, false);
        return new ViewHolderGrupoItem(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull final GrupoItemAdapter.ViewHolderGrupoItem holder, final int position) {
        holder.tvTitleGrupo.setText("Grupo " + listGrupoItem.get(position));
        final ArrayList<PublicadoresConstructor> listPublicadores = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mctx, 2);
        holder.recyclerGrupoItem.setLayoutManager(layoutManager);
        holder.recyclerGrupoItem.setHasFixedSize(true);
        final VerTodosGruposAdapter verTodosGruposAdapter = new VerTodosGruposAdapter(listPublicadores, mctx);
        holder.recyclerGrupoItem.setAdapter(verTodosGruposAdapter);
        holder.recyclerGrupoItem.setVisibility(View.GONE);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");

        Query query = reference.orderBy(VariablesEstaticas.BD_GRUPO).orderBy(VariablesEstaticas.BD_APELLIDO, Query.Direction.ASCENDING);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        PublicadoresConstructor publi = new PublicadoresConstructor();

                        double grupoD = doc.getDouble(VariablesEstaticas.BD_GRUPO);
                        int grupoInt = (int) grupoD;

                        if (listGrupoItem.get(position) == grupoInt) {

                            publi.setIdPublicador(doc.getId());
                            publi.setNombrePublicador(doc.getString(VariablesEstaticas.BD_NOMBRE));
                            publi.setApellidoPublicador(doc.getString(VariablesEstaticas.BD_APELLIDO));
                            publi.setGrupo(doc.getDouble(VariablesEstaticas.BD_GRUPO));

                            listPublicadores.add(publi);
                        }

                    }

                    verTodosGruposAdapter.updateList(listPublicadores);

                } else {

                    Toast.makeText(mctx, "Error al cargar lista. Intente nuevamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.tvTitleGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.recyclerGrupoItem.isShown()) {
                    holder.recyclerGrupoItem.setVisibility(View.GONE);
                } else {
                    holder.recyclerGrupoItem.setVisibility(View.VISIBLE);
                }
            }
        });
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

    public void updateList (ArrayList<Integer> newList) {
        listGrupoItem = new ArrayList<>();
        listGrupoItem.addAll(newList);
        notifyDataSetChanged();

    }

}
