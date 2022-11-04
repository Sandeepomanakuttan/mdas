package com.mainproject.mdas.model.base.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.mainproject.mdas.R
import com.mainproject.mdas.databinding.FragmentDialogBinding
import com.mainproject.mdas.model.viewmodel.admin.AdminViewModel

class DialogFragment : DialogFragment() {

    lateinit var viewModel: AdminViewModel
    lateinit var binding:FragmentDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.RoundedCornersDialog)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogBinding.inflate(inflater, container, false)

        Glide.with(requireContext()).load(arguments?.getString("Img")).into(binding.schImg)
        binding.schName.text=arguments?.getString("sname")
        binding.schDis.text=arguments?.getString("sDis")
        binding.schAmount.text=arguments?.getString("samount")
        binding.schdiscription.text=arguments?.getString("description")
        binding.schPan.text=arguments?.getString("panchayath")

        return binding.root
    }


}