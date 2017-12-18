package com.example.watering.investrecord;

import java.io.Serializable;

/**
 * Created by watering on 17. 10. 23.
 */

@SuppressWarnings("ALL")
public class Income implements Serializable {
    private int id_income;
    private String date;
    private int id_category_sub;
    private int amount;
    private String details;
    private int id_account;
}
