package com.example.watering.investrecord;

import java.io.Serializable;

/**
 * Created by watering on 17. 10. 23.
 */

public class Info_Dairy implements Serializable {
    String date;
    Account account;
    int principal;
    int evaluation;
    double rate;

    public Info_Dairy (String date, int id_account, int principal, int evaluation, double rate) {
        this.date = date;
        this.account.id_account = id_account;
        this.principal = principal;
        this.evaluation = evaluation;
        this.rate = rate;
    }
}
