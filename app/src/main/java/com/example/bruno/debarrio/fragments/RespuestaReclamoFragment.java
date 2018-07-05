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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bruno.debarrio.R;
import com.example.bruno.debarrio.entidades.Reclamo;
import com.google.android.gms.plus.PlusOneButton;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import android.app.Activity;
import com.example.bruno.debarrio.interfaces.ComunicacionFragments;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RespuestaReclamoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RespuestaReclamoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RespuestaReclamoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ImageView imagenFoto;
    Button botonSacarFoto, botonRespuesta;
    EditText editextComentario;
    TextView textEstado;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap bitmap;
    public int PICK_IMAGE_REQUEST = 1;
    Activity activity;
    ComunicacionFragments interfaceComunicacionFragments;
    private String UPLOAD_RESPUESTA_RECLAMO = "https://momentary-electrode.000webhostapp.com/postRespuesta.php";
    private String KEY_FECHA = "fecha";
    private String KEY_COMENTARIO = "comentario";
    private String KEY_IMAGEN = "foto_respuesta";
    private String KEY_ID_USUARIO = "id_usuario";
    private String KEY_ID_RECLAMO = "id_reclamo";
    private String KEY_ID_ESTADO = "id_estado";
    private String KEY_ESTADO = "estado";
    String[] munis = {};

    public RespuestaReclamoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RespuestaReclamoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RespuestaReclamoFragment newInstance(String param1, String param2) {
        RespuestaReclamoFragment fragment = new RespuestaReclamoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public String getStringImagen(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_respuesta_reclamo, container, false);
        View rootView = inflater.inflate(R.layout.fragment_respuesta_reclamo, container, false);
        SharedPreferences prefEstado = getContext().getSharedPreferences("estadoReclamo", MODE_PRIVATE);
        String estado = prefEstado.getString("estado","");
        imagenFoto = rootView.findViewById(R.id.imagen_para_foto);
        editextComentario = (EditText) rootView.findViewById(R.id.editext_comentario_respuesta);
        textEstado = (TextView) rootView.findViewById(R.id.estado_respuesta);
        textEstado.setText(estado);
        //String comentario = editextComentario.getText().toString();
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.camera);
        botonSacarFoto = rootView.findViewById(R.id.boton_tomar_foto);
        botonSacarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarIntentFoto();
            }
        });
        botonRespuesta = rootView.findViewById(R.id.boton_enviar_respuesta);
        botonRespuesta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                /*
                if(bitmap == null || KEY_MOTIVO == null || KEY_MOTIVO.isEmpty() || KEY_MOTIVO == "" || KEY_COMENTARIO == null || KEY_COMENTARIO.isEmpty() || KEY_COMENTARIO == ""){
                    Toast.makeText(getContext(),"Completa todos los campos, por favor!", Toast.LENGTH_LONG).show();
                }*/

                //if(bitmap == null){
                //    Toast.makeText(getContext(),"Olvidaste tomar una foto!", Toast.LENGTH_LONG).show();
                //}
                if(KEY_COMENTARIO == null || KEY_COMENTARIO.isEmpty() || KEY_COMENTARIO == ""){
                    Toast.makeText(getContext(),"Debes poner un comentario de respuesta!", Toast.LENGTH_LONG).show();
                    //AlertDialog.Builder alertBuilder2 = new AlertDialog.Builder(getContext());
                    //alertBuilder2.setMessage("Debe completar el campo 'Comentario'").setNegativeButton("Por favor", null).create().show();
                }
                else {
                    subirRespuesta();
                }
            }
        });

        return rootView;
    }

    public void subirRespuesta(){
        final SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //iso8601Format
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("sesion",getActivity().getApplication().MODE_PRIVATE);
        final String usuario = sharedpreferences.getString("username",""); //ME DEVUELVE EL PASSWORD, NO EL USERNAME, PROBLEMA DEL LOGIN??
        Intent resultIntent = new Intent();
        //resultIntent.getExtras();

        //Bundle bundle = getArguments();
        //pref_userName = preferences.getString("pref_userName", "n/a");
        //userName.setText("Welcome to "+pref_userName);
        //String username = getIntent().getStringExtra("username");
        //Muestro la carga del progreso
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Subiendo...","Espere por favor...",false,false); //getActivity()
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_RESPUESTA_RECLAMO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();
                        //Mostrando el mensaje de la respuesta
                        //Toast.makeText(getContext(), s , Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), "RESPUESTA SUBIDA " + usuario + " !", Toast.LENGTH_LONG).show();
                        //llamarIntentFotoElegir();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();

                        //Toast.makeText(getContext(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show(); //getActivity()
                        Toast.makeText(getActivity(), "NO SE SUBIO" , Toast.LENGTH_LONG).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Convertir bits a cadena
                String foto = getStringImagen(bitmap);
                SharedPreferences prefUsuario = getContext().getSharedPreferences("sesion", MODE_PRIVATE);
                String id_usuario = prefUsuario.getString("id_usuario","");
                SharedPreferences prefEstado = getContext().getSharedPreferences("estadoReclamo", MODE_PRIVATE);
                String id_estado = prefEstado.getString("id_estado","");
                SharedPreferences prefReclamo = getContext().getSharedPreferences("reclamo", MODE_PRIVATE);
                String id_reclamo = prefReclamo.getString("id_reclamo","");

                //Reclamo reclamo = null;
                //Bundle bundleObjeto = getArguments();
                //if (bundleObjeto2 != null) {
                //reclamo = (Reclamo) bundleObjeto.getSerializable("objeto");
                //String id_reclamo = reclamo.getId();
                //String id_estado = sharedPreferences.getString("id","");
                //Obtener el nombre de la imagen
                //String nombre = "CAPTURA"; //.trim()
                String comentario = editextComentario.getText().toString().trim();

                //Creación de parámetros
                Map<String,String> params = new Hashtable<String, String>();

                //Agregando de parámetros
                params.put(KEY_ID_RECLAMO, id_reclamo);
                params.put(KEY_ID_USUARIO, id_usuario);
                params.put(KEY_ID_ESTADO, id_estado);
                params.put(KEY_FECHA, fecha.format(new Date()));
                params.put(KEY_IMAGEN, foto);
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

    private void llamarIntentFoto() { //activa la camara para capturar y guardar foto
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
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

        if (context instanceof Activity) {
            this.activity = (Activity) context;
            interfaceComunicacionFragments = (ComunicacionFragments) this.activity;
        }

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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            imagenFoto.setImageBitmap(bitmap);
        }
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
        }*/
}
