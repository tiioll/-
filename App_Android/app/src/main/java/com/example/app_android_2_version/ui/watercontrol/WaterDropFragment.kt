package com.example.app_android_2_version.ui.watercontrol

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.app_android_2_version.R

// TODO: Rename parameter arguments, choose names that match


/**
 * A simple [Fragment] subclass.
 * Use the [WaterDropFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WaterDropFragment : Fragment() {
    // TODO: Rename and change types of parameters


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_water, container, false)
    }


}