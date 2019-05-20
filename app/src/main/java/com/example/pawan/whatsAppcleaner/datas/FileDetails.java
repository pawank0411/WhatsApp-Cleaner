package com.example.pawan.whatsAppcleaner.datas;

public class FileDetails {


    // TODO: 1/19/19 Values we are interested in: Name, Path, Size for now
    public String name;
    public String path;
    public static int color;
    public int image;
    public String size;
    public boolean isclicked=false;

    public FileDetails() {


    }

    public FileDetails(String name, String path, String size, boolean isclicked, int image, int color) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.isclicked = isclicked;
        this.image = image;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setSelected(boolean selected) {
        isclicked = selected;
    }

    public boolean isSelected()
    {
        return isclicked;
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

    public static int getColor() {
        return color;
    }

}