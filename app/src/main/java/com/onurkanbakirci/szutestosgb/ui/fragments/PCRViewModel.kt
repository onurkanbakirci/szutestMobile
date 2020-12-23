package com.onurkanbakirci.szutestosgb.ui.fragments

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onurkanbakirci.szutestosgb.R
import com.onurkanbakirci.szutestosgb.data.db.AppDatabase
import com.onurkanbakirci.szutestosgb.data.db.entities.Files
import com.onurkanbakirci.szutestosgb.data.db.entities.PCR
import io.reactivex.CompletableObserver
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PCRViewModel(val mContext: Context) :ViewModel(){

    var mPCR = MutableLiveData<List<PCR>>()
    var pcrNotFoundVisibility = ObservableField(8)

    val uID = mContext.applicationContext?.getSharedPreferences(
        "auth",
        Context.MODE_PRIVATE
    )!!.getInt("_uID", 0)


    fun getPCR() {
        AppDatabase.getAppDataBase(mContext)
            ?.pcrDao()
            ?.GetAllPCR(uID)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : Observer<List<PCR>> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: List<PCR>) {
                    mPCR.postValue(t)
                }

                override fun onError(e: Throwable) {
                }
            })
    }

    fun newPCR(view: View){
        (mContext as AppCompatActivity).supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container,PCRFormFragment())
            .commit()
    }

    fun deletePcr(view:View,pcr:PCR)
    {
        AppDatabase.getAppDataBase(mContext)
            ?.pcrDao()
            ?.DeletePCRByDate(pcr.date!!)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object : CompletableObserver {
                override fun onComplete() {
                    Toast.makeText(mContext,"Form Silindi", Toast.LENGTH_LONG).show()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                }
        })
    }

}