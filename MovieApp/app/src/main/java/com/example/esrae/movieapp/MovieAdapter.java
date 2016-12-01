package com.example.esrae.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.List;


public class MovieAdapter extends BaseAdapter {
    List <Movie> lst;
    Context con;
    LayoutInflater Linf;

    public MovieAdapter(List<Movie> lst, Context con) {
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
            rootview=Linf.inflate(R.layout.itemposter,null);
            hold=new Holder();
            hold.iv=(ImageView) rootview.findViewById(R.id.item);
            rootview.setTag(hold);
        }
        else
        {
            hold=(Holder)rootview.getTag();
        }
        Picasso.with(con).load("http://image.tmdb.org/t/p/w185/"+lst.get(position).getPoster()).into(hold.iv);


        return rootview;
    }

    class Holder {
        ImageView iv;
    }
}
