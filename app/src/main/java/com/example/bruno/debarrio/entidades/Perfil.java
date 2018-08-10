package com.example.bruno.debarrio.entidades;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Perfil implements Serializable,Parcelable {
    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private Bitmap imagen;
    //private Bitmap imagenDesc;
    private String username;
    private String password;

    public Perfil(){

    }
    public Perfil(String id, String nombre, String apellido, String email, String telefono, String username, String password){ //Bitmap imagen,
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.imagen = imagen;
        this.username = username;
        this.password = password;
    }

    protected Perfil(Parcel in) {
        id = in.readString();
        nombre = in.readString();
        apellido = in.readString();
        email = in.readString();
        telefono = in.readString();
        imagen = in.readParcelable(Bitmap.class.getClassLoader());
        username = in.readString();
        password = in.readString();
    }

    public static final Parcelable.Creator<Reclamo> CREATOR = new Parcelable.Creator<Reclamo>() {
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

    //public String getId_categoria(){return id_categoria;}
    //public void setId_categoria(String id_categoria){this.id_categoria = id_categoria;}

    public String getNombre(){return nombre;}
    public void setNombre(String nombre){this.nombre = nombre;}

    public String getApellido(){return apellido;}
    public void setApellido(String apellido){this.apellido = apellido;}

    public String getEmail(){return email;}
    public void setEmail(String email){this.email = email;}

    public String getTelefono(){return telefono;}
    public void setTelefono(String telefono){this.telefono = telefono;}

    public String getUsername(){return username;}
    public void setUsername(String username){this.username = username;}

    public String getPassword(){return password;}
    public void setPassword(String password){this.password = password;}

    public Bitmap getImagen(){return imagen;}
    public void setImagen(Bitmap imagen){this.imagen = imagen;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        //dest.writeString(id_categoria);
        dest.writeString(nombre);
        dest.writeString(apellido);
        dest.writeString(email);
        dest.writeString(telefono);
        dest.writeParcelable(imagen, flags);
        dest.writeString(username);
        dest.writeString(password);
    }
}
