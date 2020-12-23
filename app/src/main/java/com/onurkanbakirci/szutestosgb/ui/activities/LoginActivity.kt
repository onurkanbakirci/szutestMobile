package com.onurkanbakirci.szutestosgb.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.onurkanbakirci.szutestosgb.R
import com.onurkanbakirci.szutestosgb.data.db.entities.User
import com.onurkanbakirci.szutestosgb.databinding.ActivityLoginBinding
import com.onurkanbakirci.szutestosgb.ui.MainActivity

class LoginActivity : AppCompatActivity() , ILogin{

    lateinit var viewModel: LoginActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataBinding : ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProviders.of(this).get(LoginActivityViewModel::class.java)
        dataBinding.loginViewModel = viewModel
        viewModel.ILogin = this
    }

    override fun onSuccess(loginRes: LiveData<User>) {
        loginRes.observe(this, Observer {
            if(loginRes.value?.status == 200){
                val prefences = getSharedPreferences("auth", Context.MODE_PRIVATE)
                val editor = prefences.edit()
                editor.putString("token", loginRes.value!!.token).apply()
                editor.putString("name", loginRes.value!!.name!!).apply()
                editor.putInt("_uID", loginRes.value!!.uID!!).apply()
                startActivity(Intent(this,MainActivity::class.java))
                viewModel.loginProgress.set(8)
            }
            else{
                Toast.makeText(this,resources.getString(R.string.checkCredentials),Toast.LENGTH_LONG).show()
                viewModel.loginProgress.set(8)
            }
        })
    }
    override fun onFailure(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }

}
