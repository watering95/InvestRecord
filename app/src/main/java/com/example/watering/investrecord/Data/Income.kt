package com.example.watering.investrecord.Data

import java.io.Serializable

/**
 * Created by watering on 17. 10. 23.
 */

class Income : Serializable {
    var id = -1
    var date: String? = null
    var category = -1
    var amount = -1
    var details: String? = null
    var account = -1
}
