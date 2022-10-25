package com.mainproject.mdas.view.admin.trainee

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.mainproject.mdas.databinding.FragmentTraineeViewBinding
import com.mainproject.mdas.model.base.BaseFragments
import com.mainproject.mdas.model.response.ResponseClass
import com.mainproject.mdas.model.viewmodel.admin.AdminViewModel
import com.mainproject.mdas.progress
import com.mainproject.mdas.utils.RecyclerViewAdaptor
import com.mainproject.mdas.utils.getPreference

class TraineeViewFragment : BaseFragments<FragmentTraineeViewBinding>(FragmentTraineeViewBinding::inflate) {


    lateinit var viewModel: AdminViewModel
    lateinit var recyclerViewAdaptor: RecyclerViewAdaptor
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[AdminViewModel::class.java]


        val (user,type,disability) = getPreference(requireContext())
        when(type){
            "Admin" ->{
                recyclerViewAdaptor = RecyclerViewAdaptor(requireContext())
                viewModel.getTrainee()
                progress.isVisible = true
            }
            else ->{
                recyclerViewAdaptor = RecyclerViewAdaptor(requireContext())
                viewModel.getTrainee(disability)
                progress.isVisible = true
            }
        }


        observer()

    }

    private fun observer() {
        viewModel.traineeResponse.observe(viewLifecycleOwner){
            progress.isVisible = false
            if (it.status){
                if(it.trainee.isNotEmpty()){
                    setTrainee(it.trainee)
                }
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
        }else{
            binding.recyclerView.isVisible = false
            binding.empty.isVisible = true
        }
    }
}