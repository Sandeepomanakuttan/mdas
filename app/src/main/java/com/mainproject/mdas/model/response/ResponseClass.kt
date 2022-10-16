package com.mainproject.mdas.model.response

import android.net.Uri
import com.mainproject.mdas.model.viewmodel.User

sealed class ResponseClass{

    class TraineeClass(
        var id:String?=null,
        var traineeName: String?=null,
        var field: String?=null,
        var panchayath: String?=null, var status:String?=null,
        var traineeImg: String ?=null) :ResponseClass()
    class SchemeClass(var id:String?=null,var schemeName: String?=null,var disability:String?=null ,var panchayath : String?=null,var schemeImg: String?=null,var amount : String?=null,var type:String?=null) :ResponseClass()

    class UserResponse(var status:Boolean = false,var message:String?=null,var user: User?=null)

   class RequestCall (
        var status:Boolean = false,
        var message: String = "NO MESSAGE",
        var trainee: List<TraineeClass> = ArrayList()
   ):ResponseClass()

    class SchemeResponse (
        var status:Boolean = false,
        var message: String = "NO MESSAGE",
        var scheme: List<SchemeClass> = ArrayList()
    ):ResponseClass()

    class Hospital(var id: String?=null ,var imageUri: String?=null,var hospitalName: String?=null,var district: String?=null,var place: String?=null,var desc: String?=null,var rating: String?=null) : ResponseClass()
    class HospitalResponse(
        var status:Boolean = false,
        var message: String = "NO MESSAGE",
        var hospital: List<Hospital> = ArrayList()
    ) : ResponseClass()
}
