package com.example.socialmedia.daos

import com.example.socialmedia.Model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDao {

    val db = FirebaseFirestore.getInstance()
    val userCollection = db.collection("users")

    @OptIn(DelicateCoroutinesApi::class)
    fun addUser(user: User?){

        user?.let {
            GlobalScope.launch(Dispatchers.IO) {
                userCollection.document(user.uid).set(it)
            }
        }

    }

    fun getUserById(uId: String): Task<DocumentSnapshot>{
        return userCollection.document(uId).get()
    }
}