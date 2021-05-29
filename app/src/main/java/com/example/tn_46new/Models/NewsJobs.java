package com.example.tn_46new.Models;

public class NewsJobs {

    private String title="";
    private String descri="";
    private String type="";
    private String uid="";
    private String date="";
    private String time="";
    private String search="";

    public NewsJobs() {
    }

    public NewsJobs(String title, String descri, String type, String uid, String date, String time, String search) {
        this.title = title;
        this.descri = descri;
        this.type = type;
        this.uid = uid;
        this.date = date;
        this.time = time;
        this.search = search;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescri() {
        return descri;
    }

    public void setDescri(String descri) {
        this.descri = descri;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
