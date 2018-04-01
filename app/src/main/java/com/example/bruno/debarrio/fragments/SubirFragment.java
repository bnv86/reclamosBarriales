package com.example.bruno.debarrio.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bruno.debarrio.AddFotoElegirActivity;
import com.example.bruno.debarrio.MainTabbedActivity;
import com.example.bruno.debarrio.MapsActivity;
import com.example.bruno.debarrio.R;
import com.google.android.gms.plus.PlusOneButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link SubirFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SubirFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubirFragment extends Fragment implements View.OnClickListener{ // implements  View.OnClickListener

    /* elegirActivity
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private String UPLOAD_URL ="http://serverandrofast.webcindario.com/upload.php";
    private String KEY_IMAGEN = "foto";
    private String KEY_NOMBRE = "nombre";
    */

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // The request code must be 0 or greater.
    private static final int PLUS_ONE_REQUEST_CODE = 0;
    // The URL to +1.  Must be a valid URL.
    private final String PLUS_ONE_URL = "http://developer.android.com";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PlusOneButton mPlusOneButton;

    private OnFragmentInteractionListener mListener;

    ImageView imagenFoto;
    Button botonSacarFoto, botonElegirFoto, botonUbicacion, botonCompartir;
    EditText editextMotivo, editextComentario;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap bitmap;
    public int PICK_IMAGE_REQUEST = 1;
    private String UPLOAD_URL_FOTO = "https://momentary-electrode.000webhostapp.com/SubirElegirFoto.php";
    private String UPLOAD_URL_EVENTO = "https://momentary-electrode.000webhostapp.com/postEvento.php";
    private String KEY_IMAGEN = "foto";
    private String KEY_NOMBRE = "nombre";
    private String KEY_MOTIVO = "motivo";
    private String KEY_COMENTARIO = "comentario";
    private String KEY_FECHA = "fecha";
    private String KEY_USUARIO = "usuario";

    public SubirFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubirFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubirFragment newInstance(String param1, String param2) {
        SubirFragment fragment = new SubirFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_add_foto_elegir);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }}

    public String getStringImagen(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

        public void subirEvento(){
            final SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //iso8601Format
            SharedPreferences sharedpreferences = getActivity().getSharedPreferences("sesion",getActivity().getApplication().MODE_PRIVATE);
            final String usuario = sharedpreferences.getString("username",""); //ME DEVUELVE EL PASSWORD, NO EL USERNAME, PROBLEMA DEL LOGIN??
            //pref_userName = preferences.getString("pref_userName", "n/a");
            //userName.setText("Welcome to "+pref_userName);
            //String username = getIntent().getStringExtra("username");
            //Mostrar el diálogo de progreso
            final ProgressDialog loading = ProgressDialog.show(getActivity(),"Subiendo...","Espere por favor...",false,false); //getActivity()
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL_EVENTO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();
                        //Mostrando el mensaje de la respuesta
                        //Toast.makeText(getContext(), s , Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), "EVENTO SUBIDO " + usuario + " !", Toast.LENGTH_LONG).show();
                        //llamarIntentFotoElegir();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();

                        //Showing toast
                        //Toast.makeText(getContext(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show(); //getActivity()
                        Toast.makeText(getActivity(), "NO SE SUBIO" , Toast.LENGTH_LONG).show();
                    }
                }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    //Convertir bits a cadena
                    String foto = getStringImagen(bitmap);

                    //Obtener el nombre de la imagen
                    //String nombre = "CAPTURA"; //.trim()
                    String motivo = editextMotivo.getText().toString().trim();
                    String comentario = editextComentario.getText().toString().trim();

                    //Creación de parámetros
                    Map<String,String> params = new Hashtable<String, String>();

                    //Agregando de parámetros
                    params.put(KEY_FECHA, fecha.format(new Date()));
                    params.put(KEY_USUARIO, usuario);
                    params.put(KEY_IMAGEN, foto);
                    //params.put(KEY_NOMBRE, nombre);
                    params.put(KEY_MOTIVO, motivo);
                    params.put(KEY_COMENTARIO, comentario);

                    //Parámetros de retorno
                    return params;
                }
            };

            //stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 6, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //Creación de una cola de solicitudes
            RequestQueue requestQueue = Volley.newRequestQueue(getContext()); //getActivity()
            //Agregar solicitud a la cola
            requestQueue.add(stringRequest);
        }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccione una foto"), PICK_IMAGE_REQUEST);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
            //subirEvento();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subir, container, false);

        botonSacarFoto = rootView.findViewById(R.id.boton_tomar_foto);
        imagenFoto = rootView.findViewById(R.id.imagen_para_foto);
        editextMotivo = rootView.findViewById(R.id.editext_motivo);
        editextComentario = rootView.findViewById(R.id.editext_comentario);
        //String motivo = editextMotivo.getText().toString();
        //String comentario = editextComentario.getText().toString();

        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.camera);

        /*
        botonSacarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarIntentFoto();
            }
        });*/

        botonElegirFoto = rootView.findViewById(R.id.boton_elegir_foto);
        botonElegirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
                //llamarIntentFotoElegir();
                //llamarIntentFoto();
            }
        });

        botonUbicacion = rootView.findViewById(R.id.boton_ubicacion);
        botonUbicacion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                llamarIntentMapa();
            }

        });

        editextMotivo = rootView.findViewById(R.id.editext_motivo);
        editextComentario = rootView.findViewById(R.id.editext_comentario);

        botonCompartir = rootView.findViewById(R.id.boton_compartir);
        botonCompartir.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //uploadImage();
                if(bitmap == null || KEY_MOTIVO == null || KEY_MOTIVO.isEmpty() || KEY_MOTIVO == "" || KEY_COMENTARIO == null || KEY_COMENTARIO.isEmpty() || KEY_COMENTARIO == ""){
                    Toast.makeText(getContext(),"Completa todos los campos, por favor!", Toast.LENGTH_LONG).show();
                }
                /*
                if(bitmap == null){
                    //Toast.makeText(getContext(),"Olvidaste elegir una foto!", Toast.LENGTH_LONG).show();
                }
                if(KEY_MOTIVO == null || KEY_MOTIVO.isEmpty() || KEY_MOTIVO == ""){
                    Toast.makeText(getContext(),"Debes poner el motivo del evento!", Toast.LENGTH_LONG).show();
                    //AlertDialog.Builder alertBuilder1 = new AlertDialog.Builder(getContext());
                    //alertBuilder1.setMessage("Debe completar el campo 'Motivo'").setNegativeButton("Por favor", null).create().show();
                }
                if(KEY_COMENTARIO == null || KEY_COMENTARIO.isEmpty() || KEY_COMENTARIO == ""){
                    Toast.makeText(getContext(),"Debes poner un comentario del evento!", Toast.LENGTH_LONG).show();
                    //AlertDialog.Builder alertBuilder2 = new AlertDialog.Builder(getContext());
                    //alertBuilder2.setMessage("Debe completar el campo 'Comentario'").setNegativeButton("Por favor", null).create().show();
                }*/
                else {
                    subirEvento();
                }
                //llamarIntentCompartir();
            }
        });

        return rootView;
    }


    private void llamarIntentFoto() { //activa la camara para capturar y guardar foto
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            //startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void llamarIntentFotoElegir() {
        //Intent intentElegir = new Intent(getActivity(), AddFotoElegirActivity.class);
        //getActivity().startActivity(intentElegir);
    }

    private void llamarIntentMapa() { //pasa a un activity o fragment map para obtener un marcador
        /*
        Intent intentMaps = new Intent(SubirFragment.this, MapsActivity.class);
        SubirFragment.this.startActivity(intentMaps);*/
        Intent intentMaps = new Intent(getActivity(), MapsActivity.class);
        getActivity().startActivity(intentMaps);

        /* si no funciona lo anterior...
        private final Context context;
        context = itemView.getContext();
        Intent detail = new Intent(context.getApplicationContext(), ImageDetail.class);
        detail.putExtra("id", imagen.getId());
        context.startActivity(detail);*/
    }

    private void llamarIntentCompartir() {
        return;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //si se elige una imagen de la galeria
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //obtener el mapa de bits de la galería
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                //se setea la foto en lugar de la imagenView predeterminada
                imagenFoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*
        //para el mapa
        if(data!=null) {
            //tomo las coordenadas que devuelve el MapsActivity cuando se la llama desde el click
            //en el boton "Zona", ver el código de MapsActivity cuando se le da click al boton R.id.coords
            if (data.getStringExtra("coordLat") != null && !data.getStringExtra("coordLat").equals("")) {
                coordLat = data.getStringExtra("coordLat");
            }
            if (data.getStringExtra("coordLong") != null && !data.getStringExtra("coordLong").equals("")) {
                coordLong = data.getStringExtra("coordLong");
            }
        }*/
    }
/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) { //REQUEST_IMAGE_CAPTURE
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            imagenFoto.setImageBitmap(bitmap);
            //guardarFoto(imageBitmap);
        }

    }*/

   /* subir solo la foto
    private void uploadImage(){
        //Mostrar el diálogo de progreso
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Subiendo...","Espere por favor...",false,false); //getActivity()
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL_FOTO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();
                        //Mostrando el mensaje de la respuesta
                        Toast.makeText(getActivity(), s , Toast.LENGTH_LONG).show();
                        //llamarIntentFotoElegir();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(getActivity(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show(); //getActivity()
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Convertir bits a cadena
                String imagen = getStringImagen(bitmap);

                //Obtener el nombre de la imagen
                String nombre = "CAPTURA"; //.trim()

                //Creación de parámetros
                Map<String,String> params = new Hashtable<String, String>();

                //Agregando de parámetros
                params.put(KEY_IMAGEN, imagen);
                params.put(KEY_NOMBRE, nombre);

                //Parámetros de retorno
                return params;
            }
        };

        //Creación de una cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(getContext()); //getActivity()

        //Agregar solicitud a la cola
        requestQueue.add(stringRequest);
    }*/


}
