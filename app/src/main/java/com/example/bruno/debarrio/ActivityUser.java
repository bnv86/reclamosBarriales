package com.example.bruno.debarrio;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bruno.debarrio.entidades.Reclamo;
import com.example.bruno.debarrio.fragments.DetalleReclamoFragment;
import com.example.bruno.debarrio.NoIncluidos.ListaEventosUsuarioFragment;
import com.example.bruno.debarrio.interfaces.ComunicacionFragments;

import java.util.ArrayList;

public class ActivityUser extends FragmentActivity implements ListaEventosUsuarioFragment.OnFragmentInteractionListener,
DetalleReclamoFragment.OnFragmentInteractionListener, ComunicacionFragments{ //implements TituloFragment.onTituloSelectedListener

    ListView eventosListView;
    ProgressBar progressBarEventos;
    TextView textviewRegresar;
    String ServerURL = "https://momentary-electrode.000webhostapp.com/getEvento.php";

    ArrayList<Reclamo> listaPersonajes;
    RecyclerView recyclerViewPersonajes;

    ListaEventosUsuarioFragment listaEventosUsuarioFragment;
    DetalleReclamoFragment detalleReclamoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);

        listaEventosUsuarioFragment = new ListaEventosUsuarioFragment();
        //listaPersonajes = new ArrayList<>();
        //recyclerViewPersonajes = (RecyclerView) findViewById(R.id.reciclerId);
        //recyclerViewPersonajes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment, listaEventosUsuarioFragment).commit();
        //new GetHttpResponse(MainActivity.this).execute();

/*
        textviewRegresar = findViewById(R.id.textview_regresar);
        textviewRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(); //vuelve al activity anterior
            }
        });*/

        /*
        progressBarEventos = findViewById(R.id.progressBar);*/

        //new GetHttpResponse(MainActivity.this).execute();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void enviarPersonaje(Reclamo reclamo) {
        detalleReclamoFragment = new DetalleReclamoFragment();
        Bundle bundleEnvio = new Bundle();
        bundleEnvio.putSerializable("objeto", reclamo);
        detalleReclamoFragment.setArguments(bundleEnvio);

        //cargar el fragment en el activity
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment, detalleReclamoFragment).addToBackStack(null).commit();
    }

    /*
    public class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        public Context context;
        String ResultHolder;
        List<Subject> eventosList;
        //ImageView imagen = (ImageView) findViewById(R.id.icon);

        public GetHttpResponse(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences("sesion",getApplicationContext().MODE_PRIVATE);
            final String usuarioActual = sharedpreferences.getString("username",""); //ME DEVUELVE EL PASSWORD, NO EL USERNAME, PROBLEMA DEL LOGIN??
            HttpServices httpServiceObject = new HttpServices(ServerURL);
            try
            {
                httpServiceObject.ExecutePostRequest();

                if(httpServiceObject.getResponseCode() == 200)
                {
                    ResultHolder = httpServiceObject.getResponse();

                    if(ResultHolder != null)
                    {
                        JSONArray jsonArray = null;

                        try {
                            jsonArray = new JSONArray(ResultHolder);
                            JSONObject jsonObject;
                            Subject subject;

                            eventosList = new ArrayList<Subject>();
                            //setListAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Contenido.titulos));

                            for(int i=0; i<jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);
                                subject = new Subject();
                                String usuario = jsonObject.getString("usuario");
                                if (usuario.equals(usuarioActual)) {

                                    subject.SubjectFecha = jsonObject.getString("fecha");
                                    subject.SubjectMotivo = jsonObject.getString("motivo");
                                    subject.SubjectEstado = jsonObject.getString("estado");
                                    String dec = jsonObject.getString("foto");
                                    subject.SubjectBitmap = downloadImage(dec);
                                    eventosList.add(subject);
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
  //          progressBarEventos.setVisibility(View.GONE);
//            eventosListView.setVisibility(View.VISIBLE);

            if(ListaEventosUsuarioFragment != null)
            {
                final AdaptadorReclamos adapter = new AdaptadorReclamos(listaPersonajes);
                recyclerViewPersonajes.setAdapter(adapter);
                /*eventosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Subject subject;
                        //subject = new Subject();
                        ClipData clip = ClipData.newPlainText("text","Texto copiado al portapapeles"); //que copie el string del item "Texto copiado al portapapeles" , subject.SubjectName
                        ClipboardManager clipboard = (ClipboardManager)getBaseContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboard.setPrimaryClip(clip);
                    }
                });
            }
            else{
                Toast.makeText(context, "Sin conexión con el servidor :(", Toast.LENGTH_LONG).show();
            }
        }
    }*/

/*
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
    }*/

}