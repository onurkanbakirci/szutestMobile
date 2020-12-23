package com.onurkanbakirci.szutestosgb.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.onurkanbakirci.szutestosgb.data.db.entities.User
import com.onurkanbakirci.szutestosgb.data.network.IAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository {
    fun login(email:String , password : String ) : LiveData<User>{
        val loginResponse = MutableLiveData<User>()

        IAPI().login(email,password)
            .enqueue(object : Callback<User>{
                override fun onFailure(call: Call<User>, t: Throwable) {
                    loginResponse.value = User(400)
                }
                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    if(response.isSuccessful){
                        loginResponse.value = response.body()
                    }else{
                        loginResponse.value = response.body()
                    }
                }
            })
        return loginResponse
    }
}