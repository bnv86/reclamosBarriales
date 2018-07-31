package com.example.bruno.debarrio.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
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
import com.example.bruno.debarrio.HTTP.HttpServices;
import com.example.bruno.debarrio.HTTP.WebService;
import com.example.bruno.debarrio.MainActivity;
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
import java.util.Map;

import static android.app.ProgressDialog.show;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListaReclamosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListaReclamosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaReclamosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    ListaReclamosFragment listaReclamosFragment;
    ListaReclamosFragment listaReclamosFragment2;
    DetalleReclamoFragment detalleReclamoFragment;
    ProgressDialog pDialog;
    Context context;

    ArrayList<Reclamo> listaReclamos;
    RecyclerView recyclerViewReclamos;
    TextView textviewRegresar;
    Activity activity;
    StringRequest peticion;
    ComunicacionFragments interfaceComunicacionFragments;
    ProgressBar progressBarReclamos;
    String ServerURL = "https://momentary-electrode.000webhostapp.com/getReclamo.php";
    //String ServerURL2 = "https://momentary-electrode.000webhostapp.com/getCategoria.php";

    public ListaReclamosFragment() {
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

        //detalleReclamoFragment = new DetalleReclamoFragment();
        //android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
        //fragmentManager.beginTransaction().replace(R.id.contenedorFragment, detalleReclamoFragment).addToBackStack(null).commit();
        //listaReclamosFragment = new ListaReclamosFragment();
        // Crea el nuevo fragmento y la transacción.
        //RespuestaReclamoFragment fr = new RespuestaReclamoFragment();
        //FragmentTransaction transaction = getFragmentManager().beginTransaction();
        //transaction.replace(R.id.contenedorFragment, listaReclamosFragment);
        //transaction.addToBackStack(null);
        // Commit a la transacción
        //transaction.commit();

        //getFragmentManager().beginTransaction().replace(R.id.contenedorFragment, listaReclamosFragment).detach(listaReclamosFragment).attach(listaReclamosFragment).commit();
        //FragmentTransaction ft = getFragmentManager().beginTransaction();
        //ft.detach(this).attach(this).commit();
    }

    @Override
    public void onResume() {
        //getActivity().
        //ListaReclamosFragment listaReclamosFragment = new ListaReclamosFragment();
        //listaReclamosFragment = ListaReclamosFragment.newInstance(index);
        //FragmentTransaction ft = getFragmentManager().beginTransaction();
        //ft.replace(R.layout.fragment_lista_reclamos, listaReclamosFragment);
        //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        //ft.commit();
        //Intent intentVer = new Intent(getActivity(), MainActivity.class);
        //getActivity().startActivity(intentVer);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_lista_reclamos, container, false);
        //getActivity().recreate();
        //boton flotante regresar a pantalla anterior
        /*
        FloatingActionButton botonFloatRegresar = vista.findViewById(R.id.float_regresar);
        botonFloatRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });*/
        recyclerViewReclamos = (RecyclerView) vista.findViewById(R.id.reciclerId);
        recyclerViewReclamos.setLayoutManager(new LinearLayoutManager(getContext()));
        final Spinner spinner = (Spinner) vista.findViewById(R.id.spinner_estado);
        String[] tipos1 = {"Abierto","En curso", "Resuelto","Re-abierto"};
        //spinner.setAdapter(new ArrayAdapter<String>(this, (inflater.inflate(R.layout.fragment_detalle_reclamos, container))), tipos));
        spinner.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, tipos1));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id)
            {
                String posicion = (String) adapterView.getItemAtPosition(pos);
                //Toast.makeText(adapterView.getContext(),(String) adapterView.getItemAtPosition(pos), Toast.LENGTH_SHORT).show();
                llenarlistaEstados(posicion);
                //adapterView.getItemIdAtPosition(3);
                //view.refreshDrawableState();
                //progressBarReclamos.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {    }
        });

        return vista;
    }

    private void llenarlistaEstados(String posicion) {
        listaReclamos = new ArrayList<>();
        new GetHttpResponseEstados(getContext(), posicion).execute();
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

/*
    @Override
    public void onResume() {
        //Log.e("DEBUG", "onResume of lista fragment");
        //listaReclamosFragment = new ListaReclamosFragment();
        //getSupportFragmentManager().beginTransaction().remove(listaReclamosFragment);
        //FragmentTransaction ft = getFragmentManager().beginTransaction();
        //ft.detach(this).attach(this).commit();
        super.onResume();
    }*/

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

    public class GetHttpResponseEstados extends AsyncTask<Void, Void, Void>
    {
        public Context context;
        public String posicion;
        String ResultHolder;

        //List<Subject> eventosList;
        public GetHttpResponseEstados(Context context, String posicion)
        {
            this.context = context;
            this.posicion = posicion;
        }

        @Override
        protected void onPreExecute()
        {
            //listaReclamos.clear();
            //recyclerViewReclamos.removeAllViews();
            //recyclerViewReclamos.removeAllViewsInLayout();
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Cargando Lista");
            pDialog.setCancelable(true);
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.show();
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
                            SharedPreferences prefUsuario = getContext().getSharedPreferences("sesion", MODE_PRIVATE);
                            String id_muni = prefUsuario.getString("id_municipio","");
                            JSONObject jsonObject;

                            for(int i=0; i<jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                String estado = jsonObject.getString("estado");
                                String id_municipio = jsonObject.getString("id_municipio");

                                if ((posicion == "Abierto") && (id_municipio.equals(id_muni)))
                                {
                                    if (estado.equals(posicion)) {
                                        //String usuario = jsonObject.getString("id_usuario");
                                        String username = jsonObject.getString("username");
                                        String id = jsonObject.getString("id");
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
                                        int asociados = jsonObject.getInt("tiene_asociados");
                                        int esAsociado = jsonObject.getInt("es_asociado");
                                        Reclamo reclamo = new Reclamo(id.toString(), nombreCategoria.toString(), username.toString(), estado.toString(), fecha.toString(), foto, foto,
                                                latitud.toString(), longitud.toString(), municipalidad.toString(), descripcion.toString(), mail.toString(), asociados, esAsociado);//(fecha, "motivo", "descripcion", R.drawable.camera, R.drawable.camera);
                                        listaReclamos.add(reclamo); //cantSuscriptos,
                                        posicion = "Abierto";
                                    }
                                    else{
                                        posicion = "Abierto";
                                    }
                                }

                                if ((posicion == "En curso") && (id_municipio.equals(id_muni)))
                                {
                                    if (estado.equals(posicion)) {
                                        //String usuario = jsonObject.getString("id_usuario");
                                        String username = jsonObject.getString("username");
                                        String id = jsonObject.getString("id");
                                        String cantSuscriptos = "0"; //getSubscripcionesReclamo(id);
                                        String dec = jsonObject.getString("foto");
                                        Bitmap foto = downloadImage(dec);
                                        String fecha = jsonObject.getString("fecha");
                                        String latitud = jsonObject.getString("latitud");
                                        String longitud = jsonObject.getString("longitud");
                                        String nombreCategoria = jsonObject.getString("nombre");
                                        String municipalidad = jsonObject.getString("municipalidad");
                                        String descripcion = jsonObject.getString("descripcion");
                                        String mail = jsonObject.getString("email");
                                        int asociados = jsonObject.getInt("tiene_asociados");
                                        int esAsociado = jsonObject.getInt("es_asociado");
                                        Reclamo reclamo = new Reclamo(id.toString(), nombreCategoria.toString(), username.toString(), estado.toString(), fecha.toString(), foto, foto,
                                                latitud.toString(), longitud.toString(), municipalidad.toString(), descripcion.toString(), mail.toString(), asociados, esAsociado);//(fecha, "motivo", "descripcion", R.drawable.camera, R.drawable.camera);
                                        listaReclamos.add(reclamo); // cantSuscriptos,
                                        posicion = "En curso";
                                    }
                                    else{
                                        posicion = "En curso";
                                    }
                                }

                                if ((posicion == "Resuelto") && (id_municipio.equals(id_muni)))
                                {
                                    if (estado.equals(posicion)) {
                                        //String usuario = jsonObject.getString("id_usuario");
                                        String username = jsonObject.getString("username");
                                        String id = jsonObject.getString("id");
                                        String cantSuscriptos = "0"; //getSubscripcionesReclamo(id);
                                        String dec = jsonObject.getString("foto");
                                        Bitmap foto = downloadImage(dec);
                                        String fecha = jsonObject.getString("fecha");
                                        String latitud = jsonObject.getString("latitud");
                                        String longitud = jsonObject.getString("longitud");
                                        String nombreCategoria = jsonObject.getString("nombre");
                                        String municipalidad = jsonObject.getString("municipalidad");
                                        String descripcion = jsonObject.getString("descripcion");
                                        String mail = jsonObject.getString("email");
                                        int asociados = jsonObject.getInt("tiene_asociados");
                                        int esAsociado = jsonObject.getInt("es_asociado");
                                        Reclamo reclamo = new Reclamo(id.toString(), nombreCategoria.toString(), username.toString(), estado.toString(), fecha.toString(), foto, foto,
                                                latitud.toString(), longitud.toString(), municipalidad.toString(), descripcion.toString(), mail.toString(), asociados, esAsociado);//(fecha, "motivo", "descripcion", R.drawable.camera, R.drawable.camera);
                                        listaReclamos.add(reclamo); //cantSuscriptos,
                                        posicion = "Resuelto";
                                    }

                                    else{
                                        posicion = "Resuelto";
                                    }
                                }
                                if ((posicion == "Re-abierto") && (id_municipio.equals(id_muni)))
                                {
                                    if (estado.equals(posicion)) {
                                        //String usuario = jsonObject.getString("id_usuario");
                                        String username = jsonObject.getString("username");
                                        String id = jsonObject.getString("id");
                                        String cantSuscriptos = "0"; //getSubscripcionesReclamo(id);
                                        String dec = jsonObject.getString("foto");
                                        Bitmap foto = downloadImage(dec);
                                        String fecha = jsonObject.getString("fecha");
                                        String latitud = jsonObject.getString("latitud");
                                        String longitud = jsonObject.getString("longitud");
                                        String nombreCategoria = jsonObject.getString("nombre");
                                        String municipalidad = jsonObject.getString("municipalidad");
                                        String descripcion = jsonObject.getString("descripcion");
                                        String mail = jsonObject.getString("email");
                                        int asociados = jsonObject.getInt("tiene_asociados");
                                        int esAsociado = jsonObject.getInt("es_asociado");
                                        Reclamo reclamo = new Reclamo(id.toString(), nombreCategoria.toString(), username.toString(), estado.toString(), fecha.toString(), foto, foto,
                                                latitud.toString(), longitud.toString(), municipalidad.toString(), descripcion.toString(), mail.toString(), asociados, esAsociado);//(fecha, "motivo", "descripcion", R.drawable.camera, R.drawable.camera);
                                        listaReclamos.add(reclamo); //cantSuscriptos,
                                        posicion = "Re-abierto";
                                    }

                                    else{
                                        posicion = "Re-abierto";
                                    }
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
            if(listaReclamos != null) {
                //if (recyclerViewReclamos != null){
                    final AdaptadorReclamos adapter = new AdaptadorReclamos(listaReclamos, 0);
                    //recyclerViewReclamos.removeAllViewsInLayout();
                    //recyclerViewReclamos.removeAllViews();
                    recyclerViewReclamos.setAdapter(adapter);
                    pDialog.dismiss();
                    adapter.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            //Toast.makeText(getContext(), "Seleccionó " + listaReclamos.get(recyclerViewEventos.getChildAdapterPosition(view)).getFecha(), Toast.LENGTH_SHORT).show();
                            interfaceComunicacionFragments.enviarReclamo(listaReclamos.get(recyclerViewReclamos.getChildAdapterPosition(view)));
                            //progressDialog.dismiss();
                            //recyclerViewReclamos.setAdapter(null);
                            //listaReclamos.clear();
                            //listaReclamos.remove(recyclerViewReclamos);
                            //listaReclamos.remove(recyclerViewReclamos.getChildAdapterPosition(view));
                            //listaReclamos.remove(recyclerViewReclamos.getChildAdapterPosition(view));
                        }
                    });

                //}
                /*
                final AdaptadorReclamos adapter = new AdaptadorReclamos(listaReclamos);
                //recyclerViewReclamos.removeAllViewsInLayout();
                //recyclerViewReclamos.removeAllViews();
                recyclerViewReclamos.setAdapter(adapter);
                pDialog.dismiss();
                //loading.dismiss();
                adapter.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        //Toast.makeText(getContext(), "Seleccionó " + listaReclamos.get(recyclerViewEventos.getChildAdapterPosition(view)).getFecha(), Toast.LENGTH_SHORT).show();
                        interfaceComunicacionFragments.enviarReclamo(listaReclamos.get(recyclerViewReclamos.getChildAdapterPosition(view)));
                        //progressDialog.dismiss();
                        //recyclerViewReclamos.setAdapter(null);
                        //listaReclamos.clear();
                        //listaReclamos.remove(recyclerViewReclamos);
                        //listaReclamos.remove(recyclerViewReclamos.getChildAdapterPosition(view));
                        //listaReclamos.remove(recyclerViewReclamos.getChildAdapterPosition(view));
                    }

                });*/
                //listaReclamos.remove(recyclerViewReclamos);
                //recyclerViewReclamos.setAdapter(null);

                /*
                recyclerViewEventos.setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View view) {
                        SparseBooleanArray seleccionados = recyclerViewEventos.getCheckedItemPositions();
                        return true;
                    }
                });*/
            }
            else{
                listaReclamos.clear();
                listaReclamos.remove(recyclerViewReclamos);
                recyclerViewReclamos.setAdapter(null);
                pDialog.dismiss();
                Toast.makeText(context, "Sin conexión con el servidor :(", Toast.LENGTH_LONG).show();
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
                                //textSuscriptos.setText(cantidad);
                                /*
                                if (i == jsonArray.length()){
                                    SharedPreferences prefSusc = getContext().getSharedPreferences("suscriptos", getActivity().MODE_PRIVATE);
                                    SharedPreferences.Editor editor1 = prefSusc.edit();
                                    editor1.putString("cantidad", cantidad);
                                    editor1.commit();
                                }*/
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
        //SharedPreferences pref = getContext().getSharedPreferences("suscriptos", MODE_PRIVATE);
        //String cantSuscriptos = pref.getString("cantidad","");
        //SharedPreferences.Editor editor1 = prefSusc.edit();
        //editor1.remove("cantidad");
        //editor1.clear();
        //editor1.commit();
        //return cantSuscriptos;
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