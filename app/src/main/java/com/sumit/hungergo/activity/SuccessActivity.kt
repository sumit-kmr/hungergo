package com.sumit.hungergo.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sumit.hungergo.R

class SuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success)
    }

    fun ok_btn_clk(view: View?) {
        startActivity(Intent(this,HomeActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        ok_btn_clk(null)
    }
}
