package com.mainproject.mdas.view.admin.hospital

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mainproject.mdas.databinding.FragmentHospitalViewBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.viewmodel.admin.AdminViewModel
import com.mainproject.mdas.progress
import com.mainproject.mdas.utils.RecyclerViewAdaptor


class HospitalViewFragment : BaseFragments<FragmentHospitalViewBinding>(FragmentHospitalViewBinding::inflate) {

    lateinit var viewModel: AdminViewModel
    lateinit var recycleViewAdaptor:RecyclerViewAdaptor
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel =ViewModelProvider(this)[AdminViewModel::class.java]

        recycleViewAdaptor= RecyclerViewAdaptor(requireContext())

        progress.isVisible = true
        viewModel.getHospital()


        viewModel.hospitalResponse.observe(viewLifecycleOwner){

            if (it.status){

                progress.isVisible = false

                binding.recyclerView.apply {
                    recycleViewAdaptor.label ="hospital"
                    layoutManager = LinearLayoutManager(requireContext())
                    recycleViewAdaptor.item = it.hospital
                    adapter = recycleViewAdaptor
                    setHasFixedSize(true)
                }
            }else{
                Log.e("called",it.message)

            }
        }


    }

}