package com.example.promoapps.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.promoapps.R
import com.example.promoapps.adapter.Helper
import com.example.promoapps.adapter.ListItemHistoryAdapter
import com.example.promoapps.viewmodel.HistoryViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.net.MalformedURLException

class HistoryActivity : AppCompatActivity() {
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var listHistoryAdapter: ListItemHistoryAdapter

    private lateinit var mAuth: FirebaseAuth

    private lateinit var rvListHistory: RecyclerView
    private lateinit var role: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        mAuth = Firebase.auth

        rvListHistory = findViewById(R.id.rv_history)
        role = intent.getStringExtra(Helper.ROLE).toString()

        showList()

    }

    private fun showList() {
        rvListHistory.setHasFixedSize(true)
        rvListHistory.layoutManager = LinearLayoutManager(this)

    }
}