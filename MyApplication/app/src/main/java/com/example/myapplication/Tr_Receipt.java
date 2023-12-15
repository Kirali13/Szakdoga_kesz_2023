package com.example.myapplication;

public class Tr_Receipt {
    private String userID;
    private String tr_rec_name;
    private String tr_rec_supplier;
    private String tr_rec_date;
    private String tr_rec_url;

    public Tr_Receipt() {
    }

    public Tr_Receipt(String userID, String tr_rec_name, String tr_rec_supplier, String tr_rec_date, String tr_rec_url) {
        this.userID = userID;
        this.tr_rec_name = tr_rec_name;
        this.tr_rec_supplier = tr_rec_supplier;
        this.tr_rec_date = tr_rec_date;
        this.tr_rec_url = tr_rec_url;
    }

    public String getTr_rec_url() {
        return tr_rec_url;
    }

    public void setTr_rec_url(String tr_rec_url) {
        this.tr_rec_url = tr_rec_url;
    }

    public String getTr_rec_date() {
        return tr_rec_date;
    }

    public void setTr_rec_date(String tr_rec_date) {
        this.tr_rec_date = tr_rec_date;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTr_rec_name() {
        return tr_rec_name;
    }

    public void setTr_rec_name(String tr_rec_name) {
        this.tr_rec_name = tr_rec_name;
    }

    public String getTr_rec_supplier() {
        return tr_rec_supplier;
    }

    public void setTr_rec_supplier(String tr_rec_supplier) {
        this.tr_rec_supplier = tr_rec_supplier;
    }
}
