package com.example.bruno.debarrio.PostsDB;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PedidoDeCoordenada extends StringRequest {

    private static final String CONTACTO_REQUEST_URL = "https://momentary-electrode.000webhostapp.com/postCoordenadas.php";

    private Map<String, String> params;
    public PedidoDeCoordenada (String coordenadas, Response.Listener<String> listener) { //throws AuthFailureError
        super(Method.POST, CONTACTO_REQUEST_URL, listener, null);
        if (CONTACTO_REQUEST_URL == (getUrl())) {
            params = new HashMap<>();
            params.put("coordenadas", coordenadas + "");
        }
        else{
            //Toast.makeText(context, "Sin conexión con el servidor :(", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public Map<String, String>getParams(){
        return params;
    }
}