package com.example.tn_46new.Models;

public class Sellers {
    private String email="";
    private String reg="";
    private String uid="";

    public Sellers() {
    }

    public Sellers(String email, String reg, String uid) {
        this.email = email;
        this.reg = reg;
        this.uid = uid;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
