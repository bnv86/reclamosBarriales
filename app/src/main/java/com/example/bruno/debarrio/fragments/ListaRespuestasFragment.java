package com.example.bruno.debarrio.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
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
import com.example.bruno.debarrio.Adapters.AdaptadorReclamos;
import com.example.bruno.debarrio.Adapters.AdaptadorRespuestas;
import com.example.bruno.debarrio.HTTP.HttpServices;
import com.example.bruno.debarrio.HTTP.WebService;
import com.example.bruno.debarrio.R;
import com.example.bruno.debarrio.entidades.Reclamo;
import com.example.bruno.debarrio.entidades.Respuesta;
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
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListaRespuestasFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListaRespuestasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaRespuestasFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    ProgressDialog pDialogRespuestas;
    Context context;
    ArrayList<Respuesta> listaRespuestas;
    RecyclerView recyclerViewRespuestas;
    Activity activity;
    StringRequest peticion;
    ComunicacionFragments interfaceComunicacionFragments;
    ProgressBar progressBarReclamos;
    String ServerURL = "https://momentary-electrode.000webhostapp.com/getRespuestas1.php";

    public ListaRespuestasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaRespuestasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaRespuestasFragment newInstance(String param1, String param2) {
        ListaRespuestasFragment fragment = new ListaRespuestasFragment();
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
        View vista = inflater.inflate(R.layout.fragment_lista_respuestas, container, false);

        recyclerViewRespuestas = (RecyclerView) vista.findViewById(R.id.reciclerId);
        recyclerViewRespuestas.setLayoutManager(new LinearLayoutManager(getContext()));

        SharedPreferences prefReclamo = getContext().getSharedPreferences("reclamo", getActivity().MODE_PRIVATE);
        String id_reclamo = prefReclamo.getString("id_reclamo","");
        llenarlistaRespuestas(id_reclamo);

        return vista;
    }

    private void llenarlistaRespuestas(String id_reclamo) {
        listaRespuestas = new ArrayList<>();
        new GetHttpResponseRespuestas(getContext(), id_reclamo).execute();
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

        if (context instanceof ListaReclamosFragment.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
        if (context instanceof ListaEstadosFragment.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }else {
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

    public class GetHttpResponseRespuestas extends AsyncTask<Void, Void, Void>
    {
        public Context context;
        public String id_reclamo;
        String ResultHolder;

        public GetHttpResponseRespuestas(Context context, String id_reclamo)
        {
            this.context = context;
            this.id_reclamo = id_reclamo;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            pDialogRespuestas = new ProgressDialog(context);
            pDialogRespuestas.setMessage("Cargando Lista");
            pDialogRespuestas.setCancelable(true);
            pDialogRespuestas.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialogRespuestas.show();
            //final ProgressDialog loading = show(getActivity(),"Cargando reclamos...","Espere por favor...",true,false); //getActivity()
            StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            //Descartar el diálogo de progreso
                            //loading.dismiss();
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
                            JSONObject jsonObject;

                            for(int i=0; i<jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                String reclamo = jsonObject.getString("id_reclamo");

                                    if (reclamo.equals(id_reclamo)) {
                                        //String usuario = jsonObject.getString("id_usuario");
                                        String username = jsonObject.getString("username");
                                        String id = jsonObject.getString("id");
                                        String dec = jsonObject.getString("foto_respuesta");
                                        Bitmap foto = downloadImage(dec);
                                        String fecha = jsonObject.getString("fecha");
                                        String estado = jsonObject.getString("estado");                                        //String id_categoria = jsonObject.getString("id_categoria");
                                        //String nombreCategoria = jsonObject.getString("nombre");
                                        //int id_categoria = jsonObject.getInt("id_categoria");
                                        String comentario = jsonObject.getString("comentario");
                                        Respuesta respuesta = new Respuesta(id.toString(), id_reclamo.toString(), username.toString(), estado.toString(), fecha.toString(), foto, foto, comentario.toString());//(fecha, "motivo", "descripcion", R.drawable.camera, R.drawable.camera);
                                        listaRespuestas.add(respuesta);
                                    }
                                    else{
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
            //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            if(listaRespuestas != null) {
                final AdaptadorRespuestas adapter = new AdaptadorRespuestas(listaRespuestas);
                recyclerViewRespuestas.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                pDialogRespuestas.dismiss();
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                closefragment();
                adapter.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        //Toast.makeText(getContext(), "Seleccionó " + listaReclamos.get(recyclerViewEventos.getChildAdapterPosition(view)).getFecha(), Toast.LENGTH_SHORT).show();
                        interfaceComunicacionFragments.enviarRespuesta(listaRespuestas.get(recyclerViewRespuestas.getChildAdapterPosition(view)));
                    }
                });
            }
            else{
                listaRespuestas.clear();
                listaRespuestas.remove(recyclerViewRespuestas);
                recyclerViewRespuestas.setAdapter(null);
                pDialogRespuestas.dismiss();
                Toast.makeText(context, "Sin conexión con el servidor :(", Toast.LENGTH_LONG).show();
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                closefragment();
            }
        }

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
