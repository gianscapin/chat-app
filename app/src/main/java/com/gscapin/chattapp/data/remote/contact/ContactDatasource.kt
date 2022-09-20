package com.gscapin.chattapp.data.remote.contact

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import com.gscapin.chattapp.data.model.Chat
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

    suspend fun getUsers(): List<User> {
        val currentUser = FirebaseAuth.getInstance().currentUser

        val userList = mutableListOf<User>()

        val myUserFirebase =
            FirebaseFirestore.getInstance().collection("users").document(currentUser!!.uid).get()
                .await()

        val myUser = myUserFirebase.toObject(User::class.java)

        val usersFirebase = FirebaseFirestore.getInstance().collection("users").get().await()

        for (userFb in usersFirebase.documents) {
            val user = userFb.toObject(User::class.java)
            if (userFb.reference.id != currentUser.uid) {

                var isUserInMyList: Boolean = false

                for (contacts in myUser?.contacts!!) {
                    if (contacts.user?.username == user?.username) {
                        isUserInMyList = true
                    }
                }

                if (!isUserInMyList) userList.add(user!!)
            }
        }

        return userList
    }

    suspend fun createChat(user: User): ContactMessage {
        val myUserId = FirebaseAuth.getInstance().currentUser?.uid
        val myUserFirebase =
            FirebaseFirestore.getInstance().collection("users").document(myUserId!!).get().await()

        val myUser = myUserFirebase.toObject(User::class.java)

        val chatId = getNewChatId(user, myUser)

        modifyUsersContactLists(myUser, user, chatId, myUserId)

        return ContactMessage(
            user = user,
            idMessage = chatId
        )

    }

    private suspend fun modifyUsersContactLists(
        myUser: User?,
        user: User,
        chatId: String,
        myUserId: String?
    ) {

        val listUsers = FirebaseFirestore.getInstance().collection("users").get().await()
        var userRequest: User? = null

        for (userFirebase in listUsers.documents) {
            val us = userFirebase.toObject(User::class.java)
            if (us?.username == user.username) {
                userRequest = User(
                    username = us.username,
                    email = us.email,
                    userPhotoUrl = us.userPhotoUrl,
                    id = userFirebase.id
                )
                break
            }
        }

        addContactToUser(myUser!!, user, chatId, myUserId)
        addContactToUser(userRequest!!, myUser, chatId, userRequest?.id)
    }

    private suspend fun getNewChatId(
        user: User,
        myUser: User?
    ): String {
        val usersList: MutableList<User> = mutableListOf()
        usersList.add(user)
        usersList.add(myUser!!)

        return FirebaseFirestore.getInstance().collection("chat")
            .add(Chat(text = listOf(), user = usersList.toList())).await().id
    }

    private suspend fun addContactToUser(
        userToAddContacts: User,
        user: User,
        chatId: String,
        userToAddContactsId: String?
    ) {
        var contactList: MutableList<ContactMessage>
        if (userToAddContacts.contacts!!.isNotEmpty()) {
            contactList = userToAddContacts.contacts?.toMutableList()!!
        } else {
            contactList = mutableListOf()
        }

        contactList?.add(
            ContactMessage(
                user = User(
                    username = user.username,
                    email = user.email,
                    userPhotoUrl = user.userPhotoUrl
                ), idMessage = chatId
            )
        )

        userToAddContacts.apply {
            userToAddContacts.contacts = contactList
        }
        FirebaseFirestore.getInstance().collection("users").document(userToAddContactsId!!)
            .set(userToAddContacts, SetOptions.merge()).await()
    }
}