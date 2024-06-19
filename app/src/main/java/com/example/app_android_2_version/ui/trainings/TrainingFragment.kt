package com.example.app_android_2_version.ui.trainings

import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.app_android_2_version.databinding.FragmentTrainingBinding
import com.example.app_android_2_version.ui.home.TrainingViewModel
import com.google.android.material.tabs.TabLayout


class TrainingFragment : Fragment() {
    private lateinit var sensorManager: SensorManager
    private lateinit var stepCounterSensor: Sensor
    private var SensorEventListener: SensorEventListener? = null


    private var _binding: FragmentTrainingBinding? = null

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
    //private lateinit var progress_circular: CircularProgressBar


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTrainingBinding.inflate(inflater, container, false)
        val root: View = binding.root



        tabLayout = binding.tabLayout
        viewPager2 = binding.viewPager
        fragmentPagingAdapter = FragmentPagingAdapter(childFragmentManager, lifecycle)

        tabLayout.addTab(tabLayout.newTab().setText("Сегодня"))
        tabLayout.addTab(tabLayout.newTab().setText("Неделя"))
        tabLayout.addTab(tabLayout.newTab().setText("Месяц"))

        viewPager2.adapter = fragmentPagingAdapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(p0: TabLayout.Tab?) {
                if (p0 != null) {
                    viewPager2.currentItem = p0.position
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {
            }
        })
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })
        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val trainingViewModel =
            ViewModelProvider(this).get(TrainingViewModel::class.java)
        //resetSteps()
        trainingViewModel.nameOne.observe(viewLifecycleOwner) { nameOne ->
            //binding.FirstandLast.text = nameOne
        }

        trainingViewModel.nameOne.observe(viewLifecycleOwner) { nameOne ->
            //binding.FirstandLast.text = nameOne
        }


        //progress_circular = view.findViewById(R.id.progressBar)
    }
}





