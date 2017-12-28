package com.example.watering.investrecord;

import java.io.Serializable;

/**
 * Created by watering on 17. 10. 23.
 */

@SuppressWarnings("ALL")
public class SpendSchedule implements Serializable {
    private int id_spend_schedule;
    private int id_spend;
    private String date_draw;
    private int id_account;
    private int id_card;

    public void setId(int id) {
        this.id_spend_schedule = id;
    }
    public void setSpend(int id) {
        this.id_spend = id;
    }
    public void setDate(String date) {
        this.date_draw = date;
    }
    public void setAccount(int id) {
        this.id_account = id;
    }
    public void setCard(int id) {
        this.id_card = id;
    }

    public int getId() {
        return this.id_spend_schedule;
    }
    public int getSpend() {
        return this.id_spend;
    }
    public String getDate() {
        return this.date_draw;
    }
    public int getAccount() {
        return this.id_account;
    }
    public int getCard() {
        return this.id_card;
    }
}
