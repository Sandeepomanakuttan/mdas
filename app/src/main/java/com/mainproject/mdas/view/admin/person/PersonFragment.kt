package com.mainproject.mdas.view.admin.person

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.mainproject.mdas.databinding.FragmentPersonBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.base.adaptor.ViewPagerAdapter


class PersonFragment : BaseFragments<FragmentPersonBinding>(FragmentPersonBinding::inflate) {

    private val personArray = arrayOf("Request Person","Persons")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle,"person")
        adapter.item = personArray
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabView, binding.viewPager) { tab, position ->
            tab.text = personArray[position]
        }.attach()
    }
}