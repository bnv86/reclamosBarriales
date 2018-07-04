package com.example.bruno.debarrio;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.bruno.debarrio.HTTP.HttpServices;
import com.example.bruno.debarrio.entidades.Reclamo;
import com.example.bruno.debarrio.fragments.DetalleReclamoFragment;
import com.example.bruno.debarrio.fragments.ListaReclamosFragment;
import com.example.bruno.debarrio.fragments.ProfileFragment;
import com.example.bruno.debarrio.fragments.ReclamosFragment;
import com.example.bruno.debarrio.fragments.RespuestaReclamoFragment;
import com.example.bruno.debarrio.fragments.dummy.DummyContent;
import com.example.bruno.debarrio.interfaces.ComunicacionFragments;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ReclamosFragment.OnListFragmentInteractionListener, ListaReclamosFragment.OnFragmentInteractionListener,
DetalleReclamoFragment.OnFragmentInteractionListener, RespuestaReclamoFragment.OnFragmentInteractionListener, ComunicacionFragments{ //implements TituloFragment.onTituloSelectedListener

    //String ServerURL = "https://momentary-electrode.000webhostapp.com/getReclamo.php";
    private Locale locale;
    private Configuration config = new Configuration();
    ListaReclamosFragment listaReclamosFragment;
    DetalleReclamoFragment detalleReclamoFragment;
    boolean flagFragment = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if (findViewById(R.id.contenedorFragment) != null){
            if (savedInstanceState != null){
                return;
            }
            listaReclamosFragment = new ListaReclamosFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment, listaReclamosFragment).commit();
            //progressBarEventos = findViewById(R.id.progressBar);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_respuesta_reclamo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_profile) {
            ProfileFragment profileFragment;
            profileFragment = new ProfileFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment, profileFragment).commit();
        }

        if (id == R.id.action_languaje) {
            mostrarDialog();
        }
        if (id == R.id.action_logout) { //cierra sesion
            SharedPreferences sharedPreferences = getSharedPreferences("sesion",MODE_PRIVATE); //toma la sesion actual del usuario
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.remove("usuario"); //cierra la sesion del usuario
            edit.commit();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"Sesion cerrada", Toast.LENGTH_LONG).show();
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {}

    @Override
    public void onBackPressed() {
        //listaReclamosFragment = new ListaReclamosFragment();
        //getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment, listaReclamosFragment).commit();
        SharedPreferences prefFlag= getApplication().getSharedPreferences("flag", getApplication().MODE_PRIVATE);

        if (prefFlag.getBoolean("flag",false)==true) {
            //listaReclamosFragment = new ListaReclamosFragment();
            //getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment, listaReclamosFragment).commit();
            //getFragmentManager().popBackStack();
            flagFragment= false;

            SharedPreferences.Editor flagEdit = prefFlag.edit();
            flagEdit.putBoolean("flag",flagFragment);
            flagEdit.apply();

            listaReclamosFragment = new ListaReclamosFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment, listaReclamosFragment).commit();
        } else {
            super.onBackPressed();
            //listaReclamosFragment = new ListaReclamosFragment();
            //getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment, listaReclamosFragment).commit();
            //super.onBackPressed();
        }
        //super.onBackPressed();
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        //first clear the recycler view so items are not populated twice
        //recyclerAdapter.clear();

        //then reload the data
        //PostCall doPostCall = new PostCall(); //my AsyncTask...
        //doPostCall.execute();
    }

    @Override
    public void enviarReclamo(Reclamo reclamo) {
        //detalleReclamoFragment = (DetalleReclamoFragment) this.getSupportFragmentManager().findFragmentById(R.id.fragmentDetalle);
        //if ((detalleReclamoFragment != null) && (findViewById(R.id.contenedorFragment) == null)){
        //    detalleReclamoFragment.asignarInfo(reclamo);
        //}else{
            detalleReclamoFragment = new DetalleReclamoFragment();
            Bundle bundleEnvio = new Bundle();
            bundleEnvio.putSerializable("objeto", reclamo);
            detalleReclamoFragment.setArguments(bundleEnvio);

            //cargar el fragment en el activity
            getSupportFragmentManager().beginTransaction().replace(R.id.contenedorFragment, detalleReclamoFragment).addToBackStack(null).commit();
        //}
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

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
                Intent idiomasAlert = new Intent(MainActivity.this, MainActivity.class);
                startActivity(idiomasAlert);
                Toast.makeText(getApplicationContext(), getString(R.string.toast_idioma), Toast.LENGTH_LONG).show();
                finish();
            }
        });
        b.show();
    }
}