package com.example.esrae.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;


public class ReviewsAdapter extends BaseAdapter {
    List <reviews> lst;
    Context con;
    LayoutInflater Linf;

    public ReviewsAdapter(List<reviews> lst, Context con) {
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
            rootview=Linf.inflate(R.layout.rev,null);
            hold=new Holder();
            hold.tv=(TextView) rootview.findViewById(R.id.ere);
            rootview.setTag(hold);
        }
        else
        {
            hold=(Holder)rootview.getTag();
        }

        hold.tv.setText(lst.get(position).Rev());

        return rootview;
    }

    class Holder{
        TextView tv;
    }

}
