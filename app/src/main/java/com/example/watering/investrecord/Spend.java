package com.example.watering.investrecord;

import java.io.Serializable;

/**
 * Created by watering on 17. 10. 23.
 */

@SuppressWarnings("ALL")
public class Spend implements Serializable {
    private int id_spend;
    private int id_category_sub;
    private String date_use;
    private int amount;
}