package com.example.app_android_2_version.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class TrainingViewModel(application: Application) : AndroidViewModel(application) {




    private val _stepCountLiveData = MutableLiveData<Int>()
    val stepCountLiveData: LiveData<Int> = _stepCountLiveData

    private val _nameOne = MutableLiveData<String>()
    val nameOne: LiveData<String> = _nameOne

    private val _nameTwo = MutableLiveData<String>()
    val nameTwo: LiveData<String> = _nameTwo

    private val _steps = MutableLiveData<String>()
    val steps: LiveData<String> = _steps



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
                            val step_data = dataSnapshot.child("steps").value.toString()
                            _nameOne.value = firstName + " " + lastName
                            _steps.value = step_data
                        }

                    }

                }
        }
    }


}