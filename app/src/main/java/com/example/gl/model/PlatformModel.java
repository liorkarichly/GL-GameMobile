package com.example.gl.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlatformModel implements Serializable
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

    @SerializedName("year_start")
    @Expose
    @Nullable
    private Integer year_start;

    @SerializedName("year_end")
    @Expose
    @Nullable
    private Integer year_end;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() { return slug; }

    public void setSlug(String slug) { this.slug = slug; }

    @Nullable
    public Integer getYear_start() { return year_start; }

    public void setYear_start(@Nullable Integer year_start) { this.year_start = year_start; }

    @Nullable
    public Integer getYear_end() { return year_end; }

    public void setYear_end(@Nullable Integer year_end) { this.year_end = year_end; }

}
