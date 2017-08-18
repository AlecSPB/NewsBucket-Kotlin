package com.angad.newsbucket.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.facebook.rebound.SimpleSpringListener
import com.facebook.rebound.Spring
import com.facebook.rebound.SpringSystem
import com.facebook.rebound.SpringUtil
import com.angad.newsbucket.R
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*
import kotlin.concurrent.timerTask

class SplashActivity : AppCompatActivity() {

    val TAG = "Splash"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        appLogoAnimation()
    }

    fun appLogoAnimation() {
        val springSystem = SpringSystem.create()
        val spring = springSystem.createSpring()
        spring.addListener(object : SimpleSpringListener() {
            override fun onSpringUpdate(spring: Spring) {
                val scale = SpringUtil.mapValueFromRangeToRange(spring.currentValue, 0.0, 1.0, 1.0, 0.5).toFloat()
                img_applogo.setScaleX(scale.toFloat())
                img_applogo.setScaleY(scale.toFloat())
            }
        })

        var i = 1
        val timer = Timer();
        timer.scheduleAtFixedRate(timerTask {
            if( i <= 5) {
                spring.setEndValue(if (i % 2 == 0) 0.6 else 0.0)
                i++
            } else {
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
                this.cancel();
            }
        }, 0, 1000)
    }
}
