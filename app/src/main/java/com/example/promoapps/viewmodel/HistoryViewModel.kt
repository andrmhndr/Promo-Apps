package com.example.promoapps.viewmodel

import androidx.lifecycle.ViewModel
import com.example.promoapps.adapter.Helper
import com.example.promoapps.model.HistoryModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class HistoryViewModel: ViewModel(){
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var role: String
    var historyList: ArrayList<HistoryModel> = arrayListOf()

    fun setRole(role: String){
        this.role = role
    }

    fun getRole(): String = role

    suspend fun getHistoryData(){
        historyList.clear()
        db.collection(Helper.HISTORY)
            .orderBy(Helper.TIMESTAMP, Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { document->
            document?.forEach { data ->
                val historyModel = HistoryModel(
                    data.id,
                    data.getString(Helper.TITLE),
                    data.getDate(Helper.TIMESTAMP),
                    data.getString(Helper.DESCRIPTION),
                    data.getString(Helper.USER),
                    data.getString(Helper.USERID),
                    data.getString(Helper.ADMINID)
                )
                historyList.add(historyModel)
            }
        }.await()
    }

    suspend fun getHistoryData(currentUser: FirebaseUser){
        historyList.clear()
        db.collection(Helper.HISTORY)
            .orderBy(Helper.TIMESTAMP, Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { document->
            document?.forEach { data ->
                if (currentUser.uid == data.getString(Helper.USERID)){
                    val historyModel = HistoryModel(
                        data.id,
                        data.getString(Helper.TITLE),
                        data.getDate(Helper.TIMESTAMP),
                        data.getString(Helper.DESCRIPTION),
                        data.getString(Helper.USER),
                        data.getString(Helper.USERID),
                        data.getString(Helper.ADMINID)
                    )
                    historyList.add(historyModel)
                }
            }
        }.await()
    }
}