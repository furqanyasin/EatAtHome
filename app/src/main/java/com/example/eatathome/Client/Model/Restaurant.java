package com.example.eatathome.Client.Model;

public class Restaurant {

    private String name;
    private String image;
    private Float latitude;
    private Float longitude;
    private String location;

    public Restaurant() {
    }

    public Restaurant(String name, String image, Float latitude, Float longitude, String location) {
        this.name = name;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
    }

  /*    public Restaurant(String name, String image) {
        this.name = name;
        this.image = image;
    }*/

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}