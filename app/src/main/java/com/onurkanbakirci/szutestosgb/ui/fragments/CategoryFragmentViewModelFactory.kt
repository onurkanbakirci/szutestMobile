package com.onurkanbakirci.szutestosgb.ui.fragments

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CategoryFragmentViewModelFactory (val mContext : Context,var mCompany:String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CategoryFragmentViewModel(mContext,mCompany) as T
    }
}