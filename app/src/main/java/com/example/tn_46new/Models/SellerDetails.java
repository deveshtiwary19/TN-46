package com.example.tn_46new.Models;

public class SellerDetails {

    private String name="";
    private String shopname="";
    private String contact="";
    private String email="";
    private String adress="";

    public SellerDetails() {
    }

    public SellerDetails(String name, String shopname, String contact, String email, String adress) {
        this.name = name;
        this.shopname = shopname;
        this.contact = contact;
        this.email = email;
        this.adress = adress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
}
