package com.example.bruno.debarrio.PostsDB;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bruno on 28/03/2018.
 */

public class PedidoDeEmail extends StringRequest {

    private static final String CONTACTO_REQUEST_URL = "https://momentary-electrode.000webhostapp.com/postEmail.php";


    private Map<String, String> params;
    public PedidoDeEmail (String email, String detalle, Response.Listener<String> listener) {
        super(Method.POST, CONTACTO_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("email", email);
        params.put("detalle", detalle);
    }
    @Override
    public Map<String, String>getParams(){
        return params;
    }
}