package com.example.eatathome.Administrator.Model;

public class RestaurantsAdmin {

    private String name;
    private String image;
    private String id;
    private String location;

    public RestaurantsAdmin() {
    }

    public RestaurantsAdmin(String name, String image, String id, String location) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.location = location;
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
