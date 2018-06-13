package com.example.bruno.debarrio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruno.debarrio.Adapters.AdaptadorEventos;
//import com.example.bruno.debarrio.HTTP.HttpServices;
import com.example.bruno.debarrio.entidades.Evento;
import com.example.bruno.debarrio.entidades.Subject;
import com.example.bruno.debarrio.fragments.DetalleEventoFragment;
import com.example.bruno.debarrio.fragments.ListaEventosFragment;
import com.example.bruno.debarrio.interfaces.ComunicacionFragments;

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

public class MainActivity extends FragmentActivity implements ListaEventosFragment.OnFragmentInteractionListener,
DetalleEventoFragment.OnFragmentInteractionListener, ComunicacionFragments{ //implements TituloFragment.onTituloSelectedListener

    ListView eventosListView;
    ProgressBar progressBarEventos;
    TextView textviewRegresar;
    String ServerURL = "https://momentary-electrode.000webhostapp.com/getEvento.php";
    ArrayList<Evento> listaPersonajes;
    RecyclerView recyclerViewPersonajes;
    ListaEventosFragment listaEventosFragment;
    DetalleEventoFragment detalleEventoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        listaEventosFragment = new ListaEventosFragment();
        //listaPersonajes = new ArrayList<>();
        //recyclerViewPersonajes = (RecyclerView) findViewById(R.id.reciclerId);
        //recyclerViewPersonajes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment, listaEventosFragment).commit();
        progressBarEventos = findViewById(R.id.progressBar);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {}

    @Override
    public void enviarPersonaje(Evento evento) {
        detalleEventoFragment = new DetalleEventoFragment();
        Bundle bundleEnvio = new Bundle();
        bundleEnvio.putSerializable("objeto", evento);
        detalleEventoFragment.setArguments(bundleEnvio);

        //cargar el fragment en el activity
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment, detalleEventoFragment).addToBackStack(null).commit();
    }
}