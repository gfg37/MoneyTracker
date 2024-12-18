package com.example.myapp1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class BetweenScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_between_screen)

        val imageButton10: ImageButton = findViewById(R.id.imageButton10)
        imageButton10.setOnClickListener {
            val intent = Intent(this, AnalyticsScreen2::class.java)
            startActivity(intent)
        }
        val imageButton9: ImageButton = findViewById(R.id.imageButton9)
        imageButton9.setOnClickListener {
            val intent = Intent(this, AnalyticsScreen::class.java)
            startActivity(intent)
        }

        val backButtonCategory5: ImageButton = findViewById(R.id.backButtonCategory5)
        backButtonCategory5.setOnClickListener {
            val intent = Intent(this, MainScreen::class.java)
            startActivity(intent)
        }

    }
}
