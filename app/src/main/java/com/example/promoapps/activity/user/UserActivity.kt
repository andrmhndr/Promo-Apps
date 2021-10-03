package com.example.promoapps.activity.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidmads.library.qrgenearator.QRGEncoder
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.promoapps.R
import com.example.promoapps.activity.DetailPromoActivity
import com.example.promoapps.activity.LoginActivity
import com.example.promoapps.adapter.Helper
import com.example.promoapps.adapter.ListItemAdapter
import com.example.promoapps.model.PromoModel
import com.example.promoapps.viewmodel.user.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

class UserActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel

    private lateinit var mAuth: FirebaseAuth

    private lateinit var txtEmail: TextView
    private lateinit var txtName: TextView
    private lateinit var rvListPromo: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        mAuth = Firebase.auth

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        txtEmail = findViewById(R.id.txt_email)
        txtName = findViewById(R.id.txt_name)
        rvListPromo = findViewById(R.id.rv_promo_user)
    }

    private fun showList(){
        rvListPromo.setHasFixedSize(true)
        rvListPromo.layoutManager = LinearLayoutManager(this)
        val promoListAdapter =  ListItemAdapter(userViewModel.promoList, Helper.USER)
        rvListPromo.adapter = promoListAdapter

        promoListAdapter.setOnItemClickCallback(object : ListItemAdapter.OnItemClickCallback{
            override fun onItemClicked(data: PromoModel) {
                val goDetail = Intent(this@UserActivity, DetailPromoActivity::class.java)
                goDetail.putExtra(Helper.PROMOID, data.id)
                goDetail.putExtra(Helper.ACCOUNTS, Helper.USER)
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
                showList()
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