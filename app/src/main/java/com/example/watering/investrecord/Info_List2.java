package com.example.watering.investrecord;

/**
 * Created by watering on 17. 10. 23.
 */

@SuppressWarnings("DefaultFileTemplate")
public class Info_List2 {
    private Info_Dairy dairy = null;
    private int evaluation = 0;

    public void setDairy(Info_Dairy dairy) {
        this.dairy = dairy;
    }
    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }
    public Info_Dairy getDairy() {
        return dairy;
    }
    public int getEvaluation() {
        return evaluation;
    }
}
