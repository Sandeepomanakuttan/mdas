package com.mainproject.mdas.view.baseview.onboarding

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
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
            findNavController().navigate(com.mainproject.mdas.R.id.action_onbordingFragment_to_loginFragment)
    }
}