package com.onurkanbakirci.szutestosgb.ui.fragments

import android.content.Context

class RecentViewModelFactory (val mContext:Context) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel?> create(modelClass: Class<T>): T {
        return RecentFragmentViewModel(mContext) as T
    }
}