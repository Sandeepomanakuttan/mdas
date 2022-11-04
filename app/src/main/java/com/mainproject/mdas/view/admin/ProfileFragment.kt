package com.mainproject.mdas.view.admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.mainproject.mdas.R
import com.mainproject.mdas.databinding.FragmentProfileBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.response.ResponseClass
import com.mainproject.mdas.model.viewmodel.admin.AdminViewModel
import com.mainproject.mdas.progress
import com.mainproject.mdas.utils.getPreference

var imageUri: Uri? = null

class ProfileFragment : BaseFragments<FragmentProfileBinding>(
    FragmentProfileBinding::inflate
) {

    lateinit var viewModel: AdminViewModel
    var person: ResponseClass.Person? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(this)[AdminViewModel::class.java]

        val (userName, type) = getPreference(requireContext())

        if (userName != null) {
            viewModel.getUniqPerson(userName)
        }

        binding.profileImage.setOnClickListener {
            imageChooser()
        }

        viewModel.changeStatus.observe(viewLifecycleOwner) {
            progress.isVisible = false
            if (it) {
                Toast.makeText(requireContext(), "Successfully Updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Try Again...", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.personResponse.observe(viewLifecycleOwner) {

            if (it.status) {
                person = it.person[0]
                Glide.with(requireActivity()).load(person!!.img).into(binding.profileImage)
                binding.name.text = person!!.personName
                binding.phone.text = person!!.phone
                binding.email.text = person!!.houseName
                binding.edtDisability.setText(person!!.disability)
                binding.percentage.setText(
                    person!!.percentage?.let { it1 ->
                        String.format(
                            it1,
                            com.mainproject.mdas.R.string._40
                        )
                    })
                binding.guardian.setText(person!!.guardian)
                binding.edtName.setText(person?.personName)
                progress.isVisible = false
            }
        }

        binding.submit.setOnClickListener {

            if (binding.edtDisability.text.isNotEmpty() && binding.percentage.text.isNotEmpty() && binding.guardian.text.isNotEmpty()) {

                if (person?.percentage == binding.percentage.text.toString() && person?.personName == binding.edtName.text.toString() && person?.guardian == binding.guardian.text.toString() && imageUri == null) {
                    Toast.makeText(requireContext(), "Please Edit Information", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    person?.percentage = binding.percentage.text.toString()
                    person?.personName = binding.edtName.text.toString()
                    person?.guardian = binding.guardian.text.toString()
                    if (imageUri != null){
                        person?.img = imageUri.toString()
                    }
                    progress.isVisible = true
                    viewModel.updateUser(person!!)
                }
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
            binding.profileImage.setImageURI(imageUri)
        }
    }


}