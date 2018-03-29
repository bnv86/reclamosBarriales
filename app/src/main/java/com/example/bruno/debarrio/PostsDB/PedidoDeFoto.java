package com.example.bruno.debarrio.PostsDB;

import android.graphics.Bitmap;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Bruno on 20/03/2018.
 */

public class PedidoDeFoto extends StringRequest {
    private String KEY_IMAGEN = "foto";
    private String KEY_NOMBRE = "nombre";

    //private static final String REGISTER_REQUEST_URL="http://192.168.1.38/deBarrio/Register.php";
    private static final String FOTO_REQUEST_URL = "https://momentary-electrode.000webhostapp.com/SubirCapturaFoto.php";
    //private Bitmap bitmap;
    //private String nombre;

    private Map<String, String> params;
        //private Map<String, Bitmap> params;
        //String imagen = getStringImagen(bitmap);
    public PedidoDeFoto(String foto, String nombre, Response.Listener < String > listener)
        { //int evento_id,
            super(Method.POST, FOTO_REQUEST_URL, listener, null);
            params = new HashMap<>();
            //params = new Hashtable<String, String>();
            //Map<String,String> params = new Hashtable<String, String>();
            //params.put(KEY_IMAGEN, imagen);
            //params.put(KEY_NOMBRE, nombre);
            params.put("foto", foto);
            params.put("nombre", nombre);
            //params.put("evento_id", evento_id+"");
        }

    /*
    @Override
    public Map<String, String>getParams(){
        return params;
    }

    public String getStringImagen(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }*/
}
