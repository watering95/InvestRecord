package com.example.watering.investrecord.Data;

import java.io.Serializable;

/**
 * Created by watering on 17. 10. 23.
 */

@SuppressWarnings("DefaultFileTemplate")
public class Card implements Serializable {
    private int id_card = -1;
    private String number = null;
    private String company = null;
    private String name = null;
    private int date_draw = -1;
    private int id_account = -1;

    public void setId(int id) {
        this.id_card = id;
    }
    public void setNumber(String num) {
        this.number = num;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCompany(String com) {
        this.company = com;
    }
    public void setDrawDate(int date) {
        this.date_draw = date;
    }
    public void setAccount(int id) {
        this.id_account = id;
    }

    public int getId() {
        return this.id_card;
    }
    public String getNumber() {
        return this.number;
    }
    public String getCompany() {
        return this.company;
    }
    public String getName() {
        return this.name;
    }
    public int getDrawDate() {
        return this.date_draw;
    }
    public int getAccount() {
        return this.id_account;
    }
}
