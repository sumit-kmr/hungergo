package com.sumit.hungergo.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import androidx.viewpager.widget.ViewPager
import kotlin.concurrent.fixedRateTimer
import android.view.animation.TranslateAnimation
import android.widget.EditText
import android.content.Intent
import android.content.SharedPreferences
import android.view.MotionEvent
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sumit.hungergo.utils.ConnectionManager
import com.sumit.hungergo.R
import com.sumit.hungergo.adapters.ViewPagerAdapter
import org.json.JSONException
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    lateinit var viewPager: ViewPager
    lateinit var btnLogin: Button
    lateinit var btnSignUp: Button
    lateinit var loginLayoutView: View
    lateinit var signupLayoutView: View
    lateinit var btnSlideDownLogin: Button
    lateinit var btnSlideDownSignup: Button
    lateinit var edMobileLogin: EditText
    lateinit var edPasswordLogin: EditText
    lateinit var btnRealLogin: Button
    lateinit var edNameSignup: EditText
    lateinit var edEmailSignup: EditText
    lateinit var edMobileSignup: EditText
    lateinit var edAddressSignup: EditText
    lateinit var edPasswordSignup: EditText
    lateinit var edConfirmPasswordSignup: EditText
    lateinit var btnRealSignup: Button
    lateinit var sharedPreferences:SharedPreferences
    var isLoggedin = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sharedPreferences = getSharedPreferences(getString(R.string.shared_preference),Context.MODE_PRIVATE)
        isLoggedin = sharedPreferences.getBoolean("isLoggedin",false)
        if(isLoggedin){
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }
        // initializing all components
        init()

        // view pager for sliding images of dishes
        viewPager.adapter = ViewPagerAdapter(this)

        // sliding images of different dishes
        var c = 0
        fixedRateTimer(name = "slide-images", initialDelay = 1000, period = 2000) {
            this@LoginActivity.runOnUiThread(java.lang.Runnable {
                viewPager.setCurrentItem((++c) % 15)
            })
        }

        //slide up login screen
        btnLogin.setOnClickListener {
            val animate = TranslateAnimation(
                0f,
                0f,
                loginLayoutView.getHeight().toFloat(),
                0f
            )
            animate.duration = 500
            animate.fillAfter = true
            Handler().postDelayed(Runnable {
                loginLayoutView.startAnimation(animate)       // slide login layout upwards
                turnOnVisibilityLoginLayout()                 // turn on visibility of login layout's components
                turnOffVisibilityMainLayout()                 // turn off visibility of main layout's components
            }, 50)

        }

        //slide up signup screen
        btnSignUp.setOnClickListener {
            val animate = TranslateAnimation(
                0f,
                0f,
                signupLayoutView.getHeight().toFloat(),
                0f
            )
            animate.duration = 500
            animate.fillAfter = true
            Handler().postDelayed(Runnable {
                turnOffVisibilityMainLayout()                // turn off visibility of main layout's components
                signupLayoutView.startAnimation(animate)     // start sliding up signup layout
                turnOnVisibilitySignupLayout()               // turn on visibility of signup layout's components
            }, 50)
        }

        //slide down login screen
        btnSlideDownLogin.setOnClickListener {
            val animate = TranslateAnimation(
                0f,
                0f,
                0f,
                loginLayoutView.height.toFloat()
            )
            animate.duration = 500
            animate.fillAfter = true
            loginLayoutView.startAnimation(animate)
            Handler().postDelayed(Runnable {
                turnOffVisibilityLoginLayout()               // turn off visibility of login layout's compoents
                turnOnVisibilityMainLayout()                 // turn on visibility of main layout's components
                clearAllFields()                             // clear all edit text fields
            }, 500)
        }

        //slide down signup screen
        btnSlideDownSignup.setOnClickListener {
            val animate = TranslateAnimation(
                0f,
                0f,
                0f,
                signupLayoutView.height.toFloat()
            )
            animate.duration = 500
            animate.fillAfter = true
            signupLayoutView.startAnimation(animate)
            Handler().postDelayed(Runnable {
                turnOffVisibilitySignupLayout()         //turn off visibility of signup layout's components
                turnOnVisibilityMainLayout()            // turn on visibility of main layout components
                clearAllFields()
            }, 500)
        }
    }

    private fun init() {
        viewPager = findViewById(R.id.viewPager)
        btnLogin = findViewById(R.id.btnLogin)
        btnSignUp = findViewById(R.id.btnSignup)
        loginLayoutView = findViewById(R.id.slidingLayout) as View
        signupLayoutView = findViewById(R.id.slidingLayoutSignup) as View
        btnSlideDownLogin = findViewById(R.id.btnSlideDown)
        btnSlideDownSignup = findViewById(R.id.btnSlideDownSignup)
        edMobileLogin = findViewById(R.id.edMobile)
        edPasswordLogin = findViewById(R.id.edPassword)
        btnRealLogin = findViewById(R.id.btnRealLogin)
        edNameSignup = findViewById(R.id.edNameSignup)
        edEmailSignup = findViewById(R.id.edEmailSignup)
        edMobileSignup = findViewById(R.id.edMobileSignup)
        edAddressSignup = findViewById(R.id.edAddreddSignup)
        edPasswordSignup = findViewById(R.id.edPasswordSignup)
        edConfirmPasswordSignup = findViewById(R.id.edConfirmPasswordSignup)
        btnRealSignup = findViewById(R.id.btnRealSignup)
    }

    private fun turnOffVisibilitySignupLayout() {
        signupLayoutView.visibility = View.GONE
        btnSlideDownSignup.visibility = View.GONE
        edNameSignup.visibility = View.GONE
        edEmailSignup.visibility = View.GONE
        edMobileSignup.visibility = View.GONE
        edAddressSignup.visibility = View.GONE
        edPasswordSignup.visibility = View.GONE
        edConfirmPasswordSignup.visibility = View.GONE
        btnRealSignup.visibility = View.GONE
    }

    private fun turnOnVisibilityMainLayout() {
        btnSignUp.visibility = View.VISIBLE
        btnLogin.visibility = View.VISIBLE
    }

    private fun turnOffVisibilityLoginLayout() {
        loginLayoutView.visibility = View.GONE
        edMobileLogin.visibility = View.GONE
        edPasswordLogin.visibility = View.GONE
        btnRealLogin.visibility = View.GONE
        btnSlideDownLogin.visibility = View.GONE
    }

    private fun turnOnVisibilitySignupLayout() {
        signupLayoutView.visibility = View.VISIBLE
        btnSlideDownSignup.visibility = View.VISIBLE
        edNameSignup.visibility = View.VISIBLE
        edEmailSignup.visibility = View.VISIBLE
        edMobileSignup.visibility = View.VISIBLE
        edAddressSignup.visibility = View.VISIBLE
        edPasswordSignup.visibility = View.VISIBLE
        edConfirmPasswordSignup.visibility = View.VISIBLE
        btnRealSignup.visibility = View.VISIBLE
    }

    private fun turnOffVisibilityMainLayout() {
        btnSignUp.visibility = View.GONE
        btnLogin.visibility = View.GONE
    }

    private fun turnOnVisibilityLoginLayout() {
        loginLayoutView.visibility = View.VISIBLE
        edPasswordLogin.visibility = View.VISIBLE
        edMobileLogin.visibility = View.VISIBLE
        btnRealLogin.visibility = View.VISIBLE
        btnSlideDownLogin.visibility = View.VISIBLE
    }

    //clear all edit text fields
    private fun clearAllFields() {
        edNameSignup.text.clear()
        edEmailSignup.text.clear()
        edMobileSignup.text.clear()
        edAddressSignup.text.clear()
        edPasswordSignup.text.clear()
        edConfirmPasswordSignup.text.clear()
        edMobileLogin.text.clear()
        edPasswordLogin.text.clear()
    }

    // remove focus from keyboard when touch outside
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    // validating login event
    fun validate_login(view: View){
        var str = ""
        if(ConnectionManager().checkConnection(this)){
            val mobile_no = edMobileLogin.text.toString()
            val password = edPasswordLogin.text.toString()
            if(mobile_no.length != 10)
                Toast.makeText(this@LoginActivity,"Mobile no. length should be 10",Toast.LENGTH_SHORT).show()
            else if(password.length <= 4)
                Toast.makeText(this@LoginActivity,"Password should be at least 5 characters",Toast.LENGTH_SHORT)
            else{
                val LOGIN = "http://13.235.250.119/v2/login/fetch_result"
                val jsonParams = JSONObject()
                jsonParams.put("mobile_number",mobile_no)
                jsonParams.put("password",password)
                var queue = Volley.newRequestQueue(this@LoginActivity)
                val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST,LOGIN,jsonParams,
                    Response.Listener {
                        try {
                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")
                            if(success){
                                Toast.makeText(this,"Successfully logged in",Toast.LENGTH_SHORT).show()
                                val details = data.getJSONObject("data")
                                sharedPreferences.edit().putString("user_id",details.getString("user_id")).apply()
                                sharedPreferences.edit().putString("name",details.getString("name")).apply()
                                sharedPreferences.edit().putString("email",details.getString("email")).apply()
                                sharedPreferences.edit().putString("mobile_number",details.getString("mobile_number")).apply()
                                sharedPreferences.edit().putString("address",details.getString("address")).apply()
                                sharedPreferences.edit().putBoolean("isLoggedin",true).apply()
                                val intent = Intent(this@LoginActivity,
                                    HomeActivity::class.java)
                                startActivity(intent)
                                finish()
                            }else{
                                val errorMsg = data.getString("errorMessage")
                                Toast.makeText(this,errorMsg,Toast.LENGTH_SHORT).show()
                            }
                        }catch (ex:JSONException){
                            Toast.makeText(this,"JSON Exception occured",Toast.LENGTH_SHORT).show()
                            ex.printStackTrace()
                        }
                    },Response.ErrorListener {
                        Toast.makeText(this@LoginActivity, "Volley error occured", Toast.LENGTH_SHORT)
                            .show()
                    }){
                    override fun getHeaders(): MutableMap<String, String> {
                        var header = HashMap<String, String>()
                        header["Content-type"] = "application/json"
                        header["token"] = "e501e04bc5ccbf"
                        return header
                    }
                }
                queue.add(jsonObjectRequest)
            }

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

    fun register_user(view: View) {
        if(ConnectionManager().checkConnection(this@LoginActivity)){
            val name = edNameSignup.text.toString()
            val email = edEmailSignup.text.toString()
            val mobile = edMobileSignup.text.toString()
            val address = edAddressSignup.text.toString()
            val password = edPasswordSignup.text.toString()
            val cnfrmPasswd = edConfirmPasswordSignup.text.toString()
            if(name.length < 3)
                Toast.makeText(this,"Name should be atleast 3 character",Toast.LENGTH_SHORT).show()
            else if(mobile.length != 10)
                Toast.makeText(this,"Mobile no. should be 10 digits",Toast.LENGTH_SHORT).show()
            else if(password.length < 5)
                Toast.makeText(this,"Password should be atleast 5 character",Toast.LENGTH_SHORT).show()
            else if(!password.equals(cnfrmPasswd))
                Toast.makeText(this,"Passwords didn't matched",Toast.LENGTH_SHORT).show()
            else{
                val REGISTER = "http://13.235.250.119/v2/register/fetch_result"
                val jsonParams = JSONObject()
                jsonParams.put("name", name)
                jsonParams.put("mobile_number", mobile)
                jsonParams.put("password", password)
                jsonParams.put("address", address)
                jsonParams.put("email", email)
                var queue = Volley.newRequestQueue(this@LoginActivity)
                val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST,REGISTER,jsonParams,
                    Response.Listener {
                        try {
                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")
                            if(success){
                                Toast.makeText(this,"Successfully registered!!",Toast.LENGTH_SHORT).show()
                                val details = data.getJSONObject("data")
                                sharedPreferences.edit().putString("user_id",details.getString("user_id")).apply()
                                sharedPreferences.edit().putString("name",details.getString("name")).apply()
                                sharedPreferences.edit().putString("email",details.getString("email")).apply()
                                sharedPreferences.edit().putString("mobile_number",details.getString("mobile_number")).apply()
                                sharedPreferences.edit().putString("address",details.getString("address")).apply()
                                sharedPreferences.edit().putBoolean("isLoggedin",true).apply()
                                val intent = Intent(this@LoginActivity,
                                    HomeActivity::class.java)
                                startActivity(intent)
                                finish()
                            }else{
                                val errorMsg = data.getString("errorMessage")
                                Toast.makeText(this, errorMsg,Toast.LENGTH_SHORT).show()
                            }
                        }catch (ex:JSONException){
                            Toast.makeText(this,"JSON Exception occured",Toast.LENGTH_SHORT).show()
                        }
                    },Response.ErrorListener {
                        Toast.makeText(this@LoginActivity, "Volley error occured", Toast.LENGTH_SHORT)
                            .show()
                    }){
                    override fun getHeaders(): MutableMap<String, String> {
                        var header = HashMap<String, String>()
                        header["Content-type"] = "application/json"
                        header["token"] = "e501e04bc5ccbf"
                        return header
                    }
                }
                queue.add(jsonObjectRequest)
            }

        }else{
            val dialog = AlertDialog.Builder(this@LoginActivity)
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

    fun forgot_password(view: View) {
        val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
        startActivity(intent)
        Handler().postDelayed(Runnable { btnSlideDownLogin.performClick() },1000)
    }
}
