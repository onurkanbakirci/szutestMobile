package com.onurkanbakirci.szutestosgb.ui

import android.content.Context

class MainActivityViewModelFactory(val mContext: Context) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel?> create(modelClass: Class<T>): T {
        return MainActivityViewModel(mContext) as T
    }
}