package com.example.promoapps.activity

import android.app.AlertDialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.promoapps.R
import com.example.promoapps.adapter.Helper
import com.example.promoapps.adapter.ListItemHistoryAdapter
import com.example.promoapps.adapter.ListItemPromoAdapter
import com.example.promoapps.viewmodel.HistoryViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.MalformedURLException

class HistoryActivity : AppCompatActivity() {
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var listHistoryAdapter: ListItemHistoryAdapter

    private lateinit var mAuth: FirebaseAuth

    private lateinit var rvListHistory: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        mAuth = Firebase.auth

        rvListHistory = findViewById(R.id.rv_history)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        historyViewModel.setRole(intent.getStringExtra(Helper.ROLE).toString())

        showList()

        swipeRefresh.setOnRefreshListener {
            GlobalScope.launch(Dispatchers.IO){
                if (historyViewModel.getRole() == Helper.ADMIN){
                    historyViewModel.getHistoryData()
                }else if (historyViewModel.getRole() == Helper.USER){
                    historyViewModel.getHistoryData(mAuth.currentUser)
                }
                withContext(Dispatchers.Main){
                    listHistoryAdapter.notifyDataSetChanged()
                    swipeRefresh.isRefreshing = false
                }
            }
        }
    }

    private fun showList() {
        rvListHistory.setHasFixedSize(true)
        rvListHistory.layoutManager = LinearLayoutManager(this)
        listHistoryAdapter = ListItemHistoryAdapter(historyViewModel.historyList, historyViewModel.getRole())
        rvListHistory.adapter = listHistoryAdapter
    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch(Dispatchers.IO){
            if (historyViewModel.getRole() == Helper.ADMIN){
                historyViewModel.getHistoryData()
            }else if (historyViewModel.getRole() == Helper.USER){
                historyViewModel.getHistoryData(mAuth.currentUser)
            }
            withContext(Dispatchers.Main){
                listHistoryAdapter.notifyDataSetChanged()
            }
        }
    }
}