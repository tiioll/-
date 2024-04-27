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


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
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

        homeViewModel.nameOne.observe(viewLifecycleOwner) { nameOne ->
            binding.namee1.text = nameOne
        }
        homeViewModel.nameTwo.observe(viewLifecycleOwner) { nameTwo ->
            binding.surname1.text = nameTwo
        }
        homeViewModel.age.observe(viewLifecycleOwner) { age_ ->
            binding.editAge.text = age_
        }
        homeViewModel.weight.observe(viewLifecycleOwner) { weight_ ->
            binding.editWeight.text = weight_
        }
        homeViewModel.height.observe(viewLifecycleOwner) { height_ ->
            binding.editHeight.text = height_
        }
        homeViewModel.profile.observe(viewLifecycleOwner) { profile_ ->
            profile_?.let {
                if (it.isNotEmpty()) {
                    Glide.with(this@HomeFragment).load(it).into(binding.homePicture)
                }
            }
        }

        binding.changeprofile.setOnClickListener {

            val intent = Intent(activity, UserDescriptionActivity::class.java)
            startActivityForResult(intent, 100)
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}