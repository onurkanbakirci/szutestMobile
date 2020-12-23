package com.onurkanbakirci.szutestosgb.ui.activities

import android.view.View
import android.widget.EditText
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.onurkanbakirci.szutestosgb.data.repository.LoginRepository

class LoginActivityViewModel :ViewModel (){

    var loginProgress = ObservableField(8)
    var ILogin : ILogin ? =null

    fun loginClick(view:View,mEmail:EditText,mPassword:EditText){
        if(mEmail.text.isNullOrEmpty() || mPassword.text.isNullOrEmpty()){
            ILogin?.onFailure("Tüm alanlar dolu olmalı!")
        }
        else{
            loginProgress.set(0)
            val loginRes = LoginRepository().login(mEmail.text.toString(),mPassword.text.toString())
            ILogin?.onSuccess(loginRes)
        }
    }
}