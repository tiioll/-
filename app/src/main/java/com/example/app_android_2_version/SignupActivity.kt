package com.example.app_android_2_version

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.app_android_2_version.databinding.ActivitySignupBinding
import com.github.mikephil.charting.data.Entry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var db: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var refernce: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getSupportActionBar()?.hide()

        db = FirebaseDatabase.getInstance()


        refernce = db.reference.child("users")


        binding.registerBtn.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(applicationContext, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            } else {

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            val date: DateFormat = SimpleDateFormat("MMM dd yyyy, h:mm")
                            val dateFormatted: String = date.format(Calendar.getInstance().time)
                            val entriesMonth = mutableListOf<Entry>()
                            val entriesWeek = mutableListOf<Entry>()
                            val testEntry = Gson().toJson(entriesMonth)
                            val tasks = hashMapOf(
                                "description" to ""
                            )
                            val userInfo = hashMapOf(
                                "email" to email,
                                "profileImage" to "",
                                "password" to "",
                                "name" to "",
                                "surname" to "",
                                "weight" to 0,
                                "height" to 0,
                                "age" to 0,
                                "gender" to "",
                                "steps" to 0,
                                "joinDate" to dateFormatted,
                                "stepsForMonth" to 0,
                                "stepsForWeek" to 0,
                                "entry" to testEntry,
                            )

                            val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
                            currentUserUid?.let {
                                FirebaseDatabase.getInstance().getReference("users").child(it)
                                    .setValue(userInfo)
                            }
                            val sharpref = getSharedPreferences("myprefs", MODE_PRIVATE)
                            val editor1 = sharpref.edit()
                            editor1.putString("login", "true")
                            editor1.apply()
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
