package com.example.watering.investrecord.Data

import java.io.Serializable

/**
 * Created by watering on 17. 10. 23.
 */

class Spend : Serializable {
    var id = -1
    var code: String? = null
    var details: String? = null
    var category = -1
    var date: String? = null
    var amount = 0
}
