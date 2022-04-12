package com.example

import android.content.ClipData.newIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.notesapp.R
import com.example.notesapp.ui.MainActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000)

    }
}