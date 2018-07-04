package com.example.bruno.debarrio;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bruno.debarrio.HTTP.HttpServices;
import com.example.bruno.debarrio.PostsDB.PedidoDeRegistro;
import com.example.bruno.debarrio.entidades.Municipio;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static android.app.ProgressDialog.show;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textviewRegresar;
    EditText editNombre, editUsuario, editPassword, editTelefono, editEmail, editApellido, editMunicipio;
    Button botonRegistrar, botonFoto;
    String REQUEST_MUNICIPIO = "https://momentary-electrode.000webhostapp.com/getMunicipio.php";
    ArrayList<String> listaMunis;
    ArrayList<Municipio> listaMunicipio;
    ArrayAdapter<String> comboAdapter;
    boolean flag1 = false;
    boolean flag2 = false;
    boolean flag3 = false;


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
        editApellido = findViewById(R.id.edit_apellido_registro);
        editEmail = findViewById(R.id.edit_email_registro);
        editTelefono = findViewById(R.id.edit_telefono_registro);
        editUsuario = findViewById(R.id.edit_usuario_registro);
        editPassword = findViewById(R.id.edit_password_registro);

        if (validarEmail("")) {
            editEmail.setError("Email incorrecto");
        }
        //new GetHttpResponse(getApplicationContext()).execute();
        //GetHttpResponseUsuarios getHttpResponseUsuarios = new GetHttpResponseUsuarios(getApplicationContext());
        //getHttpResponseUsuarios.execute();
        String[] munis = {"Berazategui","Quilmes", "Florencio Varela","La Plata", "San Martin"};
        Spinner spinnerMuni = (Spinner) findViewById(R.id.spinner_municipio);
        spinnerMuni.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, munis));

        /*
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson(); //Instancia Gson.
        //Obtiene datos (json)
        String objetos = prefs.getString("listaMuni", "");
        //Convierte json  a JsonArray.
        String json = new Gson().toJson(objetos);
        JSONArray jsonArray = new JSONArray(json);
        JSONArray jsonArray = null;
        //Convierte JSONArray a Lista de Objetos!
        Type listType = new TypeToken<ArrayList<JsonArray>>(){}.getType();
        List<JsonArray> listObjetos = new Gson().fromJson(jsonArray, listType);

        // Recuperamos el string guardado
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
        //String savedList = prefs.getString("listaMunis");
        // Esta línea sirve para extraer el tipo correspondiente al listado, necesario para que Gson sepa a qué tiene que convertir
        //Type type = new TypeToken<List<JsonObject>>(){}.getType();
        // Convertimos el string en el listado
        //List<JsonObject> objects = gson.fromJson(savedList, type);

        //comboAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, listaMunis);
        //String[] munis = {};
        //Spinner spinnerMuni = (Spinner) findViewById(R.id.spinner_municipio);
        //llenarSpinner(munis);
        //spinnerMuni.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, listaMunis));
        //spinnerMuni.setAdapter(comboAdapter);*/
        spinnerMuni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id)
            {
                String posicion = (String) adapterView.getItemAtPosition(pos);
                //Toast.makeText(adapterView.getContext(),(String) adapterView.getItemAtPosition(pos), Toast.LENGTH_SHORT).show();
                //llenarlistaEstados(posicion);
                String idMuni = "";
                if(posicion == "Berazategui"){
                    idMuni = "1";
                }
                if(posicion == "Quilmes"){
                    idMuni = "2";
                }
                if(posicion == "Florencio Varela"){
                    idMuni = "3";
                }
                if(posicion == "La Plata"){
                    idMuni = "4";
                }
                if(posicion == "San Martin"){
                    idMuni = "5";
                }
                SharedPreferences prefMuniID = getApplication().getSharedPreferences("municipio", getApplication().MODE_PRIVATE);
                SharedPreferences.Editor editor1 = prefMuniID.edit();
                editor1.putString("id", idMuni);
                editor1.putString("nombre", posicion);

                editor1.commit();
                //subirMuni(posicion);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {    }
        });

        //SharedPreferences prefMuni = getApplicationContext().getSharedPreferences("municipio", MODE_PRIVATE);
        //final String id_muni = prefMuni.getString("id_municipio","");
        botonRegistrar = findViewById(R.id.boton_registrar_registro);
        SharedPreferences prefMuni = getApplication().getSharedPreferences("municipio", MODE_PRIVATE);
        final String id_muni = prefMuni.getString("id","");
        final String municipalidad = prefMuni.getString("nombre","");
        //botonRegistrar.setOnClickListener(this);
        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int id_municipio = !id_muni.equals("") ? Integer.parseInt(id_muni) : 0;;
                String name = editNombre.getText().toString();
                String username = editUsuario.getText().toString().trim();
                String password = editPassword.getText().toString();
                String apellido = editApellido.getText().toString();
                String email = editEmail.getText().toString();
                int id_rol = 2;

                //esto soluciona el error que tira al dejar en blanco campos int al agregar
                EditText t = findViewById(R.id.edit_telefono_registro);
                String telefono = t.getText().toString().trim();
                int phone = !telefono.equals("") ? Integer.parseInt(telefono) : 0;


                if (name == null || name == "" || name.isEmpty() || username == null || username == "" || username.isEmpty()
                        || password == null || password == "" || password.isEmpty()
                        || apellido == null || apellido == "" || apellido.isEmpty()
                        || email == null || email == "" || email.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Complete todos los campos!", Toast.LENGTH_LONG).show();

                    //if (!validarEmail(email)){
                    //editEmail.setError("Email no valido");
                }else{
                    GetHttpResponseUsuarios getHttpResponseUsuarios = new GetHttpResponseUsuarios(getApplicationContext());
                    getHttpResponseUsuarios.execute();
                }
            }

        });
    }

    private boolean validarEmail(String email){
        return  android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        //Pattern pattern = Patterns.EMAIL_ADDRESS;
        //return pattern.matcher(email).matches();
    }

    public void PostRegister(final int id_rol, final int id_municipio, final String name, final String apellido, final String email, final int phone,
                             final  String municipalidad, final String username, final String password){
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
        PedidoDeRegistro pedido = new PedidoDeRegistro(id_rol, id_municipio, name, apellido, email, phone, municipalidad, username, password, responseListener);
        RequestQueue queue = Volley.newRequestQueue(RegistroActivity.this);
        queue.add(pedido);
    }

    private void llenarSpinner(String [] munis) {
        //listaMunis = new ArrayList<>();
        //ProgressDialog.show(getActivity(),"Cargando reclamos...","Espere por favor...",false,false);
        //new GetHttpResponse(getApplicationContext()).execute();
    }

    @Override
    public void onClick(View view) {

    }

    public class  GetHttpResponseUsuarios extends AsyncTask<Void,Void,Void>{

        String REQUEST_USUARIO="https://momentary-electrode.000webhostapp.com/getUsuario.php";
        public Context context;
        String ResultHolder;

        public GetHttpResponseUsuarios(Context context){
            this.context=context;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpServices httpServiceObject = new HttpServices(REQUEST_USUARIO);
            try{
                httpServiceObject.ExecutePostRequest();
                if (httpServiceObject.getResponseCode()==200){
                    ResultHolder= httpServiceObject.getResponse();
                    if (ResultHolder != null){
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(ResultHolder);
                            JSONObject jsonObject;
                            //boolean flag = false;

                            for (int i=0; i<jsonArray.length();i++){
                                jsonObject= jsonArray.getJSONObject(i);
                                String user = jsonObject.getString("username");
                                String tel = jsonObject.getString("telefono");
                                String mail = jsonObject.getString("email");
                                String userText = editUsuario.getText().toString();
                                if (userText.equals(user) || (user == userText)){
                                    flag1 = true;
                                }


                                String telText = editTelefono.getText().toString();
                                if (telText.equals(tel) || (tel == telText)){
                                    flag2 = true;
                                }
                                String mailText = editEmail.getText().toString();
                                if (mailText.equals(mail) || (mail == mailText)){
                                    flag3 = true;
                                }

                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    Toast.makeText(context, httpServiceObject.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result)

        {
            if (!flag1 && !flag2 && !flag3){
                SharedPreferences prefMuni = getApplication().getSharedPreferences("municipio", MODE_PRIVATE);
                final String id_muni = prefMuni.getString("id","");
                final String municipalidad = prefMuni.getString("nombre","");
                //editor1.putBoolean("flag", false).apply();
                //AvisoRegistro();
                int id_municipio = !id_muni.equals("") ? Integer.parseInt(id_muni) : 0;;
                String name = editNombre.getText().toString();
                String username = editUsuario.getText().toString().trim();
                String password = editPassword.getText().toString();
                String apellido = editApellido.getText().toString();
                String email = editEmail.getText().toString();
                int id_rol = 2;

                //esto soluciona el error que tira al dejar en blanco campos int al agregar
                EditText t = findViewById(R.id.edit_telefono_registro);
                String telefono = t.getText().toString().trim();
                int phone = !telefono.equals("") ? Integer.parseInt(telefono) : 0;
                PostRegister(id_rol, id_municipio, name, apellido, email, phone, municipalidad, username, password);

                //Toast.makeText(getApplicationContext(), "REGISTRARRRRR", Toast.LENGTH_LONG).show();
            }
            if(flag1){
                //editor1.putBoolean("flag", true).apply();
                //AvisoNoRegistro();
                editUsuario.setError("Ya existe");
                Toast.makeText(getApplicationContext(), "Compruebe usuario", Toast.LENGTH_LONG).show();
                flag1 = false;
            }
            if(flag2){
                //editor1.putBoolean("flag", true).apply();
                //AvisoNoRegistro();
                editTelefono.setError("Ya existe");
                Toast.makeText(getApplicationContext(), "Compruebe telefono", Toast.LENGTH_LONG).show();
                flag2 = false;
            }
            if(flag3){
                //editor1.putBoolean("flag", true).apply();
                //AvisoNoRegistro();
                editEmail.setError("Ya existe");
                Toast.makeText(getApplicationContext(), "Compruebe Email", Toast.LENGTH_LONG).show();
                flag3 = false;
            }
        }

    }

    private void AvisoNoRegistro() {
        Toast.makeText(getApplicationContext(), "usuario en uso", Toast.LENGTH_LONG).show();
    }

    private void AvisoRegistro() {
        Toast.makeText(getApplicationContext(), "REGISTRARRRRR", Toast.LENGTH_LONG).show();
    }

    public class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        String REQUEST_MUNICIPIO = "https://momentary-electrode.000webhostapp.com/getMunicipio.php";
        public Context context;
        String ResultHolder;
        public GetHttpResponse(Context context, String posicion) //, ArrayList<String> arrayList
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            final ProgressDialog loading = show(getApplicationContext(),"Consultando BD...","Espere por favor...",true,false); //getActivity()
            StringRequest stringRequest = new StringRequest(Request.Method.POST, REQUEST_MUNICIPIO,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            //Descartar el diálogo de progreso
                            loading.dismiss();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            loading.dismiss();
                            Toast.makeText(getApplicationContext(), "Error " , Toast.LENGTH_LONG).show();
                        }
                    });
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //Creación de una cola de solicitudes
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext()); //getActivity()
            //Agregar solicitud a la cola
            requestQueue.add(stringRequest);
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            HttpServices httpServiceObject = new HttpServices(REQUEST_MUNICIPIO);
            final Spinner spinnerMuni = (Spinner) findViewById(R.id.spinner_municipio);
            try
            {
                httpServiceObject.ExecutePostRequest();

                if(httpServiceObject.getResponseCode() == 200)
                {
                    ResultHolder = httpServiceObject.getResponse();

                    if(ResultHolder != null)
                    {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(ResultHolder);
                            JSONObject jsonObject;

                            for(int i=0; i<jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                String nombre = jsonObject.getString("nombre");
                                String id = jsonObject.getString("id");
                                Municipio muni = new Municipio(id.toString(), nombre.toString());
                                listaMunicipio.add(muni);
                                listaMunis.add(nombre);
                            }
                            //Suponiendo tener un Listado de objetos:
                            //List<JsonObject> nombre;
                            //Crea un json a partir de la lista de objetos.
                            //Gson gson = new Gson();
                            //String jsonObjetos = gson.toJson(listaMunis);
                            //Crea preferencia
                            //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            //SharedPreferences.Editor editor = prefs.edit();
                            //Guarda lista de objetos, en formato .json
                            //editor.putString("listaMuni", jsonObjetos);
                            //editor.commit();

                            /*
                            Gson gson = new Gson();
                            String jsonList = gson.toJson(listaMunis);

                            //Crea preferencia
                            SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("listaMunis", jsonList);
                            editor.commit();*/
                            //Guarda lista de objetos, en formato .json
                            /*SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("listaMunis", listaMunis);
                            editor.commit();
                            //prefs.putString("listaMunis", listaMunis);
                            prefs.commit();*/
                            //spinnerMuni.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, listaMunis));

                            //comboAdapter = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_spinner_item, listaMunis);
                            //spinnerMuni.setAdapter(comboAdapter);
                            /*
                            spinnerMuni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id)
                                {
                                    String posicion = (String) adapterView.getItemAtPosition(pos);
                                    //Toast.makeText(adapterView.getContext(),(String) adapterView.getItemAtPosition(pos), Toast.LENGTH_SHORT).show();
                                    //llenarlistaEstados(posicion);
                                    String id_muni = spinnerMuni.getSelectedItem().toString();
                                    SharedPreferences prefMuni = getApplicationContext().getSharedPreferences("municipio", getApplicationContext().MODE_PRIVATE);
                                    SharedPreferences.Editor editor1 = prefMuni.edit();
                                    editor1.putString("id_municipio",id_muni);
                                    editor1.commit();

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent)
                                {    }
                            });*/
                        }
                        catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    Toast.makeText(context, httpServiceObject.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)

        {
            //if(listaMunicipio != null) {
            if(listaMunis != null) {
                Spinner spinnerMuni = (Spinner) findViewById(R.id.spinner_municipio);
                spinnerMuni.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, listaMunis));
                //spinnerMuni.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, listaMunicipio));
                spinnerMuni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id)
                    {
                        String posicion = (String) adapterView.getItemAtPosition(pos);
                        //Toast.makeText(adapterView.getContext(),(String) adapterView.getItemAtPosition(pos), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {    }
                });

            }
        }
    }
}