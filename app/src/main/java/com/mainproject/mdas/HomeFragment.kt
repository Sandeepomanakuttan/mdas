package com.mainproject.mdas

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.mainproject.mdas.databinding.FragmentHomeBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.response.ResponseClass
import com.mainproject.mdas.model.viewmodel.admin.AdminViewModel
import com.mainproject.mdas.utils.RecyclerViewAdaptor
import com.mainproject.mdas.utils.getPreference


class HomeFragment : BaseFragments<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
lateinit var recyclerViewAdaptor: RecyclerViewAdaptor
 lateinit var viewModel: AdminViewModel
 val sharedPrefFile = "kotlinsharedpreference"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[AdminViewModel::class.java]

    val (userName,type) = getPreference(requireContext())

        Log.e("shared",userName.toString())
        if (userName != null ){
            viewModel.getDetails(userName)
            Toast.makeText(requireContext(), userName.toString(), Toast.LENGTH_SHORT).show()
        }


        val child = arguments?.getString("phone")
        val userType = arguments?.getString("user")
        val disability = arguments?.getString("disability")

        recyclerViewAdaptor = RecyclerViewAdaptor(requireContext())

        progress.isVisible = true

//        if (type =="Admin"){
//            viewModel.getHospital()
//        }else{
//            viewModel.getHospital("Low Vision")
//        }



        viewModel.getAgreeScheme()
        observers()




    }

    private fun observers() {
        viewModel.hospitalResponse.observe(viewLifecycleOwner){

            progress.isVisible = false
            if (it.status){
                setHospitalView(it.hospital)
            }
        }

        viewModel.userResponse.observe(viewLifecycleOwner){
            if (it.status){
                Glide.with(requireContext()).load(it.person[0].img).into(binding.profileImage)
                binding.name.text = it.person[0].personName
                if (it.person[0].type == "User"){
                    viewModel.getHospital(it.person[0].disability.toString())
                }else{
                    viewModel.getHospital()
                }
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