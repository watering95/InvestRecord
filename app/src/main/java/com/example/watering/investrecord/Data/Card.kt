package com.example.watering.investrecord.Data

import java.io.Serializable

/**
 * Created by watering on 17. 10. 23.
 */

class Card : Serializable {
    var id = -1
    var number: String? = null
    var company: String? = null
    var name: String? = null
    var drawDate = -1
    var account = -1
}
