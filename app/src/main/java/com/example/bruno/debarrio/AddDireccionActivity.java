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
import com.example.bruno.debarrio.PostsDB.PedidoDeDireccion;

import org.json.JSONException;
import org.json.JSONObject;

public class AddDireccionActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textviewRegresar;
    EditText editDireccion, editDetalle;
    Button botonAgregar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_direccion);

        textviewRegresar = findViewById(R.id.textview_regresar);
        textviewRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(); //vuelve al activity anterior
            }
        });
        editDireccion = findViewById(R.id.edit_dire_contacto);
        editDetalle = findViewById(R.id.edit_detalle_contacto);
        botonAgregar = findViewById(R.id.boton_agregar_direccion);
        //botonAgregar.setOnClickListener(this);
        botonAgregar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View view) {
                final String direccion = editDireccion.getText().toString();
                final String detalle = editDetalle.getText().toString();
                if (detalle == null || detalle == "" || detalle.isEmpty() || direccion == null || direccion == "" || direccion.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Complete los campos!", Toast.LENGTH_LONG).show();
                } else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    Intent intent = new Intent(AddDireccionActivity.this, MainTabbedActivity.class);
                                    AddDireccionActivity.this.startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "Contacto agregado!", Toast.LENGTH_LONG).show();
                                } else {
                                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AddDireccionActivity.this);
                                    alertBuilder.setMessage("Error al agregar contacto").setNegativeButton("Reintentar", null).create().show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    PedidoDeDireccion pedido = new PedidoDeDireccion(direccion, detalle, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(AddDireccionActivity.this);
                    queue.add(pedido);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}


