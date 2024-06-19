package com.example.app_android_2_version.ui.trainings

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.app_android_2_version.databinding.FragmentMonthBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


class MonthFragment : Fragment() {
    private var _binding: FragmentMonthBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private lateinit var scheduledExecutor: ScheduledExecutorService
    private val binding get() = _binding!!
    private val entries = mutableListOf<Entry>()
    private lateinit var lineChart: LineChart


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //setupLineChart()
        _binding = FragmentMonthBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // Обновление данных линейного графика



        // Настраиваем график

        // Добавляем начальные данные

        loadUserInfo()




        scheduledExecutor = Executors.newSingleThreadScheduledExecutor()
        scheduledExecutor.scheduleAtFixedRate(::loadUserInfo, 0, 2, TimeUnit.MINUTES)




        return root


    }



    private fun addDataToLineChart(data: String?, steps: Float?) {
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
        Log.d("MyTag", "The data fromated is $lastTwoCharForCompariosn")
        Log.d("MyTag", "The data fromated is $steps")
        entries.add(Entry(lastTwoCharForCompariosn, steps!!))
        // saveData(steps)

    }

    private fun loadUserInfo() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        currentUserUid?.let { uid ->
            FirebaseDatabase.getInstance().getReference("users").child(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val date = snapshot.child("joinDate").getValue(String::class.java)
                        val steps = snapshot.child("steps").getValue(Long::class.java)?.toFloat()
                        //val entries = snapshot.child("entry").getValue(String::class.java)
                        //val entriesForData = Gson().fromJson(entries, Array<Entry>::class.java).toMutableList()
                        val entriesForData = mutableListOf<Entry>()
                        entriesForData.add(Entry(17f, 3620f))
                        entriesForData.add(Entry(18f, 4701f))
                        val dataSet = LineDataSet(entriesForData, "My Data")
                        dataSet.color = Color.BLACK
                        dataSet.setCircleColor(Color.BLUE)


                        // Настраиваем внешний вид графика


                        lineChart = binding.chart
                        lineChart.setTouchEnabled(true)
                        lineChart.isDragEnabled = true
                        lineChart.setScaleEnabled(true)
                        lineChart.setPinchZoom(true)
                        val description = Description()
                        description.text = "Мои шаги"

                        lineChart.description = description
                        val rightAxis = lineChart.xAxis
                        rightAxis.axisMaximum = 30f
                        val lineData = LineData(dataSet)
                        lineChart.data = lineData
                        lineChart.invalidate()

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }
    }


    private fun saveData(steps: Float?) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { uid ->
            val userReference = FirebaseDatabase.getInstance().getReference("users").child(uid)
            userReference.child("steps").setValue(0)
        }
    }

    fun startBackgroundService() {
        val intent = Intent(context, MyBackGroundService::class.java)
        context?.startService(intent)
    }
}


