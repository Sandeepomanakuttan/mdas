package com.mainproject.mdas.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mainproject.mdas.R
import com.mainproject.mdas.databinding.*
import com.mainproject.mdas.model.response.ResponseClass

class RecyclerViewAdaptor(val context: Context) : RecyclerView.Adapter<RecyclerViewViewHolder>() {

    var itemClickListener: ((view: View, item: ResponseClass, position: Int) -> Unit)? = null

    var item = listOf<ResponseClass>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var label = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewViewHolder {
        return when (viewType){
            R.layout.person_view ->RecyclerViewViewHolder.PersonViewHolder(PersonViewBinding.inflate(LayoutInflater.from(parent.context),parent,false),context)
            R.layout.trainee_view -> RecyclerViewViewHolder.TraineeViewHolder(TraineeViewBinding.inflate(LayoutInflater.from(parent.context),parent,false),context)
            R.layout.hospital_view -> RecyclerViewViewHolder.HospitalViewHolder(HospitalViewBinding.inflate(LayoutInflater.from(parent.context),parent,false),context)
            R.layout.item_home_hospital_view -> RecyclerViewViewHolder.HomeHospitalViewHolder(
                ItemHomeHospitalViewBinding.inflate(LayoutInflater.from(parent.context),parent,false),context)

            R.layout.item_scheme_view -> RecyclerViewViewHolder.SchemeViewHolder(
                ItemSchemeViewBinding.inflate(LayoutInflater.from(parent.context),parent,false),context)
            else -> {throw IllegalArgumentException("view layout not found")}
        }
    }

    override fun onBindViewHolder(holder: RecyclerViewViewHolder, position: Int) {
        holder.itemClickListener = itemClickListener

        return when (holder) {
            is RecyclerViewViewHolder.PersonViewHolder -> holder.binding(item[position] as ResponseClass.Person)
            is RecyclerViewViewHolder.TraineeViewHolder -> holder.binding(item[position] as ResponseClass.TraineeClass)
            is RecyclerViewViewHolder.HospitalViewHolder -> holder.binding(item[position] as ResponseClass.Hospital)
            is RecyclerViewViewHolder.HomeHospitalViewHolder -> holder.binding(item[position] as ResponseClass.Hospital)
            is RecyclerViewViewHolder.SchemeViewHolder -> holder.binding(item[position] as ResponseClass.SchemeClass)
        }
    }

    override fun getItemCount() = item.size

    override fun getItemViewType(position: Int): Int {
        return when (item[position]) {
            is ResponseClass.Person -> R.layout.person_view
            is ResponseClass.TraineeClass -> R.layout.trainee_view
            is ResponseClass.Hospital -> {
                if (label != "home"){
                    R.layout.hospital_view
                }else{
                    R.layout.item_home_hospital_view
                }
            }
            is ResponseClass.SchemeClass -> R.layout.item_scheme_view
            else -> {
                throw IllegalArgumentException("Invalid view")
            }
        }
    }


}