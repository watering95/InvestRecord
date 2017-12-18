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
    private String date_draw;
}
