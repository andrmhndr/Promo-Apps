package com.example.promoapps.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import com.example.promoapps.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash)

        val handler = Handler()
        handler.postDelayed({
            val goMainActivity = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(goMainActivity)
            finish()
        }, 3000L)
    }
}