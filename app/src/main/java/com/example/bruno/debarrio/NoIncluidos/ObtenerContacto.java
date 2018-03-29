package com.example.bruno.debarrio.NoIncluidos;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bruno on 20/03/2018.
 */

public class ObtenerContacto extends StringRequest {

    //private static final String REGISTER_REQUEST_URL="http://192.168.1.38/deBarrio/Register.php";
    private static final String CONTACTO_GET_URL = "https://momentary-electrode.000webhostapp.com/getContacto.php"; //https://momentary-electrode.000webhostapp.com/Register.php


    private Map<String, String> params;
    public ObtenerContacto (String email, String direccion, String detalle) {
        super(Method.GET, CONTACTO_GET_URL, null, null);
        params = new HashMap<>();
        //params.put("telefono", telefono+"");
        params.put("email", email);
        params.put("direccion", direccion);
        params.put("detalle", detalle);
    }
    @Override
    public Map<String, String>getParams(){
        return params;
    }
}