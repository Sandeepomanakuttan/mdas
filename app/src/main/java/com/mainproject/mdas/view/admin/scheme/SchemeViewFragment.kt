package com.mainproject.mdas.view.admin.scheme

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mainproject.mdas.R
import com.mainproject.mdas.databinding.FragmentSchemeBinding
import com.mainproject.mdas.databinding.FragmentSchemeViewBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.response.ResponseClass
import com.mainproject.mdas.model.viewmodel.admin.AdminViewModel
import com.mainproject.mdas.progress
import com.mainproject.mdas.utils.RecyclerViewAdaptor
import com.mainproject.mdas.utils.getPreference

class SchemeViewFragment : BaseFragments<FragmentSchemeViewBinding>(FragmentSchemeViewBinding::inflate) {

    lateinit var viewModel: AdminViewModel
    lateinit var recyclerViewAdaptor: RecyclerViewAdaptor
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[AdminViewModel::class.java]
        recyclerViewAdaptor = RecyclerViewAdaptor(requireContext())

        val (user,type,disability) = getPreference(requireContext())

        if (type != "Admin"){
            viewModel.getScheme(disability.toString())
            progress.isVisible = true
        }else{
            viewModel.getScheme()
            progress.isVisible = true
        }



        observer()

    }

    private fun observer() {
        viewModel.schemeResponse.observe(viewLifecycleOwner){
            progress.isVisible = false
            if (it.status){
                if(it.scheme.isNotEmpty()){
                    setScheme(it.scheme)
                }
            }
        }
    }

    private fun setScheme(scheme: List<ResponseClass.SchemeClass>) {

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            recyclerViewAdaptor.item = scheme
            adapter = recyclerViewAdaptor
            setHasFixedSize(true)
        }
    }
}