package com.example.bruno.debarrio;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import android.support.v7.app.AppCompatActivity;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Bruno on 15/03/2018.
 */

public class PedidoDeRegistro extends StringRequest {

    private static final String REGISTER_REQUEST_URL="http://192.168.1.38/deBarrio/Register.php";
    private Map<String, String> params;
    public PedidoDeRegistro (String username, String password, Response.Listener<String> listener) { //String idUsuario
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
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
