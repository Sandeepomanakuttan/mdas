package com.mainproject.mdas

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.mainproject.mdas.databinding.FragmentSplashScreenBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.utils.getPreference
import com.mainproject.mdas.utils.preference
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
//                    root.setBackgroundColor(
//                        ContextCompat.getColor(
//                            requireContext(),
//                            R.color.purple_200
//                        )
//                    )

                    preference(requireContext(), "9497039910", "User","Low Vision")

                    val (user,type) = getPreference(requireContext())

                    if (user == null) {
                        findNavController().navigate(R.id.action_splashScreenFragment_to_onbordingFragment)
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