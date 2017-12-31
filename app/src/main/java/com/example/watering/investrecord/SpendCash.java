package com.example.watering.investrecord;

import java.io.Serializable;

/**
 * Created by watering on 17. 10. 23.
 */

@SuppressWarnings("ALL")
public class SpendCash implements Serializable {
    private int id_spend_cash;
    private String spend_code;
    private int id_account;

    public void setId(int id) {
        this.id_spend_cash = id;
    }
    public void setCode(String code) {
        this.spend_code = code;
    }
    public void setAccount(int id) {
        this.id_account = id;
    }

    public int getId() {
        return this.id_spend_cash;
    }
    public String getCode() {
        return this.spend_code;
    }
    public int getAccount() {
        return this.id_account;
    }
}
