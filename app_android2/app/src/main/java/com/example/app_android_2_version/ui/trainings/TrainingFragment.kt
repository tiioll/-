package com.example.app_android_2_version.ui.trainings

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.app_android_2_version.R
import com.example.app_android_2_version.databinding.FragmentTrainingBinding
import com.example.app_android_2_version.ui.home.TrainingViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.mikhaellopez.circularprogressbar.CircularProgressBar


class TrainingFragment : Fragment(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var stepCounterSensor: Sensor
    private var SensorEventListener: SensorEventListener? = null


    private var _binding: FragmentTrainingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    private var running = false
    private val viewModel: TrainingViewModel by viewModels()
    private lateinit var progress_circular: CircularProgressBar


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentTrainingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val trainingViewModel =
            ViewModelProvider(this).get(TrainingViewModel::class.java)
        resetSteps()
        loadData()
        trainingViewModel.nameOne.observe(viewLifecycleOwner) { nameOne ->
            binding.FirstandLast.text = nameOne
        }

        trainingViewModel.nameOne.observe(viewLifecycleOwner) { nameOne ->
            binding.FirstandLast.text = nameOne
        }


        progress_circular = view.findViewById(R.id.progressBar)
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!!

        if (stepCounterSensor == null) {
            Toast.makeText(
                requireContext(),
                "Step counter sensor not available",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onResume() {
        super.onResume()
        running = true
        sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        super.onPause()
        running = false
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (running) {
            totalSteps = event?.values?.get(0) ?: 0f
            val currentSteps = (totalSteps - previousTotalSteps).toInt()
            binding.stepTest.setText(currentSteps.toString())
            progress_circular.apply {
                setProgressWithAnimation(currentSteps.toFloat())
            }

            val currentUser = FirebaseAuth.getInstance().currentUser
            currentUser?.uid?.let { uid ->
                val userReference = FirebaseDatabase.getInstance().getReference("users").child(uid)
                userReference.child("steps").setValue(currentSteps)

            }
        }
    }


    fun resetSteps() {
        var tv_stepsTaken = view?.findViewById<TextView>(R.id.step_test)
        tv_stepsTaken?.setOnClickListener {
            // This will give a toast message if the user want to reset the steps
            Toast.makeText(requireContext(), "Long tap to reset steps", Toast.LENGTH_SHORT).show()
        }

        tv_stepsTaken?.setOnLongClickListener {

            previousTotalSteps = totalSteps

            // When the user will click long tap on the screen,
            // the steps will be reset to 0
            tv_stepsTaken.text = 0.toString()

            // This will save the data
            true
        }
    }

    private fun saveSteps(current: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { uid ->
            val userReference = FirebaseDatabase.getInstance().getReference("users").child(uid)
            userReference.child("steps").setValue(current)

        }
    }


    private fun loadData() {

        // In this function we will retrieve data
        val sharedPreferences = context?.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences?.getFloat("key1", 0f)

        if (savedNumber != null) {
            previousTotalSteps = savedNumber.toFloat()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not needed for this example
    }
}





