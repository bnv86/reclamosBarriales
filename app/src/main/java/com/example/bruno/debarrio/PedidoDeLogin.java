package com.example.bruno.debarrio;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bruno on 16/03/2018.
 */

public class PedidoDeLogin extends StringRequest {

    //private static final String LOGIN_REQUEST_URL="http://192.168.1.38/deBarrio/Login.php";
    private static final String LOGIN_REQUEST_URL="https://momentary-electrode.000webhostapp.com/Login.php"; //https://momentary-electrode.000webhostapp.com/

    private Map<String, String> params;
    public PedidoDeLogin (String username, String password, Response.Listener<String> listener) { //String idUsuario
        super(Request.Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        //parametros.put("id_usuario", idUsuario);
        params.put("username", username);
        params.put("password", password);
    }
    @Override
    public Map<String, String>getParams(){
        return params;
    }
}
