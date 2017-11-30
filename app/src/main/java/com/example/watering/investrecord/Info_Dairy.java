package com.example.watering.investrecord;

import java.io.Serializable;

/**
 * Created by watering on 17. 10. 23.
 */

public class Info_Dairy implements Serializable {
    String date;
    int id_account;
    int principal;
    double rate;
    int id;

    public void setId(int id) {
        this.id = id;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setAccount(int id_account) {
        this.id_account = id_account;
    }
    public void setPrincipal(int principal) {
        this.principal = principal;
    }
    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getDate() {
        return date;
    }
    public int getAccount() {
        return id_account;
    }
    public int getPrincipal() {
        return principal;
    }
    public double getRate() {
        return rate;
    }
    public int getId() {
        return id;
    }
}
