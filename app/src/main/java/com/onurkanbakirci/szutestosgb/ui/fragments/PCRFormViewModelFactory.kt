package com.onurkanbakirci.szutestosgb.ui.fragments

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PCRFormViewModelFactory(val mContext : Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PCRFormFragmentViewModel(mContext) as T
    }
}