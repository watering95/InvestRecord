package com.example.watering.investrecord;

import java.io.Serializable;

/**
 * Created by watering on 17. 10. 23.
 */

public class Account implements Serializable {
    int id_account;
    String institute;
    String number;
    String discription;
    int id_group;

    public void setId(int id_account) {
        this.id_account = id_account;
    }
    public void setInstitute(String institute) {
        this.institute = institute;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public void setDiscription(String discription) {
        this.discription = discription;
    }
    public void setGroup(int id_group) {
        this.id_group = id_group;
    }

    public int getId() {
        return id_account;
    }
    public int getGroup() {
        return id_group;
    }
    public String getNumber() {
        return number;
    }
    public String getInstitute() {
        return institute;
    }
    public String getDiscription() {
        return discription;
    }
}
