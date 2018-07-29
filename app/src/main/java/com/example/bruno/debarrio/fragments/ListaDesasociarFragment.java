package com.example.bruno.debarrio.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.example.bruno.debarrio.MainTabbedActivity;
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

    ProgressDialog pDialogDes1;
    ProgressDialog pDialogDes2;
    Context context;
    ArrayList<Reclamo> listaReclamosAsociados;
    ArrayList<String> listaAsociados;
    RecyclerView recyclerViewReclamos;
    Activity activity;
    StringRequest peticion;
    ComunicacionFragments interfaceComunicacionFragments;
    String ServerURL = "https://momentary-electrode.000webhostapp.com/getReclamo.php";
    String URLDesasociar = "https://momentary-electrode.000webhostapp.com/deleteAsociacion.php";
    String URLAsociados = "https://momentary-electrode.000webhostapp.com/getAsociacion.php";
    String URLTieneAsociados = "https://momentary-electrode.000webhostapp.com/updateTieneAsociadosReclamo.php";
    String URLRemoveAsociado = "https://momentary-electrode.000webhostapp.com/removeTieneAsociadosReclamo.php";
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
        //getActivity().onBackPressed();

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
            //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            pDialogDes1 = new ProgressDialog(context);
            pDialogDes1.setMessage(getResources().getString(R.string.str_cargando));
            pDialogDes1.setCancelable(true);
            pDialogDes1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialogDes1.show();
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
                            Toast.makeText(getActivity(), getResources().getString(R.string.server_error) , Toast.LENGTH_LONG).show();
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
                pDialogDes1.dismiss();
                //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            }
            else{
                pDialogDes1.dismiss();
                Toast.makeText(context, getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
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
            //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
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
                            Toast.makeText(getActivity(), getResources().getString(R.string.server_error) , Toast.LENGTH_LONG).show();
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
            if(listaReclamosAsociados != null) {
                final AdaptadorReclamos adapter = new AdaptadorReclamos(listaReclamosAsociados, 2);
                recyclerViewReclamos.setAdapter(adapter);
                //pDialog2.dismiss();
                //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                adapter.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        //interfaceComunicacionFragments.enviarReclamo(listaReclamosAsociables.get(recyclerViewReclamos.getChildAdapterPosition(view)));
                        String idreclamoADesasociar = listaReclamosAsociados.get(recyclerViewReclamos.getChildAdapterPosition(view)).getId();
                        int posicion = recyclerViewReclamos.getChildAdapterPosition(view);
                        confirmDialog(idreclamoADesasociar, posicion, adapter);
                        adapter.notifyDataSetChanged();
                    }
                });
                //pDialog2.dismiss();
            }
            else{
                //pDialog2.dismiss();
                Toast.makeText(context, getResources().getString(R.string.sin_conexion), Toast.LENGTH_LONG).show();
                //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            }
        }
    }

    private void confirmDialog(final String idasociado, final int posicion, final AdaptadorReclamos adapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage(getResources().getString(R.string.desasociar_reclamo))
                .setPositiveButton(getResources().getString(R.string.str_confirmar),  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        quitarAsociado(idasociado);
                        listaReclamosAsociados.remove(posicion);
                        recyclerViewReclamos.removeViewAt(posicion);
                        adapter.notifyItemRemoved(posicion);
                        adapter.notifyItemRangeChanged(posicion, listaReclamosAsociados.size());
                        closefragment();
                        //Intent intent = new Intent(getActivity(), MainActivity2.class);
                        //startActivity(intent);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.str_cancelar), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public void quitarAsociado(String idAsociado){ //DELETE FILA EN ASOCIACION
        final String id_reclamo_asociado = idAsociado;
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        SharedPreferences prefReclamo = getContext().getSharedPreferences("reclamo", MODE_PRIVATE);
        final String id_reclamo_original = prefReclamo.getString("id_reclamo","");

        restarTieneAsociados(id_reclamo_original); //SI TIRA ERROR ACA ES UN PROBLEMA
        modificarEsAsociado(id_reclamo_asociado); //SI TIRA ERROR ACA ES UN PROBLEMA

        //Muestro la carga del progreso
        final ProgressDialog loading = ProgressDialog.show(getActivity(),getResources().getString(R.string.str_cargando),getResources().getString(R.string.str_espere),false,false); //getActivity()
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLDesasociar,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();
                        //Mostrando el mensaje de la respuesta
                        Toast.makeText(getActivity(), getResources().getString(R.string.reclamo_desasociado), Toast.LENGTH_LONG).show();
                        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                        //Intent intentVer = new Intent(getActivity(), MainActivity2.class);
                        //getActivity().startActivity(intentVer);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();
                        Toast.makeText(getActivity(), getResources().getString(R.string.reintente_desasociar) , Toast.LENGTH_LONG).show();
                        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                        Intent intent = new Intent(getActivity(), MainActivity2.class);
                        startActivity(intent);
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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Creación de una cola de solicitudes
        RequestQueue requestQueue = Volley.newRequestQueue(getContext()); //getActivity()
        //Agregar solicitud a la cola
        requestQueue.add(stringRequest);
    }

    public void restarTieneAsociados(String idReclamoOriginal){ //RESTA 1 A TIENE_ASOCIADOS
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        final String idOriginal = idReclamoOriginal;
        //final ProgressDialog loading = ProgressDialog.show(getActivity(),getResources().getString(R.string.str_actualizando),getResources().getString(R.string.str_espere),false,false); //getActivity()
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLRemoveAsociado,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Descartar el diálogo de progreso
                        //loading.dismiss();
                        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                        //Toast.makeText(getActivity(), "RECLAMO ACTUALIZADO!", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //loading.dismiss();
                        Toast.makeText(getActivity(), getResources().getString(R.string.str_reintente) , Toast.LENGTH_LONG).show();
                        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                        Intent intent = new Intent(getActivity(), MainActivity2.class);
                        startActivity(intent);
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creación de parámetros
                Map<String,String> params = new Hashtable<String, String>();
                //Agregando de parámetros
                params.put(KEY_ID_RECLAMO, idOriginal);
                //params.put(KEY_VALOR_TIENE, "1"); //RESTAR UNO
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
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        final String idAsociado = idReclamoAsociado;
        final ProgressDialog loading = ProgressDialog.show(getActivity(),getResources().getString(R.string.str_actualizando),getResources().getString(R.string.str_espere),false,false); //getActivity()
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLEsAsociado,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();
                        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                        //Toast.makeText(getActivity(), "RECLAMO DESASOCIADO!", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();
                        Toast.makeText(getActivity(), getResources().getString(R.string.str_reintente) , Toast.LENGTH_LONG).show();
                        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                        Intent intent = new Intent(getActivity(), MainActivity2.class);
                        startActivity(intent);
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creación de parámetros
                Map<String,String> params = new Hashtable<String, String>();
                //Agregando de parámetros
                params.put(KEY_ID_RECLAMO, idAsociado);
                params.put(KEY_VALOR_ES, "0");
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

    private void closefragment() {
        getActivity().getFragmentManager().popBackStack();
    }
}
