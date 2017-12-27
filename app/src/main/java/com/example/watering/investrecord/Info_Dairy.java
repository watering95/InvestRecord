package com.example.watering.investrecord;

import java.io.Serializable;

/**
 * Created by watering on 17. 10. 23.
 */

@SuppressWarnings({"ALL"})
public class Info_Dairy implements Serializable {
    private String date;
    private int id_account;
    private int principal;
    private double rate;
    private int id;

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
        return this.date;
    }
    public int getAccount() {
        return this.id_account;
    }
    public int getPrincipal() {
        return this.principal;
    }
    public double getRate() {
        return this.rate;
    }
    public int getId() {
        return this.id;
    }
}
