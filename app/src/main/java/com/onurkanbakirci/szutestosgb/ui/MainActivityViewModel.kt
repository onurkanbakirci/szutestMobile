package com.onurkanbakirci.szutestosgb.ui

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.onurkanbakirci.szutestosgb.R
import com.onurkanbakirci.szutestosgb.ui.activities.LoginActivity
import com.onurkanbakirci.szutestosgb.ui.fragments.RecentFragment
import com.google.android.material.navigation.NavigationView
import com.onurkanbakirci.szutestosgb.ui.fragments.PCRFragment

class MainActivityViewModel(var mContext:Context) : ViewModel(), NavigationView.OnNavigationItemSelectedListener {


    var icDrawer = ObservableField(mContext.resources.getDrawable(R.drawable.ic_menu))

    var mIDrawer: IDrawer? = null

    fun toggleBtn(view : View){
        mIDrawer?.checkDrawerStatus()
    }

    /**
     * drawer navigation view
     */
    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when(p0.itemId){
            R.id.drawerHome->{
                recentFragmentTransaction()
            }
            R.id.addPCR->{
                pcrFragmentTransaction()
            }
            R.id.drawerLogout->{
                logoutTransaction()
            }
        }
        mIDrawer?.closeDrawer()
        return true
    }

    fun recentFragmentTransaction(){
        (mContext as AppCompatActivity).supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, RecentFragment())
            .commit()
    }
    fun pcrFragmentTransaction(){
        (mContext as AppCompatActivity).supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, PCRFragment())
            .commit()
    }
    fun logoutTransaction(){
        val prefences = mContext.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val editor = prefences.edit()
        editor.remove("token").apply()
        editor.remove("name").apply()
        editor.remove("_uID").apply()
        mContext.startActivity(Intent(mContext,LoginActivity::class.java))
    }
}