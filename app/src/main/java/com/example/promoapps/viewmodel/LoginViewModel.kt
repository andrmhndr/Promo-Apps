package com.example.promoapps.viewmodel

import androidx.lifecycle.ViewModel
import com.example.promoapps.adapter.Helper
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class LoginViewModel: ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var role: String? = null

    suspend fun check(currentUser: FirebaseUser) {
        if (currentUser != null) {
            db.collection(Helper.ROLE).document(currentUser.uid).get().addOnSuccessListener { document->
                role = document?.getString("role")
            }.await()
        }
    }


    suspend fun addUser(currentUser: FirebaseUser?){
        if (currentUser != null){
            val user = hashMapOf(
                    "name" to currentUser.displayName,
                    "email" to currentUser.email,
                    "role" to Helper.USER
            )
            db.collection(Helper.ROLE).document(currentUser.uid).set(user).await()
        }
    }
}