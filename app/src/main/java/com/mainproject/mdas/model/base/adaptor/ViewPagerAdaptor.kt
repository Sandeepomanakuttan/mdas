package com.mainproject.mdas.model.base.adaptor

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mainproject.mdas.view.admin.hospital.HospitalAddFragment
import com.mainproject.mdas.view.admin.hospital.HospitalViewFragment
import com.mainproject.mdas.view.admin.person.PersonAddFragment
import com.mainproject.mdas.view.admin.person.PersonFragment
import com.mainproject.mdas.view.admin.person.PersonViewFragment
import com.mainproject.mdas.view.admin.scheme.SchemeAddFragment
import com.mainproject.mdas.view.admin.scheme.SchemeViewFragment
import com.mainproject.mdas.view.admin.trainee.TraineeAddFragment
import com.mainproject.mdas.view.admin.trainee.TraineeViewFragment

private const val NUM_TABS = 2

public class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    val type: String
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        var frag: Fragment? = null
        when (type) {
            "scheme" -> {
                frag = when (position) {
                    0 -> SchemeViewFragment()
                    else -> SchemeAddFragment()

                }
            }
            "trainee" -> {
                frag = when (position) {
                    0 -> TraineeViewFragment()
                    else -> TraineeAddFragment()

                }
            }
            "person" -> {
                frag = when (position) {
                    0 -> PersonViewFragment()
                    else -> PersonAddFragment()

                }
            }

            "hospital" -> {
                frag = when (position) {
                    0 -> HospitalViewFragment()
                    else -> HospitalAddFragment()

                }
            }



        }
        return frag!!
    }
}