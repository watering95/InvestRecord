package com.example.watering.investrecord.info

import java.io.Serializable

/**
 * Created by watering on 17. 10. 23.
 */

class InfoDairyKRW : Serializable {
    var date: String? = null
    var account = -1
    var principal = 0
    var rate = 0.0
    var id = -1
}
