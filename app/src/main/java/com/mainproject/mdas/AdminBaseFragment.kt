package com.mainproject.mdas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
var navView: BottomNavigationView?=null


class AdminBaseFragment :  Fragment(){
    lateinit var viewer: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewer = inflater.inflate(R.layout.fragment_admin_base, container, false)

        return viewer
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment = childFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        navView =viewer.findViewById(R.id.navView)
        navView?.setupWithNavController(navHostFragment.navController)
    }


    private fun onButtomNavSetUp() {

    }

}