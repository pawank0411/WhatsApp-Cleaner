package com.example.pawan.whatsupcleaner.datas;

public class Details {

    public Details(String title, int data, int image, int color){
        this.title = title;
        this.data = data;
        this.image = image;
        this.color = color;
    }

    public Details(String title, int data) {
        this.title = title;
        this.data = data;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    private String title;
    private int data;
    private int image;
    private int color;
}
