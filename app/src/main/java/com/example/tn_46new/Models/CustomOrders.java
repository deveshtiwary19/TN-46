package com.example.tn_46new.Models;

public class CustomOrders {

    private String  contact="";
    private String  image="";
    private String  name="";
    private String  link="";
    private String  descri="";
    private String  orderid="";
    private String  status="";
    private String  customerid="";

    //Following is the empty contsructor

    public CustomOrders() {
    }

    public CustomOrders(String contact, String image, String name, String link, String descri, String orderid, String status, String customerid) {
        this.contact = contact;
        this.image = image;
        this.name = name;
        this.link = link;
        this.descri = descri;
        this.orderid = orderid;
        this.status = status;
        this.customerid = customerid;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescri() {
        return descri;
    }

    public void setDescri(String descri) {
        this.descri = descri;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }
}
