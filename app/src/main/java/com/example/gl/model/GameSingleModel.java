package com.example.gl.model;

import com.example.gl.response.ResponsePlatformsForGameSingleModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GameSingleModel implements Serializable
{

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("rating")
    @Expose
    private Number rating;

    @SerializedName("slug")
    @Expose
    private String slug;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("released")
    @Expose
    private String released;

    @SerializedName("background_image")
    @Expose
    private String background_image;

    @SerializedName("website")
    @Expose
    private String website;

    @SerializedName("playtime")
    @Expose
    private Integer playtime;

    @SerializedName("platforms")
    @Expose
    private List<ResponsePlatformsForGameSingleModel> platforms;

    @SerializedName("game_series_count")
    @Expose
    private Integer game_series_count;


    public Integer getPlaytime() {
        return playtime;
    }

    public void setPlaytime(Integer playtime) {
        this.playtime = playtime;
    }

    public List<ResponsePlatformsForGameSingleModel> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<ResponsePlatformsForGameSingleModel> platforms) {
        this.platforms.addAll(platforms);
    }

    public Integer getGame_series_count() {
        return game_series_count;
    }

    public void setGame_series_count(Integer game_series_count) {
        this.game_series_count = game_series_count;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Number getRating() {
        return rating;
    }

    public void setRating(Number rating) {
        this.rating = rating;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setBackground_image(String background_image)
    {

        this.background_image = background_image;
        
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

}
