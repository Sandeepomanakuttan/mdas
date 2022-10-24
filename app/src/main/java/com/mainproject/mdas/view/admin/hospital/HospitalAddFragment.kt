package com.mainproject.mdas.view.admin.hospital

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.mainproject.mdas.R
import com.mainproject.mdas.databinding.FragmentHospitalAddBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.response.ResponseClass
import com.mainproject.mdas.model.viewmodel.admin.AdminViewModel
import com.mainproject.mdas.model.viewmodel.admin.AdminViewModel.Companion.hospitalAddResponse
import com.mainproject.mdas.progress
import com.mainproject.mdas.utils.disableArrays


class HospitalAddFragment :
    BaseFragments<FragmentHospitalAddBinding>(FragmentHospitalAddBinding::inflate) {
    private var imageUri: Uri? = null
    private lateinit var viewModel: AdminViewModel
    var hospitalName : String?=null
    var district : String?=null
    var place : String?=null
    var desc : String?=null
    var rating : Float?=null
    var disability : String?=null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[AdminViewModel::class.java]

        observers()

        val spinner = ArrayAdapter(
            requireContext(),
            com.bumptech.glide.R.layout.support_simple_spinner_dropdown_item,
            disableArrays
        )

        spinner.setDropDownViewResource(
            android.R.layout
                .simple_spinner_dropdown_item
        )

        binding.disability.adapter = spinner


        binding.imgHospital.borderColor=resources.getColor(R.color.mainColor)
        binding.imgHospital.setOnClickListener {
            imageChooser()
        }
        binding.btnSubmit.setOnClickListener {
            hospitalName = binding.etHospitalName.text.toString().trim()
            district = binding.districtName.selectedItem.toString()
            place = binding.location.text.toString().trim()
            desc = binding.address.text.toString().trim()
            rating = binding.ratingBar.rating
            disability = binding.disability.selectedItem.toString()
            if (imageUri == null) {
                Toast.makeText(requireContext(), "Please Add Hospital Image", Toast.LENGTH_SHORT)
                    .show()
            } else {
                if (
                    validation(
                        hospitalName!!,
                        district!!,
                        disability!!,
                        place!!,
                        desc!!,
                        rating!!
                    )
                ) {
                    viewModel.addHospital(
                        ResponseClass.Hospital(
                            null,
                            imageUri.toString(),
                            hospitalName,
                            district,
                            place,
                            desc,
                            rating.toString(),
                            disability
                        )
                    )
                    progress.isVisible = true
                }
            }
        }
    }

    private fun observers() {

        hospitalAddResponse.observe(viewLifecycleOwner){
            progress.isVisible = false
            if (it.status){
                if (it.message!="Hospital Already Exist"){
                    Glide.with(requireContext()).load("https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.pngitem.com%2Fmiddle%2FTRxRJxb_empty-profile-picture-icon-hd-png-download%2F&psig=AOvVaw27rmIjpNTrNL60FO6neh2u&ust=1666142630330000&source=images&cd=vfe&ved=0CA0QjRxqFwoTCLCOuo_P6PoCFQAAAAAdAAAAABAJ").into(binding.imgHospital)
                    binding.location.text.clear()
                    binding.address.text.clear()
                    binding.ratingBar.rating=0f
                    binding.etHospitalName.text.clear()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }


    }

    fun validation(
        hospitalName: String,
        district: String,
        disability: String,
        place: String,
        desc: String,
        rating: Float
    ): Boolean {

         when {
             TextUtils.isEmpty(hospitalName) -> {
                Toast.makeText(requireContext(), "Please Enter Hospital Name", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            TextUtils.equals(district, "Select District") -> {
                Toast.makeText(requireContext(), "Please Choose District", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
             disability == disableArrays[0] -> {
                 Toast.makeText(requireContext(), "Please Select Disability", Toast.LENGTH_SHORT)
                     .show()
                 return false
             }
            TextUtils.isEmpty(place) -> {
                Toast.makeText(requireContext(), "Please Enter Place Name", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
            TextUtils.isEmpty(desc) -> {
                Toast.makeText(
                    requireContext(),
                    "Please Enter Description of hospital",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
            rating<=0 -> {
                Toast.makeText(requireContext(), "Add Rating", Toast.LENGTH_SHORT).show()
                return false
            }
            else -> {
                return true
            }
        }
    }

    private fun imageChooser() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, 100)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            imageUri = data?.data
            binding.imgHospital.setImageURI(imageUri)
        }
    }

}

