package com.example.bruno.debarrio.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.bruno.debarrio.R;
import com.example.bruno.debarrio.entidades.Reclamo;
import com.example.bruno.debarrio.interfaces.ComunicacionFragments;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static android.app.ProgressDialog.show;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RespuestasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RespuestasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RespuestasFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private RespuestaReclamoFragment.OnFragmentInteractionListener mListener;
    private OnFragmentInteractionListener mListener;
    ImageView imagenFoto;
    ArrayList<String> listaRespuestas;
    ArrayList<Bitmap> listaFotos;

    Button botonVer;
    TextView textFecha, textEstado, textComentario, textUsuario;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap bitmap;
    public int PICK_IMAGE_REQUEST = 1;
    Activity activity;
    ComunicacionFragments interfaceComunicacionFragments;
    private String GET_RESPUESTAS = "https://momentary-electrode.000webhostapp.com/getRespuestas.php";
    private String KEY_FECHA = "fecha";
    private String KEY_COMENTARIO = "comentario";
    private String KEY_IMAGEN = "foto_respuesta";
    private String KEY_ID_USUARIO = "id_usuario";
    private String KEY_ID_RECLAMO = "id_reclamo";
    private String KEY_ID_ESTADO = "id_estado";

    public RespuestasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RespuestasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RespuestasFragment newInstance(String param1, String param2) {
        RespuestasFragment fragment = new RespuestasFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_respuestas, container, false);
        final View rootView = inflater.inflate(R.layout.fragment_respuestas, container, false);

        textEstado = (TextView) rootView.findViewById(R.id.text_estado);
        textComentario = (TextView) rootView.findViewById(R.id.text_comentario_respuesta);
        textEstado = (TextView) rootView.findViewById(R.id.text_estado_respuesta);
        textFecha = (TextView) rootView.findViewById(R.id.text_fecha);
        textUsuario = (TextView) rootView.findViewById(R.id.text_usuario);
        imagenFoto = (ImageView) rootView.findViewById(R.id.imagen_para_foto) ;
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.camera);

        Bundle bundleObjeto = getArguments();
        Reclamo reclamo = null;
        if (bundleObjeto != null){
            reclamo = (Reclamo) bundleObjeto.getSerializable("objeto");
            //imagenFoto.setImageBitmap(reclamo.getImagenDesc());
            //textUsuario.setText(reclamo.getId_usuario());
            //textCategoria.setText(reclamo.getId_categoria());
            //textMunicipalidad.setText(reclamo.getMunicipalidad());
            //textComentario.setText(reclamo.getDescripcionDesc());
            //textLatitud.setText(reclamo.getLatitudDesc());
            //textLongitud.setText(reclamo.getLongitudDesc());
            //spinner.setSelection(((ArrayAdapter<String>)spinner.getAdapter()).getPosition(reclamo.getId_estado()));
            //mailReclamo = reclamo.getEmail();
            //asignarInfo(reclamo);

            //guardo el id del reclamo para usar en la respuesta
            String id = reclamo.getId();
            String id_usuario = reclamo.getId_usuario();
            String id_categoria = reclamo.getId_categoria();
            //String suscriptos = reclamo.getCantSuscriptos();
            String id_estado = reclamo.getId_estado();
            String fecha = reclamo.getFecha();
            SharedPreferences prefReclamo = getContext().getSharedPreferences("reclamo", getActivity().MODE_PRIVATE);
            SharedPreferences.Editor editor1 = prefReclamo.edit();
            //editor1.putString("suscriptos", suscriptos);
            editor1.putString("id_reclamo", id);
            editor1.putString("id_usuario", id_usuario);
            editor1.putString("id_categoria", id_categoria);
            editor1.putString("id_estado", id_estado);
            editor1.putString("fecha", fecha);
            editor1.commit();
            //guardo las coordenadas para usar en el boton ubicacion del detalle
            SharedPreferences prefCoord = getContext().getSharedPreferences("coordenadas", getActivity().MODE_PRIVATE);
            SharedPreferences.Editor editor2 = prefCoord.edit();
            editor2.putString("latitud", reclamo.getLatitudDesc());
            editor2.putString("longitud", reclamo.getLongitudDesc());
            editor2.commit();
        }
        listaRespuestas = new ArrayList<>();
        listaFotos = new ArrayList<>();
        GetHttpResponseRespuestas getHttpResponseRespuestas = new GetHttpResponseRespuestas(getContext());
        getHttpResponseRespuestas.execute();

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public class  GetHttpResponseRespuestas extends AsyncTask<Void,Void,Void> {

        String REQUEST_RESPUESTAS = "https://momentary-electrode.000webhostapp.com/getRespuestas.php";
        public Context context;
        String ResultHolder;

        public GetHttpResponseRespuestas(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            final ProgressDialog loading = show(getContext(),"Consultando BD...","Espere por favor...",true,false); //getActivity()
            StringRequest stringRequest = new StringRequest(Request.Method.POST, REQUEST_RESPUESTAS,
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
                            Toast.makeText(getContext(), "Error " , Toast.LENGTH_LONG).show();
                        }
                    });
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //Creación de una cola de solicitudes
            RequestQueue requestQueue = Volley.newRequestQueue(getContext()); //getActivity()
            //Agregar solicitud a la cola
            requestQueue.add(stringRequest);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpServices httpServiceObject = new HttpServices(REQUEST_RESPUESTAS);
            try{
                httpServiceObject.ExecutePostRequest();
                if (httpServiceObject.getResponseCode()==200){
                    ResultHolder= httpServiceObject.getResponse();
                    if (ResultHolder != null){
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(ResultHolder);
                            JSONObject jsonObject;
                            SharedPreferences prefReclamo = getContext().getSharedPreferences("reclamo", getActivity().MODE_PRIVATE);
                            String id_reclamo = prefReclamo.getString("id_reclamo","");
                            listaRespuestas.clear();
                            listaFotos.clear();
                            for (int i=0; i<jsonArray.length();i++){
                                jsonObject= jsonArray.getJSONObject(i);
                                String reclamoBusqueda = jsonObject.getString("id_reclamo");
                                String id_user = jsonObject.getString("id_usuario");
                                String id_estado = jsonObject.getString("id_estado");
                                String fecha = jsonObject.getString("fecha");
                                String comentario = jsonObject.getString("comentario");
                                //String dec = jsonObject.getString("foto_respuesta");
                                //Bitmap foto = downloadImage(dec);
                                if (id_reclamo.equals(reclamoBusqueda) || (id_reclamo == reclamoBusqueda)){
                                    listaRespuestas.add(id_user.toString());
                                    listaRespuestas.add(fecha.toString());
                                    listaRespuestas.add(comentario.toString());
                                    listaRespuestas.add(id_estado.toString());
                                    //listaFotos.add(foto);
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
            if ((listaRespuestas != null) || (listaFotos != null)){
                //Toast.makeText(getContext(), listaRespuestas.get(2) , Toast.LENGTH_LONG).show();
                textUsuario.setText(listaRespuestas.get(0).toString());
                textFecha.setText(listaRespuestas.get(1).toString());
                textComentario.setText(listaRespuestas.get(2).toString());
                textEstado.setText(listaRespuestas.get(3).toString());
                //imagenFoto.setImageBitmap(listaFotos.get(0));
            }
            else{
                Toast.makeText(context, "Sin conexión con el servidor :(", Toast.LENGTH_LONG).show();
            }
            /*
            if (){
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
            }*/
        }
    }

/*
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
    }*/

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

    public static Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        InputStream stream = null;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;

        try {
            stream = getHttpConnection(url);
            bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
            stream.close();
        }
        catch (IOException e1) {
            e1.printStackTrace();
            System.out.println("downloadImage"+ e1.toString());
        }
        return bitmap;
    }

    // Makes HttpURLConnection and returns InputStream

    public static  InputStream getHttpConnection(String urlString)  throws IOException {

        InputStream stream = null;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        try {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("downloadImage" + ex.toString());
        }
        return stream;
    }
}
