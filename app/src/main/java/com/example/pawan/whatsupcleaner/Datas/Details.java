package com.example.pawan.whatsupcleaner.Datas;

public class Details {

    public Details(String title, int data){
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

    private String title;
    private int data;
}
