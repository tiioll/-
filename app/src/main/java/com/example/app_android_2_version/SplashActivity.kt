package com.example.app_android_2_version

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        getSupportActionBar()?.hide()
        android.os.Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
        }, 3000)


    }
    }