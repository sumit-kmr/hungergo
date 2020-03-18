package com.sumit.hungergo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sumit.hungergo.R
import com.sumit.hungergo.models.FoodDetails

class AdapterChildRec(val context: Context,val list: ArrayList<FoodDetails>):RecyclerView.Adapter<AdapterChildRec.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_single_row,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txtItemName.text = list[position].name
        holder.txtPrice.text = list[position].cost
    }

    class MyViewHolder(view:View):RecyclerView.ViewHolder(view){
        val txtItemName = view.findViewById(R.id.txtItemName) as TextView
        val txtPrice = view.findViewById(R.id.txtPrice) as TextView
    }
}