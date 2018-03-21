package com.example.bruno.debarrio;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bruno on 20/03/2018.
 */

public class PedidoDeContacto extends StringRequest {

    //private static final String REGISTER_REQUEST_URL="http://192.168.1.38/deBarrio/Register.php";
    private static final String CONTACTO_REQUEST_URL = "https://momentary-electrode.000webhostapp.com/Contacto.php"; //https://momentary-electrode.000webhostapp.com/Register.php


    private Map<String, String> params;
    public PedidoDeContacto (int telefono, String email, String direccion, String detalle, Response.Listener<String> listener) {
        super(Method.POST, CONTACTO_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("telefono", telefono+"");
        params.put("email", email);
        params.put("direccion", direccion);
        params.put("detalle", detalle);
    }
    @Override
    public Map<String, String>getParams(){
        return params;
    }
}