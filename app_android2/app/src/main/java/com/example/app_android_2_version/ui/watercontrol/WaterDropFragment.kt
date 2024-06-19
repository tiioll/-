package com.example.app_android_2_version.ui.watercontrol

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.app_android_2_version.R
import com.example.app_android_2_version.databinding.FragmentWaterBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WaterDropFragment : Fragment() {

    private var _binding: FragmentWaterBinding? = null
    private val binding get() = _binding!!

    val database = Firebase.database
    var auth = FirebaseAuth.getInstance()

    val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        DateTimeFormatter.ofPattern("ddMMyyyy")
    } else {
        TODO("VERSION.SDK_INT < O")
    }

    var NowDay: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalDate.now().format(formatter).trim()
    } else {
        TODO("VERSION.SDK_INT < O")
    }

    var water: Water = Water()

    var WaterCountForAdd = 0

    var MaxWaterCount = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentWaterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.waterCup.setImageResource(R.drawable.one)

        val currentUserUidDay = FirebaseAuth.getInstance().currentUser?.uid
        currentUserUidDay?.let { uid ->
            FirebaseDatabase.getInstance().getReference("users").child(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        water = snapshot.child("water").getValue(Water::class.java)!!

                        binding.waterCountNowday.text = water.Count.toString() + " мл"
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }



        if (NowDay.trim() == water.Day.trim()){
            val currentUserDel = FirebaseAuth.getInstance().currentUser
            currentUserDel?.uid?.let { uid ->
                val userReference = FirebaseDatabase.getInstance().getReference("users").child(uid)
                water.let { userReference.child("water").removeValue() }
            }

            water.Day = NowDay.trim()
            water.Count = 0

            val currentUserDay = FirebaseAuth.getInstance().currentUser
            currentUserDay?.uid?.let { uid ->
                val userReference =
                    FirebaseDatabase.getInstance().getReference("users").child(uid)
                userReference.child("water")
                    .setValue(water)
            }
        }

        binding.addWater.setOnClickListener {
            val currentUserDel = FirebaseAuth.getInstance().currentUser
            currentUserDel?.uid?.let { uid ->
                val userReference = FirebaseDatabase.getInstance().getReference("users").child(uid)
                water.let { userReference.child("water").removeValue() }
            }

            water.Count += WaterCountForAdd

            val currentUser = FirebaseAuth.getInstance().currentUser
            currentUser?.uid?.let { uid ->
                val userReference =
                    FirebaseDatabase.getInstance().getReference("users").child(uid)
                userReference.child("water")
                    .setValue(water)
            }

            if (water.Count < MaxWaterCount / 3){
                binding.waterCup.setImageResource(R.drawable.one)
            }
            else if (water.Count < MaxWaterCount * 2 / 3){
                binding.waterCup.setImageResource(R.drawable.two)
            }
            else if (water.Count < MaxWaterCount){
                binding.waterCup.setImageResource(R.drawable.three)
            }
            else{
                binding.waterCup.setImageResource(R.drawable.four)
            }

            binding.waterCountNowday.text = water.Count.toString() + " мл"

            WaterCountForAdd = 0
            binding.waterCountForAdd.text = WaterCountForAdd.toString() + " мл"
        }

        binding.waterCountPlus.setOnClickListener {
            WaterCountForAdd += 100
            binding.waterCountForAdd.text = WaterCountForAdd.toString() + " мл"
        }

        binding.waterCountMinus.setOnClickListener {
            WaterCountForAdd -= 100
            if (WaterCountForAdd < 0){
                WaterCountForAdd += 100
            }
            binding.waterCountForAdd.text = WaterCountForAdd.toString() + " мл"
        }

        return root
    }


}