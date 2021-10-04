package com.example.promoapps.activity.admin

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.promoapps.R
import com.example.promoapps.activity.AddPromoActivity
import com.example.promoapps.activity.DetailPromoActivity
import com.example.promoapps.activity.LoginActivity
import com.example.promoapps.adapter.Helper
import com.example.promoapps.adapter.ListItemAdapter
import com.example.promoapps.model.PromoModel
import com.example.promoapps.viewmodel.admin.AdminViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

class AdminActivity : AppCompatActivity() {
    private lateinit var adminViewModel: AdminViewModel
    private lateinit var promosListAdapter: ListItemAdapter

    private lateinit var mAuth: FirebaseAuth

    private lateinit var txtEmail: TextView
    private lateinit var txtName: TextView
    private lateinit var btnAddPromo: FloatingActionButton
    private lateinit var rvListPromo: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        mAuth = Firebase.auth

        adminViewModel = ViewModelProvider(this).get(AdminViewModel::class.java)
        txtEmail = findViewById(R.id.txt_email)
        txtName = findViewById(R.id.txt_name)
        btnAddPromo = findViewById(R.id.btn_add_promo)
        rvListPromo = findViewById(R.id.rv_promo_user)
        swipeRefresh = findViewById(R.id.swipeRefresh)

        showList()

        swipeRefresh.setOnRefreshListener {
            GlobalScope.launch(Dispatchers.IO){
                adminViewModel.getPromosData()
                withContext(Dispatchers.Main){
                    promosListAdapter.notifyDataSetChanged()
                    swipeRefresh.isRefreshing = false
                }
            }
        }

        btnAddPromo.setOnClickListener {
            val goAdd = Intent(this@AdminActivity, AddPromoActivity::class.java)
            startActivity(goAdd)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        return true
    }

    private fun showList(){
        rvListPromo.setHasFixedSize(true)
        rvListPromo.layoutManager = LinearLayoutManager(this)
        promosListAdapter = ListItemAdapter(adminViewModel.promoList, Helper.ADMIN)
        rvListPromo.adapter = promosListAdapter

        promosListAdapter.setOnItemClickCallback(object : ListItemAdapter.OnItemClickCallback{
            override fun onItemClicked(data: PromoModel) {
                val goDetailPromo = Intent(this@AdminActivity, DetailPromoActivity::class.java)
                goDetailPromo.putExtra(Helper.PROMOID, data.id)
                goDetailPromo.putExtra(Helper.ACCOUNTS, Helper.ADMIN)
                startActivity(goDetailPromo)
            }
        })

        promosListAdapter.setDeleteClickCallback(object : ListItemAdapter.OnDeleteClickCallback{
            override fun onDeleteClicked(data: PromoModel) {
                GlobalScope.launch(Dispatchers.IO){
                    adminViewModel.deletePromo(data.id.toString())
                    adminViewModel.getPromosData()
                    withContext(Dispatchers.Main){
                        promosListAdapter.notifyDataSetChanged()
                    }
                }
            }
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.btn_logout -> {
                val goLogin = Intent(this@AdminActivity, LoginActivity::class.java)
                startActivity(goLogin)
                finish()
                mAuth.signOut()
                true
            }
            R.id.btn_history -> {
                val goHistory = Intent(this@AdminActivity, HistoryActivity::class.java)
                goHistory.putExtra(Helper.ACCOUNTS, Helper.ADMIN)
                startActivity(goHistory)
                finish()
                true
            }
            else -> true
        }
    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch(Dispatchers.IO){
            adminViewModel.getAdminData(mAuth.currentUser)
            adminViewModel.getPromosData()
            withContext(Dispatchers.Main){
                txtEmail.text = adminViewModel.userModel.email
                txtName.text = adminViewModel.userModel.name
                promosListAdapter.notifyDataSetChanged()
            }
        }
    }

    /*override fun onResume() {
        super.onResume()
        GlobalScope.launch(Dispatchers.IO){
            adminViewModel.getPromosData()
            withContext(Dispatchers.Main){
                showList()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        GlobalScope.launch(Dispatchers.IO){
            adminViewModel.getAdminData(mAuth.currentUser)
            adminViewModel.getPromosData()
            withContext(Dispatchers.Main){
                txtEmail.text = adminViewModel.userModel.email
                txtName.text = adminViewModel.userModel.name
                showList()
            }
        }
    }*/
}