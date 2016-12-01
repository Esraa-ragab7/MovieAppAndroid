package com.example.esrae.movieapp;

import android.os.Bundle;

import java.io.Serializable;


public class Trailer implements Serializable {
    String YouId;

    public Trailer(String YouId) {
        this.YouId = YouId;
    }

    public String getYouId() {
        return YouId;
    }

}
