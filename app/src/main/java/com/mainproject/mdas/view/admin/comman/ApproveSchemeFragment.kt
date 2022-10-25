package com.mainproject.mdas.view.admin.comman

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.mainproject.mdas.databinding.FragmentAvailableTraineeBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.viewmodel.admin.AdminViewModel
import com.mainproject.mdas.utils.agreeSchemeRef
import com.mainproject.mdas.utils.getPreference


class ApproveSchemeFragment : BaseFragments<FragmentAvailableTraineeBinding>(FragmentAvailableTraineeBinding::inflate) {

   lateinit var viewModel:AdminViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel =  ViewModelProvider(this)[AdminViewModel::class.java]
        val (user,type,disability) = getPreference(requireContext())

        if (user != null) {
            viewModel.getAgreeScheme(user,"Approve")
        }else{
            Toast.makeText(requireContext(), "User Details fetching Error", Toast.LENGTH_SHORT).show()
        }



    }
}