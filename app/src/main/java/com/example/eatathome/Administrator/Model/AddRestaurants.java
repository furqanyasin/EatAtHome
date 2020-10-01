package com.example.eatathome.Administrator.Model;

public class AddRestaurants {

    private String name;
    private String image;
    private String id;
    private String location;
    private String latitude,longitude;

    public AddRestaurants() {
    }

    public AddRestaurants(String name, String image, String id, String location, String latitude, String longitude) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
