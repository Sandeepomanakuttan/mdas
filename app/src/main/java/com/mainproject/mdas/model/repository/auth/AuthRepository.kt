package com.mainproject.mdas.model.repository.auth

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mainproject.mdas.model.response.ResponseClass
import com.mainproject.mdas.model.viewmodel.AuthViewModel.Companion.userList
import com.mainproject.mdas.model.viewmodel.User


class AuthRepository {


    private val database= Firebase.database
    private val profileRef=database.getReference("User")


    suspend fun checkUser(phone: String, id: String) {
        val data = ResponseClass.UserResponse()
        profileRef.child(phone).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userData = snapshot.getValue(User::class.java)!!
                    data.status = true
                    data.message="Success"
                    data.user = userData
                    userList.value = data
                }else{
                    data.status = false
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
}