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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.app_android_2_version.databinding.FragmentTodayBinding
import com.example.app_android_2_version.ui.home.HomeViewModel
import com.example.app_android_2_version.ui.home.TrainingViewModel
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mikhaellopez.circularprogressbar.CircularProgressBar


class TodayFragment : Fragment(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var stepCounterSensor: Sensor
    private var SensorEventListener: SensorEventListener? = null


    private var _binding: FragmentTodayBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var fragmentPagingAdapter: FragmentPagingAdapter
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    private var running = false
    private val viewModel: TrainingViewModel by viewModels()
    private lateinit var progress_circular: CircularProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val todayViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)


        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        val root: View = binding.root


        progress_circular = binding.progressBar
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!!

        todayViewModel.steps.observe(viewLifecycleOwner) { steps_ ->
            binding.stepTest.text = steps_


        }

        if (stepCounterSensor == null) {
            Toast.makeText(
                requireContext(),
                "Step counter sensor not available",
                Toast.LENGTH_SHORT
            ).show()
        }

        return root

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


            //binding.stepTest.setText(currentSteps.toString())

            val currentUser = FirebaseAuth.getInstance().currentUser
            currentUser?.uid?.let { uid ->
                val userReference = FirebaseDatabase.getInstance().getReference("users").child(uid)
                userReference.child("steps").setValue(currentSteps)

            }

            val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
            currentUserUid?.let { uid ->
                FirebaseDatabase.getInstance().getReference("users").child(uid)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val steps = snapshot.child("steps").getValue(Long::class.java).toString()
                            binding.stepTest.text = steps
                            progress_circular.apply {
                                setProgressWithAnimation(steps.toFloat())
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })
            }

        }
    }


    /*fun resetSteps() {
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
    }*/

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


