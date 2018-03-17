package com.example.watering.investrecord.Data;

import java.io.Serializable;

/**
 * Created by watering on 17. 10. 23.
 */

@SuppressWarnings("DefaultFileTemplate")
public class Spend implements Serializable {
    private int id = -1;
    private String spend_code = null;
    private String details = null;
    private int id_category_sub = -1;
    private String date_use = null;
    private int amount = 0;

    public void setId(int id) {
        this.id = id;
    }
    public void setCode(String code) {
        this.spend_code = code;
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
        return this.id;
    }
    public String getCode() {
        return this.spend_code;
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
