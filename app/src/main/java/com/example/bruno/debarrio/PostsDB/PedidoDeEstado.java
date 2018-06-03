package com.example.bruno.debarrio.PostsDB;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bruno on 28/03/2018.
 */

public class PedidoDeEstado extends StringRequest {

    private static final String ESTADO_REQUEST_URL = "https://momentary-electrode.000webhostapp.com/postEstadoEvento.php";


    private Map<String, String> params;
    public PedidoDeEstado (String estado, Response.Listener<String> listener) {
        super(Method.POST, ESTADO_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("estado", estado);
    }
    @Override
    public Map<String, String>getParams(){
        return params;
    }
}