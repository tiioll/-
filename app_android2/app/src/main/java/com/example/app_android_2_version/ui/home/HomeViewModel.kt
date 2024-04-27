package com.example.app_android_2_version.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _nameOne = MutableLiveData<String>()
    val nameOne: LiveData<String> = _nameOne

    private val _nameTwo = MutableLiveData<String>()
    val nameTwo: LiveData<String> = _nameTwo

    private val _age = MutableLiveData<String?>()
    val age: LiveData<String?> = _age

    private val _height = MutableLiveData<String>()
    val height: LiveData<String> = _height

    private val _weight = MutableLiveData<String?>()
    val weight: LiveData<String?> = _weight

    private val _profile = MutableLiveData<String?>()
    val profile: LiveData<String?> = _profile

    init {
        saveUserData()
    }
    fun saveUserData() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        currentUserUid?.let { uid ->
            FirebaseDatabase.getInstance().getReference("users").child(uid).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val dataSnapshot = task.result

                        if (dataSnapshot.exists()) {
                            val firstName = dataSnapshot.child("name").value.toString()
                            val lastName = dataSnapshot.child("surname").value.toString()
                            val age = dataSnapshot.child("age").getValue(Integer::class.java)
                            val profileImage = dataSnapshot.child("profileImage").getValue(String::class.java)
                            val height = dataSnapshot.child("height").getValue(Integer::class.java)
                            val weight = dataSnapshot.child("weight").getValue(Integer::class.java)
                            _nameOne.value = firstName
                            _nameTwo.value = lastName
                            _age.value = age.toString()
                            _profile.value = profileImage
                            _weight.value = weight.toString()
                            _height.value = height.toString()





                        }

                    }

                }
        }
    }
}