package com.sumit.hungergo.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sumit.hungergo.R
import com.sumit.hungergo.adapters.AdapterCart
import com.sumit.hungergo.models.CartItem
import com.sumit.hungergo.utils.ConnectionManager
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CartActivity : AppCompatActivity() {

    lateinit var sharedPreferences:SharedPreferences
    lateinit var txtOrderingFrom:TextView
    lateinit var recyclerView: RecyclerView
    lateinit var btnPlaceOrder: Button
    lateinit var layoutManager: LinearLayoutManager
    lateinit var recyclerAdapter:AdapterCart
    lateinit var total_cost:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        sharedPreferences = getSharedPreferences(getString(R.string.shared_preference),Context.MODE_PRIVATE)
        txtOrderingFrom = findViewById(R.id.txtOrderingFrom)
        recyclerView = findViewById(R.id.recyclerView)
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)
        layoutManager = LinearLayoutManager(this)
        total_cost = intent.getStringExtra("total_cost")
        btnPlaceOrder.text = "PLACE ORDER (Total:$total_cost)"

        txtOrderingFrom.text = "Ordering from: ${intent.getStringExtra("res_name")}"
        var food_names = intent.getStringArrayListExtra("food_names")
        var food_costs = intent.getStringArrayListExtra("food_costs")
        var cart_items = ArrayList<CartItem>()
        for(i in 0 until food_names.size)
        {
            cart_items.add(CartItem(food_names[i],food_costs[i]))
        }
        recyclerAdapter = AdapterCart(this,cart_items)
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = layoutManager
    }

    fun place_order(view: View) {

        val usr_id = sharedPreferences.getString("user_id", "99")
        val res_id = intent.getStringExtra("res_id")
        val food_ids = intent.getStringArrayListExtra("food_list")
        var jsonObjectParam = JSONObject()
        jsonObjectParam.put("user_id", usr_id.toString())
        jsonObjectParam.put("restaurant_id", res_id)
        jsonObjectParam.put("total_cost", total_cost)
        var jsonArray = JSONArray()
        for(i in 0 until food_ids.size)
        {
            jsonArray.put(JSONObject().put("food_item_id",food_ids[i]))
        }
        jsonObjectParam.put("food",jsonArray)
        if(ConnectionManager().checkConnection(this)){
            val url = "http://13.235.250.119/v2/place_order/fetch_result/"
            var queue = Volley.newRequestQueue(this)
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST,url,jsonObjectParam,
                Response.Listener {
                    try {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if(success){
                            // next intent
                            Toast.makeText(this,"Order successfully placed",Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,SuccessActivity::class.java))
                            finish()
                        }else{
                            Toast.makeText(this,"Some unexpected error occured", Toast.LENGTH_SHORT).show()
                        }
                    }catch (ex: JSONException){
                        Toast.makeText(this,"JSON Exception", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener {
                    Toast.makeText(this,"Volley Error occured", Toast.LENGTH_SHORT).show()
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
            var dialog = android.app.AlertDialog.Builder(this)
            dialog.setTitle("Error")
            dialog.setMessage("Internet connection not available")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
            }
            dialog.setNegativeButton("Exit") { text, listenet ->
                ActivityCompat.finishAffinity(this as Activity)
            }
            dialog.create()
            dialog.show()
        }
    }
}
