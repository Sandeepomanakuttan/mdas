package com.mainproject.mdas.model.repository.admin


import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.mainproject.mdas.model.response.ResponseClass
import com.mainproject.mdas.model.viewmodel.admin.AdminViewModel
import com.mainproject.mdas.utils.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class AdminHomeRepository {

    val mLiveData = ResponseClass.RequestCall()
    val schemeLiveData = ResponseClass.SchemeResponse()
    val hospitalLiveData = ResponseClass.HospitalResponse()
    val personLiveData = ResponseClass.PersonResponse()

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun addTrainee(trainee: ResponseClass.TraineeClass) {

        trainee.traineeName?.let { it ->
            traineeRef.child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val data = snapshot.getValue(ResponseClass.TraineeClass()::class.java)
                        if (data?.panchayath == trainee.panchayath) {
                            mLiveData.status = true
                            mLiveData.message = "Trainee Already Exist"
                            mLiveData.trainee = emptyList()
                            AdminViewModel.traineeAddResponse.value = mLiveData
                        } else addTrainees(trainee)
                    } else {
                        addTrainees(trainee)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    mLiveData.status = false
                    mLiveData.message = error.message
                    mLiveData.trainee = emptyList()
                    AdminViewModel.traineeAddResponse.value = mLiveData
                }
            })
        }


    }

    fun addTrainees(trainee: ResponseClass.TraineeClass) {

        // Defining the child of storageReference
        // Defining the child of storageReference
        val ref: StorageReference = storageRef
            .child(
                "trainee/"
                        + UUID.randomUUID().toString()
            )
        trainee.traineeImg?.let {
            ref.putFile(it.toUri())
                .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot>() {
                    ref.downloadUrl.addOnCompleteListener { uri ->
                        if (uri.isSuccessful) {
                            trainee.traineeImg = uri.result.toString()
                            trainee.id = traineeRef.push().key
                            trainee.traineeName?.let { its ->
                                traineeRef.child(its).setValue(trainee)
                                    .addOnCompleteListener { value ->
                                        if (value.isSuccessful) {
                                            mLiveData.status = true
                                            mLiveData.message = "Successfully Added"
                                            mLiveData.trainee = emptyList()
                                            AdminViewModel.traineeAddResponse.value = mLiveData
                                        } else {
                                            mLiveData.status = false
                                            mLiveData.message = value.exception.toString()
                                            mLiveData.trainee = emptyList()
                                            AdminViewModel.traineeAddResponse.value = mLiveData
                                        }

                                    }
                            }
                        }


                    }
                })
        }

    }


    suspend fun addSchemes(scheme: ResponseClass.SchemeClass) {

        scheme.schemeName?.let { it ->
            schemeRef.child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val data = snapshot.getValue(ResponseClass.SchemeClass()::class.java)
                        if (data?.panchayath == scheme.panchayath) {
                            schemeLiveData.status = true
                            schemeLiveData.message = "Scheme Already Exist"
                            schemeLiveData.scheme = emptyList()
                            AdminViewModel.schemeAddResponse.value = schemeLiveData
                        } else addScheme(scheme)
                    } else {
                        addScheme(scheme)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    schemeLiveData.status = false
                    schemeLiveData.message = error.message
                    schemeLiveData.scheme = emptyList()
                    AdminViewModel.schemeAddResponse.value = schemeLiveData
                }
            })
        }

    }

    private fun addScheme(scheme: ResponseClass.SchemeClass) {
        val ref: StorageReference = storageRef
            .child(
                "schemes/"
                        + UUID.randomUUID().toString()
            )
        scheme.schemeImg?.let {
            ref.putFile(it.toUri())
                .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot>() {
                    ref.downloadUrl.addOnCompleteListener { uri ->

                        if (uri.isSuccessful) {
                            scheme.schemeImg = uri.result.toString()
                            scheme.id = traineeRef.push().key
                            scheme.schemeName?.let { _ ->
                                schemeRef.child(scheme.id.toString()).setValue(scheme)
                                    .addOnCompleteListener { value ->
                                        if (value.isSuccessful) {
                                            schemeLiveData.status = true
                                            schemeLiveData.message = "Successfully Added"
                                            schemeLiveData.scheme = emptyList()
                                            AdminViewModel.schemeAddResponse.value =
                                                schemeLiveData
                                        } else {
                                            schemeLiveData.status = false
                                            schemeLiveData.message = value.exception.toString()
                                            schemeLiveData.scheme = emptyList()
                                            AdminViewModel.schemeAddResponse.value =
                                                schemeLiveData
                                        }

                                    }
                            }
                        }

                    }
                })
        }

    }

    suspend fun addHospital(hospital: ResponseClass.Hospital) {

        hospital.hospitalName?.let { it ->
            hospitalRef.child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val data = snapshot.getValue(ResponseClass.Hospital()::class.java)
                        if (data?.district == hospital.district && hospital.place == data?.place) {
                            hospitalLiveData.status = true
                            hospitalLiveData.message = "Hospital Already Exist"
                            hospitalLiveData.hospital = emptyList()
                            AdminViewModel.hospitalAddResponse.value = hospitalLiveData
                        } else addNewHospital(hospital)
                    } else {
                        addNewHospital(hospital)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    hospitalLiveData.status = false
                    hospitalLiveData.message = error.message
                    hospitalLiveData.hospital = emptyList()
                    AdminViewModel.hospitalAddResponse.value = hospitalLiveData
                }
            })
        }
    }

    private fun addNewHospital(hospital: ResponseClass.Hospital) {


        val ref: StorageReference = storageRef
            .child(
                "hospital/"
                        + UUID.randomUUID().toString()
            )
        hospital.imageUri?.let {
            ref.putFile(it.toUri())
                .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot>() {
                    ref.downloadUrl.addOnCompleteListener { uri ->

                        if (uri.isSuccessful) {
                            Log.e("call", "called")
                            hospital.id = traineeRef.push().key
                            hospital.hospitalName?.let { _ ->
                                Log.e("call", uri.result.toString())
                                hospital.imageUri = uri.result.toString()
                                hospitalRef.child(hospital.id.toString()).setValue(hospital)
                                    .addOnCompleteListener { value ->
                                        if (value.isSuccessful) {
                                            Log.e("call", "called2")
                                            hospitalLiveData.status = true
                                            hospitalLiveData.message = "Successfully Added"
                                            hospitalLiveData.hospital = emptyList()
                                            AdminViewModel.hospitalAddResponse.value =
                                                hospitalLiveData
                                        } else {
                                            Log.e("call", "called3")
                                            hospitalLiveData.status = false
                                            hospitalLiveData.message = value.exception.toString()
                                            hospitalLiveData.hospital = emptyList()
                                            AdminViewModel.hospitalAddResponse.value =
                                                hospitalLiveData
                                        }
                                    }
                            }
                        }

                    }
                })
        }


    }

    suspend fun addPerson(person: ResponseClass.Person) {


        person.personName?.let { it ->
            personRef.child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val data = snapshot.getValue(ResponseClass.Person::class.java)
                        if (data?.district == person.district && person.panchayath == data?.panchayath && person.houseName == data?.houseName) {
                            personLiveData.status = true
                            personLiveData.message = "Person Already Exist"
                            personLiveData.person = emptyList()
                            AdminViewModel.personAddResponse.value = personLiveData
                        } else addNewPerson(person)
                    } else {
                        addNewPerson(person)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    personLiveData.status = false
                    personLiveData.message = error.message
                    personLiveData.person = emptyList()
                    AdminViewModel.personAddResponse.value = personLiveData
                }
            })
        }
    }

    private fun addNewPerson(person: ResponseClass.Person) {

        person.id = traineeRef.push().key
        person.personName?.let { _ ->

            personRef.child(person.id.toString()).setValue(person)
                .addOnCompleteListener { value ->
                    if (value.isSuccessful) {
                        personLiveData.status = true
                        personLiveData.message = "Successfully Added"
                        personLiveData.person = emptyList()
                        AdminViewModel.personAddResponse.value = personLiveData
                    } else {
                        Log.e("call", "called3")
                        personLiveData.status = false
                        personLiveData.message = value.exception.toString()
                        personLiveData.person = emptyList()
                        AdminViewModel.personAddResponse.value = personLiveData
                    }
                }
        }

    }

    fun getHospital(): LiveData<ResponseClass.HospitalResponse> {
        Log.e("called", "called")
        val mutableLiveData = MutableLiveData<ResponseClass.HospitalResponse>()
        hospitalRef.addValueEventListener(object : ValueEventListener {
            var response = ResponseClass.HospitalResponse()
            var array = arrayListOf<ResponseClass.Hospital>()
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (i in snapshot.children) {
                        val hospital = snapshot.getValue<ResponseClass.Hospital>()
                        if (hospital != null) {
                            array.add(hospital)
                        }
                    }
                    response.hospital = array
                    response.status = true
                    response.message = "success"

                    mutableLiveData.value = response
                }
            }

            override fun onCancelled(error: DatabaseError) {
                response.status = false
                response.message = error.message
                mutableLiveData.value = response
            }

        })
        return mutableLiveData
    }

}