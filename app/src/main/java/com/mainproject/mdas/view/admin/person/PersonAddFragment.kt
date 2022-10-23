package com.mainproject.mdas.view.admin.person

import android.app.Activity
import android.app.Person
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
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mainproject.mdas.R
import com.mainproject.mdas.databinding.FragmentPersonAddBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.response.ResponseClass
import com.mainproject.mdas.model.viewmodel.admin.AdminViewModel
import com.mainproject.mdas.model.viewmodel.admin.AdminViewModel.Companion.personAddResponse
import com.mainproject.mdas.progress
import com.mainproject.mdas.utils.RecyclerViewAdaptor


class PersonAddFragment : BaseFragments<FragmentPersonAddBinding>(FragmentPersonAddBinding::inflate) {
    lateinit var viewModel: AdminViewModel
    lateinit var recycleViewAdaptor: RecyclerViewAdaptor
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[AdminViewModel::class.java]

        recycleViewAdaptor= RecyclerViewAdaptor(requireContext())

       viewModel.getPerson()
        progress.isVisible = true

        viewModel.personResponse.observe(viewLifecycleOwner){
            if (it.status){
                progress.isVisible = false
                binding.recyclerView.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    recycleViewAdaptor.item = it.person
                    adapter = recycleViewAdaptor
                    setHasFixedSize(true)
                }

                itemClickListener(recycleViewAdaptor)
            }else{
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun itemClickListener(recycleViewAdaptor: RecyclerViewAdaptor) {

        val bundle = bundleOf()
        recycleViewAdaptor.itemClickListener={view, item, position ->
            when (item){
                is ResponseClass.Person->{
                    bundle.putString("id",item.id)
                    bundle.putString("child",item.phone)
                    bundle.putBoolean("page",true)
                    when(view.id) {
                        R.id.schemes -> {
                            findNavController().navigate(R.id.action_personFragment_to_commonSchemesFragment,bundle)
                        }
                        R.id.view ->{
                            findNavController().navigate(R.id.action_personFragment_to_viewPersonDetailsFragment,bundle)
                        }
                    }
                }
                else -> {}
            }

        }

    }
}