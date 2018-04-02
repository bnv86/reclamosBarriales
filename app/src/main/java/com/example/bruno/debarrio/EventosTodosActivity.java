package com.example.bruno.debarrio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruno.debarrio.Adapters.ListAdapter;
import com.example.bruno.debarrio.Adapters.ListAdapterEventos;
import com.example.bruno.debarrio.HTTP.HttpServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        ImageView imagen = (ImageView) findViewById(R.id.icon);
        /*
        if(incidente.getCaptura()!=null) {
            imagen.setImageBitmap(incidente.getCaptura().getImagen());
        }else{
            imagen.setImageDrawable(mParentActivity.getResources().getDrawable(R.drawable.ic_launcher_background));
        }*/
        eventosListView = findViewById(R.id.listview1);
        progressBarEventos = findViewById(R.id.progressBar);
        new GetHttpResponse(EventosTodosActivity.this).execute();
    }

    private class GetHttpResponse extends AsyncTask<Void, Void, Void>
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
                                subject.SubjectName = jsonObject.getString("fecha");
                                subject.SubjectImage = StringToBitMap(jsonObject.getString("foto")); //TENGO Q VERIFICAR ESTE METODO O ENCODE URL
                                //If you get bitmap = null, you can use:
                                //byte[] decodedString = Base64.decode(subject.SubjectName, Base64.URL_SAFE );
                                //Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                               // subject.SubjectName = jsonObject.getString("foto");
                                //Bitmap b = StringToBitMap(subject.SubjectName);
                                //imagen.setImageBitmap(b);
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
                ListAdapterEventos adapter = new ListAdapterEventos(eventosList, context);
                eventosListView.setAdapter(adapter);
            }
            else{
                Toast.makeText(context, "Sin conexión con el servidor :(", Toast.LENGTH_LONG).show();
            }
        }
    }
    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

// second solution is you can set the path inside decodeFile function
//viewImage.setImageBitmap(BitmapFactory.decodeFile("your iamge path"));
}
