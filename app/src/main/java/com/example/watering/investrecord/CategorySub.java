package com.example.watering.investrecord;

import java.io.Serializable;

/**
 * Created by watering on 17. 10. 23.
 */

@SuppressWarnings("DefaultFileTemplate")
class CategorySub implements Serializable {
    private int id_category_sub = -1;
    private String name = null;
    private int getId_category_main = -1;

    public void setId(int id) {
        this.id_category_sub = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCategoryMain(int id) {
        this.getId_category_main = id;
    }

    public int getId() {
        return this.id_category_sub;
    }
    public String getName() {
        return this.name;
    }
    public int getCategoryMain() {
        return this.getId_category_main;
    }
}
