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

import com.example.bruno.debarrio.PostsDB.PedidoDeRegistro;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textviewRegresar;
    //EditText editUsuario, editPassword;
    EditText editNombre, editUsuario, editPassword, editEdad;
    Button botonRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        textviewRegresar = findViewById(R.id.textview_regresar);
        textviewRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(); //vuelve al activity anterior
                //Intent intentRegistro = new Intent(RegistroActivity.this, LoginActivity.class);
                //RegistroActivity.this.startActivity(intentRegistro);
            }
        });
        editNombre = findViewById(R.id.edit_nombre_registro);
        editUsuario = findViewById(R.id.edit_usuario_registro);
        editPassword = findViewById(R.id.edit_password_registro);
        editEdad = findViewById(R.id.edit_edad_registro);
        botonRegistrar = findViewById(R.id.boton_registrar_registro);
        //botonRegistrar.setOnClickListener(this);
        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = editNombre.getText().toString();
                final String username = editUsuario.getText().toString();
                final String password = editPassword.getText().toString();
                //int age = Integer.parseInt(editEdad.getText().toString());
                //esto soluciona el error que tira al dejar en blanco campos int al agregar
                final EditText a = findViewById(R.id.edit_edad_registro);
                final String edad = a.getText().toString().trim();
                final int age = !edad.equals("") ? Integer.parseInt(edad) : 0;
            /*
            try {
                int age = Integer.parseInt(editEdad.getText().toString());

            }catch (NumberFormatException n){
                n.printStackTrace();
            }*/
                if (name == null || name == "" || name.isEmpty() || username == null || username == "" || username.isEmpty()
                        || password == null || password == "" || password.isEmpty() || edad == null || edad == "" || edad.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Complete todos los campos!", Toast.LENGTH_LONG).show();
                } else {

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                //if usuario ya existe, mostrar mensaje
                                if (success) {
                                    Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                                    RegistroActivity.this.startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "Usuario " + "'" + username.toString() + "'" + " registrado!", Toast.LENGTH_LONG).show();
                                } else {
                                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(RegistroActivity.this);
                                    alertBuilder.setMessage("Hubo un error al registrar").setNegativeButton("Reintentar", null).create().show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    PedidoDeRegistro pedido = new PedidoDeRegistro(name, username, age, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegistroActivity.this);
                    queue.add(pedido);
                }
            }

        });
    }
    @Override
    public void onClick(View view) {

    }
}

//en caso de: botonRegistrar.setOnClickListener(this);
    /*
    @Override
    public void onClick (View view){
            final String name = editNombre.getText().toString();
            final String username = editUsuario.getText().toString();
            final String password = editPassword.getText().toString();
            //int age = Integer.parseInt(editEdad.getText().toString());
            //esto soluciona el error que tira al dejar en blanco campos int al agregar
            final EditText a = findViewById(R.id.edit_edad_registro);
            final String edad = a.getText().toString().trim();
            final int age = !edad.equals("")?Integer.parseInt(edad) : 0;

            //try {
            //    int age = Integer.parseInt(editEdad.getText().toString());
            //}catch (NumberFormatException n){
            //    n.printStackTrace();
            //}

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        //if usuario ya existe, mostrar mensaje
                        if(success){
                            Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                            RegistroActivity.this.startActivity(intent);
                            Toast.makeText(getApplicationContext(),"Usuario "+"'"+ username.toString() +"'" + " registrado!", Toast.LENGTH_LONG).show();
                        }
                        else {
                            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(RegistroActivity.this);
                            alertBuilder.setMessage("Hubo un error al registrar").setNegativeButton("Reintentar", null).create().show();
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            };

            PedidoDeRegistro pedido = new PedidoDeRegistro(name, username, age, password, responseListener);
            RequestQueue queue = Volley.newRequestQueue(RegistroActivity.this);
            queue.add(pedido);
        }*/
