package com.example.promoapps.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.promoapps.adapter.Helper
import com.example.promoapps.model.PromoModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.*

class AddPromoViewModel: ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var promoModel: PromoModel

    fun setPromo(id: String?, title: String?, date: Date?, description: String?){
        promoModel = PromoModel(id, title, date, description)
    }

    fun addPromo(context: Context){
        val promo = hashMapOf(
            "title" to promoModel.title,
            "description" to promoModel.description,
            "timestamp" to Calendar.getInstance().time
        )
        db.collection(Helper.PROMOS).add(promo).addOnSuccessListener { document->
            Toast.makeText(context, "Promo Successfully added with id : ${document.id}", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e->
            Toast.makeText(context, "Promo add Failed : $e", Toast.LENGTH_SHORT).show()
        }
    }
}