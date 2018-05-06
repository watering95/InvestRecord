package com.example.watering.investrecord.Data

import java.io.Serializable

/**
 * Created by watering on 17. 10. 23.
 */

class Account : Serializable {
    var id = -1
    var group = -1
    var institute: String? = null
    var number: String? = null
    var description: String? = null
}
