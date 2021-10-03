package com.example.promoapps.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.promoapps.R
import com.example.promoapps.adapter.Helper
import com.example.promoapps.viewmodel.DetailPromoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailPromoActivity : AppCompatActivity() {
    private lateinit var detailPromoViewModel: DetailPromoViewModel

    private lateinit var txtTitle: TextView
    private lateinit var txtDescription: TextView
    private lateinit var btnShowQr: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_promo)

        txtTitle = findViewById(R.id.txt_title)
        txtDescription = findViewById(R.id.txt_description)
        btnShowQr = findViewById(R.id.btn_showqr)

        detailPromoViewModel = ViewModelProvider(this).get(DetailPromoViewModel::class.java)

        detailPromoViewModel.role = intent.getStringExtra(Helper.ACCOUNTS).toString()
        if (detailPromoViewModel.role == Helper.ADMIN){
            btnShowQr.visibility = View.INVISIBLE
        }

        GlobalScope.launch(Dispatchers.IO){
            detailPromoViewModel.getPromo(intent.getStringExtra(Helper.PROMOID))
            withContext(Dispatchers.Main){
                txtTitle.text = detailPromoViewModel.promoModel.title
                txtDescription.text = detailPromoViewModel.promoModel.description
            }
        }

    }
}