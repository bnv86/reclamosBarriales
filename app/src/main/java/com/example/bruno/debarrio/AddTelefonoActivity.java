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
import com.example.bruno.debarrio.Adapters.ListAdapterContactos;
import com.example.bruno.debarrio.PostsDB.PedidoDeTelefono;

import org.json.JSONException;
import org.json.JSONObject;

public class AddTelefonoActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textviewRegresar;
    EditText editTelefono, editDetalle;
    Button botonAgregar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_telefono);

        textviewRegresar = findViewById(R.id.textview_regresar);
        textviewRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(); //vuelve al activity anterior
            }
        });
        editTelefono = findViewById(R.id.edit_tel_contacto);
        editDetalle = findViewById(R.id.edit_detalle_contacto);
        botonAgregar = findViewById(R.id.boton_agregar_telefono);
        //botonAgregar.setOnClickListener(this);
        botonAgregar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //final int telefono = Integer.parseInt(editTelefono.getText().toString());
                final String detalle = editDetalle.getText().toString();
                //esto soluciona el error que tira al dejar en blanco campos int al agregar
                final EditText t = findViewById(R.id.edit_tel_contacto);
                final String tel = t.getText().toString().trim();
                final int telefono = !tel.equals("") ? Integer.parseInt(tel) : 0;
                if (detalle == null || detalle == "" || detalle.isEmpty() || tel == null || tel == "" || tel.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Complete los campos!", Toast.LENGTH_LONG).show();
                } else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    Intent intent = new Intent(AddTelefonoActivity.this, MainTabbedActivity.class);
                                    AddTelefonoActivity.this.startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "Contacto agregado!", Toast.LENGTH_LONG).show();
                                } else {
                                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AddTelefonoActivity.this);
                                    alertBuilder.setMessage("Error al agregar contacto").setNegativeButton("Reintentar", null).create().show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    PedidoDeTelefono pedido = new PedidoDeTelefono(telefono, detalle, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(AddTelefonoActivity.this);
                    queue.add(pedido);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
