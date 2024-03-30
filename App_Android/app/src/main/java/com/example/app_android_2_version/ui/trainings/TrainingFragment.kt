package com.example.app_android_2_version.ui.trainings

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.app_android_2_version.R

class TrainingFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_training, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnShow = view.findViewById<Button>(R.id.button55)

        btnShow.setOnClickListener {
            val dialogBinding = layoutInflater.inflate(R.layout.fragment_pop_up, null)
            val myDialog = Dialog(requireContext())
            myDialog.setContentView(dialogBinding)
            myDialog.setCancelable(true)
            myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialog.show()
        }
    }

}