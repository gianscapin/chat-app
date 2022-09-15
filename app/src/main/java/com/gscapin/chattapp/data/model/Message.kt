package com.gscapin.chattapp.data.model

import java.util.*

data class Message(
    val text: String = "",
    val date: Date? = null,
    val idUser: String = ""
)
