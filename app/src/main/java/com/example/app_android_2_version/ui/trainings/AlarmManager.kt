package com.example.app_android_2_version.ui.trainings

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        ResetSteps() // implement showing notification in this function
    }

    private fun ResetSteps() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { uid ->
            val userReference = FirebaseDatabase.getInstance().getReference("users").child(uid)
            Log.d("TIMER", "SHOULD BANG")
           val steps = 0

            userReference.child("steps").setValue(steps)

        }
    }
}


