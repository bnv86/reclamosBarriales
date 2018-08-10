package com.example.bruno.debarrio;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import static android.app.ProgressDialog.show;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textviewRegresar;
    EditText editNombre, editUsuario, editPassword, editTelefono, editEmail, editApellido, editMunicipio;
    Button botonRegistrar, botonElegirFoto;
    ImageView imagenFoto;
    Bitmap bitmap;
    public int PICK_IMAGE_REQUEST = 1;
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

        imagenFoto = findViewById(R.id.imagen_para_foto);
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.camera);

        botonElegirFoto = findViewById(R.id.boton_foto_registro);
        botonElegirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
                //llamarIntentFotoElegir();
                //llamarIntentFoto();
            }
        });

        if (validarEmail("")) {
            editEmail.setError(getResources().getString(R.string.email_incorrecto));
        }
        String[] munis = {"Berazategui","Quilmes", "Florencio Varela","La Plata", "San Martin"};
        Spinner spinnerMuni = (Spinner) findViewById(R.id.spinner_municipio);
        spinnerMuni.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, munis));
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
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.complete_campos), Toast.LENGTH_LONG).show();

                    //if (!validarEmail(email)){
                    //editEmail.setError("Email no valido");
                }else{
                    GetHttpResponseUsuarios getHttpResponseUsuarios = new GetHttpResponseUsuarios(getApplicationContext());
                    getHttpResponseUsuarios.execute();
                }
            }

        });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.seleccione_foto)), PICK_IMAGE_REQUEST);
    }

    public String getStringImagen(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private boolean validarEmail(String email){
        return  android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        //Pattern pattern = Patterns.EMAIL_ADDRESS;
        //return pattern.matcher(email).matches();
    }

    public void PostRegister(final int id_rol, final int id_municipio, final String name, final String apellido, final String email, final int phone,
                             final  String municipalidad, final String username, final String password){
        final ProgressDialog loading = show(RegistroActivity.this, getResources().getString(R.string.str_espere),"",true,false);
        String foto = getStringImagen(bitmap);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    //if usuario ya existe, mostrar mensaje
                    if (success) {
                        loading.dismiss();
                        Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                        startActivity(new Intent(getBaseContext(), MainTabbedActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
                        finish(); //de esta manera no se vuelve al register haciendo back en main tabbed, pero vuelve al login
                        RegistroActivity.this.startActivity(intent);
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.str_user) + " '" + username.toString() + "' " + getResources().getString(R.string.str_registrado) + "!", Toast.LENGTH_LONG).show();
                    } else {
                        loading.dismiss();
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(RegistroActivity.this);
                        alertBuilder.setMessage(getResources().getString(R.string.server_error)).setNegativeButton(getResources().getString(R.string.str_reintente), null).create().show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    loading.dismiss();
                }
            }
        };
        PedidoDeRegistro pedido = new PedidoDeRegistro(id_rol, id_municipio, name, apellido, email, phone, municipalidad, foto, username, password, responseListener);
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
            this.context = context;
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
                editUsuario.setError(getResources().getString(R.string.ya_existe));
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.compruebe_usuario), Toast.LENGTH_LONG).show();
                flag1 = false;
            }
            if(flag2){
                //editor1.putBoolean("flag", true).apply();
                //AvisoNoRegistro();
                editTelefono.setError(getResources().getString(R.string.ya_existe));
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.compruebe_telefono), Toast.LENGTH_LONG).show();
                flag2 = false;
            }
            if(flag3){
                //editor1.putBoolean("flag", true).apply();
                //AvisoNoRegistro();
                editEmail.setError(getResources().getString(R.string.ya_existe));
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.compruebe_email), Toast.LENGTH_LONG).show();
                flag3 = false;
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //si se elige una imagen de la galeria
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //obtener el mapa de bits de la galería
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), filePath);
                //se setea la foto en lugar de la imagenView predeterminada
                imagenFoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
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
            final ProgressDialog loading = show(getApplicationContext(),getResources().getString(R.string.str_cargando),getResources().getString(R.string.str_espere),true,false); //getActivity()
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
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_error) , Toast.LENGTH_LONG).show();
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