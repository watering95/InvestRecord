package com.example.watering.investrecord;

import java.io.Serializable;

/**
 * Created by watering on 17. 10. 23.
 */

@SuppressWarnings("ALL")
public class Spend implements Serializable {
    private int id_spend;
    private int id_open;
    private String details;
    private int id_category_sub;
    private String date_use;
    private int amount;

    public void setId(int id) {
        this.id_spend = id;
    }
    public void setIdOpen(int id) {
        this.id_open = id;
    }
    public void setDetails(String details) {
        this.details = details;
    }
    public void setDate(String date) {
        this.date_use = date;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public void setCategory(int id) {
        this.id_category_sub = id;
    }

    public int getId() {
        return this.id_spend;
    }
    public int getIdOpen() {
        return this.id_open;
    }
    public String getDetails() {
        return this.details;
    }
    public String getDate() {
        return this.date_use;
    }
    public int getAmount() {
        return this.amount;
    }
    public int getCategory() {
        return this.id_category_sub;
    }
}
