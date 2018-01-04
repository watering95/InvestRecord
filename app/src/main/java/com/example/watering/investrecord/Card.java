package com.example.watering.investrecord;

import java.io.Serializable;

/**
 * Created by watering on 17. 10. 23.
 */

@SuppressWarnings("ALL")
public class Card implements Serializable {
    private int id_card;
    private String number;
    private String company;
    private String name;
    private int date_draw;
    private int id_account;

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
