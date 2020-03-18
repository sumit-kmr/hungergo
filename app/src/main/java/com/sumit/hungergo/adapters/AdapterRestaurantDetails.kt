package com.sumit.hungergo.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sumit.hungergo.R
import com.sumit.hungergo.models.Food
import com.sumit.hungergo.utils.OnAddRemoveButtonClick

class AdapterRestaurantDetails(val context: Context,
                               val list:ArrayList<Food>,
                               val onAddRemoveButtonClick: OnAddRemoveButtonClick):RecyclerView.Adapter<AdapterRestaurantDetails.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_single_row,parent,false)
        return MyViewHolder(view,context,onAddRemoveButtonClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.txtSlNo.text = (position+1).toString()
        holder.txtFoodName.text = list[position].name
        holder.txtCostForOne.text = "Rs.${list[position].cost_for_one}"
    }

    class MyViewHolder(view: View,val context: Context,val onAddRemoveButtonClick: OnAddRemoveButtonClick):RecyclerView.ViewHolder(view){

        val txtSlNo = view.findViewById(R.id.txtSlNo) as TextView
        val txtFoodName = view.findViewById(R.id.txtFoodName) as TextView
        val txtCostForOne = view.findViewById(R.id.txtCostForOne) as TextView
        val btnAddRemove = view.findViewById(R.id.btnAddRemove) as Button
        var isClicked = false
        init {
            btnAddRemove.setOnClickListener {
                var state = 0
                if(isClicked){
                    btnAddRemove.setBackgroundColor(Color.parseColor("#8bc34a"))
                    btnAddRemove.setText("Add")
                    isClicked = false
                    state = 1
                }else{
                    btnAddRemove.setBackgroundColor(Color.parseColor("#ffc107"))
                    btnAddRemove.setText("Remove")
                    isClicked = true
                    state = 0
                }
                onAddRemoveButtonClick.onAddRemoveButtonClick(adapterPosition,state)
            }
        }
    }
}