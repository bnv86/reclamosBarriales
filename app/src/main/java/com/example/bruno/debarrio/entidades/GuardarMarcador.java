package com.example.bruno.debarrio.entidades;

import java.io.Serializable;

public class GuardarMarcador{
    private double latitudMarker;
    private double longitudMarker;

    public GuardarMarcador(){}

    public GuardarMarcador(double latitudMarker, double longitudMarker) {
        this.latitudMarker = latitudMarker;
        this.longitudMarker = longitudMarker;
    }

    public double getLatitudMarker(){return latitudMarker;}
    public void setLatitudMarker(double latitudMarker){this.latitudMarker = latitudMarker;}

    public double getLongitudMarker(){return longitudMarker;}
    public void setLongitudMarker(double longitudMarker){this.longitudMarker = longitudMarker;}
}
