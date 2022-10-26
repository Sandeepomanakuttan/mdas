package com.mainproject.mdas.view.admin.comman

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.mainproject.mdas.databinding.FragmentAvailableTraineeBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.viewmodel.admin.AdminViewModel
import com.mainproject.mdas.utils.RecyclerViewAdaptor
import com.mainproject.mdas.utils.agreeSchemeRef
import com.mainproject.mdas.utils.getPreference


class ApproveSchemeFragment : BaseFragments<FragmentAvailableTraineeBinding>(FragmentAvailableTraineeBinding::inflate) {

    lateinit var viewModel: AdminViewModel
    lateinit var recyclerViewAdaptor: RecyclerViewAdaptor
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel=ViewModelProvider(this)[AdminViewModel::class.java]
        recyclerViewAdaptor = RecyclerViewAdaptor(requireContext())
        val(user,type) = getPreference(requireContext())

        if (type!="Admin")
        viewModel.getAgreeScheme(user.toString(),"approve")
        else
            viewModel.getAgreeScheme(label,"approve")
        observers()
    }

    private fun observers() {
        viewModel.agreeSchemeResponse.observe(viewLifecycleOwner){
            if (it.status){
                if (it.scheme.isNotEmpty()){
                    binding.recyclerView.isVisible = true
                    binding.empty.isVisible = false
                    binding.recyclerView.apply {
                        recyclerViewAdaptor.item = it.scheme
                        adapter = recyclerViewAdaptor
                        setHasFixedSize(true)
                    }
                }else{
                    binding.recyclerView.isVisible = false
                    binding.empty.isVisible = true
                }
            }
        }
    }
}