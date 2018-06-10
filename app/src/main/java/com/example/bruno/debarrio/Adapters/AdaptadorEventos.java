package com.example.bruno.debarrio.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bruno.debarrio.R;
import com.example.bruno.debarrio.entidades.Evento;

import java.util.ArrayList;

/**
 * Created by Bruno on 08/04/2018.
 */

public class AdaptadorEventos extends RecyclerView.Adapter<AdaptadorEventos.PersonajesViewHolder> implements View.OnClickListener{

    ArrayList<Evento> listaPersonajes;
    private View.OnClickListener listener;

    public AdaptadorEventos(ArrayList<Evento> listaPersonajes){
        this.listaPersonajes = listaPersonajes;
    }

    @Override
    public PersonajesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null, false);
        view.setOnClickListener(this);
        return new PersonajesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PersonajesViewHolder holder, int position) {
        holder.textFecha.setText(listaPersonajes.get(position).getFecha());
        holder.textCategoria.setText(listaPersonajes.get(position).getId_categoria());
        holder.textEstado.setText(listaPersonajes.get(position).getId_estado());
        holder.foto.setImageBitmap(listaPersonajes.get(position).getImagen());
    }

    @Override
    public int getItemCount() {
        return listaPersonajes.size();
    }
    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public class PersonajesViewHolder extends RecyclerView.ViewHolder{

        TextView textFecha, textCategoria, textEstado;
        ImageView foto;

        public PersonajesViewHolder(View itemView) {
            super(itemView);
            textFecha = (TextView) itemView.findViewById(R.id.idFecha);
            textCategoria = (TextView) itemView.findViewById(R.id.idCategoria);
            textEstado = (TextView) itemView.findViewById(R.id.idEstado);
            foto = (ImageView) itemView.findViewById(R.id.idImagen);
        }
    }
}
