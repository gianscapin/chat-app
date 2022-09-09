package com.gscapin.chattapp.domain.contact

import com.gscapin.chattapp.data.model.ContactMessage
import javax.inject.Singleton

@Singleton
interface ContactRepo {
    suspend fun getContactList(): List<ContactMessage>
}