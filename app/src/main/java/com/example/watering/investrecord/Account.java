package com.example.watering.investrecord;

import java.io.Serializable;

/**
 * Created by watering on 17. 10. 23.
 */

@SuppressWarnings("DefaultFileTemplate")
public class Account implements Serializable {
    private int id_account = -1;
    private int id_group = -1;
    private String institute = null;
    private String number = null;
    private String description = null;

    public void setId(int id_account) {
        this.id_account = id_account;
    }
    public void setInstitute(String institute) {
        this.institute = institute;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setGroup(int id_group) {
        this.id_group = id_group;
    }

    public int getId() {
        return this.id_account;
    }
    public int getGroup() {
        return this.id_group;
    }
    public String getNumber() {
        return this.number;
    }
    public String getInstitute() {
        return this.institute;
    }
    public String getDescription() {
        return this.description;
    }
}
