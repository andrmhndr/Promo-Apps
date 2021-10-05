package com.example.promoapps.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.promoapps.adapter.Helper
import com.example.promoapps.model.PromoModel
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AddPromoViewModel: ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var promoModel: PromoModel

    fun setPromo(title: String?, description: String?, limit: Int?){
        promoModel = PromoModel(null, title, Calendar.getInstance().time, description, limit)
    }

    fun addPromo(context: Context){
        val promo = hashMapOf(
            Helper.TITLE to promoModel.title,
            Helper.TIMESTAMP to promoModel.timestamp,
            Helper.DESCRIPTION to promoModel.description,
            Helper.LIMIT to promoModel.limit
        )
        db.collection(Helper.PROMOS).add(promo).addOnSuccessListener { document->
            Toast.makeText(context, "Promo Successfully added with id : ${document.id}", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e->
            Toast.makeText(context, "Promo add Failed : $e", Toast.LENGTH_SHORT).show()
        }
    }
}