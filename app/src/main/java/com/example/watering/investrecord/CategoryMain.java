package com.example.watering.investrecord;

import java.io.Serializable;

/**
 * Created by watering on 17. 10. 23.
 */

@SuppressWarnings("DefaultFileTemplate")
public class CategoryMain implements Serializable {
    private int id_category_main = -1;
    private String name = null;
    private String kind = null;

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
        return this.id_category_main;
    }
    public String getName() {
        return this.name;
    }
    public String getKind() {
        return this.kind;
    }
}
