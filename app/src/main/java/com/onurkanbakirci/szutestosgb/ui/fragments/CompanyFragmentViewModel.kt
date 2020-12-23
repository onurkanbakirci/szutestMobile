package com.onurkanbakirci.szutestosgb.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onurkanbakirci.szutestosgb.R
import com.onurkanbakirci.szutestosgb.data.db.entities.Company
import com.onurkanbakirci.szutestosgb.data.repository.CompanyRepository
import com.onurkanbakirci.szutestosgb.ui.MainActivity
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CompanyFragmentViewModel(val mContext:Context) :ViewModel() {

    var mCompany = MutableLiveData<List<Company>>()

    var progressOfCompany = ObservableField(0)

    var accessString = "Bearer "+ mContext.getSharedPreferences("auth", Context.MODE_PRIVATE).getString("token",null)

    fun getCompanies(){
        CompanyRepository().getCompany(accessString)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Company>> {
                override fun onSubscribe(d: Disposable) {
                }
                override fun onNext(t: List<Company>) {
                    mCompany.postValue(t)
                }
                override fun onError(e: Throwable) {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.conn_err), Toast.LENGTH_LONG).show()
                }
                override fun onComplete() {
                }
            })
    }
    fun goBtnToCategories(view:View,company:Company){
        var mBundle = Bundle()
        var mFragment = CategoryFragment()
        mBundle.putString("nameOfCompany",company.name)
        mFragment.arguments = mBundle
        (mContext as AppCompatActivity).supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container,mFragment)
            .commit()
    }
    fun finishForm(view:View){
       /* (mContext as AppCompatActivity).supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container,RecentFragment())
            .commit()*/
        mContext.startActivity(Intent(mContext, MainActivity::class.java))
    }
}