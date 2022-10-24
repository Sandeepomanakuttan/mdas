package com.mainproject.mdas.view.admin.person

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.mainproject.mdas.databinding.FragmentViewPersonDetailsBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.response.ResponseClass
import com.mainproject.mdas.model.viewmodel.admin.AdminViewModel
import com.mainproject.mdas.progress
import com.mainproject.mdas.utils.getPreference


class viewPersonDetailsFragment :
    BaseFragments<FragmentViewPersonDetailsBinding>(FragmentViewPersonDetailsBinding::inflate) {

    lateinit var viewModel: AdminViewModel
    var person: ResponseClass.Person? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[AdminViewModel::class.java]
        val (userName, type) = getPreference(requireContext())

        if (type == "Admin") {
            binding.constraintLayout4.isVisible = arguments?.getBoolean("page") != true


            arguments?.getString("child")?.let { viewModel.getUniqPerson(it) }
            progress.isVisible = true
        }else{
            binding.constraintLayout4.isVisible = false
            progress.isVisible = true
            if (userName != null) {
                viewModel.getUniqPerson(userName)
            }
        }

        if (!binding.constraintLayout4.isVisible) {
            val layoutParams =
                (binding.scrollable.layoutParams as? ViewGroup.MarginLayoutParams)
            layoutParams?.setMargins(
                0, 700, 0, 0
            )

            binding.scrollable.layoutParams = layoutParams

        }
        viewModel.personResponse.observe(viewLifecycleOwner) {

            if (it.status) {
                person = it.person[0]
                Glide.with(requireActivity()).load(person!!.img).into(binding.profileImage)
                Glide.with(requireActivity()).load(person!!.imgDisability)
                    .into(binding.imgDisability)
                Glide.with(requireActivity()).load(person!!.imgGuardianAdhar)
                    .into(binding.imgGuardian)
                Glide.with(requireActivity()).load(person!!.imgAdhar).into(binding.imgUid)

                binding.name.text = person!!.personName
                binding.phone.text = person!!.phone
                binding.houseName.text = person!!.houseName
                binding.aadhar.text = person!!.adhar

                binding.disability.text = person!!.disability
                binding.percentage.text =
                    person!!.percentage?.let { it1 -> String.format(it1, com.mainproject.mdas.R.string._40) }
                binding.guardian.text = person!!.guardian

                progress.isVisible = false
            }
        }

        binding.btnApprove.setOnClickListener {

            viewModel.update(person?.phone)
            progress.isVisible = true
        }

        viewModel.changeStatus.observe(viewLifecycleOwner) {

            if (it) {
                progress.isVisible = false
                Toast.makeText(requireContext(), "Successfully Added..", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }


    }
}