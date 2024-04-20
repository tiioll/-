package com.example.app_android_2_version.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.app_android_2_version.UserDescriptionActivity
import com.example.app_android_2_version.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        loadUserInfo()

        binding

        binding.changeprofile.setOnClickListener {

            val intent = Intent(activity, UserDescriptionActivity::class.java)
            startActivity(intent)
        }




        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun loadUserInfo() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        currentUserUid?.let { uid ->
            FirebaseDatabase.getInstance().getReference("users").child(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val name = snapshot.child("name").getValue(String::class.java)
                        val surname = snapshot.child("surname").getValue(String::class.java)
                        val profileImage = snapshot.child("profileImage").getValue(String::class.java)
                        binding.name1.text = name
                        binding.surname1.text = surname




                        profileImage?.let {
                            if (it.isNotEmpty()) {
                                Glide.with(this@HomeFragment).load(it).into(binding.homePicture)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Обработка ошибки
                    }
                })
        }
    }
}