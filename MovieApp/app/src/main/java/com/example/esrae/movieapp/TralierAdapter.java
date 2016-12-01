package com.example.esrae.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;


public class TralierAdapter extends BaseAdapter {
    List <Trailer> lst;
    Context con;
    LayoutInflater Linf;

    public TralierAdapter(List<Trailer> lst, Context con) {
        this.lst = lst;
        this.con = con;
        Linf = (LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE); ///////////
    }

    @Override
    public int getCount() {
        return lst.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootview = convertView;
        Holder hold;
        if(rootview==null)
        {
            rootview=Linf.inflate(R.layout.trailer,null);
            hold=new Holder();
            hold.ib=(ImageView) rootview.findViewById(R.id.txt2);
            hold.tv=(TextView) rootview.findViewById(R.id.txt);
            rootview.setTag(hold);
        }
        else
        {
            hold=(Holder)rootview.getTag();
        }
        String txt = " Trailer "+ (position+1);

        if(hold.tv!=null)
        hold.tv.setText( txt );

        return rootview;
    }

    class Holder{
        ImageView ib;
        TextView tv;
    }

}
