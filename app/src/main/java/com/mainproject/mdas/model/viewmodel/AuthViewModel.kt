package com.mainproject.mdas.model.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mainproject.mdas.model.repository.auth.AuthRepository
import com.mainproject.mdas.model.response.ResponseClass
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AuthViewModel() : ViewModel(){

    var otps:MutableLiveData<String>?=null
    @SuppressLint("StaticFieldLeak")
    var s: Activity?=null
    private val database= Firebase.database

    val repository = AuthRepository()

    companion object{
        var userList = MutableLiveData<ResponseClass.UserResponse?>()
        val personAddResponse: MutableLiveData<ResponseClass.PersonResponse> = MutableLiveData()


    }


    private val profileRef=database.getReference("User")


    init {
        otps=MutableLiveData()
    }
    var mauth = FirebaseAuth.getInstance()



    fun sendVerificationCode(phoneNumber: String) {
        val fullnumber = "+91$phoneNumber"
        val options = PhoneAuthOptions.newBuilder(mauth)
            .setPhoneNumber(fullnumber)
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(s!!) // Activity (for callback binding)
            .setCallbacks(mCallbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun checkUser(phone: String, id: String?) {
        viewModelScope.launch {
                repository.checkUser(phone, id)

        }
    }

    fun userCheck(personClass: ResponseClass.Person){
        viewModelScope.launch {
            repository.checkUserExist(personClass)
        }
    }

    fun addUser(personClass: ResponseClass.Person) {
        viewModelScope.launch {
            repository.addNewPerson(personClass)
        }
    }

    private val  mCallbacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks= object :
        PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            Toast.makeText(s , "verification completed", Toast.LENGTH_SHORT).show()
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Log.e("",p0.toString())
            Toast.makeText(s, p0.toString(), Toast.LENGTH_SHORT).show()
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            super.onCodeSent(verificationId, token)
            otps?.value = verificationId
        }

    }
}