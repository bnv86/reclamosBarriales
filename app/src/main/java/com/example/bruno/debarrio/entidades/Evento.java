package com.example.bruno.debarrio.entidades;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Bruno on 08/04/2018.
 */

public class Evento implements Serializable{
    private String id;
    //private String usuarioDesc;
    private String id_categoria;
    private String id_usuario;
    private String id_estado;
    private String fecha;
    private Bitmap imagen;
    private Bitmap imagenDesc;
    private String latitudDesc;
    private String longitudDesc;
    //private String categoria;
    private String municipalidad;
    private String descripcionDesc;

    private int imagenId;
    private int imagenDescripcion;

    public Evento(){

    }

    //public Evento(String id, String usuarioDesc, String fecha, String latitudDesc, String longitudDesc, String motivo, String comentarioDesc, String estado, Bitmap imagen, Bitmap imagenDesc){ //int imagenDescripcion
    public Evento(String id, String id_categoria, String id_usuario, String id_estado, String fecha, Bitmap imagen, Bitmap imagenDesc, String latitudDesc, String longitudDesc, String municipalidad, String descripcionDesc){
        this.id = id;
        this.id_categoria = id_categoria;
        this.id_usuario = id_usuario;
        this.id_estado = id_estado;
        this.fecha = fecha;
        this.imagen = imagen;
        this.imagenDesc = imagenDesc;
        this.latitudDesc = latitudDesc;
        this.longitudDesc = longitudDesc;
        this.municipalidad = municipalidad;
        this.descripcionDesc = descripcionDesc;
        //this.imagenId = imagenId;
        //this.imagenDescripcion = imagenDescripcion;
    }

    public String getId() {return id;}

    public String getId_categoria(){return id_categoria;}
    public void setId_categoria(String id_categoria){this.id_categoria = id_categoria;}

    public String getId_usuario(){return id_usuario;}
    public void setId_usuario(String id_usuario){this.id_usuario = id_usuario;}

    public String getId_estado(){return id_estado;}
    public void setId_estado(String id_estado){this.id_estado = id_estado;}

    public String getFecha(){return fecha;}
    public void setFecha(String fecha){this.fecha = fecha;}

    public String getLatitudDesc(){return latitudDesc;}
    public void setLatitudDesc(String latitudDesc){this.latitudDesc = latitudDesc;}

    public String getLongitudDesc(){return longitudDesc;}
    public void setLongitudDesc(String longitudDesc){this.longitudDesc = longitudDesc;}

    public String getMunicipalidad(){return municipalidad;}
    public void setMunicipalidad(String municipalidad){this.municipalidad = municipalidad;}

    public String getDescripcionDesc(){return descripcionDesc;}
    public void setDescripcionDesc(String descripcionDesc){this.descripcionDesc = descripcionDesc;}

    //public int getImagenId(){return imagenId;}
    //public void setImagenId(int imagenId){this.imagenId = imagenId;}

    public Bitmap getImagen(){return imagen;}
    public void setImagen(Bitmap imagen){this.imagen = imagen;}

    public Bitmap getImagenDesc(){return imagenDesc;}
    public void setImagenDesc(Bitmap imagenDesc){this.imagenDesc = imagenDesc;}

    //public int getImagenDescripcion(){return imagenDescripcion;}
    //public void setImagenDescripcion(int imagenDescripcion){this.imagenDescripcion = imagenDescripcion;}


}
