package com.example.bruno.debarrio.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.example.bruno.debarrio.MainActivity2;
import com.example.bruno.debarrio.MainTabbedActivity;
import com.example.bruno.debarrio.R;
import com.example.bruno.debarrio.entidades.EnviarMail;
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
    Button botonSacarFoto, botonRespuesta, botonActualizarEstado;
    EditText editextComentario;
    String mailReclamo;
    TextView textEstado;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap bitmap;
    public int PICK_IMAGE_REQUEST = 1;
    Activity activity;
    ComunicacionFragments interfaceComunicacionFragments;
    private String UPLOAD_URL_ESTADO = "https://momentary-electrode.000webhostapp.com/postEstadoReclamo.php";
    private String UPLOAD_RESPUESTA_RECLAMO = "https://momentary-electrode.000webhostapp.com/postRespuesta.php";
    private String KEY_FECHA = "fecha";
    private String KEY_COMENTARIO = "comentario";
    private String KEY_IMAGEN = "foto_respuesta";
    private String KEY_ID_USUARIO = "id_usuario";
    private String KEY_ID_RECLAMO = "id_reclamo";
    private String KEY_ID_ESTADO = "id_estado";
    private String KEY_ESTADO = "id_estado";
    private String KEY_ID = "id";

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
        //if (bmp == null){
        //    return null;
        //}
        //else{
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
        //}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_respuesta_reclamo, container, false);

        SharedPreferences prefReclamo = getContext().getSharedPreferences("reclamo", getActivity().MODE_PRIVATE);
        final String estado_id = prefReclamo.getString("id_estado","");
        final String usuario_id = prefReclamo.getString("id_usuario","");

        //SharedPreferences prefEstado = getContext().getSharedPreferences("estadoReclamo", MODE_PRIVATE);
        //String estado = prefEstado.getString("estado","");

        imagenFoto = rootView.findViewById(R.id.imagen_para_foto);
        editextComentario = (EditText) rootView.findViewById(R.id.editext_comentario_respuesta);

        //textEstado = (TextView) rootView.findViewById(R.id.estado_respuesta);

        final Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner_estado);
        //spinner.setSelection(((ArrayAdapter<String>) spinner.getAdapter()).getPosition(id_estado));
        //String[] tipos1 = {getResources().getString(R.string.str_abierto),getResources().getString(R.string.str_encurso), getResources().getString(R.string.str_resuelto),getResources().getString(R.string.str_reabierto)};
        String[] tipos1 = {"Abierto", "En curso", "Resuelto", "Re-abierto"};
        //spinner.setAdapter(new ArrayAdapter<String>(this, (inflater.inflate(R.layout.fragment_detalle_reclamos, container))), tipos));
        spinner.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, tipos1));
        spinner.setSelection(((ArrayAdapter<String>) spinner.getAdapter()).getPosition(estado_id));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id)
            {
                String posi;
                //final String posicion = (String) adapterView.getItemAtPosition(pos);
                if (pos == 0){
                    posi = "1";
                    SharedPreferences prefSpinner = getContext().getSharedPreferences("spinner", getActivity().MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = prefSpinner.edit();
                    editor1.putString("posicion", posi);
                    editor1.commit();
                }
                if (pos == 1){
                    posi = "2";
                    SharedPreferences prefSpinner = getContext().getSharedPreferences("spinner", getActivity().MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = prefSpinner.edit();
                    editor1.putString("posicion", posi);
                    editor1.commit();
                }
                if (pos == 2){
                    posi = "3";
                    SharedPreferences prefSpinner = getContext().getSharedPreferences("spinner", getActivity().MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = prefSpinner.edit();
                    editor1.putString("posicion", posi);
                    editor1.commit();
                }
                if (pos == 3){
                    posi = "4";
                    SharedPreferences prefSpinner = getContext().getSharedPreferences("spinner", getActivity().MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = prefSpinner.edit();
                    editor1.putString("posicion", posi);
                    editor1.commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        Bundle bundleObjeto = getArguments();
        Reclamo reclamo = null;
        if (bundleObjeto != null) {
            reclamo = (Reclamo) bundleObjeto.getSerializable("objeto");
            mailReclamo = reclamo.getEmail();
        }

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
                String coment = editextComentario.getText().toString();
                //String foto = getStringImagen(bitmap);

                /*
                if(bitmap == null || KEY_MOTIVO == null || KEY_MOTIVO.isEmpty() || KEY_MOTIVO == "" || KEY_COMENTARIO == null || KEY_COMENTARIO.isEmpty() || KEY_COMENTARIO == ""){
                    Toast.makeText(getContext(),"Completa todos los campos, por favor!", Toast.LENGTH_LONG).show();
                }*/

                if(coment == null || coment.isEmpty() || coment == ""){
                    Toast.makeText(getContext(),getResources().getString(R.string.debe_escribir_comentario), Toast.LENGTH_LONG).show();
                    //AlertDialog.Builder alertBuilder2 = new AlertDialog.Builder(getContext());
                    //alertBuilder2.setMessage("Debe completar el campo 'Comentario'").setNegativeButton("Por favor", null).create().show();
                }
                else if (bitmap == null){
                    Toast.makeText(getContext(),getResources().getString(R.string.debe_tomar_foto), Toast.LENGTH_LONG).show();
                }

                else {
                    SharedPreferences prefSpinner = getContext().getSharedPreferences("spinner", getActivity().MODE_PRIVATE);
                    final String id_estado = prefSpinner.getString("posicion","");

                    subirRespuesta(id_estado);
                    //closefragment();
                }
            }
        });
        return rootView;
    }

    public void subirRespuesta(final String pos){
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        final SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //iso8601Format
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences("sesion",getActivity().getApplication().MODE_PRIVATE);
        final String usuario = sharedpreferences.getString("username","");
        //final String estadoFinal = pos;

        //Muestro la carga del progreso
        final ProgressDialog loading = ProgressDialog.show(getActivity(),getResources().getString(R.string.str_actualizando),getResources().getString(R.string.str_espere),false,false); //getActivity()
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_RESPUESTA_RECLAMO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();
                        actualizarEstado(pos);
                        //Mostrando el mensaje de la respuesta
                        Toast.makeText(getActivity(), getResources().getString(R.string.publicacion_subida) + " " + usuario + "!", Toast.LENGTH_LONG).show();
                        editextComentario.setText(null);
                        imagenFoto.setImageBitmap(null);
                        //imagenFoto = getView().findViewById(R.id.imagen_para_foto);
                        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();
                        Toast.makeText(getActivity(), getResources().getString(R.string.str_reintente) , Toast.LENGTH_LONG).show();
                        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Convertir bits a cadena
                String foto = getStringImagen(bitmap);
                SharedPreferences prefUsuario = getContext().getSharedPreferences("sesion", MODE_PRIVATE);
                String id_usuario = prefUsuario.getString("id_usuario","");
                SharedPreferences prefReclamo = getContext().getSharedPreferences("reclamo", MODE_PRIVATE);
                String id_reclamo = prefReclamo.getString("id_reclamo","");
                String comentario = editextComentario.getText().toString().trim();

                //Creación de parámetros
                Map<String,String> params = new Hashtable<String, String>();

                //Agregado de parámetros
                params.put(KEY_ID_RECLAMO, id_reclamo);
                params.put(KEY_ID_USUARIO, id_usuario);
                params.put(KEY_ID_ESTADO, pos);
                params.put(KEY_FECHA, fecha.format(new Date()));
                /*
                if (bitmap != null){
                    //String foto = getStringImagen(bitmap);
                    params.put(KEY_IMAGEN, foto);
                    bitmap = null;
                }
                else{
                    //String foto = null;
                    params.put(KEY_IMAGEN, null);
                    bitmap = null;
                }*/
                params.put(KEY_IMAGEN, foto);
                params.put(KEY_COMENTARIO, comentario);

                //Parámetros de retorno
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Creación de una cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(getContext()); //getActivity()
        //Agregar solicitud a la cola
        requestQueue.add(stringRequest);
    }

    public void actualizarEstado(final String pos){
        SharedPreferences prefReclamo = getContext().getSharedPreferences("reclamo", getActivity().MODE_PRIVATE);
        final String id_usuario = prefReclamo.getString("id_usuario","");

        //if (pos.equals("En curso")){
        if (pos.equals("2")){
            EnviarMail enviomail = new EnviarMail(getContext(), mailReclamo, "AppReclamosBarriales", id_usuario.toString() + " " + getResources().getString(R.string.reclamo_encurso));
            enviomail.execute();
        }

        //if (pos.equals("Resuelto")) {
        if (pos.equals("3")) {
            EnviarMail enviomail = new EnviarMail(getContext(), mailReclamo, "AppReclamosBarriales", id_usuario.toString() + " " + getResources().getString(R.string.reclamo_resuelto));
            enviomail.execute();
        }
        //SharedPreferences prefReclamo = getContext().getSharedPreferences("reclamo", MODE_PRIVATE);
        final String id = prefReclamo.getString("id_reclamo","");
        //final ProgressDialog loading = ProgressDialog.show(getActivity(),"Actualizando...","Espere por favor...",false,false); //getActivity()
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL_ESTADO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Descartar el diálogo de progreso
                        //loading.dismiss();
                        //Toast.makeText(getActivity(), "ESTADO ACTUALIZADO! "+id+ " " + estadoFinal, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //loading.dismiss();
                        //Toast.makeText(getActivity(), "NO SE ACTUALIZÓ...REINTENTE" , Toast.LENGTH_LONG).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creación de parámetros
                Map<String,String> params = new Hashtable<String, String>();
                //Agregando de parámetros
                params.put(KEY_ID, id);
                params.put(KEY_ID_ESTADO, pos);
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
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.seleccione_foto)), PICK_IMAGE_REQUEST);
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

    private void closefragment() {
        getActivity().getFragmentManager().popBackStack();
        //getFragmentManager().popBackStackImmediate();
    }
}
