package com.example.watering.investrecord;

import java.sql.Date;

/**
 * Created by watering on 17. 10. 23.
 */

public class Info_IO {
    String date;
    Account account;
    int input;
    int output;

    public Info_IO (String date, int id_account, int input, int output) {
        this.date = date;
        this.account.id = id_account;
        this.input = input;
        this.output = output;
    }
}
