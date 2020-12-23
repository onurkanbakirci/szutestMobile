package com.onurkanbakirci.szutestosgb.ui.fragments

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.ArrayList

class QuestionsViewModelFactory (val mContext : Context,var ids:ArrayList<Int>,var companyName:String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return QuestionsViewModel(mContext,ids,companyName) as T
    }
}