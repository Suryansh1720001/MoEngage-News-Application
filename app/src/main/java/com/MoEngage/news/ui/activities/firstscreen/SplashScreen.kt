package com.MoEngage.news.ui.activities.firstscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.MoEngage.news.R
import com.MoEngage.news.ui.activities.HomeActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val handler = android.os.Handler()
        handler.postDelayed({
            // Start the activity after 2 seconds
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }, 4000)
    }
}