package com.example.promoapps.viewmodel.admin

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.promoapps.adapter.Helper
import com.example.promoapps.model.HistoryModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AdminScanViewModel: ViewModel(){
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var historyModel: HistoryModel

    fun promoCheck(promoId: String, user: String, userid: String,currentUser: FirebaseUser, context: Context){
        db.collection(Helper.PROMOS).document(promoId).get().addOnSuccessListener { document ->
            if (document.exists()){
                val limit = document.getDouble(Helper.LIMIT)
                setHistory(
                    document.getString(Helper.TITLE).toString(),
                    document.getString(Helper.DESCRIPTION).toString(),
                    user,
                    userid,
                    currentUser.uid,
                    limit,
                    promoId
                )
                Toast.makeText(context, "Promo ${document.getString(Helper.TITLE)} Berhasil digunakan oleh $user", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "Promo tidak tersedia", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setHistory(title: String?, description: String, user: String, userid: String, adminid: String?, limit: Double?, promoId: String){
        historyModel = HistoryModel(
            null,
            title,
            Calendar.getInstance().time,
            description,
            user,
            userid,
            adminid
        )
        db.collection(Helper.HISTORY).add(historyModel).addOnSuccessListener {
            if (limit != null){
                if (limit < 2){
                    db.collection(Helper.PROMOS).document(promoId).delete()
                }
                else{
                    db.collection(Helper.PROMOS).document(promoId).update(Helper.LIMIT, limit - 1)
                }
            }
        }
    }

}