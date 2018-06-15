package com.example.bruno.debarrio.PostsDB;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class PedidoDeUsuario extends StringRequest {
    private static final String USUARIO_REQUEST_URL = "https://momentary-electrode.000webhostapp.com/postUsuarioReclamo.php";


    private Map<String, String> params;
    public PedidoDeUsuario (String usuario, Response.Listener<String> listener) {
        super(Request.Method.POST, USUARIO_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_usuario", usuario);
    }
    @Override
    public Map<String, String>getParams(){
        return params;
    }
}
