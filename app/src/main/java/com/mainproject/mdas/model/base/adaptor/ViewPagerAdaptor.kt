package com.mainproject.mdas.model.base.adaptor

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mainproject.mdas.RegistrationFragment
import com.mainproject.mdas.view.admin.ProfileFragment
import com.mainproject.mdas.view.admin.comman.ApproveSchemeFragment
import com.mainproject.mdas.view.admin.comman.PendingSchemeFragment
import com.mainproject.mdas.view.admin.comman.RejectSchemeFragment
import com.mainproject.mdas.view.admin.hospital.HospitalAddFragment
import com.mainproject.mdas.view.admin.hospital.HospitalViewFragment
import com.mainproject.mdas.view.admin.person.PersonAddFragment
import com.mainproject.mdas.view.admin.person.PersonFragment
import com.mainproject.mdas.view.admin.person.PersonViewFragment
import com.mainproject.mdas.view.admin.person.viewPersonDetailsFragment
import com.mainproject.mdas.view.admin.scheme.SchemeAddFragment
import com.mainproject.mdas.view.admin.scheme.SchemeViewFragment
import com.mainproject.mdas.view.admin.trainee.AvailableTraineeFragment
import com.mainproject.mdas.view.admin.trainee.TraineeAddFragment
import com.mainproject.mdas.view.admin.trainee.TraineeViewFragment



class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    val type: String
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    var item = emptyArray<String>()
    @SuppressLint("NotifyDataSetChanged")
    set(value) {
        field = value
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return item.size
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
            "UserHospital" -> frag =HospitalViewFragment()
            "my" -> frag = if(position==0) viewPersonDetailsFragment() else ProfileFragment()
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
            "commonScheme" ->{
                frag = when(position) {
                    0 -> SchemeViewFragment()
                    1 -> ApproveSchemeFragment()
                    2 -> PendingSchemeFragment()
                    else -> RejectSchemeFragment()
                }
            }

            "common" ->{
                frag = when(position) {
                    0 -> ApproveSchemeFragment()
                    1 -> PendingSchemeFragment()
                    else -> RejectSchemeFragment()
                }
            }
            "user Trainee" ->{
                frag = when (position){
                    0 -> TraineeViewFragment()
                    else -> AvailableTraineeFragment()
                }
            }



        }
        return frag!!
    }
}