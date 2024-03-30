package com.example.app_android_2_version.ui.habitats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.app_android_2_version.R

// TODO: Rename parameter arguments, choose names that match


/**
 * A simple [Fragment] subclass.
 * Use the [HabitatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HabitatFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_habitat, container, false)
    }



}