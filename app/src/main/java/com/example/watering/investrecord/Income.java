package com.example.watering.investrecord;

import java.io.Serializable;

/**
 * Created by watering on 17. 10. 23.
 */

@SuppressWarnings("DefaultFileTemplate")
public class Income implements Serializable {
    private int id_income = -1;
    private String date = null;
    private int id_category_sub = -1;
    private int amount = -1;
    private String details = null;
    private int id_account = -1;

    public void setId(int id) {
        this.id_income = id;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setCategory(int id) {
        this.id_category_sub = id;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public void setDetails(String details) {
        this.details = details;
    }
    public void setAccount(int id) {
        this.id_account = id;
    }

    public int getId() {
        return this.id_income;
    }
    public String getDate() {
        return this.date;
    }
    public int getCategory() {
        return this.id_category_sub;
    }
    public int getAmount() {
        return this.amount;
    }
    public String getDetails() {
        return this.details;
    }
    public int getAccount() {
        return this.id_account;
    }
}
