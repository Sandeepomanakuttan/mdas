package com.mainproject.mdas.view.admin.comman

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.mainproject.mdas.R
import com.mainproject.mdas.databinding.FragmentRejectSchemeBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.viewmodel.admin.AdminViewModel
import com.mainproject.mdas.utils.RecyclerViewAdaptor
import com.mainproject.mdas.utils.getPreference

class RejectSchemeFragment : BaseFragments<FragmentRejectSchemeBinding>(FragmentRejectSchemeBinding::inflate) {
    lateinit var viewModel: AdminViewModel
    lateinit var recyclerViewAdaptor: RecyclerViewAdaptor
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel= ViewModelProvider(this)[AdminViewModel::class.java]
        recyclerViewAdaptor = RecyclerViewAdaptor(requireContext())
        val(user,type) = getPreference(requireContext())

        if (type!="Admin") {
            viewModel.getAgreeScheme(user.toString(), "reject")
        }
        else {
             viewModel.getAgreeScheme(label, "reject")
        }


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