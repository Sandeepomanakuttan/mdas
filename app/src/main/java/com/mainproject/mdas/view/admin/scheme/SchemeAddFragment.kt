package com.mainproject.mdas.view.admin.scheme

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.mainproject.mdas.databinding.FragmentSchemeAddBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.response.ResponseClass
import com.mainproject.mdas.model.viewmodel.admin.AdminViewModel
import com.mainproject.mdas.progress
import com.mainproject.mdas.utils.disableArrays
import com.mainproject.mdas.utils.monthName
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


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
                    binding.imgScheme.setImageURI(null)

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
            else if (binding.categoryName.selectedItem.toString() == disableArrays[0]){
                Toast.makeText(requireContext(), "please select Disability type", Toast.LENGTH_SHORT).show()
            }else if(binding.amount.text.isEmpty()) {
                Toast.makeText(requireContext(), "Please Allocated amount", Toast.LENGTH_SHORT).show()
            }else if(binding.desc.text.isEmpty()) {
                Toast.makeText(requireContext(), "Please Add Description", Toast.LENGTH_SHORT).show()
            } else{
                val dateFormat: DateFormat = SimpleDateFormat("MM")
                val date = Date()
                Log.d("Month", dateFormat.format(date))
                val scheme = ResponseClass.SchemeClass(schemeName = binding.etSchemeName.text.toString(), disability = binding.categoryName.selectedItem.toString(), panchayath = binding.spinnerPachayath.text.toString(), amount = binding.amount.text.toString(), schemeImg = imageUri.toString(),type=binding.type.selectedItem.toString(), description = binding.desc.text.toString(), month =dateFormat.format(date).toString() )
                viewModel.addScheme(scheme)
                progress.isVisible = true
            }

        }


        val spinner = ArrayAdapter(
            requireContext(),
            com.bumptech.glide.R.layout.support_simple_spinner_dropdown_item,
            disableArrays
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