package com.example.app_android_2_version.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class HomeViewModel(application: Application) : AndroidViewModel(application) {


    private lateinit var scheduledExecutor: ScheduledExecutorService
    private val _steps = MutableLiveData<String>()

    val steps: LiveData<String> = _steps


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
        scheduledExecutor = Executors.newSingleThreadScheduledExecutor()
        scheduledExecutor.scheduleAtFixedRate(::loadUserInfo, 0, 5, TimeUnit.SECONDS)
    }

    private fun loadUserInfo() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        currentUserUid?.let { uid ->
            FirebaseDatabase.getInstance().getReference("users").child(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val firstName = snapshot.child("name").value.toString()
                        val lastName = snapshot.child("surname").value.toString()
                        val age = snapshot.child("age").getValue(Integer::class.java)
                        val profileImage =
                            snapshot.child("profileImage").getValue(String::class.java)
                        val height = snapshot.child("height").getValue(Integer::class.java)
                        val weight = snapshot.child("weight").getValue(Integer::class.java)
                        val steps = snapshot.child("steps").getValue(Integer::class.java)
                        _nameOne.value = firstName
                        _nameTwo.value = lastName
                        _age.value = age.toString()
                        _profile.value = profileImage
                        _weight.value = weight.toString()
                        _height.value = height.toString()
                        _steps.value = steps.toString()

                        //addDataToLineChart(date, steps, entriesForData)
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }
    }
}