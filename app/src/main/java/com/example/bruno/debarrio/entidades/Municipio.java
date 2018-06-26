package com.example.bruno.debarrio.entidades;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Municipio implements Serializable {
    private String id;
    private String nombre;

    public Municipio() {

    }

    public Municipio(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


}