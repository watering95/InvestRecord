package com.example.watering.investrecord;

import java.io.Serializable;

/**
 * Created by watering on 17. 10. 23.
 */

@SuppressWarnings("DefaultFileTemplate")
public class SpendCard implements Serializable {
    private int id_spend_card = -1;
    private String spend_code = null;
    private int id_card = -1;

    public void setId(int id) {
        this.id_spend_card = id;
    }
    public void setCode(String code) {
        this.spend_code = code;
    }
    public void setCard(int id) {
        this.id_card = id;
    }

    public int getId() {
        return this.id_spend_card;
    }
    public String getCode() {
        return this.spend_code;
    }
    public int getCard() {
        return this.id_card;
    }
}
