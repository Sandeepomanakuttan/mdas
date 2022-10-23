package com.mainproject.mdas.utils

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.mainproject.mdas.databinding.FragmentPersonAddBinding
import com.mainproject.mdas.databinding.HospitalViewBinding
import com.mainproject.mdas.databinding.ItemHomeHospitalViewBinding
import com.mainproject.mdas.databinding.ItemSchemeViewBinding
import com.mainproject.mdas.databinding.PersonViewBinding
import com.mainproject.mdas.databinding.TraineeViewBinding
import com.mainproject.mdas.model.response.ResponseClass

sealed class RecyclerViewViewHolder(binding: ViewBinding):RecyclerView.ViewHolder(binding.root) {

    var itemClickListener : ((view: View, item: ResponseClass, position : Int) -> Unit) ? = null

    class TraineeViewHolder(private val binding: TraineeViewBinding,val context: Context) : RecyclerViewViewHolder(binding){

        fun binding(item: ResponseClass.TraineeClass){
            binding.TraineeName.text=item.traineeName
            binding.experience.text = "2"
            Glide.with(context).load(item.traineeImg).into(binding.imgTrainee)
        }
    }

    class PersonViewHolder(val binding:PersonViewBinding,val context: Context) : RecyclerViewViewHolder(binding){
        fun  binding(item: ResponseClass.Person){

            binding.schemes.isVisible = item.status != "apply"
            binding.name.text=item.personName
            binding.houseName.text = item.houseName
            if (item.img != null) {
                Glide.with(context).load(item.img).into(binding.img)
            }
            else{
                Log.e("itemData",item.img.toString())

                Glide.with(context).load("https://as2.ftcdn.net/v2/jpg/01/18/03/35/1000_F_118033506_uMrhnrjBWBxVE9sYGTgBht8S5liVnIeY.jpg").into(binding.img)
            }

            binding.disability.text = item.disability

            binding.view.setOnClickListener {
                itemClickListener?.invoke(it,item,adapterPosition)
            }
            binding.schemes.setOnClickListener {
                itemClickListener?.invoke(it,item,adapterPosition)
            }
        }
    }

    class HospitalViewHolder(val binding:HospitalViewBinding,val context: Context) : RecyclerViewViewHolder(binding){
        fun  binding(item: ResponseClass.Hospital){
            binding.name.text=item.hospitalName
            binding.location.text = item.place

            Log.e("hospital",item.imageUri.toString())

            Glide.with(context).load(item.imageUri).into(binding.hospitalImg)

            binding.ratingBar.rating = (item.rating?.toFloat() ?: 1f)

             binding.desc.text = item.address

        }
    }

    class HomeHospitalViewHolder(private val binding: ItemHomeHospitalViewBinding,val context: Context) : RecyclerViewViewHolder(binding){

        fun binding(item: ResponseClass.Hospital){
            binding.hospitalName.text=item.hospitalName
            binding.disctrict.text = item.district
            Log.e("hospital",item.imageUri.toString())
            Glide.with(context).load(item.imageUri).into(binding.imgHospital)
        }
    }

    class SchemeViewHolder(private val binding: ItemSchemeViewBinding , val context: Context) : RecyclerViewViewHolder(binding) {
        fun binding(schemeClass: ResponseClass.SchemeClass) {

            binding.schemeName.text = schemeClass.schemeName
            Glide.with(context).load(schemeClass.schemeImg).into(binding.schemeImg)
            binding.desc.text= schemeClass.description
            binding.disability.text = schemeClass.disability
            binding.price.text = schemeClass.amount
            binding.month.text = monthName[schemeClass.month?.toInt()!!]

        }

    }

}
