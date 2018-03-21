package com.example.bruno.debarrio;

import android.graphics.Bitmap;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bruno on 20/03/2018.
 */

public class PedidoDeFoto extends StringRequest {

    //private static final String REGISTER_REQUEST_URL="http://192.168.1.38/deBarrio/Register.php";
    private static final String FOTO_REQUEST_URL = "https://momentary-electrode.000webhostapp.com/SubirFoto.php"; //https://momentary-electrode.000webhostapp.com/Register.php


    private Map<String, String> params;
    //private Map<String, Bitmap> params;
    public PedidoDeFoto (String username, String imagen,  Response.Listener<String> listener) { //int evento_id,
        super(Method.POST, FOTO_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("imagen", imagen);
        //params.put("evento_id", evento_id+"");
    }
    @Override
    public Map<String, String>getParams(){
        return params;
    }
}