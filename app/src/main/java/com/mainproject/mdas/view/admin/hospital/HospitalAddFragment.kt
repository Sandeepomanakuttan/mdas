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
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.mainproject.mdas.R
import com.mainproject.mdas.databinding.FragmentHospitalAddBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.response.ResponseClass
import com.mainproject.mdas.model.viewmodel.admin.AdminViewModel
import com.mainproject.mdas.model.viewmodel.admin.AdminViewModel.Companion.hospitalAddResponse
import com.mainproject.mdas.progress


class HospitalAddFragment :
    BaseFragments<FragmentHospitalAddBinding>(FragmentHospitalAddBinding::inflate) {
    private var imageUri: Uri? = null
    private lateinit var viewModel: AdminViewModel
    var hospitalName : String?=null
    var district : String?=null
    var place : String?=null
    var desc : String?=null
    var rating : Float?=null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[AdminViewModel::class.java]

        observers()

        hospitalAddResponse.observe(viewLifecycleOwner){
            progress.isVisible = false
            if (it.status){
                if (it.message!="Hospital Already Exist"){
                    binding.imgHospital.setBackgroundColor(resources.getColor(R.color.black))
                    binding.location.text.clear()
                    binding.desc.text.clear()
                    binding.ratingBar.rating=0f
                }else{
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }

        binding.imgHospital.borderColor=resources.getColor(R.color.mainColor)
        binding.imgHospital.setOnClickListener {
            imageChooser()
        }
        binding.btnSubmit.setOnClickListener {
            hospitalName = binding.etHospitalName.text.toString().trim()
            district = binding.districtName.selectedItem.toString()
            place = binding.location.text.toString().trim()
            desc = binding.desc.text.toString().trim()
            rating = binding.ratingBar.rating
            if (imageUri == null) {
                Toast.makeText(requireContext(), "Please Add Hospital Image", Toast.LENGTH_SHORT)
                    .show()
            } else {
                if (
                    validation(
                        hospitalName!!,
                        district!!,
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
                            rating.toString()
                        )
                    )
                    progress.isVisible = true
                }
            }
        }
    }

    private fun observers() {



    }

    fun validation(
        hospitalName: String,
        district: String,
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

