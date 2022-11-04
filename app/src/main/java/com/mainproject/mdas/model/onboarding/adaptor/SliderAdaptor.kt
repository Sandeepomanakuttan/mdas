package com.mainproject.mdas.model.onboarding.adaptor

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.mainproject.mdas.R
import com.mainproject.mdas.utils.startAnimations


class SliderAdaptor(private val context:Context, private val listener:SlideCallBack) :PagerAdapter() {
    val animation = AnimationUtils.loadAnimation(context, R.anim.animate_fade_enter)
    @SuppressLint("UseCompatLoadingForDrawables")
    private val image = arrayListOf(R.drawable.onboard1,R.drawable.onboard2,R.drawable.g3)

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater:LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = layoutInflater.inflate(R.layout.item_onbording,container,false)
        val slideImage = view.findViewById(R.id.sliderImg) as ImageView
        val button = view.findViewById(R.id.next) as ImageView
        val start = view.findViewById(R.id.start) as ImageView



        Glide.with(context).load(image[position]).into(slideImage)


        if (position <2) {
            Log.e("position", position.toString())
            button.isVisible = true
            start.isVisible = false

            button.setOnClickListener {
                    listener.itemClick(position)
                //RunAnimation(slideImage)
            }
        }else{
            button.isVisible = false
            start.isVisible = true

            start.setOnClickListener {
                    listener.itemClick(position)

            }
            
        }



            container.addView(view)
            return view

        }


    override fun getCount(): Int {
        return image.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view==`object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }
    private fun RunAnimation(slideImage: ImageView) {
        val a: Animation = AnimationUtils.loadAnimation(context, R.anim.animate_fade_enter)
        a.reset()
        slideImage.clearAnimation()
        slideImage.startAnimation(a)
    }
}

interface SlideCallBack {
    fun itemClick(i: Int)

}
