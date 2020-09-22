package com.example.eatathome.Server.Activities.Models;

public class FoodRes {

    private String name,image,description,price,menuId;

    public FoodRes(){

    }

    public FoodRes(String name, String image, String description, String price, String menuId) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
        this.menuId = menuId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
