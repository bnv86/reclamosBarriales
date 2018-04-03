package com.example.bruno.debarrio;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruno.debarrio.Adapters.ListAdapter;
import com.example.bruno.debarrio.Adapters.ListAdapterEventos;
import com.example.bruno.debarrio.HTTP.HttpServices;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
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

        //ImageView imagen = (ImageView) findViewById(R.id.icon);
        /*
        if(incidente.getCaptura()!=null) {
            imagen.setImageBitmap(incidente.getCaptura().getImagen());
        }else{
            imagen.setImageDrawable(mParentActivity.getResources().getDrawable(R.drawable.ic_launcher_background));
        }*/
        eventosListView = findViewById(R.id.listview1);
        progressBarEventos = findViewById(R.id.progressBar);
        Context c;
        new GetHttpResponse(EventosTodosActivity.this).execute();
        List<Subject> eventosList;
        Subject subject;
        /*
        eventosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //final Subject subject;
                //subject = new Subject(); //subject.SubjectName
                ClipData clip = ClipData.newPlainText("text","Texto copiado al portapapeles"); //que copie el string del item
                ClipboardManager clipboard = (ClipboardManager)getBaseContext().getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setPrimaryClip(clip);
            }
        });*/
/*      //ejemplos
        eventosListView.setOnItemClickListener(new ListAdapterEventos.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ImageView selectedImage = (ImageView) view.findViewById(R.id.checkBox);

                selectedImage.setImageResource(R.drawable.simplecheck);
                String str =  parent.getAdapter().getItem(position).toString();

                Toast.makeText(getApplicationContext(), "  " + str , Toast.LENGTH_SHORT).show();
            }
        });

        eventosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TarifeFavorite clickedObj = (TarifeFavorite) parent.getItemAtPosition(position);
                Intent intentToDetay = new Intent(getActivity(), TarifeDetay.class);
                intentToDetay.putExtra("tarife_name",clickedObj.getName());
                startActivity(intentToDetay);
            }
        });*/
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
                            //URL imagen;

                            eventosList = new ArrayList<Subject>();

                            for(int i=0; i<jsonArray.length(); i++)
                            {
                                subject = new Subject();
                                jsonObject = jsonArray.getJSONObject(i);
                                //subject.SubjectName = jsonObject.getString("fecha");

                                String dec = jsonObject.getString("foto");
                                subject.SubjectBitmap = StringToBitMap(dec);
                                //String dec = jsonObject.getString("foto");
                                //Picasso.with(getApplication()).load(URL).into(subject.SubjectImage);

                                //Bitmap link = StringToBitMap(jsonObject.getString("foto")); //hay un problema con get_imagen
                                //subject.SubjectImage.setImageBitmap(link); //ESTA TRAYENDO NULL
                                //OTRO EJEMPLO
                                /*
                                String base64String = jsonObject.getString("foto");
                                String base64Image = base64String.split("https://")[1];
                                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT); //aca se traba el debug
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                                subject.SubjectImage.setImageBitmap(decodedByte);*/

                                //subject.SubjectImage = StringToBitMap(jsonObject.getString("foto"));

                                /*
                                //If you get bitmap = null, you can use:
                                byte[] decodedString = Base64.decode(dec, Base64.DEFAULT); //con CRLF sigue una linea mas, pero devuelve decodedByte = null
                                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); //con DEFAULT o URL_SAFE aca muere el debug
                                subject.SubjectImage.setImageBitmap(decodedByte); //decodedByte: null
                                */
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
    //OTRO EJEMPLO
    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length); //aca se traba el debug
            return bitmap; //devuelve bitmap = null
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
    //OTRO EJEMPLO
    private Bitmap get_imagen(String url) {
        Bitmap bm = null;
        try {
            URL _url = new URL(url);
            URLConnection con = _url.openConnection();
            con.connect();
            InputStream is = con.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {

        }
        return bm;
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

/* EJEMPLO PARA TRAER LA IMAGEN AL LISTVIEW
public class Zoom extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.gallery_zoom);

        String selection = getIntent().getExtras().getString("image");
        Toast.makeText(this, selection, Toast.LENGTH_LONG).show();

        new backgroundLoader().execute();
    }


    private class backgroundLoader extends AsyncTask<Void, Void, Void> {
        Bitmap bmp;

        @Override
        protected Void doInBackground(Void... params) {

            bmp = DecodeBitmapSampleSize(getIntent().getExtras().getString("image"), 48, 64);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            ImageView image = (ImageView) findViewById(R.id.imageZoom);
            image.setImageBitmap(bmp);
        }

    }

    public Bitmap DecodeBitmapSampleSize (String strURL, int reqWidth, int reqHeight) {
        InputStream in = null;
        Bitmap bmp = null;

        in = OpenHttpConnection(strURL);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);

        options.inSampleSize = calculateSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        bmp = BitmapFactory.decodeStream(in, null, options);
        return bmp;
    }

    private InputStream OpenHttpConnection(String strURL) {

        try {
            URL url = new URL(strURL);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(connection.getInputStream());
            return in;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static int calculateSampleSize(BitmapFactory.Options options,
                                          int reqWidth, int reqHeight) {

        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;

        if (width > reqWidth || height > reqHeight) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

final BitmapFactory.Options options = new BitmapFactory.Options();
options.inJustDecodeBounds = true;
BufferedInputStream buffer=new BufferedInputStream(is);
BitmapFactory.decodeStream(buffer,null,options);
buffer.reset();

    // Calculate inSampleSize
options.inSampleSize = calculateInSampleSize(options, reqWidth,reqHeight);

    // Decode bitmap with inSampleSize set
options.inJustDecodeBounds = false;
BitmapFactory.decodeStream(buffer,null,options);

}*/