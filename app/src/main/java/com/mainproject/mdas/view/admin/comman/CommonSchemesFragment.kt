package com.mainproject.mdas.view.admin.comman

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.mainproject.mdas.R
import com.mainproject.mdas.databinding.FragmentCommonSchemesBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.base.adaptor.ViewPagerAdapter




class CommonSchemesFragment : BaseFragments<FragmentCommonSchemesBinding>(FragmentCommonSchemesBinding::inflate) {
    private val schemeArray = arrayOf("Approve","Pending","Reject")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle,"commonScheme")
        adapter.item = schemeArray
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabView, binding.viewPager) { tab, position ->
            tab.text = schemeArray[position]
        }.attach()
    }

}