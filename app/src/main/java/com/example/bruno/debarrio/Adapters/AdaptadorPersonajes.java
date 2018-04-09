package com.example.bruno.debarrio.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bruno.debarrio.R;
import com.example.bruno.debarrio.entidades.Personaje;

import java.util.ArrayList;

/**
 * Created by Bruno on 08/04/2018.
 */

public class AdaptadorPersonajes extends RecyclerView.Adapter<AdaptadorPersonajes.PersonajesViewHolder> implements View.OnClickListener{

    ArrayList<Personaje> listaPersonajes;
    private View.OnClickListener listener;

    public AdaptadorPersonajes(ArrayList<Personaje> listaPersonajes){
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
        holder.textNombre.setText(listaPersonajes.get(position).getNombre());
        holder.textInformacion.setText(listaPersonajes.get(position).getInfo());
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

        TextView textNombre, textInformacion;
        ImageView foto;

        public PersonajesViewHolder(View itemView) {
            super(itemView);
            textNombre = (TextView) itemView.findViewById(R.id.idNombre);
            textInformacion = (TextView) itemView.findViewById(R.id.idInfo);
            foto = (ImageView) itemView.findViewById(R.id.idImagen);
        }
    }
}
