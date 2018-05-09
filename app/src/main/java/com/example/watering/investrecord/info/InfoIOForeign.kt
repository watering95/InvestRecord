package com.example.watering.investrecord.info

import java.io.Serializable

/**
 * Created by watering on 17. 10. 23.
 */

class InfoIOForeign : Serializable {
    var date: String? = null
    var account = -1
    var input = 0
    var output = 0
    var input_krw = 0
    var output_krw = 0
    var currency = 0
    var evaluation = 0
    var id = -1
}
