package com.mainproject.mdas

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.mainproject.mdas.databinding.FragmentSplashScreenBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.utils.*

class SplashScreenFragment :
    BaseFragments<FragmentSplashScreenBinding>(FragmentSplashScreenBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.circle_scale).apply {
            duration = 700
            interpolator = AccelerateDecelerateInterpolator()

        }


        Handler(Looper.getMainLooper()).postDelayed({
            binding.apply {
                explosionB.visibility = View.INVISIBLE
                animationView.visibility = View.INVISIBLE

                animationView.startAnimations(animation) {


                    val (user,type) = getPreference(requireContext())

                    if (user == null) {
                        if (!getOnPreference(requireContext()))
                        findNavController().navigate(R.id.action_splashScreenFragment_to_onbordingFragment)
                        else
                            findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)

                    }
                    else {
                        findNavController().navigate(R.id.action_splashScreenFragment_to_adminBaseFragment)

                    }
                    animationView.visibility = View.INVISIBLE

                }
            }
        }, 700)


    }
}