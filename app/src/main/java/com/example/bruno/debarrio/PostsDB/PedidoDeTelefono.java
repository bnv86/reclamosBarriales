package com.example.bruno.debarrio.PostsDB;

import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bruno on 28/03/2018.
 */

public class PedidoDeTelefono extends StringRequest {

    private static final String CONTACTO_REQUEST_URL = "https://momentary-electrode.000webhostapp.com/postTelefono.php";

    private Map<String, String> params;
    public PedidoDeTelefono (int telefono, String detalle, Response.Listener<String> listener) { //throws AuthFailureError
        super(Method.POST, CONTACTO_REQUEST_URL, listener, null);
        if (CONTACTO_REQUEST_URL == (getUrl())) {
            params = new HashMap<>();
            params.put("telefono", telefono + "");
            params.put("detalle", detalle);
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