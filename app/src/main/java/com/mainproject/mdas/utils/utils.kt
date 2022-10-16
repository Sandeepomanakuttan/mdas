package com.mainproject.mdas.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.view.View
import android.view.animation.Animation
import android.webkit.MimeTypeMap
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.ApplicationProvider.getApplicationContext


val panchayatArray = arrayOf("Select Panchayath","Aikkaranadu","Alengadu","Amballur","Arakuzha","Asamannur","Avoli","Ayavana","Ayyampuzha","Chellanam","Chendamangalam","Chengamanad","Cheranallur")

val disableArray = arrayOf("select trainee Field","Leprosy Cured Person","Cerebral Palsy","Dwarfism","Muscular Dystrophy","Acid Attack Victims","Blindness","Low Vision","Deaf","Hard of Hearing","Speech and Language Disability","Specific Learning Disabilities","Autism Spectrum Disorder")

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