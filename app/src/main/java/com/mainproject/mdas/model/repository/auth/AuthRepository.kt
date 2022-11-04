package com.mainproject.mdas.model.repository.auth

import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.mainproject.mdas.model.response.ResponseClass
import com.mainproject.mdas.model.viewmodel.AuthViewModel.Companion.personAddResponse
import com.mainproject.mdas.model.viewmodel.AuthViewModel.Companion.userList
import com.mainproject.mdas.model.viewmodel.admin.AdminViewModel
import com.mainproject.mdas.utils.*
import java.util.*


class AuthRepository {


    private val database= Firebase.database
    val personLiveData = ResponseClass.PersonResponse()



    suspend fun checkUser(phone: String, id: String?) {
        val data = ResponseClass.UserResponse()
            UserRef.child(phone).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userData = snapshot.getValue(ResponseClass.Person::class.java)!!
                    data.status = true
                    data.message="Success"
                    data.user = userData
                    userList.value = data
                }else{
                    data.status = true
                    data.message = "user Not Found"
                    data.user=null
                    userList.value = data
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("data","data not exits")
                data.status = false
                data.message = "Network Error"
                userList.value = data
            }
        })
    }


    fun checkUserExist(user: ResponseClass.Person) {
        val data = ResponseClass.UserResponse()
        UserRef.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var flag = 0
                if (snapshot.exists()) {

                    for (i in snapshot.children){
                        flag =0
                    val userData = i.getValue(ResponseClass.Person::class.java)!!
                    if (userData.phone == user.phone) {
                        flag = 1
                        data.status = false
                        data.message = "Phone Number Already Exist"
                        data.user = null
                        userList.value = data
                        break
                    } else if (userData.adhar == user.adhar) {
                        flag = 1
                        data.status = false
                        data.message = "Aadhaar Already Exist"
                        data.user = userData
                        userList.value = data
                        break
                    }
                }
                    if (flag == 0){
                        data.status = true
//                        data.message = "user Not Found"
                        data.user=null
                        userList.value = data
                    }
                }else{
                    data.status = true
//                    data.message = "user Not Found"
                    data.user=null
                    userList.value = data
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("data","data not exits")
                data.status = false
                data.message = "Network Error"
                userList.value = data
            }
        })
    }


    fun addUser(personClass: ResponseClass.Person) {
        personClass.phone?.let { it ->
            UserRef.child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val data = snapshot.getValue(ResponseClass.Person()::class.java)
                        if (data?.phone == personClass.phone || data?.adhar == personClass.adhar) {
                            personLiveData.status = true
                            personLiveData.message = "User Already Exist"
                            personLiveData.person = emptyList()
                            AdminViewModel.personAddResponse.value = personLiveData
                        }
                        else addNewPerson(personClass)
                    } else {
                        addNewPerson(personClass)
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

    fun addNewPerson(personClass: ResponseClass.Person) {
//        addImage(personClass)
        val ref: StorageReference = storageRef
            .child(
                "person/${personClass.phone}/"
                        + UUID.randomUUID().toString()
            )
        personClass.img?.let {
            ref.putFile(it.toUri())
                .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot>() {
                    ref.downloadUrl.addOnCompleteListener { uri ->
                        if (uri.isSuccessful){
                            personClass.img = uri.result.toString()

                            personClass.imgGuardianAdhar?.let { imgG->
                                ref.putFile(imgG.toUri()).addOnSuccessListener {
                                    ref.downloadUrl.addOnCompleteListener { uriG->
                                        if (uriG.isSuccessful){
                                            personClass.imgGuardianAdhar = uriG.result.toString()

                                            personClass.imgDisability?.let { imgD->
                                                ref.putFile(imgD.toUri()).addOnSuccessListener {
                                                    ref.downloadUrl.addOnCompleteListener { uriD->
                                                        if (uriD.isSuccessful){
                                                            personClass.imgDisability = uriD.result.toString()

                                                            personClass.imgAdhar?.let { imgA->
                                                                ref.putFile(imgA.toUri()).addOnSuccessListener {
                                                                    ref.downloadUrl.addOnCompleteListener { uriA->
                                                                        if (uriA.isSuccessful){
                                                                            personClass.imgAdhar = uriA.result.toString()

                                                                            personClass.id = UserRef.push().key
                                                                            personClass.phone?.let { _ ->

                                                                                UserRef.child(personClass.phone.toString()).setValue(personClass)
                                                                                    .addOnCompleteListener { value ->
                                                                                        if (value.isSuccessful) {
                                                                                            personLiveData.status = true
                                                                                            personLiveData.message = "Successfully Added"
                                                                                            personLiveData.person = emptyList()
                                                                                            personAddResponse.value = personLiveData
                                                                                        } else {
                                                                                            Log.e("call", "called3")
                                                                                            personLiveData.status = false
                                                                                            personLiveData.message = value.exception.toString()
                                                                                            personLiveData.person = emptyList()
                                                                                            personAddResponse.value = personLiveData
                                                                                        }
                                                                                    }
                                                                            }
                                                                        }
                                                                    }
                                                                }

                                                            }
                                                        }
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }

                })

        }

    }

    private fun addImage(personClass: ResponseClass.Person) {

        addPersonImage(personClass)
       addGurdianAdhar(personClass)
       addAdhar(personClass)
       addDisability(personClass)





    }

    private fun addDisability(personClass: ResponseClass.Person) {
        val ref: StorageReference = storageRef
            .child(
                "person/${personClass.phone}/"
                        + UUID.randomUUID().toString()
            )
        personClass.imgDisability.let {
            ref.putFile(it!!.toUri())
                .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot>() {
                    ref.downloadUrl.addOnCompleteListener { uri ->
                        if (uri.isSuccessful){
                            personClass.imgDisability = uri.result.toString()
                        }
                    }

                })

        }
    }

    private fun addAdhar(personClass: ResponseClass.Person) {
        val ref: StorageReference = storageRef
            .child(
                "person/${personClass.phone}/"
                        + UUID.randomUUID().toString()
            )
        personClass.imgAdhar.let {
            ref.putFile(it!!.toUri())
                .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot>() {
                    ref.downloadUrl.addOnCompleteListener { uri ->
                        if (uri.isSuccessful){
                            personClass.imgAdhar = uri.result.toString()
                        }
                    }

                })

        }
    }

    private fun addGurdianAdhar(personClass: ResponseClass.Person) {
        val ref: StorageReference = storageRef
            .child(
                "person/${personClass.phone}/"
                        + UUID.randomUUID().toString()
            )
        personClass.imgGuardianAdhar.let {
            ref.putFile(it!!.toUri())
                .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot>() {
                    ref.downloadUrl.addOnCompleteListener { uri ->
                        if (uri.isSuccessful){
                            personClass.imgGuardianAdhar = uri.result.toString()
                        }
                    }

                })

        }
    }

    private fun addPersonImage(person: ResponseClass.Person) {

    }
}