package com.gscapin.chattapp.data.remote.chat

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.gscapin.chattapp.data.model.Chat
import com.gscapin.chattapp.data.model.Message
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatDatasource @Inject constructor() {

    fun getMessages(idChat: String): MutableList<Message> {
        val listMessages: MutableList<Message> = mutableListOf()
        FirebaseFirestore.getInstance().collection("chat").document(idChat)
            .addSnapshotListener { snapshot, e ->
                getList(snapshot, listMessages)
            }
        return listMessages
    }

    private fun getList(
        snapshot: DocumentSnapshot?,
        listMessages: MutableList<Message>
    ) {
        val chat = snapshot!!.toObject(Chat::class.java)
        for (messageChat in chat?.text!!) {
            listMessages.add(messageChat)
        }
    }

    suspend fun sendMessage(text: String, idChat: String) {
        val chatFirebase =
            FirebaseFirestore.getInstance().collection("chat").document(idChat).get().await()

        val user = FirebaseAuth.getInstance().currentUser

        val chat = chatFirebase.toObject(Chat::class.java)

        var listMessages = chat!!.text?.toMutableList()

        listMessages?.add(
            Message(
                text = text,
                date = Timestamp.now().toDate(),
                idUser = user!!.uid
            )
        )

        chat.apply {
            chat.text = listMessages!!.toList()
        }

        FirebaseFirestore.getInstance().collection("chat").document(idChat)
            .set(chat, SetOptions.merge()).await()

    }

}