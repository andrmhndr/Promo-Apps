package com.example.promoapps.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.promoapps.R
import com.example.promoapps.adapter.Helper
import com.example.promoapps.viewmodel.AddPromoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddPromoActivity : AppCompatActivity() {
    private lateinit var addPromoViewModel: AddPromoViewModel

    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnSubmit: FloatingActionButton
    private lateinit var switchLimit: Switch
    private lateinit var etLimit: EditText
    private lateinit var img_food: ImageView
    private lateinit var btn_upload_image: Button
    private lateinit var btn_delete_image: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_promo)

        addPromoViewModel = ViewModelProvider(this).get(AddPromoViewModel::class.java)

        etLimit = findViewById(R.id.et_limit)
        switchLimit = findViewById(R.id.switch_limit)
        etTitle = findViewById(R.id.et_title)
        etDescription = findViewById(R.id.et_description)
        btnSubmit = findViewById(R.id.btn_submit)
        img_food = findViewById(R.id.img_food)
        btn_upload_image = findViewById(R.id.btn_upload_img)
        btn_delete_image = findViewById(R.id.btn_delete_image)

        btnSubmit.setOnClickListener {
            if (etTitle.text.isEmpty()){
                Toast.makeText(this, "Judul tidak boleh kosong", Toast.LENGTH_SHORT).show()
            } else if(etDescription.text.isEmpty()){
                Toast.makeText(this, "Deskripsi tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }else{
                if (addPromoViewModel.getLimit()){
                    if (etLimit.text.isEmpty()){
                        Toast.makeText(this, "Limit tidak boleh kosong", Toast.LENGTH_SHORT).show()
                    }else {
                        addPromoViewModel.addPromo(this, etTitle.text.toString(), etDescription.text.toString(), etLimit.text.toString().toInt())
                    }
                }else{
                    addPromoViewModel.addPromo(this, etTitle.text.toString(), etDescription.text.toString(), null)
                }
                finish()
            }
        }
        
        switchLimit.setOnClickListener {
            if (switchLimit.isChecked){
                addPromoViewModel.setLimit(true)
                etLimit.visibility = View.VISIBLE
            }else{
                addPromoViewModel.setLimit(false)
                etLimit.visibility = View.INVISIBLE
            }
        }

        btn_upload_image.setOnClickListener {
            choosePicture()
        }

        btn_delete_image.setOnClickListener {
            deletePicture()
        }

    }

    private fun deletePicture() {
        addPromoViewModel.setImageUri(null)
        img_food.setImageDrawable(getDrawable(R.drawable.img_food))
    }

    private fun choosePicture() {
        val intentGetContent: Intent = Intent()
        intentGetContent.setType("image/*")
        intentGetContent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intentGetContent,Helper.RC_GET_CONTENT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Helper.RC_GET_CONTENT && resultCode == RESULT_OK && data != null && data.data != null){
            addPromoViewModel.setImageUri(data.data!!)
            img_food.setImageURI(addPromoViewModel.getImageUri())
        }
    }

}