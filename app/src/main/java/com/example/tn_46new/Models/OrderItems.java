package com.example.tn_46new.Models;

public class OrderItems {

    private String cid="";
    private String image="";
    private String name="";
    private String qty="";
    private String sellerid="";
    private String totalprice="";
    private String unitprice="";
    private String variant="";

    public OrderItems() {
    }

    public OrderItems(String cid, String image, String name, String qty, String sellerid, String totalprice, String unitprice, String variant) {
        this.cid = cid;
        this.image = image;
        this.name = name;
        this.qty = qty;
        this.sellerid = sellerid;
        this.totalprice = totalprice;
        this.unitprice = unitprice;
        this.variant = variant;
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

    public String getSellerid() {
        return sellerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
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
}
