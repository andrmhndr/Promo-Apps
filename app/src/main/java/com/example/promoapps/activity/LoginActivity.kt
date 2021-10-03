 package com.example.promoapps.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.promoapps.R
import com.example.promoapps.activity.admin.AdminActivity
import com.example.promoapps.activity.admin.AdminLoginActivity
import com.example.promoapps.activity.user.UserActivity
import com.example.promoapps.adapter.Helper
import com.example.promoapps.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

 class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var btnLoginAdmin: Button
    private lateinit var btnLoginUser: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        btnLoginAdmin = findViewById(R.id.btn_login_admin)
        btnLoginUser = findViewById(R.id.btn_login_user)

        loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        btnLoginAdmin.setOnClickListener {
            val goAdminLogin = Intent(this@LoginActivity, AdminLoginActivity::class.java)
            startActivity(goAdminLogin)
            finish()
        }

        btnLoginUser.setOnClickListener {
            signIn()
        }

    }

    private fun signIn(){
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, Helper.RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Helper.RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            }catch (e: ApiException){
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) {task ->
                    if (task.isSuccessful) {
                        GlobalScope.launch(Dispatchers.IO){
                            loginViewModel.check(mAuth.currentUser)
                        }
                        if (loginViewModel.role.equals(Helper.USER)){
                            updateUi(Helper.USER)
                        } else {
                            GlobalScope.launch(Dispatchers.IO){
                                loginViewModel.addUser(mAuth.currentUser)
                                loginViewModel.check(mAuth.currentUser)
                                withContext(Dispatchers.Main){
                                    updateUi(Helper.USER)
                                }
                            }
                        }
                    } else {
                        updateUi(null)
                    }
                }
    }

    override fun onStart() {
        super.onStart()
        val currentUser: FirebaseUser? = mAuth.currentUser
        if (currentUser != null) {
            GlobalScope.launch(Dispatchers.IO){
                loginViewModel.check(currentUser)
                withContext(Dispatchers.Main){
                    updateUi(loginViewModel.role)
                }
            }
        }

    }

    private fun updateUi(role: String?){
        when {
            role.equals(Helper.USER) -> {
                Toast.makeText(this, R.string.loggedInUser, Toast.LENGTH_SHORT).show()
                val goUser = Intent(this@LoginActivity, UserActivity::class.java)
                startActivity(goUser)
                finish()
            }
            role.equals(Helper.ADMIN) -> {
                Toast.makeText(this, R.string.loggedInAdmin, Toast.LENGTH_SHORT).show()
                val goAdmin = Intent(this@LoginActivity, AdminActivity::class.java)
                startActivity(goAdmin)
                finish()
            }
            else -> {
                Toast.makeText(this, "Please Login", Toast.LENGTH_SHORT).show()
            }
        }
    }

}