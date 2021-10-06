package com.example.promoapps.model

import java.util.*

data class HistoryModel(
    var id: String? = null,
    var title: String? = null,
    var timestamp: Date? = null,
    var description: String? = null,
    var user: String? = null,
    var userid: String? = null,
    var adminid: String? = null
)
