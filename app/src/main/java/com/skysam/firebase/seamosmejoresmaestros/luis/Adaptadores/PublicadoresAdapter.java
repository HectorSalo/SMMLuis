package com.skysam.firebase.seamosmejoresmaestros.luis.Adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.skysam.firebase.seamosmejoresmaestros.luis.Constructores.PublicadoresConstructor;
import com.skysam.firebase.seamosmejoresmaestros.luis.Publicadores.EditarPubActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skysam.firebase.seamosmejoresmaestros.luis.R;
import com.skysam.firebase.seamosmejoresmaestros.luis.Variables.VariablesEstaticas;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PublicadoresAdapter extends RecyclerView.Adapter<PublicadoresAdapter.ViewHolderPublicadores> implements View.OnClickListener{

    private ArrayList<PublicadoresConstructor> listPublicadores;
    private View.OnClickListener listener;
    private Context mctx;

    public PublicadoresAdapter(ArrayList<PublicadoresConstructor> listPublicadores, Context mctx) {
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mctx);
        boolean temaOscuro = sharedPreferences.getBoolean("activarOscuro", false);

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
                if (temaOscuro) {
                    viewHolderPublicadores.imagenPublicador.setImageResource(R.drawable.ic_caballero_blanco);
                } else {
                    viewHolderPublicadores.imagenPublicador.setImageResource(R.drawable.ic_caballero);
                }
            }

        } else if (listPublicadores.get(i).getGenero().equals("Mujer")) {
            if (listPublicadores.get(i).getImagen() != null) {
                Glide.with(mctx).load(listPublicadores.get(i).getImagen()).into(viewHolderPublicadores.imagenPublicador);
            } else {
                if (temaOscuro){
                    viewHolderPublicadores.imagenPublicador.setImageResource(R.drawable.ic_dama_blanco);
                } else {
                    viewHolderPublicadores.imagenPublicador.setImageResource(R.drawable.ic_dama);
                }
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

    private void eliminarPublicador(final PublicadoresConstructor i) {
        String doc = i.getIdPublicador();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection(VariablesEstaticas.BD_PUBLICADORES);

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

    public void updateList (ArrayList<PublicadoresConstructor> newList) {
        listPublicadores = new ArrayList<>();
        listPublicadores.addAll(newList);
        notifyDataSetChanged();
    }
}
