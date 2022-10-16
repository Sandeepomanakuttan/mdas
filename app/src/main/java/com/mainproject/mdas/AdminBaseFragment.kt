package com.mainproject.mdas

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mainproject.mdas.databinding.FragmentAdminBaseBinding
import com.mainproject.mdas.model.base.BaseFragments


class AdminBaseFragment :  Fragment(){
    lateinit var viewer: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewer = inflater.inflate(com.mainproject.mdas.R.layout.fragment_admin_base, container, false)

        return viewer
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment = childFragmentManager.findFragmentById(
            com.mainproject.mdas.R.id.nav_host_fragment
        ) as NavHostFragment
        val navView =viewer.findViewById<BottomNavigationView>(com.mainproject.mdas.R.id.navView)
        navView.setupWithNavController(navHostFragment.navController)
    }


    private fun onButtomNavSetUp() {

    }

}