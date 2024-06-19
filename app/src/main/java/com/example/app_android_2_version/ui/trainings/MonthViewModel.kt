package com.example.app_android_2_version.ui.trainings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.concurrent.ScheduledExecutorService


class MonthViewModel(application: Application) : AndroidViewModel(application) {
    private val _chart = MutableLiveData<List<Entry>>()
    val chart: LiveData<List<Entry>> = _chart
    private lateinit var lineChart: LineChart
    private lateinit var scheduledExecutor: ScheduledExecutorService
    private var listOfEntries = mutableListOf<Entry>()

    init {
        loadUserInfo()
    }


    private fun loadUserInfo() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        currentUserUid?.let { uid ->
            FirebaseDatabase.getInstance().getReference("users").child(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val date = snapshot.child("joinDate").getValue(String::class.java)
                        val steps = snapshot.child("steps").getValue(Long::class.java)?.toFloat()
                        val entries = snapshot.child("entry").getValue(String::class.java)
                        val entriesForData = Gson().fromJson(entries, Array<Entry>::class.java).toMutableList()
                        _chart.value = entriesForData
                        //addDataToLineChart(date, steps, entriesForData)
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }
    }

    private fun addDataToLineChart(data: String?, steps: Float?, entries: MutableList<Entry>) {
        //val entries = mutableListOf<Entry>()
        /*entries.add(Entry(0f, 3600f))
        entries.add(Entry(1f, 10000f))
        entries.add(Entry(2f, 1000f))
        entries.add(Entry(3f, 5000f))*/
        val lastTwoChars = data?.substring(data.length - 2)?.toFloat()
        val date: DateFormat = SimpleDateFormat("MMM dd yyyy, h:mm")
        val dateFormatted: String = date.format(Calendar.getInstance().time)
        val lastTwoCharForCompariosn = dateFormatted.substring(dateFormatted.length - 2).toFloat()
        val dayNumber = dateFormatted.substring(5, 7).toFloat()
        val dayNumberForCompariosn = data?.substring(5, 7)?.toFloat()
        var entries02 = Entry(lastTwoCharForCompariosn, steps!!)
        if (!entries.contains(entries02))
            entries.add(entries02)
        saveData(entries)
        _chart.value = entries



    }

    private fun saveData(entries: MutableList<Entry>) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { uid ->
            val userReference = FirebaseDatabase.getInstance().getReference("users").child(uid)

            val entriesForSave = Gson().toJson(entries)
            userReference.child("entry").setValue(entriesForSave)

        }
    }

}








