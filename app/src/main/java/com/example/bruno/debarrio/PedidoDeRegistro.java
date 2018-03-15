package com.example.bruno.debarrio;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Bruno on 15/03/2018.
 */

public class PedidoDeRegistro extends StringRequest {

    private static final String REGISTER_REQUEST_URL="http://192.168.1.38/deBarrio/Register.php";
    private Map<String, String> parametros;
    public PedidoDeRegistro (String nombreUsuario, String password, Response.Listener<String> listener) { //String idUsuario
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        parametros = new HashMap<>();
        //parametros.put("id_usuario", idUsuario);
        parametros.put("nombre_usuario", nombreUsuario);
        parametros.put("password", password);
    }
    //@Override
    public Map<String, String>getParametros(){
        return parametros;
    }


}
