package com.example.bruno.debarrio;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

//import com.example.bruno.debarrio.HTTP.HttpServices;
import com.example.bruno.debarrio.entidades.Reclamo;
import com.example.bruno.debarrio.fragments.DetalleReclamoFragment;
import com.example.bruno.debarrio.fragments.ListaReclamosFragment;
import com.example.bruno.debarrio.fragments.ReclamosFragment;
import com.example.bruno.debarrio.fragments.dummy.DummyContent;
import com.example.bruno.debarrio.interfaces.ComunicacionFragments;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ReclamosFragment.OnListFragmentInteractionListener, ListaReclamosFragment.OnFragmentInteractionListener,
DetalleReclamoFragment.OnFragmentInteractionListener, ComunicacionFragments{ //implements TituloFragment.onTituloSelectedListener

    ListView eventosListView;
    ProgressBar progressBarEventos;
    TextView textviewRegresar;
    String ServerURL = "https://momentary-electrode.000webhostapp.com/getEvento.php";
    ArrayList<Reclamo> listaPersonajes;
    RecyclerView recyclerViewPersonajes;
    ListaReclamosFragment listaReclamosFragment;
    DetalleReclamoFragment detalleReclamoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        listaReclamosFragment = new ListaReclamosFragment();
        //listaPersonajes = new ArrayList<>();
        //recyclerViewPersonajes = (RecyclerView) findViewById(R.id.reciclerId);
        //recyclerViewPersonajes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment, listaReclamosFragment).commit();
        progressBarEventos = findViewById(R.id.progressBar);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {}

    @Override
    public void enviarPersonaje(Reclamo reclamo) {
        detalleReclamoFragment = new DetalleReclamoFragment();
        Bundle bundleEnvio = new Bundle();
        bundleEnvio.putSerializable("objeto", reclamo);
        detalleReclamoFragment.setArguments(bundleEnvio);

        //cargar el fragment en el activity
        getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment, detalleReclamoFragment).addToBackStack(null).commit();
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}