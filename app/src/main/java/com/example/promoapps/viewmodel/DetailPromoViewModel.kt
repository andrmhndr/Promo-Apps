package com.example.promoapps.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import com.example.promoapps.adapter.Helper
import com.example.promoapps.model.PromoModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.IOException
import java.util.*

class DetailPromoViewModel: ViewModel() {
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var promoModel: PromoModel
    lateinit var role: String

    private fun setPromo(id: String?, title: String?, date: Date, description: String?, limit: Double?, image: String?){
        promoModel = if (limit != null) {
            PromoModel(id, title, date, description, limit.toInt(), image)
        }else{
            PromoModel(id, title, date, description, null, image)
        }
    }

    suspend fun getPromo(id: String?){
        if (id != null ){
            db.collection(Helper.PROMOS).document(id).get().addOnSuccessListener { document->
                setPromo(document.id,
                    document.getString(Helper.TITLE),
                    document.getDate(Helper.TIMESTAMP)!!,
                    document.getString(Helper.DESCRIPTION),
                    document.getDouble(Helper.LIMIT),
                    document.getString(Helper.IMAGE)
                )
            }.await()
        }
    }


}