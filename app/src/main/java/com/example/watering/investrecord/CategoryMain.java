package com.example.watering.investrecord;

import java.io.Serializable;

/**
 * Created by watering on 17. 10. 23.
 */

@SuppressWarnings("ALL")
public class CategoryMain implements Serializable {
    private int id_category_main;
    private String name;
    private String kind;

    public void setId(int id) {
        this.id_category_main = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getId() {
        return id_category_main;
    }
    public String getName() {
        return name;
    }
    public String getKind() {
        return kind;
    }
}
