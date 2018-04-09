package com.example.bruno.debarrio.entidades;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Bruno on 08/04/2018.
 */

public class Personaje implements Serializable{
    private String nombre;
    private String info;
    private String descripcion;
    private int imagenId;
    private int imagenDescripcion;

    private Bitmap imagen;
    private Bitmap imagenDesc;

    public Personaje(){

    }

    public Personaje(String nombre, String info, String descripcion, Bitmap imagen, Bitmap imagenDesc){ //int imagenDescripcion
        this.nombre = nombre;
        this.info = info;
        this.descripcion = descripcion;
        //this.imagenId = imagenId;
        this.imagen = imagen;
        this.imagenDesc = imagenDesc;
        //this.imagenDescripcion = imagenDescripcion;

    }

    public String getNombre(){return nombre;}
    public void setNombre(String nombre){this.nombre = nombre;}

    public String getInfo(){return info;}
    public void setInfo(String info){this.info = info;}

    public String getDescripcion(){return descripcion;}
    public void setDescripcion(String descripcion){this.descripcion = descripcion;}

    //public int getImagenId(){return imagenId;}
    //public void setImagenId(int imagenId){this.imagenId = imagenId;}

    public Bitmap getImagen(){return imagen;}
    public void setImagen(Bitmap imagen){this.imagen = imagen;}

    public Bitmap getImagenDesc(){return imagenDesc;}
    public void setImagenDesc(Bitmap imagenDesc){this.imagenDesc = imagenDesc;}

    //public int getImagenDescripcion(){return imagenDescripcion;}
    //public void setImagenDescripcion(int imagenDescripcion){this.imagenDescripcion = imagenDescripcion;}


}
