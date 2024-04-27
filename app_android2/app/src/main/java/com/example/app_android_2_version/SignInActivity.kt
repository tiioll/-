package com.example.app_android_2_version

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.app_android_2_version.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignInActivity : AppCompatActivity() {


    private lateinit var db: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var refernce: DatabaseReference
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getSupportActionBar()?.hide()

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)


        checkBox()

        binding.loginBtn.setOnClickListener {
            val email = binding.username2.text.toString()
            val password = binding.password2.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(applicationContext, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val sharpref = getSharedPreferences("myprefs", MODE_PRIVATE)
                            val editor1 = sharpref.edit()
                            editor1.putString("login", "true")
                            editor1.apply()
                            startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                        }
                    }
            }
        }

        binding.notregistered.setOnClickListener {
            startActivity(Intent(this@SignInActivity, SignupActivity::class.java))
        }


    }

    private fun checkBox() {
        val sharCheck = getSharedPreferences("myprefs", MODE_PRIVATE)
        val check = sharCheck.getString("login", "")
        if (check.equals("true"))
        {
            startActivity(Intent(this@SignInActivity, MainActivity::class.java))
            finish()
        }

    }


}