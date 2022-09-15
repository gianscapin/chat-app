package com.gscapin.chattapp.data.model

data class Chat(
    var text: List<Message>? = null,
    val user: List<User>? = null
)
