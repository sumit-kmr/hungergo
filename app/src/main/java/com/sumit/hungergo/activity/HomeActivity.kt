package com.sumit.hungergo.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.sumit.hungergo.*
import com.sumit.hungergo.fragments.*

class HomeActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var navigationView: NavigationView
    lateinit var frameLayout: FrameLayout
    lateinit var sharedPreferences: SharedPreferences
    lateinit var headerView:View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        navigationView = findViewById(R.id.navigationView)
        frameLayout = findViewById(R.id.frameLayout)
        headerView = navigationView.getHeaderView(0)
        sharedPreferences = getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE)
        setUpToolbar()

        val actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout,
                R.string.open_drawer,
                R.string.close_drawer
            )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        openHomeFragment()
        setupDrawerHeader()

        navigationView.setNavigationItemSelectedListener {

            when(it.itemId){
                R.id.home -> openHomeFragment()
                R.id.my_profile ->  openProfileFragment()
//                R.id.favourites -> openFavouritesFragment()
                R.id.order_history -> openOrderHistoryFragment()
                R.id.faq -> openFAQFragment()
                R.id.logout -> {
                    val dialog = AlertDialog.Builder(this@HomeActivity)
                    dialog.setTitle("Confirmation")
                    dialog.setMessage("Sure want to logout?")
                    dialog.setPositiveButton("Yes"){text,listener ->
                        sharedPreferences.edit().putBoolean("isLoggedin",false).commit()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                    dialog.setNegativeButton("No"){text,listener->
                        drawerLayout.closeDrawers()
                    }
                    dialog.create().show()

                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    override fun onBackPressed() {
        val currFrag = supportFragmentManager.findFragmentById(R.id.frameLayout)
        if(currFrag !is HomeFragment)
            openHomeFragment()
        else
            ActivityCompat.finishAffinity(this as Activity)
    }

    private fun setupDrawerHeader() {
        val header_name = headerView.findViewById(R.id.txtName) as TextView
        val header_ph_no = headerView.findViewById(R.id.txtPhone) as TextView
        header_name.text = sharedPreferences.getString("name","Name")
        val ph = sharedPreferences.getString("mobile_number","9999999999")
        header_ph_no.text = "+91-$ph"
    }

    private fun openFAQFragment() {
        supportFragmentManager.beginTransaction().replace(
            R.id.frameLayout,
            FAQFragment()
        ).commit()
        supportActionBar?.title = "FAQs"
        drawerLayout.closeDrawers()
    }

    private fun openOrderHistoryFragment() {
        supportFragmentManager.beginTransaction().replace(
            R.id.frameLayout,
            OrderHistoryFragment()
        ).commit()
        supportActionBar?.title = "Order History"
        drawerLayout.closeDrawers()
    }

//    private fun openFavouritesFragment() {
//        supportFragmentManager.beginTransaction().replace(
//            R.id.frameLayout,
//            FavouritesFragment()
//        ).commit()
//        supportActionBar?.title = "Favourite Restaurants"
//        drawerLayout.closeDrawers()
//    }

    private fun openProfileFragment() {
        supportFragmentManager.beginTransaction().replace(
            R.id.frameLayout,
            ProfileFragment()
        ).commit()
        supportActionBar?.title = "Profile"
        drawerLayout.closeDrawers()
    }

    private fun openHomeFragment() {
        supportFragmentManager.beginTransaction().replace(
            R.id.frameLayout,
            HomeFragment()
        ).commit()
        supportActionBar?.title = "All Restaurants"
        drawerLayout.closeDrawers()
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Home"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if(id == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item)
    }
}
