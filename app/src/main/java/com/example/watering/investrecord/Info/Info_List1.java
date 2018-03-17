package com.example.watering.investrecord.Info;

import com.example.watering.investrecord.Data.Account;

/**
 * Created by watering on 17. 10. 23.
 */

@SuppressWarnings("DefaultFileTemplate")
public class Info_List1 {
    private Account account = null;
    private Info_List6 list6 = null;

    public void setInfoList6(Info_List6 list6) {
        this.list6 = list6;
    }
    public void setAccount(Account account) {
        this.account = account;
    }
    public Info_List6 getList6() {
        return list6;
    }
    public Account getAccount() {
        return account;
    }
}
