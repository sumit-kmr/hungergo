package com.sumit.hungergo.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sumit.hungergo.R
import com.sumit.hungergo.adapters.AdapterDashboard
import com.sumit.hungergo.adapters.AdapterRestaurantDetails
import com.sumit.hungergo.models.Food
import com.sumit.hungergo.models.Restaurant
import com.sumit.hungergo.utils.ConnectionManager
import com.sumit.hungergo.utils.OnAddRemoveButtonClick
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class RestaurantDetailActivity : AppCompatActivity(),OnAddRemoveButtonClick {

    lateinit var progressBarRelativeLayout:RelativeLayout
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerViewAdapter:AdapterRestaurantDetails
    lateinit var layoutManager: LinearLayoutManager
    lateinit var btnAddRemove:Button
    lateinit var sharedPreferences: SharedPreferences
    lateinit var toolbar: Toolbar
    var foodList = ArrayList<Food>()
    var cartItems = ArrayList<Boolean>()
    var items_in_cart = 0
    lateinit var txt:TextView
    var url:String? = null
    var res_name:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_detail)
        res_name = intent.getStringExtra("res_name")
        url = intent.getStringExtra("url")

        recyclerView = findViewById(R.id.recyclerViewResDetails)
        btnAddRemove = findViewById(R.id.btnProceedToCart)
        progressBarRelativeLayout = findViewById(R.id.progressLayout)
        toolbar = findViewById(R.id.toolbar)
        sharedPreferences = getSharedPreferences(getString(R.string.shared_preference),Context.MODE_PRIVATE)
        progressBarRelativeLayout.visibility = View.VISIBLE
        setUpToolbar()
        if(ConnectionManager().checkConnection(this)){
            var queue = Volley.newRequestQueue(this)
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET,url,null,
                Response.Listener {
                    try {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if(success){
                            val jsonArray = data.getJSONArray("data")
                            for(i in 0 until jsonArray.length())
                            {
                                val jsonObject = jsonArray.getJSONObject(i)
                                foodList.add(
                                    Food(
                                        jsonObject.getString("id"),
                                        jsonObject.getString("name"),
                                        jsonObject.getString("cost_for_one"),
                                        jsonObject.getString("restaurant_id")
                                    )
                                )
                                cartItems.add(false)
                            }
                            recyclerViewAdapter = AdapterRestaurantDetails(this,foodList,this)
                            layoutManager = LinearLayoutManager(this)
                            recyclerView.adapter = recyclerViewAdapter
                            recyclerView.layoutManager = layoutManager
                            progressBarRelativeLayout.visibility = View.GONE
                        }else{
                            Toast.makeText(this,data.getString("errorMessage"),
                                Toast.LENGTH_SHORT).show()
                        }
                    }catch (ex: JSONException){
                        Toast.makeText(this,"JSON exception", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener {
                    Toast.makeText(this,"Volley error occured", Toast.LENGTH_SHORT).show()
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
                ActivityCompat.finishAffinity(this)
            }
            dialog.create()
            dialog.show()
        }

    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = res_name
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if(itemId == android.R.id.home){
            if(items_in_cart != 0)
                askBeforeLeaving()
            else
                finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if(items_in_cart != 0)
            askBeforeLeaving()
        else
            finish()
    }

    private fun askBeforeLeaving() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Confirmation")
        alertDialog.setMessage("Items in the cart will be removed.\nContinue?")
        alertDialog.setPositiveButton("Yes"){text,listener->
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }
        alertDialog.setNegativeButton("No"){text,listener->

        }
        alertDialog.create()
        alertDialog.show()
    }

    override fun onAddRemoveButtonClick(index: Int, state: Int) {
        if(state == 0){
            cartItems[index] = true
            items_in_cart++
        }else if(state == 1){
            cartItems[index] = false
            items_in_cart--
        }
        if(items_in_cart > 0){
            btnAddRemove.visibility = View.VISIBLE
        }else if(items_in_cart == 0){
            btnAddRemove.visibility = View.GONE
        }
    }

    fun proceed_to_cart(view: View) {

        var total_cost = 0
        var food_ids = ArrayList<String>()
        var food_names = ArrayList<String>()
        var food_costs = ArrayList<String>()
        for (i in 0 until cartItems.size) {
            if (cartItems[i]) {
                total_cost += (foodList[i].cost_for_one).toInt()
                food_ids.add(foodList[i].id)
                food_names.add(foodList[i].name)
                food_costs.add(foodList[i].cost_for_one)
            }
        }
        val intent = Intent(this,CartActivity::class.java)
        intent.putExtra("res_id",foodList[0].restaurant_id)
        intent.putExtra("food_list",food_ids)
        intent.putExtra("total_cost",total_cost.toString())
        intent.putExtra("res_name",res_name)
        println(intent.getStringExtra("res_name"))
        intent.putExtra("food_names",food_names)
        intent.putExtra("food_costs",food_costs)
        startActivity(intent)
    }

}
