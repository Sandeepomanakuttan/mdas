package com.mainproject.mdas.view.admin.scheme

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.mainproject.mdas.R
import com.mainproject.mdas.databinding.FragmentSchemeAddBinding
import com.mainproject.mdas.databinding.FragmentSchemeBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.response.ResponseClass
import com.mainproject.mdas.model.viewmodel.admin.AdminViewModel
import com.mainproject.mdas.progress
import com.mainproject.mdas.utils.disableArray
import com.mainproject.mdas.utils.panchayatArray


class SchemeAddFragment : BaseFragments<FragmentSchemeAddBinding>(FragmentSchemeAddBinding::inflate),AdapterView.OnItemSelectedListener {

    private var imageUri: Uri?=null
    private lateinit var viewModel: AdminViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel= ViewModelProvider(this)[AdminViewModel()::class.java]


        AdminViewModel.schemeAddResponse.observe(viewLifecycleOwner){
            if (it!=null){
                progress.isVisible = false
                if (it.status){
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }

        }

        binding.btnSubmit.setOnClickListener {
            if (imageUri==null){
                Toast.makeText(requireContext(), "choose image", Toast.LENGTH_SHORT).show()
            }
            else if (binding.etSchemeName.text.toString().isEmpty()){
                binding.etSchemeName.error = "Please Fill Scheme Name"
            }else if (binding.type.selectedItem.toString() == "Select type"){
                Toast.makeText(requireContext(), "please select Scheme type", Toast.LENGTH_SHORT).show()
            }
            else if (binding.categoryName.selectedItem.toString() == disableArray[0]){
                Toast.makeText(requireContext(), "please select Disability type", Toast.LENGTH_SHORT).show()
            }else if(binding.spinnerPachayath.selectedItem.toString() == panchayatArray[0]){
                Toast.makeText(requireContext(), "please select trainer Available Panchayath", Toast.LENGTH_SHORT).show()
            }else if(binding.amount.text.isEmpty()) {
                Toast.makeText(requireContext(), "Please Allocated amount", Toast.LENGTH_SHORT).show()
            } else{
                val scheme = ResponseClass.SchemeClass(schemeName = binding.etSchemeName.text.toString(), disability = binding.categoryName.selectedItem.toString(), panchayath = binding.spinnerPachayath.selectedItem.toString(), amount = binding.amount.text.toString(), schemeImg = imageUri.toString(),type=binding.type.selectedItem.toString())
                viewModel.addScheme(scheme)
                progress.isVisible = true
            }

        }

        val spinner = ArrayAdapter(
            requireContext(),
            com.bumptech.glide.R.layout.support_simple_spinner_dropdown_item,
            disableArray
        )

        spinner.setDropDownViewResource(
            android.R.layout
                .simple_spinner_dropdown_item
        )

        binding.categoryName.adapter = spinner

        binding.imgScheme.setOnClickListener {
            imageChooser()
        }

        binding.categoryName.onItemSelectedListener = this
        binding.spinnerPachayath.onItemSelectedListener = this
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    private fun imageChooser() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, 100)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            imageUri = data?.data
            binding.imgScheme.setImageURI(imageUri)
        }
    }


}