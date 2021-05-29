package com.example.tn_46new.Models;

public class Banners {

    private String Banner="";
    private String Add="";

    public Banners() {
    }

    public Banners(String banner, String add) {
        Banner = banner;
        Add = add;
    }

    public String getBanner() {
        return Banner;
    }

    public void setBanner(String banner) {
        Banner = banner;
    }

    public String getAdd() {
        return Add;
    }

    public void setAdd(String add) {
        Add = add;
    }
}
