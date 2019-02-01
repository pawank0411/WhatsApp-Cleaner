package com.example.pawan.whatsupcleaner.datas;


import org.parceler.Parcel;

@Parcel
public class FileDetails {

    // TODO: 1/19/19 Values we are interested in: Name, Path, Size for now
    public String name;
    public String path;
    public String size;
    public boolean isclicked=false;

    public FileDetails() {


    }

    public FileDetails(String name, String path, String size, boolean isclicked) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.isclicked = isclicked;
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
    @Override
    public String toString() {
        return "FileDetails{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
