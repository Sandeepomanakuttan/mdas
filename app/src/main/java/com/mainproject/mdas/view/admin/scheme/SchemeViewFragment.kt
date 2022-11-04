package com.mainproject.mdas.view.admin.scheme

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mainproject.mdas.R
import com.mainproject.mdas.SwipeToDelete
import com.mainproject.mdas.databinding.FragmentSchemeBinding
import com.mainproject.mdas.databinding.FragmentSchemeViewBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.response.ResponseClass
import com.mainproject.mdas.model.viewmodel.admin.AdminViewModel
import com.mainproject.mdas.progress
import com.mainproject.mdas.utils.RecyclerViewAdaptor
import com.mainproject.mdas.utils.getPreference
import com.mainproject.mdas.utils.schemeRef
import com.mainproject.mdas.utils.traineeRef
import java.text.SimpleDateFormat
import java.util.*

class SchemeViewFragment : BaseFragments<FragmentSchemeViewBinding>(FragmentSchemeViewBinding::inflate) {

    lateinit var viewModel: AdminViewModel
    lateinit var recyclerViewAdaptor: RecyclerViewAdaptor
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[AdminViewModel::class.java]
        recyclerViewAdaptor = RecyclerViewAdaptor(requireContext())

        val (user,type,disability) = getPreference(requireContext())

        if (type != "Admin"){
            viewModel.getScheme(disability.toString())
            progress.isVisible = true
        }else{
            viewModel.getScheme()
            progress.isVisible = true
        }

        observer()

    }

    private fun observer() {
        viewModel.schemeResponse.observe(viewLifecycleOwner){
            progress.isVisible = false
            if (it.status){
                if(it.scheme.isNotEmpty()){
                    setScheme(it.scheme)
                }
            }
        }
    }

    private fun setScheme(scheme: List<ResponseClass.SchemeClass>) {

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            recyclerViewAdaptor.item = scheme
            adapter = recyclerViewAdaptor
            setHasFixedSize(true)
        }
        val (user, type, disability) = getPreference(requireContext())

        if (type == "Admin") {
            val swipetodelete = object : SwipeToDelete(requireContext()) {

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    when (direction) {

                        ItemTouchHelper.LEFT -> {

                            progress.isVisible = true
                            schemeRef.child(scheme[viewHolder.absoluteAdapterPosition].id!!)
                                .removeValue().addOnCompleteListener {
                                    if (it.isSuccessful){
                                        progress.isVisible = false
                                    }
                                }

                        }

                        ItemTouchHelper.RIGHT -> {
                            val c = Calendar.getInstance()
                            val df = SimpleDateFormat("yyyy-MM-dd")
                            val reg_date = df.format(c.time)
                            scheme[viewHolder.absoluteAdapterPosition].month = reg_date
                            progress.isVisible = true
                            schemeRef.child(scheme[viewHolder.absoluteAdapterPosition].id!!).setValue(scheme[viewHolder.absoluteAdapterPosition]).addOnCompleteListener {

                                if (it.isSuccessful){
                                    progress.isVisible = false
                                }else{
                                    progress.isVisible = false
                                    Toast.makeText(requireContext(), "Try Again..", Toast.LENGTH_SHORT).show()
                                }
                            }

                        }
                    }

                }
            }

            val touchHelper = ItemTouchHelper(swipetodelete)
            touchHelper.attachToRecyclerView(binding.recyclerView)
        }
    }
}