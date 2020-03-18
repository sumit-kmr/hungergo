package com.sumit.hungergo.models

class Food(
    val id:String,
    val name:String,
    val cost_for_one:String,
    val restaurant_id:String
){
    public override fun toString(): String {
        return "food_id:$id\tname:$name\tcost_for_one:$cost_for_one\tres_id:$restaurant_id"
    }
}