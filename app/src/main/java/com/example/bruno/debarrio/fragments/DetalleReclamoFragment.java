package com.example.bruno.debarrio.fragments;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import com.example.bruno.debarrio.HTTP.WebService;
import com.example.bruno.debarrio.MainActivity;
import com.example.bruno.debarrio.MapActivity;
import com.example.bruno.debarrio.R;
import com.example.bruno.debarrio.entidades.EnviarMail;
import com.example.bruno.debarrio.entidades.Reclamo;
import com.example.bruno.debarrio.entidades.Save;
import com.example.bruno.debarrio.interfaces.ComunicacionFragments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetalleReclamoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DetalleReclamoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetalleReclamoFragment extends Fragment{ //implements AdapterView.OnItemSelectedListener    implements View.OnClickListener
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

    private OnFragmentInteractionListener mListener;

    TextView textUsuario, textCategoria, textDescripcion, textMunicipalidad, textFecha, textSuscriptos, textLongitud; //, textID
    String mailReclamo;
    ImageView imagenDetalle;
    Button botonActualizarEstado, botonUbicacion, botonRespuesta;
    //Button botonEnviarMail;
    //Spinner spinner;
    private String KEY_ID = "id";
    private String KEY_ESTADO = "id_estado";
    private String KEY_SUSCRIPTOS;
    StringRequest peticion;
    private RequestQueue rqt;
    private TreeMap<String, String> descrip;
    Activity activity;
    ComunicacionFragments interfaceComunicacionFragments;
    public int PICK_IMAGE_REQUEST = 1;


    public DetalleReclamoFragment() {
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
    public static DetalleReclamoFragment newInstance(String param1, String param2) {
        DetalleReclamoFragment fragment = new DetalleReclamoFragment();
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
        final View vista = inflater.inflate(R.layout.fragment_detalle_reclamos, container, false);
        textUsuario = (TextView) vista.findViewById(R.id.detalle_usuario);
        textCategoria = (TextView) vista.findViewById(R.id.detalle_categoria);
        textMunicipalidad = (TextView) vista.findViewById(R.id.detalle_municipalidad);
        textDescripcion = vista.findViewById(R.id.detalle_descripcion);
        textSuscriptos = vista.findViewById(R.id.detalle_suscriptos);
        getSubscripcionesReclamo();
        //textSuscriptos.setText(getSubscripcionesReclamo());
        //getSuscriptores(KEY_SUSCRIPTOS);
        //textSuscriptos.setText(KEY_SUSCRIPTOS);
        imagenDetalle = (ImageView) vista.findViewById(R.id.imagen_detalle);
        final Spinner spinner = (Spinner) vista.findViewById(R.id.spinner_estado);
        String[] tipos1 = {"Abierto","En curso", "Resuelto","Re-abierto"};
        //spinner.setAdapter(new ArrayAdapter<String>(this, (inflater.inflate(R.layout.fragment_detalle_reclamos, container))), tipos));
        spinner.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, tipos1));
        //ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tipos2);
/*
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.string-estados,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
*/
        //spinner.setAdapter(adapter);
        //spinner.setOnItemSelectedListener(this);

        imagenDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "(Mantenga presionado para guardar la imagen)", Toast.LENGTH_LONG).show();            }
        });

        imagenDetalle.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View arg0) {
                confirmDialog();
                return true;
            }
        });

        botonActualizarEstado = vista.findViewById(R.id.boton_actualizar_estado);
        botonUbicacion = vista.findViewById(R.id.boton_ubicacion_reclamo);
        botonUbicacion.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                llamarIntentMapa();
            }
        });

        botonRespuesta = vista.findViewById(R.id.boton_respuesta_reclamo);
        botonRespuesta.setVisibility(View.GONE);
        botonRespuesta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                llamarIntentRespuesta();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id)
            {
                final String posicion = (String) adapterView.getItemAtPosition(pos);
                botonActualizarEstado.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (posicion.equals("En curso")){
                            EnviarMail enviomail = new EnviarMail(getContext(), mailReclamo, "AppReclamosBarriales", textUsuario.getText().toString() +" el reclamo esta en curso");
                            enviomail.execute();
                        }

                        if (posicion.equals("Resuelto")) {
                            EnviarMail enviomail = new EnviarMail(getContext(), mailReclamo, "AppReclamosBarriales", textUsuario.getText().toString() +" el reclamo fue resuelto");
                            enviomail.execute();
                        }
                        subirEstado(posicion);
                        botonRespuesta.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {    }
        });

        Bundle bundleObjeto = getArguments();
        Reclamo reclamo = null;

        if (bundleObjeto != null){
            reclamo = (Reclamo) bundleObjeto.getSerializable("objeto");
            imagenDetalle.setImageBitmap(reclamo.getImagenDesc());
            textUsuario.setText(reclamo.getId_usuario());
            textCategoria.setText(reclamo.getId_categoria());
            textMunicipalidad.setText(reclamo.getMunicipalidad());
            textDescripcion.setText(reclamo.getDescripcionDesc());
            //textLatitud.setText(reclamo.getLatitudDesc());
            //textLongitud.setText(reclamo.getLongitudDesc());
            spinner.setSelection(((ArrayAdapter<String>)spinner.getAdapter()).getPosition(reclamo.getId_estado()));
            mailReclamo = reclamo.getEmail();
            //asignarInfo(reclamo);

            //guardo el id del reclamo para usar en la respuesta
            String id = reclamo.getId();
            SharedPreferences prefReclamo = getContext().getSharedPreferences("reclamo", getActivity().MODE_PRIVATE);
            SharedPreferences.Editor editor1 = prefReclamo.edit();
            editor1.putString("id_reclamo",id);
            editor1.commit();
            //guardo las coordenadas para usar en el boton ubicacion del detalle
            SharedPreferences prefCoord = getContext().getSharedPreferences("coordenadas", getActivity().MODE_PRIVATE);
            SharedPreferences.Editor editor2 = prefCoord.edit();
            editor2.putString("latitud", reclamo.getLatitudDesc());
            editor2.putString("longitud", reclamo.getLongitudDesc());
            editor2.commit();
        }
        return vista;
    }

    private void llamarIntentRespuesta() {
        // Crea el nuevo fragmento y la transacción.
        RespuestaReclamoFragment fr = new RespuestaReclamoFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.contenedorFragment, fr);
        transaction.addToBackStack(null);
        // Commit a la transacción
        transaction.commit();
    }

public void subirEstado(String pos){
        String estado = "";
        if(pos == "Abierto"){
            estado = "1";
        }
        if(pos == "En curso"){
            estado = "2";
        }
        if(pos == "Resuelto"){
            estado = "3";
        }
        if(pos == "Re-abierto"){
            estado = "4";
        }
        //guardo los datos del estado
        SharedPreferences prefEstado = getContext().getSharedPreferences("estadoReclamo", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefEstado.edit();
        editor.putString("id_estado", estado); //GUARDA EL ID PARA USARLO EN LA RESPUESTA DEL RECLAMO
        editor.putString("estado", pos); //GUARDA EL ESTADO PARA USARLO EN LA RESPUESTA DEL RECLAMO
        editor.commit();

        final String estadoFinal = estado;
        //Bundle bundleObjeto = getArguments();
        //final String id = e.getId();
        Reclamo reclamo2 = null;
        Bundle bundleObjeto2 = getArguments();
        reclamo2 = (Reclamo) bundleObjeto2.getSerializable("objeto");
        final String id = reclamo2.getId();
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Actualizando...","Espere por favor...",false,false); //getActivity()
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL_ESTADO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();
                        Toast.makeText(getActivity(), "ESTADO ACTUALIZADO!", Toast.LENGTH_LONG).show();
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
                params.put(KEY_ID, id);
                params.put(KEY_ESTADO, estadoFinal);
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

    public void getSuscriptores(String key){
        Reclamo reclamo2 = null;
        Bundle bundleObjeto2 = getArguments();
        //if (bundleObjeto2 != null) {
        reclamo2 = (Reclamo) bundleObjeto2.getSerializable("objeto");
        final String id = reclamo2.getId();
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Actualizando...","Espere por favor...",false,false); //getActivity()
        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_URL_SUSCRIPTOS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Descartar el diálogo de progreso
                        loading.dismiss();
                        Toast.makeText(getActivity(), "ESTADO ACTUALIZADO!", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();
                        Toast.makeText(getActivity(), "NO SE PUDO TRAER" , Toast.LENGTH_LONG).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creación de parámetros
                Map<String,String> params = new Hashtable<String, String>();
                //Agregando de parámetros
                params.put(KEY_ID, id);
                params.get(KEY_SUSCRIPTOS);
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

    //Obtiene todas la cantidad de suscriptores del reclamo del reclamo
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
/*
    public void asignarInfo(Reclamo reclamo) {
        imagenDetalle.setImageBitmap(reclamo.getImagenDesc());
        textUsuario.setText(reclamo.getId_usuario());
        textCategoria.setText(reclamo.getId_categoria());
        textMunicipalidad.setText(reclamo.getMunicipalidad());
        textDescripcion.setText(reclamo.getDescripcionDesc());
        textLatitud.setText(reclamo.getLatitudDesc());
        textLongitud.setText(reclamo.getLongitudDesc());
        spinner.setSelection(((ArrayAdapter<String>)spinner.getAdapter()).getPosition(reclamo.getId_estado()));
        mailReclamo = reclamo.getEmail();
    }*/

    private void llamarIntentMapa() { //pasa a un activity o fragment map
        Intent intentMap = new Intent(getActivity(), MapActivity.class);
        startActivity(intentMap);
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
        Bundle bundleObjeto = getArguments();
        Reclamo reclamo = null;
        if (bundleObjeto != null) {
            reclamo = (Reclamo) bundleObjeto.getSerializable("objeto"); //TRAIGO LOS DATOS DEL RECLAMO ACTUAL
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
                .setLargeIcon(reclamo.getImagenDesc()) //TRAIGO LA IMAGEN DEL RECLAMO
                .setContentTitle("Reclamos Municipales").setContentText("Imagen guardada en galeria/ReclamosMunicipales").setContentIntent(pendingIntent);

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

        builder.setMessage("¿Desea guardar la imagen?")
                .setPositiveButton("Si",  new DialogInterface.OnClickListener() {
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
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    /*
    public void showNotification() {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// Podrás mostrar el icono de la notificación, en este caso una alerta
        Notification notification = new Notification(android.R.drawable.stat_sys_warning,
                "Notificación", System.currentTimeMillis());

        CharSequence titulo = "Alerta";

// Clase de Notification
        Intent notificationIntent = new Intent(this, NotificationActivity.class);
        PendingIntent contIntent = PendingIntent.getActivity(this, , notificationIntent, );
        notification.setLatestEventInfo(this, "Aviso de notificación", "Esto es un ejemplo de notificación", contIntent);

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

//importante
        int not_id = 1;
        notificationManager.notify(not_id, notification);
    }*/

}
