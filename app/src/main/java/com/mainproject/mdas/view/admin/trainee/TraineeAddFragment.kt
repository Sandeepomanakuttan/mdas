package com.mainproject.mdas.view.admin.trainee

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.mainproject.mdas.databinding.FragmentTraineeAddBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.response.ResponseClass
import com.mainproject.mdas.model.viewmodel.admin.AdminViewModel
import com.mainproject.mdas.model.viewmodel.admin.AdminViewModel.Companion.traineeAddResponse
import com.mainproject.mdas.progress
import com.mainproject.mdas.utils.disableArray
import com.mainproject.mdas.utils.panchayatArray


class TraineeAddFragment : BaseFragments<FragmentTraineeAddBinding>(FragmentTraineeAddBinding::inflate),AdapterView.OnItemSelectedListener{

    private var imageUri:Uri?=null
    private lateinit var viewModel:AdminViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel= ViewModelProvider(this)[AdminViewModel()::class.java]

        traineeAddResponse.observe(viewLifecycleOwner){
            if (it!=null){
                progress.isVisible = false
                if (it.status){
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    binding.imgTrainee.setImageURI(null)
                    binding.etTraineeName.text.clear()
                    binding.exp.text.clear()
                    binding.spinnerPachayath.setSelection(0)
                    binding.spinnerField.setSelection(0)

                }
            }

        }
        binding.spinnerField.onItemSelectedListener = this
        binding.spinnerPachayath.onItemSelectedListener = this

//spinner disable
        val spinner = ArrayAdapter(
            requireContext(),
            com.bumptech.glide.R.layout.support_simple_spinner_dropdown_item,
            disableArray
        )

        spinner.setDropDownViewResource(
            android.R.layout
                .simple_spinner_dropdown_item
        )

        binding.spinnerField.adapter = spinner

        // spinner disable end

        //spinner panchayath
        val spinner1 = ArrayAdapter(
            requireContext(),
            com.bumptech.glide.R.layout.support_simple_spinner_dropdown_item,
            panchayatArray
        )

        spinner1.setDropDownViewResource(
            android.R.layout
                .simple_spinner_dropdown_item
        )

        binding.spinnerPachayath.adapter = spinner1


        binding.imgTrainee.setOnClickListener {
            imageChooser()
        }

        binding.btnSubmit.setOnClickListener {
            if (imageUri==null){
                Toast.makeText(requireContext(), "choose image", Toast.LENGTH_SHORT).show()
            }
            else if (binding.etTraineeName.text.toString().isEmpty()){
                binding.etTraineeName.error = "Please Fill Trainee Name"
            } else if (binding.exp.text.toString().isEmpty()){
                binding.etTraineeName.error = "Please Fill Trainee Experience"
            }
            else if (binding.spinnerField.selectedItem.toString() == disableArray[0]){
                Toast.makeText(requireContext(), "please select trainer domain", Toast.LENGTH_SHORT).show()
            }else if(binding.spinnerPachayath.selectedItem.toString() == panchayatArray[0]){
                Toast.makeText(requireContext(), "please select trainer Available Panchayath", Toast.LENGTH_SHORT).show()
            }else{
                val trainee = ResponseClass.TraineeClass(traineeName = binding.etTraineeName.text.toString(), field = binding.spinnerField.selectedItem.toString(), panchayath = binding.spinnerPachayath.selectedItem.toString(), status = "Available", traineeImg = imageUri.toString(), experience = binding.exp.text.trim().toString())
                viewModel.addTrainee(trainee)
                progress.isVisible = true
            }

        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    private fun imageChooser() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, 100)
    }



   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            imageUri = data?.data
            binding.imgTrainee.setImageURI(imageUri)
        }
    }


}