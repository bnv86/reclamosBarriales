package com.example.bruno.debarrio.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bruno.debarrio.Adapters.AdaptadorReclamos;
import com.example.bruno.debarrio.HTTP.HttpServices;
import com.example.bruno.debarrio.HTTP.WebService;
import com.example.bruno.debarrio.MainActivity2;
import com.example.bruno.debarrio.R;
import com.example.bruno.debarrio.entidades.Reclamo;
import com.example.bruno.debarrio.interfaces.ComunicacionFragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListaDesasociarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListaDesasociarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaDesasociarFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ProgressDialog pDialog1;
    ProgressDialog pDialog2;
    Context context;
    ArrayList<Reclamo> listaReclamosAsociados;
    ArrayList<String> listaAsociados;
    RecyclerView recyclerViewReclamos;
    Activity activity;
    StringRequest peticion;
    ComunicacionFragments interfaceComunicacionFragments;
    String ServerURL = "https://momentary-electrode.000webhostapp.com/getReclamo.php";
    String URLAsociar = "https://momentary-electrode.000webhostapp.com/postAsociar.php";
    String URLAsociados = "https://momentary-electrode.000webhostapp.com/getAsociacion.php";
    String URLTieneAsociados = "https://momentary-electrode.000webhostapp.com/updateTieneAsociadosReclamo.php";
    String URLEsAsociado = "https://momentary-electrode.000webhostapp.com/updateEsAsociadoReclamo.php";
    private String KEY_ID_RECLAMO_ORIGINAL = "id_reclamo";
    private String KEY_ID_RECLAMO = "id";
    private String KEY_ID_RECLAMO_ASOCIADO = "id_reclamo_asociado";
    private String KEY_VALOR_TIENE = "tiene_asociados";
    private String KEY_VALOR_ES = "es_asociado";

    public ListaDesasociarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaReclamosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaReclamosFragment newInstance(String param1, String param2) {
        ListaReclamosFragment fragment = new ListaReclamosFragment();
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_lista_desasociar, container, false);
        recyclerViewReclamos = (RecyclerView) vista.findViewById(R.id.reciclerId);
        recyclerViewReclamos.setLayoutManager(new LinearLayoutManager(getContext()));

        llenarListaAsociados();
        return vista;
    }

    private void llenarListaAsociados() {
        listaReclamosAsociados = new ArrayList<>();
        listaAsociados = new ArrayList<>();
        //buscarAsociados();
        new GetHttpResponseBuscarAsociados(getContext()).execute();
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

        if (context instanceof ListaEstadosFragment.OnFragmentInteractionListener) {
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
    public void onDestroyView(){
        super.onDestroyView();
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

    public class GetHttpResponseBuscarAsociados extends AsyncTask<Void, Void, Void>
    {
        public Context context;
        String ResultHolder;

        public GetHttpResponseBuscarAsociados(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            pDialog1 = new ProgressDialog(context);
            pDialog1.setMessage("Buscando...espere");
            pDialog1.setCancelable(true);
            pDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog1.show();
            //final ProgressDialog loading = show(getActivity(),"Cargando reclamos...","Espere por favor...",true,false); //getActivity()
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URLAsociados,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            //Descartar el diálogo de progreso
                            //loading.dismiss();
                            //Toast.makeText(getActivity(), "Responde" , Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //loading.dismiss();
                            Toast.makeText(getActivity(), "Error en servidor" , Toast.LENGTH_LONG).show();
                        }
                    });
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //Creación de una cola de solicitudes
            RequestQueue requestQueue = Volley.newRequestQueue(getContext()); //getActivity()
            //Agregar solicitud a la cola
            requestQueue.add(stringRequest);
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            HttpServices httpServiceObject = new HttpServices(URLAsociados);
            try
            {
                httpServiceObject.ExecutePostRequest();

                if((httpServiceObject.getResponseCode() == 200))
                {
                    ResultHolder = httpServiceObject.getResponse();

                    if((ResultHolder != null))
                    {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(ResultHolder);
                            JSONObject jsonObject;
                            SharedPreferences prefReclamo = getContext().getSharedPreferences("reclamo", MODE_PRIVATE);
                            String id_reclamo_original = prefReclamo.getString("id_reclamo","");

                            for(int i=0; i<jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                String id_reclamo = jsonObject.getString("id_reclamo");
                                if (id_reclamo.equals(id_reclamo_original))
                                {
                                    String idReclamoAsociado = jsonObject.getString("id_reclamo_asociado");
                                    listaAsociados.add(idReclamoAsociado);
                                }
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
            if(listaAsociados != null) {

                //PRIMER INTENTO
                /*
                for(int i=0; i<listaAsociados.size(); i++) {
                    new GetHttpResponseDesasociar(getContext(), listaAsociados.get(i).toString()).execute();
                }*/

                //SEGUNDO INTENTO
                for (String object: listaAsociados) {
                    //listaAsociados.get(i);
                    new GetHttpResponseDesasociar(getContext(), object.toString()).execute();
                //listaAsociados.forEach(new GetHttpResponseDesasociar);
                    //new GetHttpResponseDesasociar(getContext(), listaAsociados[i]).execute();
                }
                pDialog1.dismiss();
            }
            else{
                pDialog1.dismiss();
                Toast.makeText(context, "Error GetHttpResponseBuscarAsociados", Toast.LENGTH_LONG).show();
            }
        }
    }

    public class GetHttpResponseDesasociar extends AsyncTask<Void, Void, Void>
    {
        public Context context;
        public String id_asociado;
        String ResultHolder;

        public GetHttpResponseDesasociar(Context context, String id_asociado)
        {
            this.context = context;
            this.id_asociado = id_asociado;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            /*
            pDialog2 = new ProgressDialog(context);
            pDialog2.setMessage("Cargando Lista");
            pDialog2.setCancelable(true);
            pDialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog2.show();*/
            //final ProgressDialog loading = show(getActivity(),"Cargando reclamos...","Espere por favor...",true,false); //getActivity()
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            //Descartar el diálogo de progreso
                            //loading.dismiss();
                            //pDialog2.dismiss();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //loading.dismiss();
                            Toast.makeText(getActivity(), "Error en servidor" , Toast.LENGTH_LONG).show();
                        }
                    });
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //Creación de una cola de solicitudes
            RequestQueue requestQueue = Volley.newRequestQueue(getContext()); //getActivity()
            //Agregar solicitud a la cola
            requestQueue.add(stringRequest);
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            HttpServices httpServiceObject = new HttpServices(ServerURL);
            try
            {
                httpServiceObject.ExecutePostRequest();

                if((httpServiceObject.getResponseCode() == 200))
                {
                    ResultHolder = httpServiceObject.getResponse();

                    if((ResultHolder != null))
                    {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(ResultHolder);
                            SharedPreferences prefUsuario = getContext().getSharedPreferences("sesion", MODE_PRIVATE);
                            String id_muni = prefUsuario.getString("id_municipio","");
                            JSONObject jsonObject;

                            for(int i=0; i<jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                String estado = jsonObject.getString("estado");
                                String id_municipio = jsonObject.getString("id_municipio");
                                int asociados = jsonObject.getInt("tiene_asociados");
                                int esAsociado = jsonObject.getInt("es_asociado");
                                String id = jsonObject.getString("id");

                                if (id_municipio.equals(id_muni) && (id.equals(id_asociado)))
                                {
                                    //String usuario = jsonObject.getString("id_usuario");
                                    String username = jsonObject.getString("username");
                                    String cantSuscriptos = "0"; //getSubscripcionesReclamo(id);
                                    String dec = jsonObject.getString("foto");
                                    Bitmap foto = downloadImage(dec);
                                    String fecha = jsonObject.getString("fecha");
                                    String latitud = jsonObject.getString("latitud");
                                    String longitud = jsonObject.getString("longitud");
                                    //String id_categoria = jsonObject.getString("id_categoria");
                                    String nombreCategoria = jsonObject.getString("nombre");
                                    //int id_categoria = jsonObject.getInt("id_categoria");
                                    String municipalidad = jsonObject.getString("municipalidad");
                                    String descripcion = jsonObject.getString("descripcion");
                                    String mail = jsonObject.getString("email");

                                    Reclamo reclamo = new Reclamo(id.toString(), nombreCategoria.toString(), username.toString(), estado.toString(), fecha.toString(), foto, foto,
                                            latitud.toString(), longitud.toString(), municipalidad.toString(), descripcion.toString(), mail.toString(), cantSuscriptos, asociados, esAsociado);
                                    listaReclamosAsociados.add(reclamo);
                                }
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
            //pDialog2.dismiss();
            if(listaReclamosAsociados != null) {

                final AdaptadorReclamos adapter = new AdaptadorReclamos(listaReclamosAsociados);
                recyclerViewReclamos.setAdapter(adapter);

                adapter.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        //interfaceComunicacionFragments.enviarReclamo(listaReclamosAsociables.get(recyclerViewReclamos.getChildAdapterPosition(view)));
                        String idreclamoADesasociar = listaReclamosAsociados.get(recyclerViewReclamos.getChildAdapterPosition(view)).getId();
                        confirmDialog(idreclamoADesasociar);
                    }
                });
                //pDialog2.dismiss();
            }
            else{
                //pDialog2.dismiss();
                Toast.makeText(context, "Sin conexión con el servidor :(", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void buscarAsociados(){

        SharedPreferences prefReclamo = getContext().getSharedPreferences("reclamo", MODE_PRIVATE);
        final String id_reclamo_original = prefReclamo.getString("id_reclamo","");

        //Muestro la carga del progreso
        //final ProgressDialog loading = ProgressDialog.show(getActivity(),"Subiendo...","Espere por favor...",false,false); //getActivity()
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLAsociar,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Descartar el diálogo de progreso
                        //loading.dismiss();
                        //Mostrando el mensaje de la respuesta
                        //Intent intentVer = new Intent(getActivity(), MainActivity2.class);
                        //getActivity().startActivity(intentVer);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Descartar el diálogo de progreso
                        //loading.dismiss();
                        //Toast.makeText(getActivity(), "NO SE ASOCIÓ, REINTENTE..." , Toast.LENGTH_LONG).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creación de parámetros
                Map<String,String> params = new Hashtable<String, String>();

                //Agregado de parámetros
                params.put(KEY_ID_RECLAMO_ORIGINAL, id_reclamo_original);

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

    private void confirmDialog(final String idasociado) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage("¿Desea desasociar el reclamo?")
                .setPositiveButton("Si",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //subirAsociado(idasociado);
                        Toast.makeText(getActivity(), "HACER ALGO" , Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void subirAsociado(String idAsociado){
        final String id_reclamo_asociado = idAsociado;

        SharedPreferences prefReclamo = getContext().getSharedPreferences("reclamo", MODE_PRIVATE);
        final String id_reclamo_original = prefReclamo.getString("id_reclamo","");

        modificarTieneAsociados(id_reclamo_original);
        modificarEsAsociado(id_reclamo_asociado);

        //Muestro la carga del progreso
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Subiendo...","Espere por favor...",false,false); //getActivity()
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLAsociar,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();
                        //Mostrando el mensaje de la respuesta
                        //Toast.makeText(getContext(), s , Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), "RECLAMO ASOCIADO!", Toast.LENGTH_LONG).show();
                        Intent intentVer = new Intent(getActivity(), MainActivity2.class);
                        getActivity().startActivity(intentVer);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();
                        //Toast.makeText(getContext(), volleyError.getMessage().toString(), Toast.LENGTH_LONG).show(); //getActivity()
                        Toast.makeText(getActivity(), "NO SE ASOCIÓ, REINTENTE..." , Toast.LENGTH_LONG).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Creación de parámetros
                Map<String,String> params = new Hashtable<String, String>();

                //Agregado de parámetros
                params.put(KEY_ID_RECLAMO_ORIGINAL, id_reclamo_original);
                params.put(KEY_ID_RECLAMO_ASOCIADO, id_reclamo_asociado);

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

    //Obtiene la cantidad de suscriptores del reclamo
    private void getSubscripcionesReclamo(final String id){ //private String getSubscripcionesReclamo(final String id)
        peticion = new StringRequest(Request.Method.POST, WebService.urlGetSubscripciones,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("Respuesta servidor", response);
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject subsJson = jsonArray.getJSONObject(i);
                                int cantSubs = subsJson.getInt("COUNT(*)");
                                String cantidad = subsJson.getString("COUNT(*)");
                                Log.d("Cantidad de suscriptos",String.valueOf(cantSubs));
                                //textSuscriptos.setText(cantidad);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error_servidor", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> parametros = new HashMap<>();
                parametros.put("id_reclamo", id);
                parametros.put("subscriptores","2");
                return parametros;
            }
        };
        peticion.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Creación de una cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(getContext()); //getActivity()
        //Agregar solicitud a la cola
        requestQueue.add(peticion);
    }

    public void modificarTieneAsociados(String idReclamoOriginal){

        final String idOriginal = idReclamoOriginal;
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Actualizando...","Espere por favor...",false,false); //getActivity()
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLTieneAsociados,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();
                        Toast.makeText(getActivity(), "RECLAMO ACTUALIZADO!", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();
                        Toast.makeText(getActivity(), "NO SE ACTUALIZÓ...REINTENTE" , Toast.LENGTH_LONG).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creación de parámetros
                Map<String,String> params = new Hashtable<String, String>();
                //Agregando de parámetros
                params.put(KEY_ID_RECLAMO, idOriginal);
                params.put(KEY_VALOR_TIENE, "1");
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

    public void modificarEsAsociado(String idReclamoAsociado){

        final String idAsociado = idReclamoAsociado;
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Actualizando...","Espere por favor...",false,false); //getActivity()
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLEsAsociado,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();
                        Toast.makeText(getActivity(), "RECLAMO ASOCIADO!", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();
                        Toast.makeText(getActivity(), "NO SE ASOCIÓ...REINTENTE" , Toast.LENGTH_LONG).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creación de parámetros
                Map<String,String> params = new Hashtable<String, String>();
                //Agregando de parámetros
                params.put(KEY_ID_RECLAMO, idAsociado);
                params.put(KEY_VALOR_ES, "1");
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
