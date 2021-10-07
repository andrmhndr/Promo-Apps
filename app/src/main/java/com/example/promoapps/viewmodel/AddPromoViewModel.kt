package com.example.promoapps.viewmodel

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.promoapps.adapter.Helper
import com.example.promoapps.model.PromoModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class AddPromoViewModel: ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var storage: FirebaseStorage = FirebaseStorage.getInstance()
    private  var storageReference: StorageReference= storage.getReference()
    private lateinit var promoModel: PromoModel
    private var limit: Boolean = false
    private var imageUri: Uri? = null

    fun setImageUri(imageUri: Uri?){
        this.imageUri = imageUri
    }

    fun getImageUri(): Uri? = imageUri

    fun setLimit(limit: Boolean){
        this.limit = limit
    }

    fun getLimit(): Boolean = limit

    fun setPromo(title: String?, description: String?, limit: Int?, image: String?){
        promoModel = PromoModel(null, title, Calendar.getInstance().time, description, limit, image)
    }

    fun addPromo(context: Context, title: String?, description: String?, limit: Int?){
        if (imageUri == null){
            setPromo(title, description, limit, null)
        } else {
            val randomKey: String = UUID.randomUUID().toString()
            setPromo(title, description, limit, randomKey)
            uploadPicture(context, randomKey, imageUri!!)
        }
        db.collection(Helper.PROMOS).add(promoModel).addOnSuccessListener { document->
            Toast.makeText(context, "Promo Successfully added with id : ${document.id}", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e->
            Toast.makeText(context, "Promo add Failed : $e", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadPicture(context: Context, randomKey: String,imageUri: Uri){
        val pictRef = storageReference.child("${Helper.IMG_FOOD}/$randomKey.jpg")
        pictRef.putFile(imageUri)
            .addOnSuccessListener {
                Toast.makeText(context, "Image Uploaded", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(context, "Failed to Upload", Toast.LENGTH_SHORT).show()
            }
    }
}