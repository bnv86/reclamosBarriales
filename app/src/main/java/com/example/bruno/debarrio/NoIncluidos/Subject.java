package com.example.bruno.debarrio.NoIncluidos;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.widget.ImageViewCompat;
import android.text.Html;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;

import java.net.URL;

/**
 * Created by Bruno on 27/03/2018.
 */

//esta clase sirve para hacer puente a ListAdapter
public class Subject {
    public String SubjectName;
    public ImageView SubjectImage;
    public Bitmap SubjectBitmap;
    public String SubjectFecha;
    public String SubjectMotivo;
    public String SubjectEstado;
    public String SubjectComentario;

}
