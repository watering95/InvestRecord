package com.example.watering.investrecord;

import java.io.Serializable;

/**
 * Created by watering on 17. 10. 23.
 */

@SuppressWarnings("ALL")
public class CategorySub implements Serializable {
    private int id_category_sub;
    private String name;
    private int getId_category_main;
}
