package com.mainproject.mdas.model.viewmodel.admin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mainproject.mdas.model.repository.admin.AdminHomeRepository
import com.mainproject.mdas.model.response.ResponseClass
import com.mainproject.mdas.utils.*
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {
companion object {
    val hospitalAddResponse: MutableLiveData<ResponseClass.HospitalResponse> = MutableLiveData()
    val traineeAddResponse: MutableLiveData<ResponseClass.RequestCall> = MutableLiveData()
    val schemeAddResponse: MutableLiveData<ResponseClass.SchemeResponse> = MutableLiveData()
    val personAddResponse: MutableLiveData<ResponseClass.PersonResponse> = MutableLiveData()
}

    val hospitalResponse: MutableLiveData<ResponseClass.HospitalResponse> = MutableLiveData()
    val personResponse: MutableLiveData<ResponseClass.PersonResponse> = MutableLiveData()
    val userResponse: MutableLiveData<ResponseClass.PersonResponse> = MutableLiveData()
    val schemeResponse: MutableLiveData<ResponseClass.SchemeResponse> = MutableLiveData()
    val agreeSchemeResponse: MutableLiveData<ResponseClass.SchemeResponse> = MutableLiveData()
    val traineeResponse: MutableLiveData<ResponseClass.RequestCall> = MutableLiveData()
    val changeStatus: MutableLiveData<Boolean> = MutableLiveData()



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

    fun addPerson(person: ResponseClass.Person) {
        viewModelScope.launch {
            repository.addPerson(person)
        }
    }

    fun getHospital() {

        hospitalRef.addValueEventListener(object :ValueEventListener{
            val response = ResponseClass.HospitalResponse()

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = arrayListOf<ResponseClass.Hospital>()
                for (singleSnap in snapshot.children){
                    val data = singleSnap.getValue(ResponseClass.Hospital::class.java)
                    list.add(data!!)
                }
                response.hospital=list
                response.status = true
                    hospitalResponse.value=response
            }

            override fun onCancelled(error: DatabaseError) {
                response.hospital= emptyList()
                response.status = true
                hospitalResponse.value=response
            }
        })


    }

    fun getHospital(disability:String) {

        hospitalRef.orderByChild("disability").equalTo(disability).addValueEventListener(object :ValueEventListener{
            val response = ResponseClass.HospitalResponse()

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = arrayListOf<ResponseClass.Hospital>()
                for (singleSnap in snapshot.children){
                    val data = singleSnap.getValue(ResponseClass.Hospital::class.java)
                    list.add(data!!)
                }
                response.hospital=list
                response.status = true
                hospitalResponse.value=response
            }

            override fun onCancelled(error: DatabaseError) {
                response.hospital= emptyList()
                response.status = true
                hospitalResponse.value=response
            }
        })


    }

    fun getPerson()  {

        UserRef.orderByChild("status").equalTo("Verify").addValueEventListener(object :ValueEventListener{
            val response = ResponseClass.PersonResponse()

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = arrayListOf<ResponseClass.Person>()
                for (singleSnap in snapshot.children){
                    val data = singleSnap.getValue(ResponseClass.Person::class.java)
                    list.add(data!!)
                }
                response.person=list
                response.status = true
                personResponse.value=response
            }

            override fun onCancelled(error: DatabaseError) {
                response.person= emptyList()
                response.status = true
                personResponse.value=response
            }
        })


    }

    fun getNewPerson()  {

        UserRef.orderByChild("status").equalTo("apply").addValueEventListener(object :ValueEventListener{
            val response = ResponseClass.PersonResponse()

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = arrayListOf<ResponseClass.Person>()
                for (singleSnap in snapshot.children){
                    val data = singleSnap.getValue(ResponseClass.Person::class.java)
                    list.add(data!!)
                }
                response.person=list
                response.status = true
                personResponse.value=response
            }

            override fun onCancelled(error: DatabaseError) {
                response.person= emptyList()
                response.status = true
                personResponse.value=response
            }
        })


    }

    fun getAgreeScheme(user: String, s: String) {
        val response = ResponseClass.SchemeResponse()

        agreeSchemeRef.child(user).orderByChild("status").equalTo(s).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = arrayListOf<ResponseClass.SchemeClass>()
                if(snapshot.exists()) {
                    for (singleSnap in snapshot.children) {
                        val data = singleSnap.getValue(ResponseClass.SchemeClass::class.java)
                        list.add(data!!)
                    }
                    response.scheme = list
                    response.status = true
                    agreeSchemeResponse.value = response
                }else{
                    response.scheme = list
                    response.status = true
                    agreeSchemeResponse.value = response
                }
            }

            override fun onCancelled(error: DatabaseError) {
                response.scheme= emptyList()
                response.status = true
                agreeSchemeResponse.value=response
            }
        })
    }

    fun getAgreeScheme() {
        val response = ResponseClass.SchemeResponse()

        agreeSchemeRef.orderByChild("status").equalTo("apply").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = arrayListOf<ResponseClass.SchemeClass>()
                for (singleSnap in snapshot.children){
                    val data = singleSnap.getValue(ResponseClass.SchemeClass::class.java)
                    list.add(data!!)
                }
                response.scheme=list
                response.status = true
                agreeSchemeResponse.value=response
            }

            override fun onCancelled(error: DatabaseError) {
                response.scheme= emptyList()
                response.status = true
                agreeSchemeResponse.value=response
            }
        })
    }

    fun getScheme() {
        schemeRef.addValueEventListener(object :ValueEventListener{
            val response = ResponseClass.SchemeResponse()

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = arrayListOf<ResponseClass.SchemeClass>()
                for (singleSnap in snapshot.children){
                    val data = singleSnap.getValue(ResponseClass.SchemeClass::class.java)
                    list.add(data!!)
                }
                response.scheme=list
                response.status = true
                schemeResponse.value=response
            }

            override fun onCancelled(error: DatabaseError) {
                response.scheme= emptyList()
                response.status = true
                schemeResponse.value=response
            }
        })
    }

    fun getUniqPerson(child: String) {
        UserRef.child(child).addValueEventListener(object :ValueEventListener{
            val response = ResponseClass.PersonResponse()

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = arrayListOf<ResponseClass.Person>()
                    val data = snapshot.getValue(ResponseClass.Person::class.java)
                    list.add(data!!)

                response.person=list
                response.status = true
                personResponse.value=response
            }

            override fun onCancelled(error: DatabaseError) {
                response.person= emptyList()
                response.status = true
                personResponse.value=response
            }
        })
    }

    fun getScheme(disability: String) {
        schemeRef.orderByChild("disability").equalTo(disability).addValueEventListener(object :ValueEventListener{
            val response = ResponseClass.SchemeResponse()

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val list = arrayListOf<ResponseClass.SchemeClass>()
                    for (singleSnap in snapshot.children) {
                        val data = singleSnap.getValue(ResponseClass.SchemeClass::class.java)
                        list.add(data!!)
                    }
                    response.scheme = list
                    response.status = true
                    schemeResponse.value = response
                }else{
                    response.scheme = emptyList()
                    response.status = true
                    schemeResponse.value = response
                }
            }

            override fun onCancelled(error: DatabaseError) {
                response.scheme= emptyList()
                response.status = true
                schemeResponse.value=response
            }
        })
    }

    fun getUserScheme(disability: String) {
        schemeRef.orderByChild("disability").equalTo(disability).addValueEventListener(object :ValueEventListener{
            val response = ResponseClass.SchemeResponse()

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val list = arrayListOf<ResponseClass.SchemeClass>()
                    for (singleSnap in snapshot.children) {
                        val data = singleSnap.getValue(ResponseClass.SchemeClass::class.java)
                        list.add(data!!)
                    }
                    response.scheme = list
                    response.status = true
                    schemeResponse.value = response

                }else{
                    response.scheme = emptyList()
                    response.status = true
                    schemeResponse.value = response
                }
            }

            override fun onCancelled(error: DatabaseError) {
                response.scheme= emptyList()
                response.status = true
                schemeResponse.value=response
            }
        })
    }

    fun  agreeScheme(user:String){
        val response = ResponseClass.SchemeResponse()
        agreeSchemeRef.child(user).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = arrayListOf<ResponseClass.SchemeClass>()
                if (snapshot.exists()){
                    for (i in snapshot.children){
                        val data = i.getValue(ResponseClass.SchemeClass::class.java)
                        list.add(data!!)
                        Log.e("hello", data.description.toString())

                    }
                    response.scheme=list
                    response.status = true
                    agreeSchemeResponse.value = response

                }else{
                    response.scheme= emptyList()
                    response.status = true
                    agreeSchemeResponse.value = response

                }
//                response.scheme = list
//                response.status = true
//                schemeResponse.value = response
//                Log.e("hello","heloo.toString()")
            }


            override fun onCancelled(error: DatabaseError) {
                response.scheme= emptyList()
                response.status = false
                schemeResponse.value = response
            }
        })
    }

    fun update(phone: String?) {
        phone?.let {
            UserRef.child(phone).child("status").setValue("Verify").addOnCompleteListener {
                if (it.isSuccessful){
                    changeStatus.value = it.isSuccessful

                }else{
                    changeStatus.value = false
                }
            }

        }
    }

    fun getDetails(sharedNameValue: String?) {
        UserRef.child(sharedNameValue.toString()).addValueEventListener(object :ValueEventListener{
            val response = ResponseClass.PersonResponse()

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = arrayListOf<ResponseClass.Person>()
                    val data = snapshot.getValue(ResponseClass.Person::class.java)
                    list.add(data!!)

                response.person=list
                response.status = true
                userResponse.value=response
            }

            override fun onCancelled(error: DatabaseError) {
                response.person= emptyList()
                response.status = true
                userResponse.value=response
            }
        })
    }

    fun getTrainee(disability: String?) {

        traineeRef.orderByChild("field").equalTo(disability).addValueEventListener(object :ValueEventListener{
            val response = ResponseClass.RequestCall()

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = arrayListOf<ResponseClass.TraineeClass>()
                if (snapshot.exists()) {
                    for (singleSnap in snapshot.children) {
                        val data = singleSnap.getValue(ResponseClass.TraineeClass::class.java)
                        list.add(data!!)
                    }
                    response.trainee = list
                    response.status = true
                    traineeResponse.value = response
                }else{
                    response.trainee = emptyList()
                    response.status = true
                    traineeResponse.value = response
                }
            }

            override fun onCancelled(error: DatabaseError) {
                response.trainee= emptyList()
                response.status = true
                traineeResponse.value=response
            }
        })


    }

    fun getTrainee() {

        traineeRef.addValueEventListener(object :ValueEventListener{
            val response = ResponseClass.RequestCall()

            override fun onDataChange(snapshot: DataSnapshot) {
                val list = arrayListOf<ResponseClass.TraineeClass>()
                for (singleSnap in snapshot.children){
                    val data = singleSnap.getValue(ResponseClass.TraineeClass::class.java)
                    list.add(data!!)
                }
                response.trainee=list
                response.status = true
                traineeResponse.value=response
            }

            override fun onCancelled(error: DatabaseError) {
                response.trainee= emptyList()
                response.status = true
                traineeResponse.value=response
            }
        })


    }



    fun getTrainee(disability: String?, s: String) {

        traineeRef.orderByChild("field").equalTo(disability).addValueEventListener(object :ValueEventListener{
            val response = ResponseClass.RequestCall()

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                val list = arrayListOf<ResponseClass.TraineeClass>()
                for (singleSnap in snapshot.children) {
                    val data = singleSnap.getValue(ResponseClass.TraineeClass::class.java)
                    if (data?.status == s)
                        list.add(data)
                }
                response.trainee = list
                response.status = true
                traineeResponse.value = response
            }
                else{
                    response.trainee = emptyList()
                    response.status = true
                    traineeResponse.value = response
                }
            }

            override fun onCancelled(error: DatabaseError) {
                response.trainee= emptyList()
                response.status = false
                traineeResponse.value=response
            }
        })


    }

}