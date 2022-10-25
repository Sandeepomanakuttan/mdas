package com.mainproject.mdas

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mainproject.mdas.databinding.FragmentHomeBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.response.ResponseClass
import com.mainproject.mdas.model.viewmodel.admin.AdminViewModel
import com.mainproject.mdas.utils.*


class HomeFragment : BaseFragments<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
private lateinit var recyclerViewAdaptor: RecyclerViewAdaptor
private lateinit var recyclerView: RecyclerViewAdaptor
 lateinit var viewModel: AdminViewModel
 val sharedPrefFile = "kotlinsharedpreference"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[AdminViewModel::class.java]

    val (userName,type,disable) = getPreference(requireContext())

        Log.e("shared",userName.toString())
        if (userName != null ){
            viewModel.getDetails(userName)
            Toast.makeText(requireContext(), userName.toString(), Toast.LENGTH_SHORT).show()
        }


        recyclerViewAdaptor = RecyclerViewAdaptor(requireContext())
        recyclerView = RecyclerViewAdaptor(requireContext())

        progress.isVisible = true


        binding.btnShowScheme.setOnClickListener {
            navView?.selectedItemId = R.id.schemeFragment
        }

        binding.btnShowHospital.setOnClickListener {
            navView?.selectedItemId = R.id.hospitalFragment
        }




        observers()


        binding.logout.setOnClickListener {
            clearPreference(requireContext())
            requireActivity().finish()
        }


    }

    private fun observers() {
        viewModel.hospitalResponse.observe(viewLifecycleOwner){

            progress.isVisible = false
            if (it.status){
                setHospitalView(it.hospital)
            }
        }

        viewModel.schemeResponse.observe(viewLifecycleOwner){
            if (it.status){
                setScheme(it.scheme)
            }
        }

        viewModel.userResponse.observe(viewLifecycleOwner){
            if (it.status){
                Glide.with(requireContext()).load(it.person[0].img).into(binding.profileImage)
                binding.name.text = it.person[0].personName
                if (it.person[0].type == "User"){
                    viewModel.getHospital(it.person[0].disability.toString())
                    viewModel.getScheme(it.person[0].disability.toString(),it.person[0].phone.toString())
                }else{
                    viewModel.getHospital()
                    viewModel.getAgreeScheme()

                }
            }
        }
    }

    private fun setScheme(scheme: List<ResponseClass.SchemeClass>) {
        if (scheme.isNotEmpty()){
            binding.recyclerViewScheme.apply {
                layoutManager = LinearLayoutManager(requireContext())
                recyclerViewAdaptor.item = scheme
                adapter = recyclerViewAdaptor
            }
        }else{
            binding.empty.isVisible = true
            binding.recyclerViewScheme.isVisible = false
        }
    }

    private fun setHospitalView(hospital: List<ResponseClass.Hospital>) {

        if (hospital.isEmpty()){
            binding.emptyHost.isVisible = false
            binding.recyclerViewHospital.isVisible = true
            binding.btnShowHospital.isVisible = true
            binding.recyclerViewHospital.apply {
                recyclerView.item = hospital
                recyclerView.label = "home"
                adapter = recyclerView
                setHasFixedSize(true)
            }
        }else{
            binding.emptyHost.isVisible = true
            binding.recyclerViewHospital.isVisible = false
            binding.btnShowHospital.isVisible = false
        }

    }

    private fun itemClickListener(recycleViewAdaptor: RecyclerViewAdaptor) {
        recycleViewAdaptor.itemClickListener = { view, item, position ->

            val (user,type,disability) =  getPreference(requireContext())
            when (item) {
                is ResponseClass.SchemeClass -> {
                    progress.isVisible = true
                    item.status = "apply"
                    agreeSchemeRef.child(user!!).child(item.id!!).setValue(item).addOnCompleteListener {
                        if (it.isSuccessful) {

//                            viewModel.getTrainee(disable, "Available")
                            Toast.makeText(
                                context,
                                "Successfully Send Connect Request Trainee will be Contact you",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }
                }
                else -> {}
            }

        }
    }

}