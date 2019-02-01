package com.google.firebase.example.seamosmejoresmaestros;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AdapterPublicadores extends RecyclerView.Adapter<AdapterPublicadores.ViewHolderPublicadores> implements View.OnClickListener{

    private ArrayList<ConstructorPublicadores> listPublicadores;
    private View.OnClickListener listener;
    private Context mctx;

    public AdapterPublicadores (ArrayList<ConstructorPublicadores> listPublicadores, Context mctx) {
        this.listPublicadores = listPublicadores;
        this.mctx = mctx;
    }

    @NonNull
    @Override
    public ViewHolderPublicadores onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_publicadores, null, false);
        view.setOnClickListener(this);
        return new ViewHolderPublicadores(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderPublicadores viewHolderPublicadores, final int i) {
        viewHolderPublicadores.nombrePub.setText(listPublicadores.get(i).getNombrePublicador());
        viewHolderPublicadores.apellidoPub.setText(listPublicadores.get(i).getApellidoPublicador());

        if (listPublicadores.get(i).getUltAsignacion() == null){
            viewHolderPublicadores.ultiDiscurso.setText("");
        } else {
            viewHolderPublicadores.ultiDiscurso.setText(new SimpleDateFormat("EEE d MMM yyyy").format(listPublicadores.get(i).getUltAsignacion()));
        }

        if (listPublicadores.get(i).getGenero().equals("Hombre")) {
            if (listPublicadores.get(i).getImagen() != null) {
                Glide.with(mctx).load(listPublicadores.get(i).getImagen()).into(viewHolderPublicadores.imagenPublicador);
            } else {
                viewHolderPublicadores.imagenPublicador.setImageResource(R.drawable.ic_caballero);
            }

        } else if (listPublicadores.get(i).getGenero().equals("Mujer")) {
            if (listPublicadores.get(i).getImagen() != null) {
                Glide.with(mctx).load(listPublicadores.get(i).getImagen()).into(viewHolderPublicadores.imagenPublicador);
            } else {
                viewHolderPublicadores.imagenPublicador.setImageResource(R.drawable.ic_dama);
            }
        }

        viewHolderPublicadores.menuPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mctx, viewHolderPublicadores.menuPub);
                popupMenu.inflate(R.menu.option_menu_pub);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.option_edit:
                                Intent myIntent = new Intent(mctx, EditarPubActivity.class);
                                Bundle myBundle = new Bundle();
                                myBundle.putString("idEditar", listPublicadores.get(i).getIdPublicador());
                                myIntent.putExtras(myBundle);
                                mctx.startActivity(myIntent);
                                break;

                            case R.id.option_delete:
                                AlertDialog.Builder dialog = new AlertDialog.Builder(mctx);
                                dialog.setTitle("Confirmar");
                                dialog.setMessage("¿Desea eliminar este publicador?");

                                dialog.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        eliminarPublicador(listPublicadores.get(i));
                                    }
                                });
                                dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                dialog.setIcon(R.drawable.ic_delete);
                                dialog.show();
                                break;

                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
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

    public class ViewHolderPublicadores extends RecyclerView.ViewHolder {

        ImageView imagenPublicador;
        TextView nombrePub, apellidoPub, ultiDiscurso, menuPub;

        public ViewHolderPublicadores(@NonNull View itemView) {
            super(itemView);

            imagenPublicador = (ImageView) itemView.findViewById(R.id.imagenPublicadores);
            menuPub = (TextView) itemView.findViewById(R.id.menuPub);
            nombrePub = (TextView) itemView.findViewById(R.id.textViewNombre);
            apellidoPub = (TextView) itemView.findViewById(R.id.textViewApellido);
            ultiDiscurso = (TextView) itemView.findViewById(R.id.textViewFecha);
        }
    }

    private void eliminarPublicador(final ConstructorPublicadores i) {
        String doc = i.getIdPublicador();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("publicadores");

        reference.document(doc)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listPublicadores.remove(i);
                        notifyDataSetChanged();
                        Toast.makeText(mctx,"Eliminado", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mctx, "Error al eliminar. Intente nuevamente", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateList (ArrayList<ConstructorPublicadores> newList) {
        listPublicadores = new ArrayList<>();
        listPublicadores.addAll(newList);
        notifyDataSetChanged();
    }
}
