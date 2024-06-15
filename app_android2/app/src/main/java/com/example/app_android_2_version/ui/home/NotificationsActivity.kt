package com.example.app_android_2_version

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_android_2_version.databinding.NotificationsBinding
import com.example.app_android_2_version.ui.home.Notification
import com.example.app_android_2_version.ui.home.NotificationAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class NotificationsActivity : AppCompatActivity(), NotificationAdapter.Listener {
    private lateinit var binding: NotificationsBinding

    private lateinit var adapter: NotificationAdapter

    val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        DateTimeFormatter.ofPattern("dd.MM.yyyy")
    } else {
        TODO("VERSION.SDK_INT < O")
    }
    val timeFormatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        DateTimeFormatter.ofPattern("HH:mm")
    } else {
        TODO("VERSION.SDK_INT < O")
    }

    var NowDay: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalDate.now().format(formatter)
    } else {
        TODO("VERSION.SDK_INT < O")
    }
    var NowTime: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalTime.now().format(timeFormatter)
    } else {
        TODO("VERSION.SDK_INT < O")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getSupportActionBar()?.hide()
        binding = NotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ;

        onOpen()
    }

    override fun onClick(notification: Notification) {
        val currentUserDel = FirebaseAuth.getInstance().currentUser
        currentUserDel?.uid?.let { uid ->
            val userReference = FirebaseDatabase.getInstance().getReference("users").child(uid)
            notification.Text?.let { userReference.child("notifications").child(notification.Text!!).removeValue() }
        }

        onChangeListener()
    }

    private fun onOpen(){
        initRcView()
        onChangeListener()
    }

    private fun onChangeListener() {

        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        currentUserUid?.let { uid ->
            FirebaseDatabase.getInstance().getReference("users").child(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val list = ArrayList<Notification>()
                        var snap = snapshot.child("notifications")

                        for (s in snap.children) {
                            val notification = s.getValue(Notification::class.java)

                            if (notification != null) {
                                if ((dateCompare(notification.Date.toString(), NowDay) == -1) ||
                                    ((dateCompare(notification.Date.toString(), NowDay) == 0) && (timeCompare(notification.Time.toString(), NowTime) != 1))){
                                    list.add(notification)
                                }
                            }
                        }
                        adapter.notifyDataSetChanged()
                        adapter.submitList(list)
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }

        val updCurrentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        updCurrentUserUid?.let { uid ->
            FirebaseDatabase.getInstance().getReference("users").child(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var snap = snapshot.child("notifications")

                        for (s in snap.children) {
                            val notification = s.getValue(Notification::class.java)

                            if (notification != null) {
                                if ((dateCompare(notification.Date.toString(), NowDay) == -1) ||
                                    ((dateCompare(notification.Date.toString(), NowDay) == 0) && (timeCompare(notification.Time.toString(), NowTime) != 1))){
                                    val currentUserDel = FirebaseAuth.getInstance().currentUser
                                    currentUserDel?.uid?.let { uid ->
                                        val userReference = FirebaseDatabase.getInstance().getReference("users").child(uid)
                                        notification.Text.let { notification.Text?.let { it1 ->
                                            userReference.child("notifications").child(
                                                it1
                                            ).removeValue()
                                        } }
                                    }

                                    val currentUserAdd = FirebaseAuth.getInstance().currentUser
                                    currentUserAdd?.uid?.let { uid ->
                                        val userReference =
                                            FirebaseDatabase.getInstance().getReference("users").child(uid)
                                        notification.Text?.let {
                                            userReference.child("notifications").child(it)
                                                .setValue(Notification(notification.Text, notification.Date, notification.Time, false))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }
    }

    private fun initRcView() = with(binding) {
        adapter = NotificationAdapter(this@NotificationsActivity)
        notificationList.layoutManager = LinearLayoutManager(this@NotificationsActivity)
        notificationList.adapter = adapter
    }

    private fun dateCompare(date0: String, date1: String): Int {
        //date0 < date1: return -1
        //date0 > date1: return 1
        //date0 == date1: return 0

        var day0 = (date0[0] + date0[1].toString()).toInt()
        var month0 = (date0[3] + date0[4].toString()).toInt()
        var year0 = (date0[6].toString() + date0[7].toString() + date0[8].toString() + date0[9].toString()).toInt()

        var day1 = (date1[0] + date1[1].toString()).toInt()
        var month1 = (date1[3] + date1[4].toString()).toInt()
        var year1 = (date1[6].toString() + date1[7].toString() + date1[8].toString() + date1[9].toString()).toInt()

        if (year0 < year1)
            return -1
        else if (year0 > year1)
            return 1
        else if (month0 < month1)
            return -1
        else if (month0 > month1)
            return 1
        else if (day0 < day1)
            return -1
        else if (day0 > day1)
            return 1
        else
            return 0
    }

    private fun timeCompare(time0: String, time1: String): Int {
        //time0 < time1: return -1
        //time0 > time1: return 1
        //time0 == time1: return 0

        var hour0 = (time0[0] + time0[1].toString()).toInt()
        var min0 = (time0[3] + time0[4].toString()).toInt()

        var hour1 = (time1[0] + time1[1].toString()).toInt()
        var min1 = (time1[3] + time1[4].toString()).toInt()

        if (hour0 < hour1)
            return -1
        else if (hour0 > hour1)
            return 1
        else if (min0 < min1)
            return -1
        else if (min0 > min1)
            return 1
        else
            return 0
    }
}



