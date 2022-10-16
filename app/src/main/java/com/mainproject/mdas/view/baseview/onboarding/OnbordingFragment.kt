package com.mainproject.mdas.view.baseview.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mainproject.mdas.R
import com.mainproject.mdas.databinding.FragmentOnbordingBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.onboarding.adaptor.SlideCallBack
import com.mainproject.mdas.model.onboarding.adaptor.SliderAdaptor

class OnbordingFragment : BaseFragments<FragmentOnbordingBinding>(FragmentOnbordingBinding::inflate),SlideCallBack{


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sliderAdaptor = SliderAdaptor(requireContext(), this)
        binding.viewPager.adapter = sliderAdaptor
    }

    override fun itemClick(i: Int) {
        if (i != 2)
        binding.viewPager.currentItem = binding.viewPager.currentItem+1
        else
            findNavController().navigate(R.id.action_onbordingFragment_to_loginFragment)
    }
}