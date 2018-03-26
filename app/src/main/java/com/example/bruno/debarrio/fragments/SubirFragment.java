package com.example.bruno.debarrio.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bruno.debarrio.AddContactoActivity;
import com.example.bruno.debarrio.AddFotoElegirActivity;
import com.example.bruno.debarrio.MainTabbedActivity;
import com.example.bruno.debarrio.MapsActivity;
import com.example.bruno.debarrio.PedidoDeContacto;
import com.example.bruno.debarrio.PedidoDeFoto;
import com.example.bruno.debarrio.PedidoDeRegistro;
import com.example.bruno.debarrio.R;
import com.example.bruno.debarrio.RegistroActivity;
import com.google.android.gms.plus.PlusOneButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    private int PICK_IMAGE_REQUEST = 1;
    private String UPLOAD_URL ="https://momentary-electrode.000webhostapp.com/upload.php";
    private String KEY_IMAGEN = "foto";
    private String KEY_NOMBRE = "nombre";

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

        private void uploadImage(){
            //Mostrar el diálogo de progreso
            final ProgressDialog loading = ProgressDialog.show(getContext(),"Subiendo...","Espere por favor...",false,false); //getActivity()
            StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();
                        //Mostrando el mensaje de la respuesta
                        Toast.makeText(getContext(), s , Toast.LENGTH_LONG).show();
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
            uploadImage();
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
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.camera);

        botonSacarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarIntentFoto();
            }
        });

        botonElegirFoto = rootView.findViewById(R.id.boton_elegir_foto);
        botonElegirFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarIntentFotoElegir();
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
        editextMotivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return;
            }
        });

        editextComentario = rootView.findViewById(R.id.editext_comentario);
        editextComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return;
            }
        });

        botonCompartir = rootView.findViewById(R.id.boton_compartir);
        botonCompartir.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                    uploadImage();
                //llamarIntentCompartir();
            }
        });

        return rootView;
    }


    private void llamarIntentFoto() { //activa la camara para capturar y guardar foto
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void llamarIntentFotoElegir() {
        Intent intentElegir = new Intent(getActivity(), AddFotoElegirActivity.class);
        getActivity().startActivity(intentElegir);
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
/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                //Cómo obtener el mapa de bits de la Galería
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Configuración del mapa de bits en ImageView
                imagen_foto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            imagenFoto.setImageBitmap(bitmap);
            //guardarFoto(imageBitmap);
        }

    }
/*
    private void guardarFoto(Bitmap imagen) {
        final String nombre = "User";
        final String foto = getStringImagen(imagen);
        //final int evento_id = 1;

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if(success){
                        //Intent intent = new Intent(AddContactoActivity.this, MainTabbedActivity.class);
                        //AddContactoActivity.this.startActivity(intent);
                        Toast.makeText(getContext(),"Foto agregada!", Toast.LENGTH_LONG).show();
                    }else {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                        alertBuilder.setMessage("Error al agregar foto").setNegativeButton("Reintentar", null).create().show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        PedidoDeFoto pedido = new PedidoDeFoto(foto, nombre, responseListener); //evento_id,
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(pedido);
    }*/

    /* boton flotante
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subir, container, false); //predeterminado del fragment

        //Find the +1 button
        mPlusOneButton = (PlusOneButton) view.findViewById(R.id.plus_one_button); //predeterminado del fragment

        return view;
    }*/

    /*
    @Override
    public void onResume() {
        super.onResume();

        // Refresh the state of the +1 button each time the activity receives focus.
        mPlusOneButton.initialize(PLUS_ONE_URL, PLUS_ONE_REQUEST_CODE);
    }*/

}
