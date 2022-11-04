package com.mainproject.mdas.view.admin.trainee

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.mainproject.mdas.databinding.FragmentAvailableTraineeBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.response.ResponseClass
import com.mainproject.mdas.model.viewmodel.admin.AdminViewModel
import com.mainproject.mdas.progress
import com.mainproject.mdas.utils.RecyclerViewAdaptor
import com.mainproject.mdas.utils.getPreference
import com.mainproject.mdas.utils.traineeRef


class AvailableTraineeFragment :
    BaseFragments<FragmentAvailableTraineeBinding>(FragmentAvailableTraineeBinding::inflate) {

    lateinit var disable: String
    lateinit var viewModel: AdminViewModel
    lateinit var recyclerViewAdaptor: RecyclerViewAdaptor
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[AdminViewModel::class.java]
        observer()
        val (user, type, disability) = getPreference(requireContext())

        disable = disability.toString()
        if (type == "Admin") {

        } else {
            recyclerViewAdaptor = RecyclerViewAdaptor(requireContext())
            viewModel.getTrainee(disability, "Available")
            progress.isVisible = true
            recyclerViewAdaptor.label = "user"
        }


    }

    private fun observer() {
        viewModel.traineeResponse.observe(viewLifecycleOwner) {
            progress.isVisible = false
            if (it.status) {

                    setTrainee(it.trainee)

            }
        }
    }

    private fun setTrainee(trainee: List<ResponseClass.TraineeClass>) {
        if (trainee.isNotEmpty()) {
            binding.recyclerView.isVisible = true
            binding.empty.isVisible = false
            binding.recyclerView.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                recyclerViewAdaptor.item = trainee
                adapter = recyclerViewAdaptor
                setHasFixedSize(true)
            }
        } else {
            binding.recyclerView.isVisible = false
            binding.empty.isVisible = true
        }
        itemClickListener(recyclerViewAdaptor)


    }


    private fun itemClickListener(recycleViewAdaptor: RecyclerViewAdaptor) {
        recycleViewAdaptor.itemClickListener = { view, item, position ->

            when (item) {
                is ResponseClass.TraineeClass -> {
                    progress.isVisible = true
                    item.status = "connect"
                    traineeRef.child(item.traineeName!!).setValue(item).addOnCompleteListener {
                        if (it.isSuccessful) {
                            viewModel.getTrainee(disable, "Available")
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