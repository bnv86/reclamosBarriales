package com.example.bruno.debarrio.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.bruno.debarrio.HTTP.HttpServices;
import com.example.bruno.debarrio.HTTP.WebService;
import com.example.bruno.debarrio.MainActivity;
import com.example.bruno.debarrio.MainActivity2;
import com.example.bruno.debarrio.MapActivity;
import com.example.bruno.debarrio.R;
import com.example.bruno.debarrio.entidades.EnviarMail;
import com.example.bruno.debarrio.entidades.Reclamo;
import com.example.bruno.debarrio.entidades.Respuesta;
import com.example.bruno.debarrio.entidades.Save;
import com.example.bruno.debarrio.interfaces.ComunicacionFragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;

import static android.app.ProgressDialog.show;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetalleRespuestaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetalleRespuestaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetalleRespuestaFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    NotificationCompat.Builder mBuilder;
    int mNotificationID = 001;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String UPLOAD_URL_ESTADO = "https://momentary-electrode.000webhostapp.com/postEstadoReclamo.php";
    private String POST_URL_SUSCRIPTOS = "https://momentary-electrode.000webhostapp.com/getSubscripciones.php";
    private String DELETE_SUSCRIPCIONES = "https://momentary-electrode.000webhostapp.com/deleteSuscripciones.php";
    private String DELETE_PUBLICACION = "https://momentary-electrode.000webhostapp.com/deleteRespuesta.php";


    private OnFragmentInteractionListener mListener;
    TextView textUsuario, textCategoria, textComentario, textEstado, textFecha, textSuscriptos; //, textID
    ImageView imagenDetalle, botonEliminar;
    Button botonFloat;
    private String KEY_ID_RESPUESTA = "id";
    private String KEY_ID = "id";
    private String KEY_ID_RECLAMO = "id_reclamo";
    private String KEY_ESTADO = "id_estado";
    StringRequest peticion;
    boolean flag = false;
    private TreeMap<String, String> descrip;
    Activity activity;
    ComunicacionFragments interfaceComunicacionFragments;
    ListaReclamosFragment listaReclamosFragment;
    DetalleReclamoFragment detalleReclamoFragment;
    FragmentManager fm;
    public int PICK_IMAGE_REQUEST = 1;

    public DetalleRespuestaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetalleReclamoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetalleRespuestaFragment newInstance(String param1, String param2) {
        DetalleRespuestaFragment fragment = new DetalleRespuestaFragment();
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
        //getActivity().onBackPressed();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            return true;
            //Acción
        }
        return false;
    }

    public void callParentMethod(){
        getActivity().onBackPressed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View vista = inflater.inflate(R.layout.fragment_respuestas, container, false);
        //getActivity().onBackPressed();
        textUsuario = (TextView) vista.findViewById(R.id.text_usuario);
        textFecha = (TextView) vista.findViewById(R.id.text_fecha);
        textEstado = (TextView) vista.findViewById(R.id.text_estado_respuesta);

        botonEliminar = vista.findViewById(R.id.boton_eliminar_respuesta);
        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setMessage(getResources().getString(R.string.eliminar_publicacion))
                        .setPositiveButton(getResources().getString(R.string.str_confirmar),  new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                eliminarRespuesta();
                                Intent intent = new Intent(getContext(), MainActivity2.class);
                                getActivity().startActivity(intent);
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
        });
        //textCategoria = (TextView) vista.findViewById(R.id.detalle_categoria);
        textComentario = vista.findViewById(R.id.text_comentario_respuesta);
        imagenDetalle = (ImageView) vista.findViewById(R.id.imagen_para_foto);
        imagenDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), getResources().getString(R.string.mantener_boton), Toast.LENGTH_LONG).show();            }
        });

        imagenDetalle.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                confirmDialog();
                return true;
            }
        });


        Bundle bundleObjeto = getArguments();
        Respuesta respuesta = null;
        if (bundleObjeto != null){
            respuesta = (Respuesta) bundleObjeto.getSerializable("objeto2");
            //textIDRespuesta.setText(respuesta.getId());
            imagenDetalle.setImageBitmap(respuesta.getImagenDesc());
            textUsuario.setText(respuesta.getId_usuario());
            textFecha.setText(respuesta.getFecha());
            textEstado.setText(respuesta.getId_estado());
            //textCategoria.setText(respuesta.getId_categoria());
            textComentario.setText(respuesta.getComentario());
            //guardo el id de la respuesta  para usar en el detalle
            String id_respuesta = respuesta.getId();
            String id_reclamo = respuesta.getId_reclamo();
            String id_usuario = respuesta.getId_usuario();
            //String id_categoria = respuesta.getId_categoria();
            String id_estado = respuesta.getId_estado();
            String fecha = respuesta.getFecha();
            String comentario = respuesta.getComentario();
            SharedPreferences prefRespuesta = getContext().getSharedPreferences("respuesta", getActivity().MODE_PRIVATE);
            SharedPreferences.Editor editor1 = prefRespuesta.edit();
            editor1.putString("id_respuesta", id_respuesta);
            editor1.putString("id_reclamo", id_reclamo);
            editor1.putString("id_usuario", id_usuario);
            //editor1.putString("id_categoria", id_categoria);
            editor1.putString("id_estado", id_estado);
            editor1.putString("fecha", fecha);
            editor1.putString("comentario", comentario);
            editor1.commit();
        }
        return vista;
    }

    private void eliminarRespuesta() {
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        SharedPreferences prefRespuesta = getContext().getSharedPreferences("respuesta", getActivity().MODE_PRIVATE);
        final String id_respuesta = prefRespuesta.getString("id_respuesta","");
        final ProgressDialog loading = ProgressDialog.show(getActivity(),getResources().getString(R.string.str_eliminando),getResources().getString(R.string.str_espere),false,false); //getActivity()
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELETE_PUBLICACION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();
                        Toast.makeText(getActivity(), getResources().getString(R.string.publicacion_eliminada), Toast.LENGTH_LONG).show();
                        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();
                        Toast.makeText(getActivity(), getResources().getString(R.string.sin_conexion) , Toast.LENGTH_LONG).show();
                        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creación de parámetros
                Map<String,String> params = new Hashtable<String, String>();
                //Agregando de parámetros
                params.put(KEY_ID, id_respuesta);
                //Parámetros de retorno
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Creación de una cola de solicitudes
        RequestQueue requestQ = Volley.newRequestQueue(getContext()); //getActivity()
        //Agregar solicitud a la cola
        requestQ.add(stringRequest);
    }

    //Obtiene la cantidad de suscriptores del reclamo
    private void getSubscripcionesReclamo(){
        Reclamo reclamo2 = null;
        Bundle bundleObjeto2 = getArguments();
        reclamo2 = (Reclamo) bundleObjeto2.getSerializable("objeto");
        final String id = reclamo2.getId();
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
                                textSuscriptos.setText(cantidad);
                                SharedPreferences prefReclamo = getContext().getSharedPreferences("reclamo", getActivity().MODE_PRIVATE);
                                SharedPreferences.Editor editor1 = prefReclamo.edit();
                                editor1.putString("suscriptos", cantidad);
                                editor1.commit();
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
        //rqt.add(peticion);
    }

    public class  GetHttpResponseRespuestas extends AsyncTask<Void,Void,Void> { //NO SE USA POR AHORA, EVALUAR BORRAR

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
            final ProgressDialog loading = show(getContext(),getResources().getString(R.string.str_cargando),getResources().getString(R.string.str_espere),true,false); //getActivity()
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
                            Toast.makeText(getContext(), getResources().getString(R.string.server_error) , Toast.LENGTH_LONG).show();
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
                            for (int i=0; i<jsonArray.length();i++){
                                jsonObject= jsonArray.getJSONObject(i);
                                String reclamoBusqueda = jsonObject.getString("id_reclamo");
                                if (id_reclamo.equals(reclamoBusqueda) || (id_reclamo == reclamoBusqueda)){
                                    flag = true;
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
            if (flag){
                //botonVerRespuestas.setVisibility(View.VISIBLE);
            }
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
        /*
        if (context instanceof Activity) {
            this.activity = (Activity) context;
            interfaceComunicaFragments = (ComunicacionFragments) this.activity;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/

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

    private  void notificationPush() {
        Bundle bundleObjeto2 = getArguments();
        Respuesta respuesta = null;
        if (bundleObjeto2 != null) {
            respuesta = (Respuesta) bundleObjeto2.getSerializable("objeto2"); //TRAIGO LOS DATOS DEL RECLAMO ACTUAL
        }
        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //Intent intentGalleria = new Intent(Intent.ACTION_VIEW, MediaStore.Images.Media.EXTERNAL_CONTENT_URI); ABRE LA GALERIA
        Intent intentGalleria = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI); //ABRO LA CARPETA DONDE SE HALLA LA IMAGEN GUARDADA
        intentGalleria.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //Intent intentGalleria = new Intent();
        //intentGalleria.setType("image/*");
        //intentGalleria.setAction(Intent.ACTION_GET_CONTENT); //android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        //intentGalleria.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.journaldev.com"));
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intentGalleria, 0);
        mBuilder = new NotificationCompat.Builder(getContext()).setSmallIcon(android.R.drawable.ic_menu_gallery)
                .setLargeIcon(respuesta.getImagenDesc()) //TRAIGO LA IMAGEN DEL RECLAMO
                .setContentTitle("Reclamos Municipales").setContentText(getResources().getString(R.string.imagen_guardada) + "/ReclamosMunicipales").setContentIntent(pendingIntent);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(getContext(),
                        0,
                        intentGalleria,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(mNotificationID, mBuilder.build());
    }

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMessage(getResources().getString(R.string.guardar_imagen))
                .setPositiveButton(getResources().getString(R.string.str_confirmar),  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //convertir imagen a bitmap
                        imagenDetalle.buildDrawingCache();
                        Bitmap bmap = imagenDetalle.getDrawingCache();

                        //guardar imagen
                        Save savefile = new Save();
                        savefile.SaveImage(getContext(), bmap);
                        notificationPush();
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
}
