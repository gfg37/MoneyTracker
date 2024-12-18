package com.example.myapp1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.content.Intent

class ExpenseScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_screen)

        val backButtonCategory5: ImageButton = findViewById(R.id.backButtonCategory5)
        backButtonCategory5.setOnClickListener {
            val intent = Intent(this, MainScreen::class.java)
            startActivity(intent)
        }
    }
}