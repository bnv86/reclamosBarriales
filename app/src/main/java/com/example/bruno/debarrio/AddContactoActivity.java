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

import org.json.JSONException;
import org.json.JSONObject;

public class AddContactoActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textview_regresar;
    EditText editTelefono, editEmail, editDireccion, editDetalle;
    Button botonAgregar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacto);

        textview_regresar = findViewById(R.id.textview_regresar);
        textview_regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(); //vuelve al activity anterior
                //Intent intentRegistro = new Intent(RegistroActivity.this, LoginActivity.class);
                //RegistroActivity.this.startActivity(intentRegistro);
            }
        });
        editTelefono = findViewById(R.id.edit_tel_contacto);
        editEmail = findViewById(R.id.edit_email_contacto);
        editDireccion = findViewById(R.id.edit_dire_contacto);
        editDetalle = findViewById(R.id.edit_detalle_contacto);
        botonAgregar = findViewById(R.id.boton_agregar_contacto);
        botonAgregar.setOnClickListener(this);
    }

    @Override
    public void onClick (View view){
        final int tel = Integer.parseInt(editTelefono.getText().toString());
        final String email = editEmail.getText().toString();
        final String dire = editDireccion.getText().toString();
        final String detalle = editDetalle.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if(success){
                        Intent intent = new Intent(AddContactoActivity.this, MainTabbedActivity.class);
                        AddContactoActivity.this.startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Contacto agregado!", Toast.LENGTH_LONG).show();
                    }else {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AddContactoActivity.this);
                        alertBuilder.setMessage("Error al agregar contacto").setNegativeButton("Reintentar", null).create().show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        PedidoDeContacto pedido = new PedidoDeContacto(tel, email, dire, detalle, responseListener);
        RequestQueue queue = Volley.newRequestQueue(AddContactoActivity.this);
        queue.add(pedido);
    }
}
