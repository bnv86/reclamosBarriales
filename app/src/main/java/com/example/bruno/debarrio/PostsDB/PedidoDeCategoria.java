package com.example.bruno.debarrio.PostsDB;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PedidoDeCategoria extends StringRequest {
    private static final String CATEGORIA_REQUEST_URL = "https://momentary-electrode.000webhostapp.com/getCategoria.php";


    private Map<String, String> params;
    public PedidoDeCategoria (String categoria, Response.Listener<String> listener) {
        super(Request.Method.POST, CATEGORIA_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_categoria", categoria);
    }
    @Override
    public Map<String, String>getParams(){
        return params;
    }
}