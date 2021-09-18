package com.example.gl.googleApiMaps;

public class PlaceByIDModel
{


    private Number rating ;
    private Integer user_ratings_total ;
    private String formatted_address;
    private String formatted_phone_number;
    private String name;
    private String website;
    private PlaceOpeningHours opening_hours;

    public Number getRating() {
        return rating;
    }

    public void setRating(Number rating) {
        this.rating = rating;
    }

    public Integer getUser_ratings_total() {
        return user_ratings_total;
    }

    public void setUser_ratings_total(Integer user_ratings_total) {
        this.user_ratings_total = user_ratings_total;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getFormatted_phone_number() {
        return formatted_phone_number;
    }

    public void setFormatted_phone_number(String formatted_phone_number) {
        this.formatted_phone_number = formatted_phone_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public PlaceOpeningHours getOpening_hours() {
        return opening_hours;
    }

    public void setOpening_hours(PlaceOpeningHours opening_hours) {
        this.opening_hours = opening_hours;
    }
}
