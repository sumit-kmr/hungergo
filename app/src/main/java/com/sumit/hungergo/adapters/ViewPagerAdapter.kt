package com.sumit.hungergo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.sumit.hungergo.R

class ViewPagerAdapter(private var context: Context) : PagerAdapter() {
    private lateinit var inflater : LayoutInflater
    private val images_list = arrayListOf<Int>(
        R.drawable.scroll1,
        R.drawable.scroll2,
        R.drawable.scroll3,
        R.drawable.scroll4,
        R.drawable.scroll5,
        R.drawable.scroll1,
        R.drawable.scroll2,
        R.drawable.scroll3,
        R.drawable.scroll4,
        R.drawable.scroll5,
        R.drawable.scroll1,
        R.drawable.scroll2,
        R.drawable.scroll3,
        R.drawable.scroll4,
        R.drawable.scroll5
    )
    override fun getCount(): Int {
        return images_list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.sliding_images,null)
        var image = view.findViewById<ImageView>(R.id.imageView)
        image.setImageResource(images_list[position])
        val viewPager = container as ViewPager
        viewPager.addView(view,0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val v = `object` as View
        vp.removeView(v)
    }
}