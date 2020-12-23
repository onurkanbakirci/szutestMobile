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
import com.onurkanbakirci.szutestosgb.data.db.entities.Category
import com.onurkanbakirci.szutestosgb.data.repository.CategoryRepository
import com.onurkanbakirci.szutestosgb.ui.MainActivity
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CategoryFragmentViewModel (val mContext:Context,var mCompanyCat:String): ViewModel() {

    var mCategory       = MutableLiveData<List<Category>>()

    var progressOfCategory = ObservableField(0)

    var accessString = "Bearer "+ mContext.getSharedPreferences("auth", Context.MODE_PRIVATE).getString("token",null)

    companion object{
        var mCat = arrayListOf<Int>()
    }

    fun clearCache(){
        mCat.clear()
    }

    fun fetchCategories(){
        CategoryRepository().getCategories(accessString)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Category>> {
                override fun onSubscribe(d: Disposable) {
                }
                override fun onNext(t: List<Category>) {
                    mCategory.postValue(t)
                }
                override fun onError(e: Throwable) {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.conn_err),Toast.LENGTH_LONG).show()
                }
                override fun onComplete() {
                }
            })
    }
    fun goBtnToQuestions(view:View){

        var mBundle = Bundle()
        var mFragment = QuestionsFragment()
        mBundle.putIntegerArrayList("idsOfCat",mCat)
        mBundle.putString("nameOfCompany",mCompanyCat)
        mFragment.arguments = mBundle

        (mContext as AppCompatActivity).supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container,mFragment)
            .commit()
    }

    fun status(view:View,category:Category){
        category.isChecked = !category.isChecked
        if(category.isChecked){
            mCat.add(category.uID!!)
        }
        if(!category.isChecked){
            mCat.add(category.uID!!)
        }
    }

    fun finishForm(view:View){
        mCat.clear()
        /*(mContext as AppCompatActivity).supportFragmentManager
            .beginTransaction()
            //.setCustomAnimations(R.anim.fade_in,R.anim.fade_out)
            .replace(R.id.fragment_container,RecentFragment())
            .commit()*/
        mContext.startActivity(Intent(mContext, MainActivity::class.java))
    }
}