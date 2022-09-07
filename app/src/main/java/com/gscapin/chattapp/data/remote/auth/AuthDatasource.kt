package com.gscapin.chattapp.data.remote.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gscapin.chattapp.data.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthDatasource @Inject constructor() {

    suspend fun signIn(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
    }

    suspend fun signUp(email: String, password: String, nickname: String) {
        val result =
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()

        FirebaseFirestore.getInstance().collection("users").document(result.user?.uid!!).set(
            User(username = nickname, email = email)
        ).await()
    }
}