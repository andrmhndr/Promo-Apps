package com.example.promoapps.viewmodel.admin

import androidx.lifecycle.ViewModel
import com.example.promoapps.adapter.Helper
import com.example.promoapps.model.PromoModel
import com.example.promoapps.model.UserModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

class AdminViewModel: ViewModel() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var storage: FirebaseStorage = FirebaseStorage.getInstance()
    private  var storageReference: StorageReference = storage.getReference()
    lateinit var userModel: UserModel
    var promoList: ArrayList<PromoModel> = arrayListOf()

    suspend fun getAdminData(currentUser: FirebaseUser?){
        if (currentUser != null) {
            db.collection(Helper.ROLE).document(currentUser.uid).get().addOnSuccessListener { document->
                if (document != null){
                    userModel = UserModel(document.getString("name"),
                        document.getString("email"),
                        currentUser.uid,
                        document.getString("role")
                    )
                }
            }.await()
        }
    }

    fun deleteImage(image: String){
        val imgRef = storageReference.child("${Helper.IMG_FOOD}/$image.jpg")
        imgRef.delete()
    }

    suspend fun getPromosData(){
        promoList.clear()
        db.collection(Helper.PROMOS).orderBy(Helper.TIMESTAMP, Query.Direction.DESCENDING).get().addOnSuccessListener { document->
            document?.forEach { data->
                val promoModel = PromoModel(
                    data.id,
                    data.getString(Helper.TITLE),
                    data.getDate(Helper.TIMESTAMP),
                    data.getString(Helper.DESCRIPTION)
                )
                promoList.add(promoModel)
            }
        }.await()
    }

    suspend fun deletePromo(id: String){
        db.collection(Helper.PROMOS).document(id).get().addOnSuccessListener { document ->
            if (document.getString(Helper.IMAGE) != null) {
                deleteImage(document.getString(Helper.IMAGE)!!)
            }
            db.collection(Helper.PROMOS).document(id).delete()
        }.await()
    }

}