package com.example.app_android_2_version.ui.trainings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.ScheduledExecutorService


class TodayViewModel(application: Application) : AndroidViewModel(application) {


    private lateinit var scheduledExecutor: ScheduledExecutorService


    private val _steps = MutableLiveData<String>()
    val steps: LiveData<String> = _steps



    init {
        loadUserInfo()
    }




private fun loadUserInfo() {
    val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
    currentUserUid?.let { uid ->
        FirebaseDatabase.getInstance().getReference("users").child(uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val steps = snapshot.child("steps").getValue(Long::class.java)
                    _steps.value = steps.toString()

                    //addDataToLineChart(date, steps, entriesForData)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}
}