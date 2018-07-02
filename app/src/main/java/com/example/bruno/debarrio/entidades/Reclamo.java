package com.example.bruno.debarrio.entidades;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Bruno on 08/04/2018.
 */

public class Reclamo implements Serializable,Parcelable{
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
    private String email;
    private String cantSuscriptos;

    private int imagenId;
    private int imagenDescripcion;

    public Reclamo(){

    }

    //public Reclamo(String id, String usuarioDesc, String fecha, String latitudDesc, String longitudDesc, String motivo, String comentarioDesc, String estado, Bitmap imagen, Bitmap imagenDesc){ //int imagenDescripcion
    public Reclamo(String id, String id_categoria, String id_usuario, String id_estado, String fecha, Bitmap imagen, Bitmap imagenDesc, String latitudDesc, String longitudDesc, String municipalidad, String descripcionDesc, String email, String cantSuscriptos){
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
        this.email = email;
        this.cantSuscriptos = cantSuscriptos;
    }

    protected Reclamo(Parcel in) {
        id = in.readString();
        id_categoria = in.readString();
        id_usuario = in.readString();
        id_estado = in.readString();
        fecha = in.readString();
        imagen = in.readParcelable(Bitmap.class.getClassLoader());
        imagenDesc = in.readParcelable(Bitmap.class.getClassLoader());
        latitudDesc = in.readString();
        longitudDesc = in.readString();
        municipalidad = in.readString();
        descripcionDesc = in.readString();
        email = in.readString();
        imagenId = in.readInt();
        imagenDescripcion = in.readInt();
        cantSuscriptos = in.readString();
    }

    public static final Creator<Reclamo> CREATOR = new Creator<Reclamo>() {
        @Override
        public Reclamo createFromParcel(Parcel in) {
            return new Reclamo(in);
        }

        @Override
        public Reclamo[] newArray(int size) {
            return new Reclamo[size];
        }
    };

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

    public String getEmail(){return email;}
    public void setEmail(String email){this.email = email;}

    public String getCantSuscriptos(){return cantSuscriptos;}
    public void setCantSuscriptos(String cantSuscriptos){this.cantSuscriptos = cantSuscriptos;}

    //public int getImagenId(){return imagenId;}
    //public void setImagenId(int imagenId){this.imagenId = imagenId;}

    public Bitmap getImagen(){return imagen;}
    public void setImagen(Bitmap imagen){this.imagen = imagen;}

    public Bitmap getImagenDesc(){return imagenDesc;}
    public void setImagenDesc(Bitmap imagenDesc){this.imagenDesc = imagenDesc;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(id_categoria);
        dest.writeString(id_usuario);
        dest.writeString(id_estado);
        dest.writeString(fecha);
        dest.writeParcelable(imagen, flags);
        dest.writeParcelable(imagenDesc, flags);
        dest.writeString(latitudDesc);
        dest.writeString(longitudDesc);
        dest.writeString(municipalidad);
        dest.writeString(descripcionDesc);
        dest.writeString(email);
        dest.writeInt(imagenId);
        dest.writeInt(imagenDescripcion);
        dest.writeString(cantSuscriptos);
    }

    //public int getImagenDescripcion(){return imagenDescripcion;}
    //public void setImagenDescripcion(int imagenDescripcion){this.imagenDescripcion = imagenDescripcion;}


}
