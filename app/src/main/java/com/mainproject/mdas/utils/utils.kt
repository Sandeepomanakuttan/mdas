package com.mainproject.mdas.utils

import android.content.Context
import android.view.View
import android.view.animation.Animation
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


val panchayatArray = arrayOf("Select Panchayath","Aikkaranadu","Alengadu","Amballur","Arakuzha","Asamannur","Avoli","Ayavana","Ayyampuzha","Chellanam","Chendamangalam","Chengamanad","Cheranallur","Paingottoor")

val disableArray = arrayOf("select trainee Field","Leprosy Cured Person","Cerebral Palsy","Dwarfism","Muscular Dystrophy","Acid Attack Victims","Blindness","Low Vision","Deaf","Hard of Hearing","Speech and Language Disability","Specific Learning Disabilities","Autism Spectrum Disorder")
val disableArrays = arrayOf("Select disability Type","Leprosy Cured Person","Cerebral Palsy","Dwarfism","Muscular Dystrophy","Acid Attack Victims","Blindness","Low Vision","Deaf","Hard of Hearing","Speech and Language Disability","Specific Learning Disabilities","Autism Spectrum Disorder")

var monthName = arrayOf("",
    "Jan", "Feb",
    "Mar", "Apr", "May", "June", "July",
    "Aug", "Sep", "Oct", "Nov",
    "Dec"
)

var storageRef = FirebaseStorage.getInstance().reference
val root = FirebaseDatabase.getInstance().reference
val traineeRef = root.child("Trainee")
val schemeRef = root.child("Scheme")
val hospitalRef = root.child("Hospital")
val agreeSchemeRef = root.child("AgreeScheme")
val personRef = root.child("Person Validation")
val UserRef = root.child("User")

fun View.startAnimations(animation: Animation, onEndListener: () -> Unit) {
    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) = Unit

        override fun onAnimationEnd(animation: Animation?) {
            onEndListener()
        }

        override fun onAnimationRepeat(animation: Animation?) = Unit
    })

    this.startAnimation(animation)
}

fun preference(context: Context, userName: String, userType: String, disability: String?=null) {
    val preference = context.getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
    val edit = preference.edit()
    edit.putString("userName",userName)
    edit.putString("type",userType)
    edit.putString("disability",disability)
    edit.apply()
}

fun getPreference(context: Context): ArrayList<String?> {
    val preference = context.getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
    val userName =preference.getString("userName",null)
    val type = preference.getString("type",null)
    val disability = preference.getString("disability",null)
    val array: ArrayList<String?> = arrayListOf()
    array.add(userName)
    array.add(type)
    array.add(disability)
    return array
}

fun clearPreference(context: Context) {
    context.getSharedPreferences("MY_APP", Context.MODE_PRIVATE).edit().clear().apply()
}

fun onPreference(context: Context, onboard:Boolean) {
    val preference = context.getSharedPreferences("MY_On", Context.MODE_PRIVATE)
    val edit = preference.edit()
    edit.putBoolean("board",onboard)
    edit.apply()
}

fun getOnPreference(context: Context): Boolean {
    val preference = context.getSharedPreferences("MY_On", Context.MODE_PRIVATE)
    return preference.getBoolean("board",false)
}

fun onClearPreference(context: Context) {
    context.getSharedPreferences("MY_On", Context.MODE_PRIVATE).edit().clear().apply()
}