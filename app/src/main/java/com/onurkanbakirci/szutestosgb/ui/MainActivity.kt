package com.onurkanbakirci.szutestosgb.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.onurkanbakirci.szutestosgb.R
import com.onurkanbakirci.szutestosgb.databinding.ActivityMainBinding
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.onurkanbakirci.szutestosgb.databinding.HeaderOfDrawerBinding
import com.onurkanbakirci.szutestosgb.ui.fragments.RecentFragment
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.header_of_drawer.view.*


class MainActivity : AppCompatActivity() ,IDrawer {

    private lateinit var mDrawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var username = this.applicationContext?.getSharedPreferences(
            "auth",
            Context.MODE_PRIVATE
        )!!.getString("name", null)

        val dataBinding : ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        var _bind = HeaderOfDrawerBinding.bind(dataBinding.homeNavigationView.getHeaderView(0))
        val viewModel = ViewModelProviders.of(this,MainActivityViewModelFactory(this)).get(MainActivityViewModel::class.java)
        dataBinding.mainActivityViewModel = viewModel
        mDrawerLayout = dataBinding.mainActivityDrawerLayout
        _bind.textUserNameDrawer.text = username
        viewModel.mIDrawer = this

        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, RecentFragment()).commit()

        if (ContextCompat.checkSelfPermission(this@MainActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE) !==
            PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            } else {
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                    if ((ContextCompat.checkSelfPermission(this@MainActivity,
                            Manifest.permission.READ_EXTERNAL_STORAGE) ===
                                PackageManager.PERMISSION_GRANTED)) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    override fun openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.END)
    }

    override fun closeDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun checkDrawerStatus() {
        if (!mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.openDrawer(GravityCompat.START)
        else
            mDrawerLayout.closeDrawer(GravityCompat.END)
    }
}
