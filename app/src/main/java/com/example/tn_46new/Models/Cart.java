package com.example.tn_46new.Models;

public class Cart {

    private String cid="";
    private String image="";
    private String name="";
    private String qty="";
    private String totalprice="";
    private String unitprice="";
    private String variant="";
    private String sellerid="";

    //The empty constructor

    public Cart() {

    }


    public Cart(String cid, String image, String name, String qty, String totalprice, String unitprice, String variant, String sellerid) {
        this.cid = cid;
        this.image = image;
        this.name = name;
        this.qty = qty;
        this.totalprice = totalprice;
        this.unitprice = unitprice;
        this.variant = variant;
        this.sellerid = sellerid;
    }


    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
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

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(String unitprice) {
        this.unitprice = unitprice;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getSellerid() {
        return sellerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
    }
}
