package com.example.bruno.debarrio.NoIncluidos;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bruno.debarrio.R;

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

        if(convertView == null)
        {
            viewItem = new ViewItemEventos();
            LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.layout_item_evento, null);
            viewItem.ImageViewSubjectImage = convertView.findViewById(R.id.icon);
            viewItem.TextViewSubjectName = convertView.findViewById(R.id.textView1);
            viewItem.TextViewSubjectMotivo = convertView.findViewById(R.id.textView2);
            convertView.setTag(viewItem);
        }
        else
        {
            viewItem = (ViewItemEventos) convertView.getTag();
        }

        viewItem.TextViewSubjectName.setText(valueList.get(position).SubjectFecha);
        viewItem.ImageViewSubjectImage.setImageBitmap(valueList.get(position).SubjectBitmap);
        viewItem.TextViewSubjectMotivo.setText(valueList.get(position).SubjectMotivo);

        return convertView;
    }
}

class ViewItemEventos
{
    TextView TextViewSubjectName;
    TextView TextViewSubjectMotivo;
    ImageView ImageViewSubjectImage;
    Bitmap BitmapSubjectImage;
}

//PROBANDO
//img = (ImageView) convertView.findViewById(R.id.icon);
//Picasso.with(null).load(url).into(img);

//viewItem.ImageViewSubjectImage = convertView.findViewById(R.id.icon);
//viewItem.ImageViewSubjectImage.setImage(valueList.get(position).SubjectImage);
//imageView.setImageResource(valueList.get(position).SubjectImage);
//viewItem.ImageViewSubjectImage.setImageBitmap(valueList.get(position).SubjectImage);
//viewItem.ImageViewSubjectImage.getPath(valueList.get(position).SubjectImage); //SI SACO ESTO SALE EL IF PORQUE LA IMAGEN ESTA EN NULL, O SEA, SALE LA CAMARA