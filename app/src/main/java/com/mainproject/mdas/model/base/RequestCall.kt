package com.mainproject.mdas.model.base

import com.mainproject.mdas.model.response.ResponseClass

class RequestCall {
    var status = 0
    var message: String = "NO MESSAGE"
    var trainee: List<ResponseClass.TraineeClass> = ArrayList()

}