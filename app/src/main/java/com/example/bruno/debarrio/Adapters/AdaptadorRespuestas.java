package com.example.bruno.debarrio.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.bruno.debarrio.R;
import com.example.bruno.debarrio.entidades.Respuesta;
import java.util.ArrayList;

public class AdaptadorRespuestas extends RecyclerView.Adapter<AdaptadorRespuestas.RespuestasViewHolder> implements View.OnClickListener{

    ArrayList<Respuesta> listaRespuestas;
    private View.OnClickListener listener;

    public AdaptadorRespuestas(ArrayList<Respuesta> listaRespuestas){
        this.listaRespuestas = listaRespuestas;
    }

    @Override
    public RespuestasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_respuesta, null, false);
        view.setOnClickListener(this);
        return new RespuestasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RespuestasViewHolder holder, int position) {
        holder.textFecha.setText(listaRespuestas.get(position).getFecha());
        //holder.textCategoria.setText(listaRespuestas.get(position).getId_categoria());
        holder.textUsuario.setText(listaRespuestas.get(position).getId_usuario());
        holder.textEstado.setText(listaRespuestas.get(position).getId_estado());
        holder.foto.setImageBitmap(listaRespuestas.get(position).getImagen());
        //holder.textSuscriptos.setText(listaRespuestas.get(position).getCantSuscriptos());
    }

    @Override
    public int getItemCount() {
        return listaRespuestas.size();
    }
    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
        //listener = null;
    }

    public class RespuestasViewHolder extends RecyclerView.ViewHolder{

        TextView textFecha, textUsuario, textEstado, textSuscriptos;
        ImageView foto;

        public RespuestasViewHolder(View itemView) {
            super(itemView);
            textFecha = (TextView) itemView.findViewById(R.id.id_fecha);
            //textCategoria = (TextView) itemView.findViewById(R.id.id_categoria);
            textUsuario = (TextView) itemView.findViewById(R.id.id_usuario);
            textEstado = (TextView) itemView.findViewById(R.id.id_estado);
            foto = (ImageView) itemView.findViewById(R.id.id_imagen);
            //textSuscriptos = (TextView) itemView.findViewById(R.id.cant_suscriptos);
        }
    }
}