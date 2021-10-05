package com.example.promoapps.model

import java.util.*

data class PromoModel(
    var id: String? = null,
    var title: String? = null,
    var timestamp: Date? = null,
    var description: String? = null,
    var limit: Int? = null
)
