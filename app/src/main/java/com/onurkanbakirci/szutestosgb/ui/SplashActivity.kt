package com.onurkanbakirci.szutestosgb.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.onurkanbakirci.szutestosgb.R
import com.onurkanbakirci.szutestosgb.ui.activities.LoginActivity

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT:Long = 2000
    private var alphaText : TextView ? =null
    private val alphaAnim : Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.alpha_anim) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        alphaText = findViewById(R.id.alphaText)
        alphaText?.startAnimation(alphaAnim)

        val mPrefences = getSharedPreferences("auth", Context.MODE_PRIVATE)
        if(mPrefences.getString("token",null) == null){
            Handler().postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }, SPLASH_TIME_OUT)
        }
        else{
            Handler().postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, SPLASH_TIME_OUT)
        }
    }
}
