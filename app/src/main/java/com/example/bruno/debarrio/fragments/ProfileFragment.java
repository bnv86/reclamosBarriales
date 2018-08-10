package com.example.bruno.debarrio.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.bruno.debarrio.MainActivity2;
import com.example.bruno.debarrio.R;
import com.example.bruno.debarrio.entidades.Perfil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static android.app.ProgressDialog.show;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    EditText editNombre, editUsuario, editPassword, editTelefono, editEmail, editApellido, editMunicipio;
    Button botonActualizar, botonCambiarFoto;
    ImageView imagenFoto;
    Bitmap bitmap;
    public int PICK_IMAGE_REQUEST = 1;
    boolean flag1 = false;
    ArrayList<Perfil> listaDatosUser;
    ArrayList<String> lista;
    ArrayList<Bitmap> listaFoto;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        final View vista = inflater.inflate(R.layout.fragment_profile, container, false);

        editNombre = vista.findViewById(R.id.edit_nombre_perfil);
        editApellido = vista.findViewById(R.id.edit_apellido_perfil);
        editEmail = vista.findViewById(R.id.edit_email_perfil);
        editTelefono = vista.findViewById(R.id.edit_telefono_perfil);
        editUsuario = vista.findViewById(R.id.edit_usuario_perfil);
        editPassword = vista.findViewById(R.id.edit_password_perfil);

        imagenFoto = vista.findViewById(R.id.imagen_para_foto);
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.camera);

        botonCambiarFoto = vista.findViewById(R.id.boton_foto_perfil);
        botonCambiarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        if (validarEmail("")) {
            editEmail.setError(getResources().getString(R.string.email_incorrecto));
        }
        GetHttpResponseDatosUser getHttpResponseDatosUser = new GetHttpResponseDatosUser(getContext());
        getHttpResponseDatosUser.execute();

        return vista;
    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.seleccione_foto)), PICK_IMAGE_REQUEST);
    }

    public String getStringImagen(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private boolean validarEmail(String email){
        return  android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        //Pattern pattern = Patterns.EMAIL_ADDRESS;
        //return pattern.matcher(email).matches();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //si se elige una imagen de la galeria
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //obtener el mapa de bits de la galería
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                //se setea la foto en lugar de la imagenView predeterminada
                imagenFoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class  GetHttpResponseDatosUser extends AsyncTask<Void,Void,Void> {

        String REQUEST_USUARIO = "https://momentary-electrode.000webhostapp.com/getUsuario.php";
        public Context context;
        String ResultHolder;

        public GetHttpResponseDatosUser(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            final ProgressDialog loading = show(getContext(),getResources().getString(R.string.str_cargando),getResources().getString(R.string.str_espere),true,false); //getActivity()
            StringRequest stringRequest = new StringRequest(Request.Method.POST, REQUEST_USUARIO,
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
                            Intent intent = new Intent(getActivity(), MainActivity2.class);
                            startActivity(intent);
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
            HttpServices httpServiceObject = new HttpServices(REQUEST_USUARIO);
            try{
                httpServiceObject.ExecutePostRequest();
                if (httpServiceObject.getResponseCode()==200){
                    ResultHolder= httpServiceObject.getResponse();
                    if (ResultHolder != null){
                        JSONArray jsonArray = null;
                        try {
                            //listaDatosUser = new ArrayList<>();
                            lista = new ArrayList<>();
                            listaFoto = new ArrayList<>();
                            jsonArray = new JSONArray(ResultHolder);
                            JSONObject jsonObject;
                            SharedPreferences sharedpreferences = getActivity().getSharedPreferences("sesion", getActivity().getApplication().MODE_PRIVATE);
                            //SharedPreferences prefReclamo = getContext().getSharedPreferences("reclamo", getActivity().MODE_PRIVATE);
                            String id_usuario = sharedpreferences.getString("id_usuario","");
                            for (int i=0; i<jsonArray.length();i++){
                                jsonObject= jsonArray.getJSONObject(i);
                                String usuarioBusqueda = jsonObject.getString("id");
                                if (id_usuario.equals(usuarioBusqueda) || (id_usuario == usuarioBusqueda)){
                                    //flag1 = true;
                                    String nombre = jsonObject.getString("nombre");
                                    String apellido = jsonObject.getString("apellido");
                                    String username = jsonObject.getString("username");
                                    String password = jsonObject.getString("password");
                                    String dec = jsonObject.getString("foto");
                                    Bitmap foto = downloadImage(dec);
                                    String email = jsonObject.getString("email");
                                    String telefono = jsonObject.getString("telefono");
                                    //Perfil perfil = new Perfil(id_usuario.toString(), nombre.toString(), apellido.toString(), email.toString(), telefono.toString(),
                                    //        username.toString(), password.toString()); //foto.toString()
                                    //listaDatosUser.add(perfil);
                                    lista.add(nombre.toString());
                                    lista.add(apellido.toString());
                                    lista.add(username.toString());
                                    lista.add(password.toString());
                                    lista.add(email.toString());
                                    lista.add(telefono.toString());
                                    listaFoto.add(foto);
                                    break;
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
                    Toast.makeText(context, httpServiceObject.getErrorMessage(), Toast.LENGTH_SHORT).show(); //ESTE ERROR ES EL QUE GENERA PROBLEMAS CON LA DB
                    Intent intent = new Intent(getActivity(), MainActivity2.class); //HAGO INTENT PARA QUE NO PERMITA AL USUARIO ACCIONAR EN LA VISTA
                    startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            if (lista != null){ //listaDatosUser
                editNombre.setText(lista.get(0).toString(), TextView.BufferType.EDITABLE);

                editApellido.setText(lista.get(1).toString(), TextView.BufferType.EDITABLE);
                editEmail.setText(lista.get(2).toString(), TextView.BufferType.EDITABLE);
                editTelefono.setText(lista.get(3).toString(), TextView.BufferType.EDITABLE);
                editUsuario.setText(lista.get(4).toString(), TextView.BufferType.EDITABLE);
                editPassword.setText(lista.get(5).toString(), TextView.BufferType.EDITABLE);
                imagenFoto.setImageBitmap(listaFoto.get(0));
                //String fot = lista.get(6).toString();
                //Bitmap foto = downloadImage(fot);
                //imagenFoto.setImageBitmap(foto);
            }
            else{

            }
        }
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
    }*/

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
}
