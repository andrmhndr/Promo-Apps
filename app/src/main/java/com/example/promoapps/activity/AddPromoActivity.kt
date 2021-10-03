package com.example.promoapps.activity

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.promoapps.R
import com.example.promoapps.adapter.Helper
import com.example.promoapps.viewmodel.AddPromoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*

class AddPromoActivity : AppCompatActivity() {
    private lateinit var addPromoViewModel: AddPromoViewModel

    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnSubmit: FloatingActionButton

    private lateinit var promoId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_promo)

        addPromoViewModel = ViewModelProvider(this).get(AddPromoViewModel::class.java)

        etTitle = findViewById(R.id.et_title)
        etDescription = findViewById(R.id.et_description)
        btnSubmit = findViewById(R.id.btn_submit)

        promoId = intent.getStringExtra(Helper.PROMOID).toString()

        btnSubmit.setOnClickListener {
            addPromoViewModel.setPromo(null, etTitle.text.toString(), null, etDescription.text.toString())
            addPromoViewModel.addPromo(this)
            finish()
        }

    }

}