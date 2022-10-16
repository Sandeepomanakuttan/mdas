package com.mainproject.mdas

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.mainproject.mdas.databinding.FragmentSplashScreenBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.utils.startAnimations

class SplashScreenFragment :
    BaseFragments<FragmentSplashScreenBinding>(FragmentSplashScreenBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.circle_scale).apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
        }


        Handler(Looper.getMainLooper()).postDelayed({
            binding.apply {
                explosionB.visibility = View.INVISIBLE
                animationView.visibility = View.INVISIBLE

                animationView.startAnimations(animation) {
                    root.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.purple_500
                        )
                    )
                    animationView.visibility = View.INVISIBLE

                    Handler(Looper.getMainLooper()).postDelayed({
                        findNavController().navigate(R.id.action_splashScreenFragment_to_onbordingFragment)
                    }, 1100)
                }
            }
        }, 700)


    }
}