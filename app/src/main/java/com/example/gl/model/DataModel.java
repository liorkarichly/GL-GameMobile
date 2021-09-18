package com.example.gl.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataModel implements Serializable
{

    @SerializedName("max")
    @Expose
   private String max;

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

}
