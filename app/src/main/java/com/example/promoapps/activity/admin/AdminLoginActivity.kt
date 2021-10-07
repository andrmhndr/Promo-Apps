package com.example.promoapps.activity.admin

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.promoapps.R
import com.example.promoapps.viewmodel.admin.AdminLoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AdminLoginActivity : AppCompatActivity() {
    private lateinit var adminLoginViewModel: AdminLoginViewModel

    private lateinit var mAuth: FirebaseAuth

    private lateinit var progressDialog: ProgressDialog
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        mAuth = Firebase.auth

        adminLoginViewModel = ViewModelProvider(this).get(AdminLoginViewModel::class.java)

        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        progressDialog = ProgressDialog(this@AdminLoginActivity)
        progressDialog.setTitle("Loading...")
        progressDialog.setCancelable(false)

        btnLogin.setOnClickListener {
            adminLoginViewModel.email = etEmail.text.toString()
            adminLoginViewModel.password = etPassword.text.toString().trim()
            if (adminLoginViewModel.email.equals("")){
                Toast.makeText(this, R.string.emailempty, Toast.LENGTH_SHORT).show()
            }else if(adminLoginViewModel.password.equals("")){
                Toast.makeText(this, R.string.passwordempty, Toast.LENGTH_SHORT).show()
            }else{
                progressDialog.show()
                firebaseAuthEmailandPassword(adminLoginViewModel.email, adminLoginViewModel.password)
                progressDialog.dismiss()
            }
        }
    }

    private fun firebaseAuthEmailandPassword(email: String, password: String){
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task->
                if (task.isSuccessful){
                    updateUi()
                    Toast.makeText(this, R.string.loggedInAdmin, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, R.string.loginadminfail, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun updateUi(){
        val goAdmin = Intent(this@AdminLoginActivity, AdminActivity::class.java)
        startActivity(goAdmin)
        finish()
    }
}