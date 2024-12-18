package com.example.myapp1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class AnalyticsScreen2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics_screen2)

        val backButtonAnal2: ImageButton = findViewById(R.id.backButtonAnal2)
        backButtonAnal2.setOnClickListener {
            val intent = Intent(this, BetweenScreen::class.java)
            startActivity(intent)
        }
    }
}