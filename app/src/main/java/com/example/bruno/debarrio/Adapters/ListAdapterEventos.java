package com.example.bruno.debarrio.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bruno.debarrio.R;
import com.example.bruno.debarrio.Subject;

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
        ViewItemImage viewItem = null;
        //ImageView imageView = convertView.findViewById(R.id.icon);

        if(convertView == null)
        {
            viewItem = new ViewItemImage();
            LayoutInflater layoutInfiater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInfiater.inflate(R.layout.layout_item_evento, null);
            viewItem.TextViewSubjectImage = convertView.findViewById(R.id.icon);
            viewItem.TextViewSubjectName = convertView.findViewById(R.id.textView1);
            convertView.setTag(viewItem);
        }
        else
        {
            viewItem = (ViewItemImage) convertView.getTag();
        }

        //imageView.setImageResource(valueList.get(position).SubjectImage);
        //viewItem.TextViewSubjectImage.setImageResource(valueList.get(position).SubjectImage);
        viewItem.TextViewSubjectImage.setImageBitmap(valueList.get(position).SubjectImage); //SI SACO ESTO SALE EL IF PORQUE LA IMAGEN ESTA EN NULL, O SEA, SALE LA CAMARA
        viewItem.TextViewSubjectName.setText(valueList.get(position).SubjectName);

        return convertView;
    }
}

class ViewItemImage
{
    TextView TextViewSubjectName;
    ImageView TextViewSubjectImage;

}