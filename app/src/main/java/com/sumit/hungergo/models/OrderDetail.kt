package com.sumit.hungergo.models

class OrderDetail(
    val order_id:String,
    val res_name:String,
    val total_cost:String,
    val date:String,
    val items:ArrayList<FoodDetails>
){
    override fun toString(): String {
        var str = "od_id:$order_id\nres_nm:$res_name\ntotal_cst:$total_cost\ndate:$date"
        var str2 = ""
        for(i in 0 until items.size){
            str2 += items[i].name+"\n"
        }
        str += str2
        return str
    }
}