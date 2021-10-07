package com.example.promoapps.activity.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.promoapps.R
import com.example.promoapps.activity.DetailPromoActivity
import com.example.promoapps.activity.HistoryActivity
import com.example.promoapps.activity.LoginActivity
import com.example.promoapps.adapter.Helper
import com.example.promoapps.adapter.ListItemPromoAdapter
import com.example.promoapps.model.PromoModel
import com.example.promoapps.viewmodel.user.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

class UserActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var promosListPromoAdapter: ListItemPromoAdapter

    private lateinit var mAuth: FirebaseAuth

    private lateinit var txtEmail: TextView
    private lateinit var txtName: TextView
    private lateinit var rvListPromo: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        mAuth = Firebase.auth

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        txtEmail = findViewById(R.id.txt_email)
        txtName = findViewById(R.id.txt_name)
        rvListPromo = findViewById(R.id.rv_promo_user)
        swipeRefresh = findViewById(R.id.swipeRefresh)

        showList()

        swipeRefresh.setOnRefreshListener {
            GlobalScope.launch(Dispatchers.IO){
                userViewModel.getPromosData()
                withContext(Dispatchers.Main){
                    promosListPromoAdapter.notifyDataSetChanged()
                    swipeRefresh.isRefreshing = false
                }
            }
        }
    }

    private fun showList(){
        rvListPromo.setHasFixedSize(true)
        rvListPromo.layoutManager = LinearLayoutManager(this)
        promosListPromoAdapter =  ListItemPromoAdapter(userViewModel.promoList, Helper.USER)
        rvListPromo.adapter = promosListPromoAdapter

        promosListPromoAdapter.setOnItemClickCallback(object : ListItemPromoAdapter.OnItemClickCallback{
            override fun onItemClicked(data: PromoModel) {
                val goDetail = Intent(this@UserActivity, DetailPromoActivity::class.java)
                goDetail.putExtra(Helper.PROMOID, data.id)
                goDetail.putExtra(Helper.ROLE, Helper.USER)
                startActivity(goDetail)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.btn_logout ->{
                val goLogin = Intent(this@UserActivity, LoginActivity::class.java)
                startActivity(goLogin)
                finish()
                mAuth.signOut()
                true
            }
            R.id.btn_history -> {
                val goHistory = Intent(this@UserActivity, HistoryActivity::class.java)
                goHistory.putExtra(Helper.ROLE, Helper.USER)
                startActivity(goHistory)
                true
            }
            else -> true
        }
    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch(Dispatchers.IO){
            userViewModel.getUserData(mAuth.currentUser)
            userViewModel.getPromosData()
            withContext(Dispatchers.Main){
                txtEmail.text = userViewModel.userModel.email
                txtName.text = userViewModel.userModel.name
                promosListPromoAdapter.notifyDataSetChanged()
            }
        }
    }
   /*
    override fun onResume() {
        super.onResume()
        GlobalScope.launch(Dispatchers.IO){
            userViewModel.getPromosData()
            withContext(Dispatchers.Main){
                showList()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        GlobalScope.launch(Dispatchers.IO){
            userViewModel.getUserData(mAuth.currentUser)
            userViewModel.getPromosData()
            withContext(Dispatchers.Main){
                txtEmail.text = userViewModel.userModel.email
                txtName.text = userViewModel.userModel.name
                showList()
            }
        }
    }*/
}