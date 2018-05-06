package com.example.watering.investrecord.Info

import java.io.Serializable

/**
 * Created by watering on 17. 10. 23.
 */

class Info_IO : Serializable {
    var date: String? = null
    var account = -1
    var input = 0
    var output = 0
    var income = 0
    var spendCash = 0
    var spendCard = 0
    var evaluation = 0
    var id = -1
}
