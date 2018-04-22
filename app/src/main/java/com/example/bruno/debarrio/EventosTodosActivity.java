package com.example.bruno.debarrio;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruno.debarrio.Adapters.ListAdapterEventos;
import com.example.bruno.debarrio.HTTP.HttpServices;
import com.example.bruno.debarrio.entidades.Subject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class EventosTodosActivity extends AppCompatActivity {
    ListView eventosListView;
    ProgressBar progressBarEventos;
    TextView textviewRegresar;
    String ServerURL = "https://momentary-electrode.000webhostapp.com/getEvento.php";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_todos);
        textviewRegresar = findViewById(R.id.textview_regresar);
        textviewRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(); //vuelve al activity anterior
            }
        });

        eventosListView = findViewById(R.id.listview1);
        progressBarEventos = findViewById(R.id.progressBar);
        Context c;
        new GetHttpResponse(EventosTodosActivity.this).execute();
        //List<Subject> eventosList;
        //Subject subject;
    }

    public class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        public Context context;
        String ResultHolder;
        List<Subject> eventosList;
        ImageView imagen = (ImageView) findViewById(R.id.icon);

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

                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                subject = new Subject();
                                jsonObject = jsonArray.getJSONObject(i);
                                subject.SubjectFecha = jsonObject.getString("fecha");
                                subject.SubjectMotivo = jsonObject.getString("motivo");
                                String dec = jsonObject.getString("foto");
                                subject.SubjectBitmap = downloadImage(dec);
                                eventosList.add(subject);
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
            progressBarEventos.setVisibility(View.GONE);
            eventosListView.setVisibility(View.VISIBLE);

            if(eventosList != null)
            {
                final ListAdapterEventos adapter = new ListAdapterEventos(eventosList, context);
                eventosListView.setAdapter(adapter);
                eventosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
    }


    public static  Bitmap downloadImage(String url) {
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
/*
  //PARA COPIAR UN ITEM DE LA LIST AL PORTAPAPELES
    ClipData clip = ClipData.newPlainText("text", "Texto copiado al portapapeles");
    ClipboardManager clipboard = (ClipboardManager)this.getSystemService(CLIPBOARD_SERVICE);
    clipboard.setPrimaryClip(clip);
    o
    android.text.ClipboardManager clipboard = (android.text.ClipboardManager)this.getSystemService(CLIPBOARD_SERVICE);
    clipboard.setText("Texto copiado al portapapeles");*/


// second solution is you can set the path inside decodeFile function
//viewImage.setImageBitmap(BitmapFactory.decodeFile("your iamge path"));*/