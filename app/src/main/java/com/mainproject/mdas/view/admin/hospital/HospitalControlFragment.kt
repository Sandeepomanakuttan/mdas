package com.mainproject.mdas.view.admin.hospital

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.mainproject.mdas.databinding.FragmentHospitalContrilBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.base.adaptor.ViewPagerAdapter
import com.mainproject.mdas.utils.getPreference


class HospitalControlFragment : BaseFragments<FragmentHospitalContrilBinding>(FragmentHospitalContrilBinding::inflate){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val (userName, type) = getPreference(requireContext())

        if (type == "Admin") {
            val traineeArray = arrayOf("Hospital","Add Hospital")
            val adapter = ViewPagerAdapter(childFragmentManager, lifecycle, "hospital")
            adapter.item = traineeArray
            binding.viewPager.adapter = adapter

            TabLayoutMediator(binding.tabView, binding.viewPager) { tab, position ->
                tab.text = traineeArray[position]
            }.attach()

        }else{
            val traineeArray = arrayOf("Hospital")
            val adapter = ViewPagerAdapter(childFragmentManager, lifecycle, "UserHospital")
            adapter.item = traineeArray
            binding.viewPager.adapter = adapter

            TabLayoutMediator(binding.tabView, binding.viewPager) { tab, position ->
                tab.text = traineeArray[position]
            }.attach()
        }
    }

}