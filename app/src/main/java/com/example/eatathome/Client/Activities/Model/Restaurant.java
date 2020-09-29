package com.example.eatathome.Client.Activities.Model;

public class Restaurant {

    private String name;
    private String image;
    private String location;

    public Restaurant() {
    }

    public Restaurant(String name, String image, String location) {
        this.name = name;
        this.image = image;
        this.location = location;
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