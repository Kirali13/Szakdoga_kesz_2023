package com.example.myapplication;


import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Tr_Object implements Serializable {
    @Exclude
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String userID;
    private String tr_name;
    private int tr_amount;
    public String tr_date;

    public String getTr_date() {
        return tr_date;
    }

    public enum Currency{
        FT,
        EUR,
        USD,
        OTHER
    }

    public enum Type{
        KÉSZPÉNZES,
        KÁRTYÁS
    }

    public enum Group{
        ÉLELMISZER,
        SZÓRAKOZÁS,
        BEVÉTEL,
        EGYÉB_KIADÁS
    }

    private Group group;
    private Currency currency;
    private Type type;

    public Tr_Object() {
        // Default constructor required for Firestore
    }



    public Tr_Object(String userID, String tr_name, int tr_amount, Currency currency, Type type, String tr_date, Group group) {
        this.userID = userID;
        this.tr_name = tr_name;
        this.tr_amount = tr_amount;
        this.currency = currency;
        this.type = type;
        this.tr_date = tr_date;
        this.group = group;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTr_name() {
        return tr_name;
    }

    public void setTr_name(String tr_name) {
        this.tr_name = tr_name;
    }

    public int getTr_amount() {
        return tr_amount;
    }

    public void setTr_date(String tr_date) {
        this.tr_date = tr_date;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setTr_amount(int tr_amount) {
        this.tr_amount = tr_amount;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
