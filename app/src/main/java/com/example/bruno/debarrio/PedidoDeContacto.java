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
    private static final String REGISTER_REQUEST_URL="https://momentary-electrode.000webhostapp.com/Contacto.php"; //https://momentary-electrode.000webhostapp.com/Register.php


    private Map<String, String> params;
    public PedidoDeContacto (int tel, String email, String dire, String detalle, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        //parametros.put("id_usuario", idUsuario);
        params.put("telefono", tel+"");
        params.put("email", email);
        params.put("direccion", dire);
        params.put("detalle", detalle);
    }
    @Override
    public Map<String, String>getParams(){
        return params;
    }
}