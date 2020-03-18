package com.sumit.hungergo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sumit.hungergo.R
import com.sumit.hungergo.models.OrderDetail

class AdapterOrderHistory(val context: Context,val list:ArrayList<OrderDetail>):RecyclerView.Adapter<AdapterOrderHistory.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_row_order_history,parent,false)
        return MyViewHolder(view,parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txtRest_Name.text = list[position].res_name
        holder.txtDate.text = list[position].date
        holder.childRecyclerView.adapter= AdapterChildRec(holder.parent.context,list[position].items)
        holder.childRecyclerView.layoutManager= LinearLayoutManager(holder.parent.context)
    }

    class MyViewHolder(view: View,val parent:ViewGroup):RecyclerView.ViewHolder(view){
        val txtRest_Name = view.findViewById<TextView>(R.id.txtRest_Name)
        val txtDate = view.findViewById<TextView>(R.id.txtDate)
        val childRecyclerView = view.findViewById<RecyclerView>(R.id.childRecyclerView)
    }
}