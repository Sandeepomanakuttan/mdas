package com.mainproject.mdas.view.admin.hospital

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.mainproject.mdas.databinding.FragmentHospitalContrilBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.base.adaptor.ViewPagerAdapter


class HospitalControlFragment : BaseFragments<FragmentHospitalContrilBinding>(FragmentHospitalContrilBinding::inflate){
    private val traineeArray = arrayOf("Hospital","Add Hospital")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle,"hospital")
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabView, binding.viewPager) { tab, position ->
            tab.text = traineeArray[position]
        }.attach()
    }
}