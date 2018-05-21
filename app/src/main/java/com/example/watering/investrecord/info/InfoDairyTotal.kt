package com.example.watering.investrecord.info

import java.io.Serializable

/**
 * Created by watering on 17. 10. 23.
 */

class InfoDairyTotal : Serializable {
    var id = -1
    var date: String? = null
    var account = -1
    var principal = 0
    var evaluation = 0
    var rate = 0.0
}
