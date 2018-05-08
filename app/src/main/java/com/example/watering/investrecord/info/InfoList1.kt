package com.example.watering.investrecord.info

import com.example.watering.investrecord.data.Account

/**
 * Created by watering on 17. 10. 23.
 */

class InfoList1 {
    var account: Account? = null
    var list6: InfoList6? = null
        private set

    fun setInfoList6(list6: InfoList6) {
        this.list6 = list6
    }
}
