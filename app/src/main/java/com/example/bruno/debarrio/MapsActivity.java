package com.example.bruno.debarrio;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.android.volley.Response;
import com.example.bruno.debarrio.entidades.GuardarMarcador;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double latActual = 0.0;
    double lngActual = 0.0;
    double latFoto = 0.0;
    double lngFoto = 0.0;
    private Marker marcadorPos;
    private Marker marcadorCam;
    //TextView textview_regresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /*
        textview_regresar = findViewById(R.id.textview_regresar);
        textview_regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });*/

        //boton flotante regresar a pantalla anterior
        FloatingActionButton botonFloatRegresar = findViewById(R.id.float_regresar);
        botonFloatRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                 //       .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //doy los permisos para que se rastree mi ubicacion
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        mMap.setMyLocationEnabled(true); //se habilita mi localizacion
        miUbicacion();
        miMarcador();
    }

    private void miMarcador(){
        final LatLng coordenadasMarker = new LatLng(latFoto, lngFoto);
        final CameraUpdate miMarker = CameraUpdateFactory.newLatLngZoom(coordenadasMarker,16);
        double lati = 0.0;
        double longi = 0.0;
        final GuardarMarcador guardar = new GuardarMarcador(lati,longi);
        //final Location locationMarker;
        /*
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationManager locationManagerMarker = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final Location locationMarker = locationManagerMarker.getLastKnownLocation(LocationManager.GPS_PROVIDER);*/
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (marcadorCam != null) marcadorCam.remove(); //si ya existe un marcador lo borra para crear uno nuevo
                marcadorCam = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).anchor(0.0f, 1.0f).position(latLng).title("Foto"));
                //marcadorCam = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ubicacion)).anchor(0.0f, 1.0f).position(latLng));
                mMap.animateCamera(miMarker);
                final double lati = latLng.latitude;
                final double longi = latLng.longitude;
                guardar.setLatitudMarker(lati);
                guardar.setLongitudMarker(longi);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                /*
                if (locationMarker != null){
                    latFoto = locationMarker.getLatitude();
                    lngFoto = locationMarker.getLongitude();
                }*/
            }
        });
        Button botonGuardar = (Button)findViewById(R.id.boton_guardar_ubicacion);
        botonGuardar.setVisibility(View.VISIBLE);
        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    Intent intent = new Intent(MapsActivity.this, MainTabbedActivity.class);
                                    MapsActivity.this.startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "Coordenadas agregadas!", Toast.LENGTH_LONG).show();
                                } else {
                                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MapsActivity.this);
                                    alertBuilder.setMessage("Error al agregar coordenadas").setNegativeButton("Reintentar", null).create().show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                final double latiMarker = guardar.getLatitudMarker();
                final double longiMarker = guardar.getLongitudMarker();
                /* PRUEBA PARA SUBIR A LA DB LAS COORDENADAS
                PedidoDeCoordenada2 pedido = new PedidoDeCoordenada2(latiMarker, longiMarker, responseListener);
                RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);
                queue.add(pedido);
                */
                Toast.makeText(getApplicationContext(),"Guardando...", Toast.LENGTH_LONG).show();

                /* INTENTOS FALLIDOS DE PASAR DATOS
                Bundle bundleMap = new Bundle();
                //bundle.putDouble("Latitud", Double.valueOf(latiMarker)); //latiMarker
                //bundle.putDouble("Longitud", Double.valueOf(longiMarker)); //longiMarker
                bundleMap.putDouble("Latitud", latiMarker); //latiMarker
                bundleMap.putDouble("Longitud", longiMarker); //longiMarker
                //bundle.putString("Latitud", String.valueOf(latiMarker)); //latiMarker
                //bundle.putString("Longitud", String.valueOf(longiMarker)); //longiMarker
                SubirFragment subirFragment = new SubirFragment();
                subirFragment.setArguments(bundleMap);*/
                //getSupportFragmentManager().beginTransaction().replace(R.id.container, subirFragment).commit();

                /*Intent resultIntent =  new Intent(getApplicationContext(), MainTabbedActivity.class); // new Intent(getApplicationContext(), SubirFragment.class)
                //resultIntent.putExtra("Latitud", String.valueOf(coordenadas.latitude));
                //resultIntent.putExtra("Longitud", String.valueOf(coordenadas.longitude));
                //resultIntent.putExtra("Latitud", Double.valueOf(latiMarker));
                //resultIntent.putExtra("Longitud", Double.valueOf(longiMarker));
                resultIntent.putExtra("Latitud", latiMarker);
                resultIntent.putExtra("Longitud", longiMarker);
                //setResult(Activity.RESULT_OK, resultIntent);
                setResult(FragmentActivity.RESULT_OK, resultIntent);*/

                Intent resultIntent =  new Intent(getApplicationContext(), MainTabbedActivity.class);
                SharedPreferences sharedpreferencesMap = getSharedPreferences("sesion", getApplication().MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferencesMap.edit();
                editor.putString("Latitud", String.valueOf(latiMarker));
                editor.putString("Longitud", String.valueOf(longiMarker));
                Toast.makeText(getApplicationContext(),latiMarker + "", Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),longiMarker + "", Toast.LENGTH_LONG).show();
                editor.commit();
                finish();
            }
        });
    }

    // creo un metodo que sirve para agregar el marcador al mapa, creo un objeto Latlng en el cual se incluye la longitud y la latitud
    //luego utilizo el elemento CameraUpdate, para centrar la camara en la posisicon del marker.

    private void agregarMarcadorUbicacion(Double lat, Double lng) {
        final LatLng coordenadas = new LatLng(lat, lng);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas,16);
        //si el marcador es diferente de null se debera remover. Agrego unas propiedades al marker, como titulo y una imagen
        //y se le agrega un metodo animateCamera para  mover la camara desde una posicion a otra.
        if (marcadorPos != null) marcadorPos.remove();
        marcadorPos = mMap.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title("Estoy aquí")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.animateCamera(miUbicacion);

        //ProgressBar progressBar = (ProgressBar)findViewById(R.id.cargaMapa);
        //progressBar.setVisibility(View.GONE);
        /*
        Button imgbtn = (Button)findViewById(R.id.guardar_ubicacion);
        imgbtn.setVisibility(View.VISIBLE);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Guardando...",Toast.LENGTH_LONG).show();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("coordLat", String.valueOf(coordenadas.latitude));
                resultIntent.putExtra("coordLong", String.valueOf(coordenadas.longitude));
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });*/
    }

    private void actualizarMiUbicacion(Location location){

        if (location != null){
            latActual = location.getLatitude();
            lngActual = location.getLongitude();
            agregarMarcadorUbicacion(latActual,lngActual);
        }
    }

    /*
    private void actualizarMiMarker(Location location){

        if (location != null){
            latFoto = location.getLatitude();
            lngFoto = location.getLongitude();
        }
    }*/

    private void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        actualizarMiUbicacion(location);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000,0,locListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,600000,0,locationListener);
    }

    LocationListener locationListener = new LocationListener() {

        // este metodo se lanza cada vez que se recibe una actualizacion de la posicion
        //dentro de este metodo llamo a actualizarMiUbicacion. para actualizar la posicion actual del mapa.
        @Override
        public void onLocationChanged(Location location) {
            actualizarMiUbicacion(location);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
