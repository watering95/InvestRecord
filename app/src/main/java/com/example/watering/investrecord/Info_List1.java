package com.example.watering.investrecord;

/**
 * Created by watering on 17. 10. 23.
 */

@SuppressWarnings("ALL")
class Info_List1 {
    private Account account;
    private Info_List2 list2;

    public void setInfoList2(Info_List2 list2) {
        this.list2 = list2;
    }
    public void setAccount(Account account) {
        this.account = account;
    }
    public Info_List2 getList2() {
        return list2;
    }
    public Account getAccount() {
        return account;
    }
}
