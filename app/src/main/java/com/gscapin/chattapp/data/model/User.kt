package com.gscapin.chattapp.data.model

data class User(
    val username: String = "",
    val email: String = "",
    var userPhotoUrl: String = "",
    var contacts: List<ContactMessage>? = emptyList(),
    var id: String? = null
)