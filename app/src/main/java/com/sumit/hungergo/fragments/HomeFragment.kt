package com.sumit.hungergo.fragments


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sumit.hungergo.R
import com.sumit.hungergo.adapters.AdapterDashboard
import com.sumit.hungergo.models.Restaurant
import com.sumit.hungergo.utils.ConnectionManager
import org.json.JSONException


class HomeFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var progressBarRelativeLayout: RelativeLayout
    lateinit var recyclerAdapter: AdapterDashboard
    lateinit var layoutManager: GridLayoutManager
    lateinit var restaurentList: ArrayList<Restaurant>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        restaurentList = ArrayList<Restaurant>()
        progressBarRelativeLayout = view.findViewById(R.id.relativeLayout)
        layoutManager = GridLayoutManager(activity,2)
        progressBarRelativeLayout.visibility = View.VISIBLE
        if(ConnectionManager().checkConnection(activity as Context)){
            var queue = Volley.newRequestQueue(activity as Context)
            val all_restaurants_url = "http://13.235.250.119/v2/restaurants/fetch_result/"
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET,all_restaurants_url,null,
                Response.Listener {
                    try {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if(success){
                            val jsonArray = data.getJSONArray("data")
                            for(i in 0 until jsonArray.length())
                            {
                                val jsonObject = jsonArray.getJSONObject(i)
                                restaurentList.add(
                                    Restaurant(
                                    jsonObject.getString("id"),
                                        jsonObject.getString("name"),
                                        jsonObject.getString("rating"),
                                        jsonObject.getString("cost_for_one"),
                                        jsonObject.getString("image_url")
                                )
                                )
                            }
                            recyclerAdapter = AdapterDashboard(activity as Context,restaurentList)
                            recyclerView.adapter = recyclerAdapter
                            recyclerView.layoutManager = layoutManager
                            progressBarRelativeLayout.visibility = View.GONE
                        }else{
                            Toast.makeText(activity as Context,data.getString("errorMessage"),Toast.LENGTH_SHORT).show()
                        }
                    }catch (ex:JSONException){
                        Toast.makeText(activity as Context,"JSON exception",Toast.LENGTH_SHORT).show()
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

        return view
    }



}
