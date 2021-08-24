package com.autobots.innopark.data;

public class MenuItemList {

    private int image_resource;
    private String text;

    public MenuItemList(int image_resource, String text) {
        this.image_resource = image_resource;
        this.text = text;
    }

    public int getImage_resource() {
        return image_resource;
    }

    public void setImage_resource(int image_resource) {
        this.image_resource = image_resource;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
