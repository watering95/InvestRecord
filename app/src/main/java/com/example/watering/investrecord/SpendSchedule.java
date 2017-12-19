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
}
