package com.mainproject.mdas

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

lateinit var progress:ConstraintLayout
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)

        progress= findViewById(R.id.progress)
        progress.isVisible = false

        if (checkGooglePlayServices()) {

        } else {
            //You won't be able to send notifications to this device
            Log.w("update", "Device doesn't have google play services")
        }
    }


    private fun checkGooglePlayServices(): Boolean {
        // 1
        val status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        // 2
        return if (status != ConnectionResult.SUCCESS) {
            Log.e("update", "Error")
            // ask user to update google play services and manage the error.
            false
        } else {
            // 3
            Log.i("update", "Google play services updated")
            true
        }
    }

}