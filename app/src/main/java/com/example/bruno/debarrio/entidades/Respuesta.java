package com.example.bruno.debarrio.entidades;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Respuesta implements Serializable,Parcelable{
    private String id;
    //private String usuarioDesc;
    //private String id_categoria;
    private String id_reclamo;
    private String id_usuario;
    private String id_estado;
    private String fecha;
    private Bitmap imagen;
    private Bitmap imagenDesc;
    private String comentario;
    private int imagenId;
    private int imagenDescripcion;

    public Respuesta(){

    }

    //public Reclamo(String id, String usuarioDesc, String fecha, String latitudDesc, String longitudDesc, String motivo, String comentarioDesc, String estado, Bitmap imagen, Bitmap imagenDesc){ //int imagenDescripcion
    public Respuesta(String id, String id_reclamo, String id_usuario, String id_estado, String fecha, Bitmap imagen, Bitmap imagenDesc, String comentario){ //String id_categoria,
        this.id = id;
        //this.id_categoria = id_categoria;
        this.id_reclamo = id_reclamo;
        this.id_usuario = id_usuario;
        this.id_estado = id_estado;
        this.fecha = fecha;
        this.imagen = imagen;
        this.imagenDesc = imagenDesc;
        this.comentario = comentario;
    }

    protected Respuesta(Parcel in) {
        id = in.readString();
        //id_categoria = in.readString();
        id_reclamo = in.readString();
        id_usuario = in.readString();
        id_estado = in.readString();
        fecha = in.readString();
        imagen = in.readParcelable(Bitmap.class.getClassLoader());
        imagenDesc = in.readParcelable(Bitmap.class.getClassLoader());
        comentario = in.readString();
        imagenId = in.readInt();
        imagenDescripcion = in.readInt();
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

    public String getId_reclamo(){return id_reclamo;}
    public void setId_reclamo(String id_reclamo){this.id_reclamo = id_reclamo;}

    public String getId_usuario(){return id_usuario;}
    public void setId_usuario(String id_usuario){this.id_usuario = id_usuario;}

    public String getId_estado(){return id_estado;}
    public void setId_estado(String id_estado){this.id_estado = id_estado;}

    public String getFecha(){return fecha;}
    public void setFecha(String fecha){this.fecha = fecha;}

    public String getComentario(){return comentario;}
    public void setComentario(String comentario){this.comentario = comentario;}

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
        //dest.writeString(id_categoria);
        dest.writeString(id_reclamo);
        dest.writeString(id_usuario);
        dest.writeString(id_estado);
        dest.writeString(fecha);
        dest.writeParcelable(imagen, flags);
        dest.writeParcelable(imagenDesc, flags);
        dest.writeString(comentario);
        dest.writeInt(imagenId);
        dest.writeInt(imagenDescripcion);
    }
}
