package com.example.promoapps.viewmodel

import androidx.lifecycle.ViewModel
import com.example.promoapps.adapter.Helper
import com.example.promoapps.model.PromoModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.*

class DetailPromoViewModel: ViewModel() {
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var promoModel: PromoModel
    lateinit var role: String

    private fun setPromo(id: String?, title: String?, date: Date, description: String?){
        promoModel = PromoModel(id, title, date, description)
    }

    suspend fun getPromo(id: String?){
        if (id != null ){
            db.collection(Helper.PROMOS).document(id).get().addOnSuccessListener { document->
                setPromo(document.id, document.getString("title"), document.getDate("timestamp")!!, document.getString("description"))
            }.await()
        }
    }
}