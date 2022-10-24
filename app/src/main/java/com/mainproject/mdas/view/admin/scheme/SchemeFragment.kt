package com.mainproject.mdas.view.admin.scheme

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.mainproject.mdas.databinding.FragmentSchemeBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.base.adaptor.ViewPagerAdapter
import com.mainproject.mdas.utils.getPreference
import com.mainproject.mdas.view.admin.comman.CommonSchemesFragment


class SchemeFragment : BaseFragments<FragmentSchemeBinding>(FragmentSchemeBinding::inflate) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val (userName, type) = getPreference(requireContext())

        if (type == "Admin") {
            val schemeArray = arrayOf("View Scheme","Add Scheme")
            val adapter = ViewPagerAdapter(childFragmentManager, lifecycle, "scheme")
            adapter.item = schemeArray
            binding.viewPager.adapter = adapter

            TabLayoutMediator(binding.tabView, binding.viewPager) { tab, position ->
                tab.text = schemeArray[position]
            }.attach()
        }
        else{
//            CommonSchemesFragment()
            val schemeArray = arrayOf("Approve","Pending","Reject")
            val adapter = ViewPagerAdapter(childFragmentManager, lifecycle, "commonScheme")
            adapter.item = schemeArray
            binding.viewPager.adapter = adapter

            TabLayoutMediator(binding.tabView, binding.viewPager) { tab, position ->
                tab.text = schemeArray[position]
            }.attach()
        }
    }

}