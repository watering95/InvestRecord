package com.example.watering.investrecord;

/**
 * Created by watering on 17. 10. 23.
 */

@SuppressWarnings("DefaultFileTemplate")
class Info_List5 {
    private String month;
    private int income;
    private int spend;
    private int change;

    public void setMonth(String month) {
        this.month = month;
    }
    public void setIncome(int income) {
        this.income = income;
    }
    public void setSpend(int spend) {
        this.spend = spend;
    }
    public void setChange(int change) {
        this.change = change;
    }
    public String getMonth() {
        return month;
    }
    public int getIncome() {
        return income;
    }
    public int getSpend() {
        return spend;
    }
    public int getChange() {
        return change;
    }
}
