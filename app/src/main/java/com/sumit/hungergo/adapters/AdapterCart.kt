package com.sumit.hungergo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sumit.hungergo.R
import com.sumit.hungergo.models.CartItem

class AdapterCart(val context: Context,val list:ArrayList<CartItem>):RecyclerView.Adapter<AdapterCart.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cart_single_row,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txtItemName.text = list[position].name
        holder.txtPrice.text = "Rs.${list[position].cost}"
    }

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
        val txtItemName = view.findViewById<TextView>(R.id.txtItemName)
        val txtPrice = view.findViewById<TextView>(R.id.txtPrice)
    }
}