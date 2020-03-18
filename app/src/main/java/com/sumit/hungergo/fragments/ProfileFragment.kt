package com.sumit.hungergo.fragments


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sumit.hungergo.R
import org.w3c.dom.Text

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    lateinit var txtName:TextView
    lateinit var txtPhone:TextView
    lateinit var txtEmail:TextView
    lateinit var txtAddress:TextView
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        txtName = view.findViewById(R.id.txtName)
        txtPhone = view.findViewById(R.id.txtPhone)
        txtEmail = view.findViewById(R.id.txtEmail)
        txtAddress = view.findViewById(R.id.txtAddress)
        sharedPreferences = activity!!.getSharedPreferences(getString(R.string.shared_preference),Context.MODE_PRIVATE)

        txtName.text = sharedPreferences.getString("name","Name")
        txtPhone.text = "+91-${sharedPreferences.getString("mobile_number","9999999999")}"
        txtEmail.text = sharedPreferences.getString("email","abc@xyz.com")
        txtAddress.text = sharedPreferences.getString("address","Address")
        return view
    }


}
