package com.example.gl.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenresModel
{

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("slug")
    @Expose
    private String slug;

    @SerializedName("games_count")
    @Expose
    private Integer games_count;

    @SerializedName("image_background")
    @Expose
    private String image_background;

    public Integer getGames_count() {
        return games_count;
    }

    public void setGames_count(Integer games_count) {
        this.games_count = games_count;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_background() {
        return image_background;
    }

    public void setImage_background(String image_background) {
        this.image_background = image_background;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

}
