package com.example.tn_46new.Models;

public class Orders {

    private String orderid="";
    private String customerid="";
    private String date="";
    private String time="";
    private String total="";
    private String status="";
    private String payment="";
    private String delname="";
    private String delcontact="";
    private String deladress="";


    public Orders() {
    }

    public Orders(String orderid, String customerid, String date, String time, String total, String status, String payment, String delname, String delcontact, String deladress) {
        this.orderid = orderid;
        this.customerid = customerid;
        this.date = date;
        this.time = time;
        this.total = total;
        this.status = status;
        this.payment = payment;
        this.delname = delname;
        this.delcontact = delcontact;
        this.deladress = deladress;
    }


    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getDelname() {
        return delname;
    }

    public void setDelname(String delname) {
        this.delname = delname;
    }

    public String getDelcontact() {
        return delcontact;
    }

    public void setDelcontact(String delcontact) {
        this.delcontact = delcontact;
    }

    public String getDeladress() {
        return deladress;
    }

    public void setDeladress(String deladress) {
        this.deladress = deladress;
    }
}
