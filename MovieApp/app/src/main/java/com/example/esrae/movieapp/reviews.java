package com.example.esrae.movieapp;

import java.io.Serializable;


public class reviews implements Serializable {
    String Rev;

    public reviews(String Rev) {
        this.Rev = Rev;
    }

    public String Rev() {
        return Rev;
    }

}
