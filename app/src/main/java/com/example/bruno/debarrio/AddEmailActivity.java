package com.example.bruno.debarrio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.bruno.debarrio.PostsDB.PedidoDeEmail;

import org.json.JSONException;
import org.json.JSONObject;

public class AddEmailActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textviewRegresar;
    EditText editEmail, editDetalle;
    Button botonAgregar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_email);

        textviewRegresar = findViewById(R.id.textview_regresar);
        textviewRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(); //vuelve al activity anterior
            }
        });
        editEmail = findViewById(R.id.edit_email_contacto);
        editDetalle = findViewById(R.id.edit_detalle_contacto);
        botonAgregar = findViewById(R.id.boton_agregar_email);
        botonAgregar.setOnClickListener(this);
    }

    @Override
    public void onClick (View view){
        final String email = editEmail.getText().toString();
        final String detalle = editDetalle.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if(success){
                        Intent intent = new Intent(AddEmailActivity.this, MainTabbedActivity.class);
                        AddEmailActivity.this.startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Contacto agregado!", Toast.LENGTH_LONG).show();
                    }else {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AddEmailActivity.this);
                        alertBuilder.setMessage("Error al agregar contacto").setNegativeButton("Reintentar", null).create().show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        PedidoDeEmail pedido = new PedidoDeEmail(email, detalle, responseListener);
        RequestQueue queue = Volley.newRequestQueue(AddEmailActivity.this);
        queue.add(pedido);
    }
}
