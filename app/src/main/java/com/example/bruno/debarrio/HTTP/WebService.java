package com.example.bruno.debarrio.HTTP;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Nicole on 13/6/2018.
 */

public class WebService {
    private RequestQueue rqt;
    private StringRequest peticion;
    Context context;

    public static final String urlPostReclamo="https://momentary-electrode.000webhostapp.com/pruebas/postReclamo.php";
    public static final String urlLogin="https://momentary-electrode.000webhostapp.com/pruebas/Login.php";
    public static final String urlRegister="https://momentary-electrode.000webhostapp.com/pruebas/Register.php";
    public static final String urlMisReclamos="https://momentary-electrode.000webhostapp.com/MisReclamos.php";
    public static final String urlGetAllReclamos="https://momentary-electrode.000webhostapp.com/pruebas/getReclamo.php";
    public static final String urlGetCategorias="https://momentary-electrode.000webhostapp.com/getCategorias.php";
    public static final String urlGetSubscripciones="https://momentary-electrode.000webhostapp.com/getSubscripciones.php";
    public static final String urlPostSubscripcion="https://momentary-electrode.000webhostapp.com/postSubscripcion.php";

    private ArrayList datos;

    public WebService(Context context){
        rqt=Volley.newRequestQueue(context);
        this.context=context;
        handleSSLHandshake();
        if (isConnectedToInternet(context)==true)
            Log.d("Conexion","Conectado a internet");
        else
            Log.d("Conexion","No conectado a internet");
    }

    public static boolean isConnectedToInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null){
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null){
                for (int i = 0; i < info.length; i++){
                    if (info[i].getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /*Habilita conexión HTTPS*/
    @SuppressLint("TrulyRandom")
    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }
}
