package com.example.promoapps.viewmodel.admin

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class AdminLoginViewModel: ViewModel() {
    lateinit var email: String
    lateinit var password: String

}