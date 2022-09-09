package com.gscapin.chattapp.data.remote.contact

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gscapin.chattapp.data.model.ContactMessage
import com.gscapin.chattapp.data.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ContactDatasource @Inject constructor() {

    suspend fun getContactListFromUser(): List<ContactMessage> {
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

        val userFirebase =
            FirebaseFirestore.getInstance().collection("users").document(currentUserId).get()
                .await()

        val user = userFirebase.toObject(User::class.java)

        return if (user!!.contacts?.isNotEmpty() == true) {
            user.contacts!!
        } else {
            emptyList()
        }
    }
}