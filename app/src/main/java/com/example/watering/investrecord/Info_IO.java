package com.example.watering.investrecord;

import java.io.Serializable;

/**
 * Created by watering on 17. 10. 23.
 */

public class Info_IO implements Serializable {
    String date;
    int id_account;
    int input;
    int output;
    int evaluation;
    int id;

    public void setId(int id) {
        this.id = id;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setInput(int input) {
        this.input = input;
    }
    public void setOutput(int output) {
        this.output = output;
    }
    public void setAccount(int id_account) {
        this.id_account = id_account;
    }
    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

    public int getId() {
        return id;
    }
    public String getDate() {
        return date;
    }
    public int getAccount() {
        return id_account;
    }
    public int getInput() {
        return input;
    }
    public int getOutput() {
        return output;
    }
    public int getEvaluation() {
        return evaluation;
    }
}
