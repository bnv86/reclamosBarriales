package com.example.bruno.debarrio.entidades;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Bruno on 08/04/2018.
 */

public class Evento implements Serializable{
    private String usuarioDesc;
    private String fecha;
    private String latitudDesc;
    private String longitudDesc;
    private String motivo;
    private String comentarioDesc;
    private int imagenId;
    private int imagenDescripcion;

    private Bitmap imagen;
    private Bitmap imagenDesc;

    public Evento(){

    }

    public Evento(String usuarioDesc, String fecha, String latitudDesc, String longitudDesc, String motivo, String comentarioDesc, Bitmap imagen, Bitmap imagenDesc){ //int imagenDescripcion
        this.usuarioDesc = usuarioDesc;
        this.fecha = fecha;
        this.latitudDesc = latitudDesc;
        this.longitudDesc = longitudDesc;
        this.motivo = motivo;
        this.comentarioDesc = comentarioDesc;
        //this.imagenId = imagenId;
        this.imagen = imagen;
        this.imagenDesc = imagenDesc;
        //this.imagenDescripcion = imagenDescripcion;

    }

    public String getUsuarioDesc(){return usuarioDesc;}
    public void setUsuarioDesc(String usuarioDesc){this.usuarioDesc = usuarioDesc;}

    public String getFecha(){return fecha;}
    public void setFecha(String fecha){this.fecha = fecha;}

    public String getLatitudDesc(){return latitudDesc;}
    public void setLatitudDesc(String latitudDesc){this.latitudDesc = latitudDesc;}

    public String getLongitudDesc(){return longitudDesc;}
    public void setLongitudDesc(String longitudDesc){this.longitudDesc = longitudDesc;}

    public String getMotivo(){return motivo;}
    public void setMotivo(String motivo){this.motivo = motivo;}

    public String getComentarioDesc(){return comentarioDesc;}
    public void setComentarioDesc(String comentarioDesc){this.comentarioDesc = comentarioDesc;}

    //public int getImagenId(){return imagenId;}
    //public void setImagenId(int imagenId){this.imagenId = imagenId;}

    public Bitmap getImagen(){return imagen;}
    public void setImagen(Bitmap imagen){this.imagen = imagen;}

    public Bitmap getImagenDesc(){return imagenDesc;}
    public void setImagenDesc(Bitmap imagenDesc){this.imagenDesc = imagenDesc;}

    //public int getImagenDescripcion(){return imagenDescripcion;}
    //public void setImagenDescripcion(int imagenDescripcion){this.imagenDescripcion = imagenDescripcion;}


}
