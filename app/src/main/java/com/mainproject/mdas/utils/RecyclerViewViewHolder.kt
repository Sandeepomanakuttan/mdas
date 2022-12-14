package com.mainproject.mdas.utils

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.mainproject.mdas.R
import com.mainproject.mdas.databinding.*
import com.mainproject.mdas.model.response.ResponseClass
import java.time.LocalDate
import java.time.format.DateTimeFormatter

sealed class RecyclerViewViewHolder(binding: ViewBinding):RecyclerView.ViewHolder(binding.root) {

    var itemClickListener : ((view: View, item: ResponseClass, position : Int) -> Unit) ? = null

    class TraineeViewHolder(private val binding: TraineeViewBinding,val context: Context) : RecyclerViewViewHolder(binding){

        fun binding(item: ResponseClass.TraineeClass, label: String){
            binding.connect.isVisible = label == "user" && item.status == "Available"
            binding.TraineeName.text=item.traineeName
            binding.experience.text = String.format(context.resources.getString(R.string.trainee_exp),item.field,item.experience)
            Glide.with(context).load(item.traineeImg).into(binding.imgTrainee)

            binding.connect.setOnClickListener {
              itemClickListener?.invoke(it,item,adapterPosition)
            }
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
            Glide.with(context).load(item.imageUri).into(binding.imgHospital)
        }
    }

    class SchemeViewHolder(private val binding: ItemSchemeViewBinding , val context: Context) : RecyclerViewViewHolder(binding) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun binding(schemeClass: ResponseClass.SchemeClass) {
            val outputFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")


            binding.schemeName.text = schemeClass.schemeName
            Glide.with(context).load(schemeClass.schemeImg).into(binding.schemeImg)
            binding.desc.text= schemeClass.description
            binding.disability.text = schemeClass.disability
            binding.price.text = schemeClass.amount
            val startDate: LocalDate = LocalDate.parse(schemeClass.month)
            val newDate: LocalDate = startDate.plusDays(10)
            binding.end.text =String.format(context.resources.getString(R.string.end_date_s),newDate.format(outputFormatter))
           // binding.month.text = monthName[schemeClass.month?.toInt()!!]

        }

    }

    class SchemeUserViewHolder(private val binding: ItemUserSchemeViewBinding ,val context: Context) : RecyclerViewViewHolder(binding) {
        fun binding(schemeClass: ResponseClass.SchemeClass) {

            binding.schemeName.text = schemeClass.schemeName
            Glide.with(context).load(schemeClass.schemeImg).into(binding.schemeImg)
            binding.desc.text= schemeClass.description
            binding.disability.text = schemeClass.disability
            binding.price.text = schemeClass.amount
            binding.month.text = monthName[schemeClass.month?.toInt()!!]

            binding.button2.setOnClickListener {
                itemClickListener?.invoke(it,schemeClass,adapterPosition)
            }

        }

    }

    class SchemeUserApplyViewHolder(private val binding: HomeSchemeBinding ,val context: Context) : RecyclerViewViewHolder(binding) {
        fun binding(schemeClass: ResponseClass.SchemeClass) {

            binding.name.text = schemeClass.Name
            Glide.with(context).load(schemeClass.schemeImg).into(binding.img)
            binding.disability.text = schemeClass.disability
            binding.houseName.text = schemeClass.amount

            binding.view.setOnClickListener {
                itemClickListener?.invoke(it,schemeClass,adapterPosition)
            }

            binding.btnApprove.setOnClickListener {
                itemClickListener?.invoke(it,schemeClass,adapterPosition)
            }

            binding.schemes.setOnClickListener {

                itemClickListener?.invoke(it,schemeClass,adapterPosition)
            }

        }

    }

}
