package com.example.bruno.debarrio.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bruno.debarrio.EventosTodosActivity;
import com.example.bruno.debarrio.R;
import com.example.bruno.debarrio.Subject;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Bruno on 02/04/2018.
 */

public class ListAdapterEventos extends BaseAdapter {

    Context context;
    List<Subject> valueList;

    public ListAdapterEventos(List<Subject> listValue, Context context)
    {
        this.context = context;
        this.valueList = listValue;
    }

    @Override
    public int getCount()
    {
        return this.valueList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return this.valueList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //String url = "https://momentary-electrode.000webhostapp.com/fotos/14.png";
        ViewItemEventos viewItem = null;
        //ImageView imageView = convertView.findViewById(R.id.icon);
        ImageView img;
        Context context;

        if(convertView == null)
        {
            viewItem = new ViewItemEventos();
            LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.layout_item_evento, null);
            viewItem.ImageViewSubjectImage = convertView.findViewById(R.id.icon);
            //img = (ImageView) convertView.findViewById(R.id.icon);
            //Picasso.with(null).load(url).into(img);
            viewItem.TextViewSubjectName = convertView.findViewById(R.id.textView1);
            convertView.setTag(viewItem);
        }
        else
        {
            viewItem = (ViewItemEventos) convertView.getTag();
        }
        //viewItem.ImageViewSubjectImage = convertView.findViewById(R.id.icon);
        //viewItem.ImageViewSubjectImage.setImage(valueList.get(position).SubjectImage);
        //imageView.setImageResource(valueList.get(position).SubjectImage);
        //viewItem.ImageViewSubjectImage.setImageBitmap(valueList.get(position).SubjectImage);
        //viewItem.ImageViewSubjectImage.getPath(valueList.get(position).SubjectImage); //SI SACO ESTO SALE EL IF PORQUE LA IMAGEN ESTA EN NULL, O SEA, SALE LA CAMARA
        viewItem.TextViewSubjectName.setText(valueList.get(position).SubjectName);
        viewItem.ImageViewSubjectImage.setImageBitmap(valueList.get(position).SubjectBitmap);

        return convertView;
    }
}

class ViewItemEventos
{
    TextView TextViewSubjectName;
    ImageView ImageViewSubjectImage;
    Bitmap BitmapSubjectImage;
}