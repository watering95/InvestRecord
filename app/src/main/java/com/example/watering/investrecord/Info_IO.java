package com.example.watering.investrecord;

import java.io.Serializable;

/**
 * Created by watering on 17. 10. 23.
 */

@SuppressWarnings("ALL")
public class Info_IO implements Serializable {
    private String date;
    private int id_account;
    private int input;
    private int output;
    private int income;
    private int spend;
    private int evaluation;
    private int id;

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
    public void setIncome(int income) {
        this.income = income;
    }
    public void setSpend(int spend) {
        this.spend = spend;
    }

    public int getId() {
        return this.id;
    }
    public String getDate() {
        return this.date;
    }
    public int getAccount() {
        return this.id_account;
    }
    public int getInput() {
        return this.input;
    }
    public int getOutput() {
        return this.output;
    }
    public int getEvaluation() {
        return this.evaluation;
    }
    public int getIncome() {
        return this.income;
    }
    public int getSpend() {
        return this.spend;
    }
}
