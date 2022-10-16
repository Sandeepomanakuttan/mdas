package com.mainproject.mdas.model.viewmodel.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mainproject.mdas.model.base.RequestCall
import com.mainproject.mdas.model.repository.admin.AdminHomeRepository
import com.mainproject.mdas.model.response.ResponseClass
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {
companion object {
    val hospitalAddResponse: MutableLiveData<ResponseClass.HospitalResponse> = MutableLiveData()
    val traineeAddResponse: MutableLiveData<ResponseClass.RequestCall> = MutableLiveData()
    val schemeAddResponse: MutableLiveData<ResponseClass.SchemeResponse> = MutableLiveData()
}


    private val repository=AdminHomeRepository()
    fun addTrainee(trainee: ResponseClass.TraineeClass) {
        viewModelScope.launch {
           repository.addTrainee(trainee)
        }
    }

    fun addScheme(scheme: ResponseClass.SchemeClass) {
        viewModelScope.launch {
            repository.addSchemes(scheme)
        }
    }

    fun addHospital(hospital: ResponseClass.Hospital) {
        viewModelScope.launch {
            repository.addHospital(hospital)
        }
    }

}