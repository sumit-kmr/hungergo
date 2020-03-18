package com.sumit.hungergo.fragments


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sumit.hungergo.R
import com.sumit.hungergo.adapters.AdapterOrderHistory
import com.sumit.hungergo.models.FoodDetails
import com.sumit.hungergo.models.OrderDetail
import com.sumit.hungergo.utils.ConnectionManager
import org.json.JSONException


class OrderHistoryFragment : Fragment() {

    lateinit var sharedPreferences: SharedPreferences
    var previous_orders = ArrayList<OrderDetail>()
    lateinit var parentRecyclerView: RecyclerView
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var recyclerAdapter: AdapterOrderHistory
    lateinit var relativeLayout: RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_history, container, false)

        sharedPreferences = activity!!.getSharedPreferences(getString(R.string.shared_preference),Context.MODE_PRIVATE)
        parentRecyclerView = view.findViewById(R.id.parentRecyclerView)
        linearLayoutManager = LinearLayoutManager(activity as Context)
        relativeLayout = view.findViewById(R.id.relativeLayout)
        //fetchData()

        if(ConnectionManager().checkConnection(activity as Context))
        {
            val usr_id = sharedPreferences.getString("user_id","99")
            val url = "http://13.235.250.119/v2/orders/fetch_result/$usr_id"
            val queue = Volley.newRequestQueue(activity as Context)
            val jsonObjectRequest = object: JsonObjectRequest(Request.Method.GET,url,null,
                Response.Listener {
                    try {
                        val data = it.getJSONObject("data")
                        println(data.toString())
                        val success = data.getBoolean("success")
                        if(success)
                        {
                            println("Success")
                            val dataJsonArray = data.getJSONArray("data")
                            for(i in 0 until dataJsonArray.length())
                            {
                                val jsonObjectData = dataJsonArray.getJSONObject(i)
                                val jsonFoodItemsArray = jsonObjectData.getJSONArray("food_items")
                                var foods = ArrayList<FoodDetails>()
                                for(j in 0 until jsonFoodItemsArray.length())
                                {
                                    val food_item = jsonFoodItemsArray.getJSONObject(j)
                                    foods.add(
                                        FoodDetails(
                                            food_item.getString("food_item_id"),
                                            food_item.getString("name"),
                                            food_item.getString("cost")
                                        )
                                    )
                                }
                                previous_orders.add(OrderDetail(
                                    jsonObjectData.getString("order_id"),
                                    jsonObjectData.getString("restaurant_name"),
                                    jsonObjectData.getString("total_cost"),
                                    jsonObjectData.getString("order_placed_at").substring(0,8),
                                    foods
                                ))
                            }
                            Toast.makeText(activity as Context,"Successful",Toast.LENGTH_SHORT).show()
                            println(previous_orders[0].toString())
                            recyclerAdapter = AdapterOrderHistory(activity as Context,previous_orders)
                            parentRecyclerView.adapter = recyclerAdapter
                            parentRecyclerView.layoutManager = linearLayoutManager
                            relativeLayout.visibility = View.GONE
                        }else{
                            Toast.makeText(activity as Context,"Some error occured",Toast.LENGTH_SHORT).show()
                        }
                    }catch (ex:JSONException){
                        Toast.makeText(activity as Context,"JSON Exception",Toast.LENGTH_SHORT).show()
                    }
                },Response.ErrorListener {
                    Toast.makeText(activity as Context,"Volley error occured",Toast.LENGTH_SHORT).show()
                }){
                override fun getHeaders(): MutableMap<String, String> {
                    var header = HashMap<String, String>()
                    header["Content-type"] = "application/json"
                    header["token"] = "e501e04bc5ccbf"
                    return header
                }
            }
            queue.add(jsonObjectRequest)
        }else{
            var dialog = android.app.AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet connection not available")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
            }
            dialog.setNegativeButton("Exit") { text, listenet ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

        //
        return view
    }

    private fun fetchData() {
        if(ConnectionManager().checkConnection(activity as Context))
        {
            val usr_id = sharedPreferences.getString("user_id","99")
            val url = "http://13.235.250.119/v2/orders/fetch_result/$usr_id"
            val queue = Volley.newRequestQueue(activity as Context)
            val jsonObjectRequest = object: JsonObjectRequest(Request.Method.GET,url,null,
                Response.Listener {
                    try {
                        val data = it.getJSONObject("data")
                        println(data.toString())
                        val success = data.getBoolean("success")
                        if(success)
                        {
                            println("Success")
                            val dataJsonArray = data.getJSONArray("data")
                            for(i in 0 until dataJsonArray.length())
                            {
                                val jsonObjectData = dataJsonArray.getJSONObject(i)
                                val jsonFoodItemsArray = jsonObjectData.getJSONArray("food_items")
                                var foods = ArrayList<FoodDetails>()
                                for(j in 0 until jsonFoodItemsArray.length())
                                {
                                    val food_item = jsonFoodItemsArray.getJSONObject(j)
                                    foods.add(
                                        FoodDetails(
                                        food_item.getString("food_item_id"),
                                            food_item.getString("name"),
                                            food_item.getString("cost")
                                    )
                                    )
                                }
                                previous_orders.add(OrderDetail(
                                    jsonObjectData.getString("order_id"),
                                    jsonObjectData.getString("restaurant_name"),
                                    jsonObjectData.getString("total_cost"),
                                    jsonObjectData.getString("order_placed_at").substring(0,8),
                                    foods
                                ))
                            }
                            Toast.makeText(activity as Context,"Successful",Toast.LENGTH_SHORT).show()
                            println(previous_orders[0].toString())
                        }else{
                            Toast.makeText(activity as Context,"Some error occured",Toast.LENGTH_SHORT).show()
                        }
                    }catch (ex:JSONException){
                        Toast.makeText(activity as Context,"JSON Exception",Toast.LENGTH_SHORT).show()
                    }
                },Response.ErrorListener {
                    Toast.makeText(activity as Context,"Volley error occured",Toast.LENGTH_SHORT).show()
                }){
                override fun getHeaders(): MutableMap<String, String> {
                    var header = HashMap<String, String>()
                    header["Content-type"] = "application/json"
                    header["token"] = "e501e04bc5ccbf"
                    return header
                }
            }
            queue.add(jsonObjectRequest)
        }else{
            var dialog = android.app.AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet connection not available")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
            }
            dialog.setNegativeButton("Exit") { text, listenet ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }
    }


}
