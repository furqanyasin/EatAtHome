package com.example.eatathome.Server.Activities.Models;

public class CategoryRes {

    private String Name;
    private String Image;

    public CategoryRes(){

    }

    public CategoryRes(String name, String image){
        Name = name;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
