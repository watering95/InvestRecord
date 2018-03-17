package com.example.watering.investrecord.Info;

import java.io.Serializable;

/**
 * Created by watering on 17. 10. 23.
 */

@SuppressWarnings("DefaultFileTemplate")
public class Info_IO implements Serializable {
    private String date = null;
    private int id_account = -1;
    private int input = 0;
    private int output = 0;
    private int income = 0;
    private int spend_cash = 0;
    private int spend_card = 0;
    private int evaluation = 0;
    private int id = -1;

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
    public void setSpendCash(int spend) {
        this.spend_cash = spend;
    }
    public void setSpendCard(int spend) {
        this.spend_card = spend;
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
    public int getSpendCash() {
        return this.spend_cash;
    }
    public int getSpendCard() {
        return this.spend_card;
    }
}
