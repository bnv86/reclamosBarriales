package com.example.bruno.debarrio;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.bruno.debarrio.PostsDB.PedidoDeContacto;

import org.json.JSONException;
import org.json.JSONObject;

public class AddContactoActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textviewRegresar;
    EditText editTelefono, editEmail, editDireccion, editDetalle;
    Button botonAgregar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacto);

        textviewRegresar = findViewById(R.id.textview_regresar);
        textviewRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(); //vuelve al activity anterior
            }
        });
        editTelefono = findViewById(R.id.edit_tel_contacto);
        editEmail = findViewById(R.id.edit_email_contacto);
        editDireccion = findViewById(R.id.edit_dire_contacto);
        editDetalle = findViewById(R.id.edit_detalle_contacto);
        botonAgregar = findViewById(R.id.boton_agregar_contacto);
        //botonAgregar.setOnClickListener(this);
        botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                //final int telefono = Integer.parseInt(editTelefono.getText().toString());
                final String email = editEmail.getText().toString();
                final String direccion = editDireccion.getText().toString();
                final String detalle = editDetalle.getText().toString();
                //esto soluciona el error que tira al dejar en blanco campos int al agregar
                final EditText t = findViewById(R.id.edit_tel_contacto);
                final String tel = t.getText().toString().trim();
                final int telefono = !tel.equals("") ? Integer.parseInt(tel) : 0;
                if (detalle == null || detalle == "" || detalle.isEmpty() || direccion == null || direccion == "" || direccion.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Complete todos los campos!", Toast.LENGTH_LONG).show();
                } else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    Intent intent = new Intent(AddContactoActivity.this, MainTabbedActivity.class);
                                    AddContactoActivity.this.startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "Contacto agregado!", Toast.LENGTH_LONG).show();
                                } else {
                                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AddContactoActivity.this);
                                    alertBuilder.setMessage("Error al agregar contacto").setNegativeButton("Reintentar", null).create().show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    PedidoDeContacto pedido = new PedidoDeContacto(telefono, email, direccion, detalle, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(AddContactoActivity.this);
                    queue.add(pedido);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
