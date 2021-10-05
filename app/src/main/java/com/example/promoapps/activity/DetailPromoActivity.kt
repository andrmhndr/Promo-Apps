package com.example.promoapps.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import com.example.promoapps.R
import com.example.promoapps.adapter.Helper
import com.example.promoapps.viewmodel.DetailPromoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.zxing.WriterException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailPromoActivity : AppCompatActivity() {
    private lateinit var detailPromoViewModel: DetailPromoViewModel
    private lateinit var mAuth: FirebaseAuth

    private lateinit var txtTitle: TextView
    private lateinit var txtDescription: TextView
    private lateinit var btnShowQr: FloatingActionButton

    private lateinit var popupDialog: Dialog
    private lateinit var imgQrCode: ImageView
    private lateinit var qrData: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_promo)

        txtTitle = findViewById(R.id.txt_title)
        txtDescription = findViewById(R.id.txt_description)
        btnShowQr = findViewById(R.id.btn_showqr)
        popupDialog = Dialog(this)

        mAuth = Firebase.auth

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

        btnShowQr.setOnClickListener {
            val goPopup = Intent(this@DetailPromoActivity, PopupActivity::class.java)
            goPopup.putExtra(Helper.PROMOID, intent.getStringExtra(Helper.PROMOID))
            goPopup.putExtra(Helper.USER, mAuth.currentUser.uid)
            startActivity(goPopup)
        }

    }
}