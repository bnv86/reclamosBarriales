package com.example.bruno.debarrio.interfaces;

import android.content.Context;

import com.example.bruno.debarrio.entidades.Reclamo;
import com.example.bruno.debarrio.entidades.Respuesta;

/**
 * Created by Bruno on 08/04/2018.
 */

public interface ComunicacionFragments {

    public void enviarReclamo(Reclamo reclamo);
    public void enviarRespuesta(Respuesta respuesta);
    public void reejecutarGetHttpResponseDatosUser ();

}
