package com.example.gl.googleApiMaps;

public class StoresPlaces {

    String name;
    String place_id;
    String vicinity;
    Geometry geometry;
    float distance;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;

    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "StoresPlaces{" +
                "name='" + name + '\'' +
                ", place_id='" + place_id + '\'' +
                ", vicinity='" + vicinity + '\'' +
                ", geometry=" + geometry.location.latitude + "," +geometry.location.longitude +
                ", distance= " + distance +
                '}';
    }

}


