package com.example.gl.model;

import com.example.gl.response.ResponsePlatformsForGameSingleModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameModel implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id ;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("platform")
    @Expose
    private List<ResponsePlatformsForGameSingleModel> platforms = new ArrayList<>();

    @SerializedName("released")
    @Expose
    private String released;

    @SerializedName("background_image")
    @Expose
    private String background_image;

    @SerializedName("rating")
    @Expose
    private Double rating;

    public Integer getId() {
        return id;
    }

    public void setId(Integer i_id) {
        id = i_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getBackground_image() {
        return background_image;
    }

    public void setBackground_image(String background_image) {
        this.background_image = background_image;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

}
