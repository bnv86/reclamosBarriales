package com.example.bruno.debarrio.NoIncluidos;

import android.content.Context;
import java.util.List;
import android.app.Activity;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bruno.debarrio.R;

/**
 * Created by Bruno on 27/03/2018.
 */

public class ListAdapterContactos extends BaseAdapter {

    Context context;
    List<Subject> valueList;

    public ListAdapterContactos(List<Subject> listValue, Context context)
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
        ViewItemContactos viewItemContactos = null;

        if(convertView == null)
        {
            viewItemContactos = new ViewItemContactos();
            LayoutInflater layoutInflater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.layout_item, null);
            viewItemContactos.TextViewSubjectName = (TextView)convertView.findViewById(R.id.textView1);
            convertView.setTag(viewItemContactos);
        }
        else
        {
            viewItemContactos = (ViewItemContactos) convertView.getTag();
        }

        viewItemContactos.TextViewSubjectName.setText(valueList.get(position).SubjectName);
        return convertView;
    }
}

class ViewItemContactos
{
    TextView TextViewSubjectName;

}