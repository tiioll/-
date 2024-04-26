package com.example.app_android_2_version

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.app_android_2_version.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var db: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var refernce: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseDatabase.getInstance()


        refernce = db.reference.child("users")

        binding.alreadyReg.setOnClickListener {
            startActivity(Intent(this@SignupActivity, SignInActivity::class.java))
        }

        binding.registerBtn.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val username = binding.username.text.toString()

            if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
                Toast.makeText(applicationContext, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val tasks = hashMapOf(
                                "tasks" to null
                            )

                            val userInfo = hashMapOf(
                                "email" to email,
                                "username" to username,
                                "profileImage" to "",
                                "password" to "",
                                "name" to "",
                                "surname" to "",
                                "age" to 0,
                                "gender" to "",
                                "tasks" to tasks
                            )

                            val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
                            currentUserUid?.let {
                                FirebaseDatabase.getInstance().getReference("users").child(it)
                                    .setValue(userInfo)
                            }

                            startActivity(Intent(this@SignupActivity, MainActivity::class.java))
                        }
                    }
            }
        }

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        }

    }
