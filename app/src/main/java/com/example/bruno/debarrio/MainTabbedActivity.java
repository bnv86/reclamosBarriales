package com.example.bruno.debarrio;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bruno.debarrio.HTTP.HttpServices;
import com.example.bruno.debarrio.entidades.Reclamo;
import com.example.bruno.debarrio.entidades.Respuesta;
import com.example.bruno.debarrio.fragments.*;
import com.example.bruno.debarrio.fragments.dummy.DummyContent;
import com.example.bruno.debarrio.interfaces.ComunicacionFragments;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Locale;

import static android.app.ProgressDialog.show;

public class MainTabbedActivity extends AppCompatActivity implements ReclamosFragment.OnListFragmentInteractionListener, ListaReclamosFragment.OnFragmentInteractionListener,
        DetalleReclamoFragment.OnFragmentInteractionListener, RespuestaReclamoFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener, ListaEstadosFragment.OnFragmentInteractionListener, ComunicacionFragments{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private Locale locale;
    private Configuration config = new Configuration();
    //ListaReclamosFragment listaReclamosFragment;
    ListaEstadosFragment listaEstadosFragment;
    DetalleReclamoFragment detalleReclamoFragment;
    ImageView imagenFoto;
    Bitmap bitmap;
    ArrayList<Bitmap> listaFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabbed);
        //listaReclamosFragment = new ListaReclamosFragment();
        listaEstadosFragment = new ListaEstadosFragment();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        imagenFoto = findViewById(R.id.imagen_appbar);
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.camera);

        //GetHttpResponseDatosUser getHttpResponseDatosUser = new GetHttpResponseDatosUser(getApplicationContext());
        //getHttpResponseDatosUser.execute();
        //reejecutarGetHttpResponseDatosUser();

        /*
        SharedPreferences sharedpreferences = getSharedPreferences("sesion", getApplication().MODE_PRIVATE);
        String foto_usuario = sharedpreferences.getString("foto_usuario","");
        Bitmap foto = downloadImage(foto_usuario);
        imagenFoto.setImageBitmap(foto);*/


        /* boton flotante de correo
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportFragmentManager().beginTransaction().replace(R.id.container, listaEstadosFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_tabbed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*
        if (id == R.id.action_profile) {
            //llamarIntentProfile();
            ProfileFragment profileFragment;
            profileFragment = new ProfileFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
        }*/

        if (id == R.id.action_languaje) {
            mostrarDialog();
            /*
            Intent intent = new Intent(getApplicationContext(), IdiomaActivity.class);
            startActivity(intent);*/
        }
        if (id == R.id.action_logout) { //cierra sesion
            SharedPreferences sharedPreferences = getSharedPreferences("sesion",MODE_PRIVATE); //toma la sesion actual del usuario
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.remove("usuario"); //cierra la sesion del usuario
            edit.commit();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.str_logout), Toast.LENGTH_LONG).show();
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void llamarIntentProfile() {

        Intent intentMaps = new Intent(MainTabbedActivity.this, ProfileFragment.class);
        MainTabbedActivity.this.startActivity(intentMaps);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /*
    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }*/

    @Override
    public void enviarReclamo(Reclamo reclamo) {
        detalleReclamoFragment = new DetalleReclamoFragment();
        Bundle bundleEnvio = new Bundle();
        bundleEnvio.putSerializable("objeto", reclamo);
        detalleReclamoFragment.setArguments(bundleEnvio);
        //cargar el fragment en el activity
        getSupportFragmentManager().beginTransaction().replace(R.id.container, detalleReclamoFragment).addToBackStack(null).commit();
    }

    @Override
    public void enviarRespuesta(Respuesta respuesta) {
        detalleReclamoFragment = new DetalleReclamoFragment();
        Bundle bundleEnvio = new Bundle();
        bundleEnvio.putSerializable("objeto2", respuesta);
        detalleReclamoFragment.setArguments(bundleEnvio);
        //cargar el fragment en el activity
        getSupportFragmentManager().beginTransaction().replace(R.id.container, detalleReclamoFragment).addToBackStack(null).commit();
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static Fragment newInstance(int sectionNumber) {
            //PlaceholderFragment fragment = new PlaceholderFragment(); //DEFECTO
            Fragment fragment = null;
            switch (sectionNumber){
                //case 1:
                //    fragment = new SubirFragment();
                //    break;
                case 1:
                    fragment = new ReclamosFragment();
                    //fragment = new ListaReclamosFragment();
                    break;
                case 2:
                    fragment = new ProfileFragment();
                    break;
            }
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            //fragment.setArguments(args);  //DEFECTO
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_tabbed, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }

    private void mostrarDialog() { //
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(getResources().getString(R.string.seleccion_idioma));
        //obtiene los idiomas del array de string.xml
        String[] types = getResources().getStringArray(R.array.languages);
        b.setItems(types, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                switch (which) {
                    case 0:
                        locale = new Locale("en");
                        config.locale = locale;
                        break;
                    case 1:
                        locale = new Locale("es");
                        config.locale = locale;
                        break;
                    case 2:
                        locale = new Locale("it");
                        config.locale = locale;
                        break;
                    case 3:
                        locale = new Locale("ja");
                        config.locale = locale;
                        break;
                }
                getResources().updateConfiguration(config, null);
                Intent idiomasAlert = new Intent(MainTabbedActivity.this, MainTabbedActivity.class);
                startActivity(idiomasAlert);
                Toast.makeText(getApplicationContext(), getString(R.string.toast_idioma), Toast.LENGTH_LONG).show();
                finish();
            }
        });
        b.show();
    }

    public class  GetHttpResponseFotoUser extends AsyncTask<Void,Void,Void> {

        String REQUEST_USUARIO = "https://momentary-electrode.000webhostapp.com/getUsuario.php";
        public Context context;
        String ResultHolder;

        public GetHttpResponseFotoUser(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, REQUEST_USUARIO,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_error) , Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                            startActivity(intent);
                        }
                    });
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //Creación de una cola de solicitudes
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext()); //getActivity()
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
                            /*
                            if (listaFoto != null){
                                listaFoto.clear();
                            }*/
                            listaFoto = new ArrayList<>();
                            jsonArray = new JSONArray(ResultHolder);
                            JSONObject jsonObject;
                            SharedPreferences sharedpreferences = getSharedPreferences("sesion", getApplication().MODE_PRIVATE);
                            String id_usuario = sharedpreferences.getString("id_usuario","");
                            for (int i=0; i<jsonArray.length();i++){
                                jsonObject= jsonArray.getJSONObject(i);
                                String usuarioBusqueda = jsonObject.getString("id");
                                if (id_usuario.equals(usuarioBusqueda) || (id_usuario == usuarioBusqueda)){
                                    //String nombre = jsonObject.getString("nombre");
                                    //String username = jsonObject.getString("username");
                                    String dec = jsonObject.getString("foto");
                                    Bitmap foto = downloadImage(dec);
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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            if (listaFoto != null){ //listaDatosUser
                imagenFoto.setImageBitmap(listaFoto.get(0));
                //listaFoto.clear();
            }
            else{

            }
        }
    }

    public void reejecutarGetHttpResponseDatosUser(){
        GetHttpResponseFotoUser getHttpResponseFotoUser = new GetHttpResponseFotoUser(getApplicationContext());
        getHttpResponseFotoUser.execute();
    }


    private void mostrarLista(){
        //if (findViewById(R.id.contenedorFragment) != null){
        //  if (savedInstanceState != null){
        //      return;
        // }
        //listaReclamosFragment = new ListaReclamosFragment();
        //getSupportFragmentManager().beginTransaction().replace(R.id.container, listaReclamosFragment).commit();
        //progressBarEventos = findViewById(R.id.progressBar);

        //}
    }

}
