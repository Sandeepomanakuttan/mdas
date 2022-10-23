package com.mainproject.mdas.view.admin.scheme

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.mainproject.mdas.databinding.FragmentSchemeBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.base.adaptor.ViewPagerAdapter


class SchemeFragment : BaseFragments<FragmentSchemeBinding>(FragmentSchemeBinding::inflate) {

    val schemeArray = arrayOf("View Scheme","Add Scheme")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle,"scheme")
        adapter.item = schemeArray
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabView, binding.viewPager) { tab, position ->
            tab.text = schemeArray[position]
        }.attach()
    }

}