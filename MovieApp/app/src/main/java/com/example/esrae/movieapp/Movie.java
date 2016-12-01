package com.example.esrae.movieapp;

import android.os.Bundle;

import java.io.Serializable;

/**
 * Created by esrae on 10/27/2016.
 */

public class Movie implements Serializable {
    String title,rate,poster,desc,year,movieId;
    public Movie(Bundle movie){}

    public Movie(String title, String rate, String poster, String desc, String year, String movieId) {
        this.title = title;
        this.rate = rate;
        this.poster = poster;
        this.desc = desc;
        this.year = year;
        this.movieId = movieId;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getYear() {
        return year;
    }

    public String getTitle() {
        return title;
    }

    public String getRate() {
        return rate;
    }

    public String getPoster() {
        return poster;
    }

    public String getDesc() {
        return desc;
    }
}
