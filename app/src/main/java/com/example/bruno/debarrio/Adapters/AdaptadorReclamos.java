package com.example.bruno.debarrio.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bruno.debarrio.R;
import com.example.bruno.debarrio.entidades.Reclamo;

import java.util.ArrayList;

/**
 * Created by Bruno on 08/04/2018.
 */

public class AdaptadorReclamos extends RecyclerView.Adapter<AdaptadorReclamos.ReclamosViewHolder> implements View.OnClickListener{

    ArrayList<Reclamo> listaReclamos;
    private View.OnClickListener listener;

    public AdaptadorReclamos(ArrayList<Reclamo> listaReclamos){
        this.listaReclamos = listaReclamos;
    }

    @Override
    public ReclamosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null, false);
        view.setOnClickListener(this);
        return new ReclamosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReclamosViewHolder holder, int position) {
        holder.textFecha.setText(listaReclamos.get(position).getFecha());
        holder.textCategoria.setText(listaReclamos.get(position).getId_categoria());
        holder.textEstado.setText(listaReclamos.get(position).getId_estado());
        holder.foto.setImageBitmap(listaReclamos.get(position).getImagen());
        holder.textSuscriptos.setText(listaReclamos.get(position).getCantSuscriptos());
    }

    @Override
    public int getItemCount() {
        return listaReclamos.size();
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

    public class ReclamosViewHolder extends RecyclerView.ViewHolder{

        TextView textFecha, textCategoria, textEstado, textSuscriptos;
        ImageView foto;

        public ReclamosViewHolder(View itemView) {
            super(itemView);
            textFecha = (TextView) itemView.findViewById(R.id.id_fecha);
            textCategoria = (TextView) itemView.findViewById(R.id.id_categoria);
            textEstado = (TextView) itemView.findViewById(R.id.id_estado);
            foto = (ImageView) itemView.findViewById(R.id.id_imagen);
            textSuscriptos = (TextView) itemView.findViewById(R.id.cant_suscriptos);
        }
    }
}
