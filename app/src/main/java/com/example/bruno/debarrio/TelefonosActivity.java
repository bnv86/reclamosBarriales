package com.example.bruno.debarrio;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruno.debarrio.Adapters.ListAdapterContactos;
import com.example.bruno.debarrio.HTTP.HttpServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TelefonosActivity extends AppCompatActivity {

    ListView telefonosListView;
    ProgressBar progressBarTelefonos;
    TextView textviewRegresar;
    String ServerURL = "https://momentary-electrode.000webhostapp.com/getTelefono.php";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telefonos);
        textviewRegresar = findViewById(R.id.textview_regresar);
        textviewRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(); //vuelve al activity anterior
            }
        });
        telefonosListView = findViewById(R.id.listview1);
        progressBarTelefonos = findViewById(R.id.progressBar);
        new GetHttpResponse(TelefonosActivity.this).execute();
    }

    private class GetHttpResponse extends AsyncTask<Void, Void, Void>
    {
        public Context context;
        String ResultHolder;
        List<Subject> telefonoList;

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
                            Subject telefono;

                            telefonoList = new ArrayList<Subject>();

                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                telefono = new Subject();
                                jsonObject = jsonArray.getJSONObject(i);
                                telefono.SubjectName = jsonObject.getString("telefono");
                                telefonoList.add(telefono);
                            }
                        }
                        catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        //Toast.makeText(context, httpServiceObject.getErrorMessage(), Toast.LENGTH_LONG).show();
                        Toast.makeText(context, "Sin conexión :(", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    //Toast.makeText(context, httpServiceObject.getErrorMessage(), Toast.LENGTH_LONG).show();
                    Toast.makeText(context, "Sin conexión :(", Toast.LENGTH_LONG).show();
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
            progressBarTelefonos.setVisibility(View.GONE);
            telefonosListView.setVisibility(View.VISIBLE);

            if(telefonoList != null)
            {
                ListAdapterContactos adapter = new ListAdapterContactos(telefonoList, context);
                telefonosListView.setAdapter(adapter);
            }
            else{
                Toast.makeText(context, "Sin conexión con el servidor :(", Toast.LENGTH_LONG).show();
            }
        }
    }
}
