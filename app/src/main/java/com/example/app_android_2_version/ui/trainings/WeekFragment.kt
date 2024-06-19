package com.example.app_android_2_version.ui.trainings

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.fragment.app.Fragment
import com.example.app_android_2_version.databinding.FragmentWeekBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


class WeekFragment : Fragment() {
    private var _binding: FragmentWeekBinding? = null
    private lateinit var lineChart: LineChart

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentWeekBinding.inflate(inflater, container, false)
        val root: View = binding.root
        lineChart = binding.chart

        // Настраиваем график
        setupLineChart()

        // Добавляем начальные данные
        addDataToLineChart()






        return root


    }

    private fun setupLineChart() {
        lineChart.setTouchEnabled(true)
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(true)
        lineChart.setPinchZoom(true)

        // Настраиваем внешний вид графика
        val description = Description()
        description.text = "Мои шаги"
        description.textSize = 16f
        description.textAlign = Paint.Align.CENTER
        lineChart.description = description
        val rightAxis = lineChart.xAxis
        rightAxis.axisMaximum = 7f
    }

    private fun addDataToLineChart() {
        val entries = mutableListOf<Entry>()
        entries.add(Entry(1f, 3620f))
        entries.add(Entry(2f, 4701f))

        val dataSet = LineDataSet(entries, "My Data")
        dataSet.color = Color.Black.toArgb()
        dataSet.setCircleColor(Color.Blue.toArgb())

        val lineData = LineData(dataSet)
        lineChart.data = lineData
        lineChart.invalidate()
    }
}