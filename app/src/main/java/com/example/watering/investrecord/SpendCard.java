package com.example.watering.investrecord;

import java.io.Serializable;

/**
 * Created by watering on 17. 10. 23.
 */

@SuppressWarnings("ALL")
public class SpendCard implements Serializable {
    private int id_spend_card;
    private int id_spend;
    private int id_card;

    public void setId(int id) {
        this.id_spend_card = id;
    }
    public void setSpend(int id) {
        this.id_spend = id;
    }
    public void setCard(int id) {
        this.id_card = id;
    }

    public int getId() {
        return this.id_spend_card;
    }
    public int getSpend() {
        return this.id_spend;
    }
    public int getCard() {
        return this.id_card;
    }
}
