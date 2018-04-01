package com.example.bruno.debarrio.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bruno.debarrio.Contactos;
import com.example.bruno.debarrio.Eventos;
import com.example.bruno.debarrio.R;

import java.util.List;

/**
 * Created by Bruno on 01/04/2018.
 */

public class ListAdapterEventos extends BaseAdapter {

    Context context;
    List<Eventos> valueList;

    public ListAdapterEventos(List<Eventos> listValue, Context context)
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
        ViewItem viewItem = null;

        if(convertView == null)
        {
            viewItem = new ViewItem();
            LayoutInflater layoutInfiater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInfiater.inflate(R.layout.layout_item, null);
            viewItem.TextViewSubjectName = (TextView)convertView.findViewById(R.id.textView1);
            convertView.setTag(viewItem);
        }
        else
        {
            viewItem = (ViewItem) convertView.getTag();
        }

        viewItem.TextViewSubjectName.setText(valueList.get(position).EventoName);
        return convertView;
    }
}

class ViewItem2
{
    TextView TextViewSubjectName;

}
