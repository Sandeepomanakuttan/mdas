package com.mainproject.mdas.utils

import android.view.View
import android.view.animation.Animation
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


val panchayatArray = arrayOf("Select Panchayath","Aikkaranadu","Alengadu","Amballur","Arakuzha","Asamannur","Avoli","Ayavana","Ayyampuzha","Chellanam","Chendamangalam","Chengamanad","Cheranallur","Paingottoor")

val disableArray = arrayOf("select trainee Field","Leprosy Cured Person","Cerebral Palsy","Dwarfism","Muscular Dystrophy","Acid Attack Victims","Blindness","Low Vision","Deaf","Hard of Hearing","Speech and Language Disability","Specific Learning Disabilities","Autism Spectrum Disorder")
val disableArrays = arrayOf("Select disability Type","Leprosy Cured Person","Cerebral Palsy","Dwarfism","Muscular Dystrophy","Acid Attack Victims","Blindness","Low Vision","Deaf","Hard of Hearing","Speech and Language Disability","Specific Learning Disabilities","Autism Spectrum Disorder")

var monthName = arrayOf("",
    "January", "February",
    "March", "April", "May", "June", "July",
    "August", "September", "October", "November",
    "December"
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