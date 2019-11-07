package com.google.firebase.example.seamosmejoresmaestros;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdapterGrupoItem extends RecyclerView.Adapter<AdapterGrupoItem.ViewHolderGrupoItem> {

    private ArrayList<Integer> listGrupoItem;
    private Context mctx;


    public AdapterGrupoItem(ArrayList<Integer> listGrupoItem, Context mctx) {
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
    public void onBindViewHolder(@NonNull final AdapterGrupoItem.ViewHolderGrupoItem holder, final int position) {
        holder.tvTitleGrupo.setText("Grupo " + listGrupoItem.get(position));
        final ArrayList<ConstructorPublicadores> listPublicadores = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mctx, LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerGrupoItem.setLayoutManager(layoutManager);
        holder.recyclerGrupoItem.setHasFixedSize(true);
        final AdapterVerTodosGrupos adapterVerTodosGrupos = new AdapterVerTodosGrupos(listPublicadores, mctx);
        holder.recyclerGrupoItem.setAdapter(adapterVerTodosGrupos);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");

        Query query = reference.orderBy(UtilidadesStatic.BD_GRUPO).orderBy(UtilidadesStatic.BD_APELLIDO, Query.Direction.ASCENDING);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        ConstructorPublicadores publi = new ConstructorPublicadores();

                        double grupoD = doc.getDouble(UtilidadesStatic.BD_GRUPO);
                        int grupoInt = (int) grupoD;

                        if (listGrupoItem.get(position) == grupoInt) {

                            publi.setIdPublicador(doc.getId());
                            publi.setNombrePublicador(doc.getString(UtilidadesStatic.BD_NOMBRE));
                            publi.setApellidoPublicador(doc.getString(UtilidadesStatic.BD_APELLIDO));
                            publi.setGrupo(doc.getDouble(UtilidadesStatic.BD_GRUPO));

                            listPublicadores.add(publi);
                        }

                    }

                    adapterVerTodosGrupos.updateList(listPublicadores);

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
