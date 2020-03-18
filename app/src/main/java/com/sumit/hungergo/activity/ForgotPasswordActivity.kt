package com.sumit.hungergo.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.MotionEvent
import android.view.View
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sumit.hungergo.utils.ConnectionManager
import com.sumit.hungergo.R
import org.json.JSONException
import org.json.JSONObject

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var edMobile:EditText
    lateinit var edEmail:EditText
    lateinit var btnNext:Button
    lateinit var edOTP:EditText
    lateinit var edPass:EditText
    lateinit var edCnfPass:EditText
    lateinit var btnSubmit:Button
    lateinit var secondLayout:View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        supportActionBar?.title = "Reset Your Password"
        edMobile = findViewById(R.id.edMobile)
        edEmail = findViewById(R.id.edEmail)
        btnNext = findViewById(R.id.btnNext)
        edOTP = findViewById(R.id.edOtp)
        edPass = findViewById(R.id.edPass)
        edCnfPass = findViewById(R.id.edConfirmPass)
        btnSubmit = findViewById(R.id.btnSubmit)
        secondLayout = findViewById(R.id.secondLayout) as View
    }

    // remove focus from keyboard when touch outside
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
    var mobile = ""
    fun next_btn_click_listener(view: View) {
         mobile = edMobile.text.toString()
        val email = edEmail.text.toString()
        if(mobile.length != 10){
            Toast.makeText(this,"Invaild mobile number",Toast.LENGTH_SHORT).show()
        }else if(email.length == 0){
            Toast.makeText(this,"Input fields can't be empty",Toast.LENGTH_SHORT).show()
        }else{
            if(ConnectionManager().checkConnection(this)){
                val FORGOT = "http://13.235.250.119/v2/forgot_password/fetch_result"
                val queue = Volley.newRequestQueue(this)
                var params = JSONObject()
                params.put("mobile_number",mobile)
                params.put("email",email)
                val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST,FORGOT,params,
                    Response.Listener {
                        try {
                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")
                            if(success){
                                val animate = TranslateAnimation(
                                    0f,
                                    0f,
                                    secondLayout.getHeight().toFloat(),
                                    0f
                                )
                                animate.duration = 500
                                animate.fillAfter = true
                                Handler().postDelayed(Runnable {
                                    secondLayout.startAnimation(animate)
                                    edMobile.visibility = View.GONE
                                    edEmail.visibility = View.GONE
                                    btnNext.visibility = View.GONE
                                    secondLayout.visibility = View.VISIBLE
                                    edOTP.visibility = View.VISIBLE
                                    edPass.visibility = View.VISIBLE
                                    edCnfPass.visibility = View.VISIBLE
                                    btnSubmit.visibility = View.VISIBLE
                                },50)
                            }else{
                                val errMsg = data.getString("errorMessage")
                                Toast.makeText(this,errMsg,Toast.LENGTH_SHORT).show()
                            }
                        }catch (ex:JSONException){
                            Toast.makeText(this,"JSON exception occured",Toast.LENGTH_SHORT).show()
                        }
                    },Response.ErrorListener {
                        Toast.makeText(this,"Volley error occured",Toast.LENGTH_SHORT).show()
                    }){
                    override fun getHeaders(): MutableMap<String, String> {
                        val header = HashMap<String,String>()
                        header["Content-type"] = "application/json"
                        header["token"] = "e501e04bc5ccbf"
                        return header
                    }
                }
                queue.add(jsonObjectRequest)
            }else{
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Error")
                dialog.setMessage("Internet connection not available")
                dialog.setPositiveButton("Open Settings"){text,listener ->
                    val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingIntent)
                }
                dialog.setNegativeButton("Exit"){text,listener ->
                    ActivityCompat.finishAffinity(this)
                }
                dialog.create()
                dialog.show()
            }
        }
    }
    fun submit(view: View) {
        val otp = edOTP.text.toString()
        val password = edPass.text.toString()
        val cnfPass = edCnfPass.text.toString()
        if(otp.length != 4){
            Toast.makeText(this,"Invalid OTP",Toast.LENGTH_SHORT).show()
        }else if(password.length < 4){
            Toast.makeText(this,"Password must be atleast 4 characters",Toast.LENGTH_SHORT).show()
        }else if(!password.equals(cnfPass)){
            Toast.makeText(this,"Passwords didn't matched",Toast.LENGTH_SHORT).show()
        }else{
            if(ConnectionManager().checkConnection(this)){
                val RESET = "http://13.235.250.119/v2/reset_password/fetch_result"
                val queue = Volley.newRequestQueue(this)
                var params = JSONObject()
                params.put("mobile_number",mobile)
                params.put("password",password)
                params.put("otp",otp)
                val jsonObjectRequest = object : JsonObjectRequest(Request.Method.POST,RESET,params,
                    Response.Listener {
                        try {
                            val data = it.getJSONObject("data")
                            val success = data.getBoolean("success")
                            if(success){
                                val successMsg = data.getString("successMessage")
                                Toast.makeText(this,successMsg,Toast.LENGTH_SHORT).show()
                                finish()
                            }else{
                                Toast.makeText(this,"Some error occured",Toast.LENGTH_SHORT).show()
                            }
                        }catch (ex : JSONException){
                            Toast.makeText(this,"JSON exception",Toast.LENGTH_SHORT).show()
                        }
                    },Response.ErrorListener {
                        Toast.makeText(this,"Volley error occured",Toast.LENGTH_SHORT).show()
                    }){
                    override fun getHeaders(): MutableMap<String, String> {
                        val header = HashMap<String,String>()
                        header["Content-type"] = "application/json"
                        header["token"] = "e501e04bc5ccbf"
                        return header
                    }
                }
                queue.add(jsonObjectRequest)
            }else{
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("Error")
                dialog.setMessage("Internet connection not available")
                dialog.setPositiveButton("Open Settings"){text,listener ->
                    val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingIntent)
                }
                dialog.setNegativeButton("Exit"){text,listener ->
                    ActivityCompat.finishAffinity(this)
                }
                dialog.create()
                dialog.show()
            }
        }
    }
}

