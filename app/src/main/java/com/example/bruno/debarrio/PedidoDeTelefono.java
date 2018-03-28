package com.example.bruno.debarrio;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bruno on 28/03/2018.
 */

public class PedidoDeTelefono extends StringRequest {

    //private static final String REGISTER_REQUEST_URL="http://192.168.1.38/deBarrio/Register.php";
    private static final String CONTACTO_REQUEST_URL = "https://momentary-electrode.000webhostapp.com/postTelefono.php"; //https://momentary-electrode.000webhostapp.com/Register.php

    private Map<String, String> params;
    public PedidoDeTelefono (int telefono, String detalle, Response.Listener<String> listener) {
        super(Method.POST, CONTACTO_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("telefono", telefono+"");
        params.put("detalle", detalle);
    }
    @Override
    public Map<String, String>getParams(){
        return params;
    }
}