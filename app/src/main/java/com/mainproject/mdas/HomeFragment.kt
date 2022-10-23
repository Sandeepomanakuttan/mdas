package com.mainproject.mdas

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.mainproject.mdas.databinding.FragmentHomeBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.response.ResponseClass
import com.mainproject.mdas.model.viewmodel.admin.AdminViewModel
import com.mainproject.mdas.utils.RecyclerViewAdaptor


class HomeFragment : BaseFragments<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
lateinit var recyclerViewAdaptor: RecyclerViewAdaptor
 lateinit var viewModel: AdminViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[AdminViewModel::class.java]
        recyclerViewAdaptor = RecyclerViewAdaptor(requireContext())

        viewModel.getHospital()

        viewModel.getAgreeScheme()
        observers()



    }

    private fun observers() {
        viewModel.hospitalResponse.observe(viewLifecycleOwner){

            if (it.status){
                setHospitalView(it.hospital)
            }
        }
    }

    private fun setHospitalView(hospital: List<ResponseClass.Hospital>) {

        binding.recyclerViewHospital.apply {
            recyclerViewAdaptor.item = hospital
            recyclerViewAdaptor.label = "home"
            adapter = recyclerViewAdaptor
            setHasFixedSize(true)
        }
    }

}