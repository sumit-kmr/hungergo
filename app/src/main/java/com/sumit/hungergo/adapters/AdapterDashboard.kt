package com.sumit.hungergo.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.sumit.hungergo.R
import com.sumit.hungergo.activity.RestaurantDetailActivity
import com.sumit.hungergo.models.Restaurant

class AdapterDashboard(val context: Context,val list:ArrayList<Restaurant>):RecyclerView.Adapter<AdapterDashboard.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_single_cell,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txtView.text = list[position].name
        val imageLink = list[position].image_url
        Picasso.get().load(imageLink).error(R.drawable.scroll1).into(holder.imgBackground)
        val res_id = list[position].id
        holder.layout.setOnClickListener {
            var intent = Intent(context,RestaurantDetailActivity::class.java)
            intent.putExtra("url","http://13.235.250.119/v2/restaurants/fetch_result/$res_id")
            intent.putExtra("res_name",list[position].name)
            context.startActivity(intent)
        }
    }

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtView = view.findViewById<TextView>(R.id.txtResName)
        val imgBackground = view.findViewById<ImageView>(R.id.backgroundImage)
        val layout = view.findViewById<RelativeLayout>(R.id.contentLayout)
    }
}