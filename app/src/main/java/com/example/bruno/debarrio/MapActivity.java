package com.example.bruno.debarrio;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
//import com.example.bruno.debarrio.PostsDB.PedidoDeCoordenada;
import com.example.bruno.debarrio.PostsDB.PedidoDeCoordenada2;
import com.example.bruno.debarrio.entidades.GuardarMarcador;
import com.example.bruno.debarrio.entidades.Reclamo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.security.Permission;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

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
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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

        SharedPreferences sharedpreferencesMap = getApplicationContext().getSharedPreferences("coordenadas",getApplicationContext().MODE_PRIVATE);
        final String latitud = sharedpreferencesMap.getString("latitud","");
        final String longitud = sharedpreferencesMap.getString("longitud","");
        mMap.setMyLocationEnabled(true); //se habilita mi localizacion
        double lati = Double.valueOf(latitud);
        double longi = Double.valueOf(longitud);
        final LatLng coordenadasMarker = new LatLng(lati, longi);
        final CameraUpdate miMarker = CameraUpdateFactory.newLatLngZoom(coordenadasMarker,20);
        final GuardarMarcador guardar = new GuardarMarcador(lati,longi);

        //mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
        //    @Override
        //    public void onMapLongClick(LatLng coordenadasMarker) {
                //if (marcadorCam != null) marcadorCam.remove(); //si ya existe un marcador lo borra para crear uno nuevo
        marcadorCam = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).anchor(0.0f, 1.0f).position(coordenadasMarker).title(latitud + ", " + longitud));
                //marcadorCam = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.ubicacion)).anchor(0.0f, 1.0f).position(latLng));
        mMap.animateCamera(miMarker);
                //final double lati = coordenadasMarker.latitude;
                //final double longi = coordenadasMarker.longitude;
        guardar.setLatitudMarker(lati);
        guardar.setLongitudMarker(longi);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadasMarker,15));
        //    }
        //});
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
    }

    private void actualizarMiUbicacion(Location location){

        if (location != null){
            latActual = location.getLatitude();
            lngActual = location.getLongitude();
            agregarMarcadorUbicacion(latActual,lngActual);
        }
    }

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

    private void ubicacionReclamo(Double lat, Double lng) {
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