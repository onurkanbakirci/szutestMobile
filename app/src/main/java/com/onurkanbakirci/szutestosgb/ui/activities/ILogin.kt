package com.onurkanbakirci.szutestosgb.ui.activities

import androidx.lifecycle.LiveData
import com.onurkanbakirci.szutestosgb.data.db.entities.User

interface ILogin {
    fun onSuccess(loginRes: LiveData<User>)
    fun onFailure(message: String)
}