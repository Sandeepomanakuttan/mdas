package com.mainproject.mdas.view.admin.trainee

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.mainproject.mdas.databinding.FragmentTrainingBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.base.adaptor.ViewPagerAdapter

class TrainingFragment :BaseFragments<FragmentTrainingBinding>(FragmentTrainingBinding::inflate) {
    private val traineeArray = arrayOf("View Trainees","Add Trainee")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle,"trainee")
        adapter.item = traineeArray
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabView, binding.viewPager) { tab, position ->
            tab.text = traineeArray[position]
        }.attach()
    }
}