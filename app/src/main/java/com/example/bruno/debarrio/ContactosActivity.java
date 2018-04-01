package com.example.bruno.debarrio;

import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruno.debarrio.Adapters.ListAdapterContactos;
import com.example.bruno.debarrio.HTTP.HttpServices;

public class ContactosActivity extends AppCompatActivity {

    ListView ContactosListView;
    ProgressBar progressBarContactos;
    TextView textviewRegresar;
    //String ServerURL = "http://androidblog.esy.es/AndroidJSon/Subjects.php";
    String ServerURL = "https://momentary-electrode.000webhostapp.com/getContacto.php";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);
        textviewRegresar = findViewById(R.id.textview_regresar);
        textviewRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(); //vuelve al activity anterior
            }
        });
        ContactosListView = findViewById(R.id.listview1);
        progressBarContactos = findViewById(R.id.progressBar);
        new GetHttpResponse(ContactosActivity.this).execute();
    }

    private class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        public Context context;
        String ResultHolder;
        List<Contactos> contactosList;

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
                            Contactos contactos;
                            contactosList = new ArrayList<Contactos>();

                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                contactos = new Contactos();
                                jsonObject = jsonArray.getJSONObject(i);
                                contactos.ContactoName = jsonObject.getString("email");
                                contactosList.add(contactos);
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
            progressBarContactos.setVisibility(View.GONE);
            ContactosListView.setVisibility(View.VISIBLE);

            if(contactosList != null)
            {
                ListAdapterContactos adapter = new ListAdapterContactos(contactosList, context);
                ContactosListView.setAdapter(adapter);
            }
            else{
                Toast.makeText(context, "Sin conexión con el servidor :(", Toast.LENGTH_LONG).show();
            }
        }
    }

}