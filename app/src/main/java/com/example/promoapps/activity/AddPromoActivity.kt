package com.example.promoapps.activity

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
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
    private lateinit var switchLimit: Switch
    private lateinit var etLimit: EditText

    private lateinit var promoId: String
    private var limit: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_promo)

        addPromoViewModel = ViewModelProvider(this).get(AddPromoViewModel::class.java)

        etLimit = findViewById(R.id.et_limit)
        switchLimit = findViewById(R.id.switch_limit)
        etTitle = findViewById(R.id.et_title)
        etDescription = findViewById(R.id.et_description)
        btnSubmit = findViewById(R.id.btn_submit)

        promoId = intent.getStringExtra(Helper.PROMOID).toString()

        btnSubmit.setOnClickListener {
            if (limit){
                addPromoViewModel.setPromo(etTitle.text.toString(), etDescription.text.toString(), etLimit.text.toString().toInt())
            }else{
                addPromoViewModel.setPromo(etTitle.text.toString(), etDescription.text.toString(), null)
            }
            addPromoViewModel.addPromo(this)
            finish()
        }
        
        switchLimit.setOnClickListener {
            if (switchLimit.isChecked){
                limit = true
                etLimit.visibility = View.VISIBLE
            }else{
                limit = false
                etLimit.visibility = View.INVISIBLE
            }
        }

    }

}