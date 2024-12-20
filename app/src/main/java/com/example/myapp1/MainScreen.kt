package com.example.myapp1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp1.ExpenseScreen.Companion.REQUEST_CODE_EXPENSE
import com.example.myapp1.RevenueScreen.Companion.REQUEST_CODE_EXPENSEE
import com.example.myapp1.serv.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainScreen : AppCompatActivity() {

    private lateinit var balanceTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        // Получаем userId из Intent
        val userId = intent.getLongExtra("userId", -1)
        val username = intent.getStringExtra("user")


        balanceTextView = findViewById(R.id.textView33)

        // Загрузка баланса
        if (userId != -1L) {
            loadBalance(userId)
        } else {
            Toast.makeText(this, "Ошибка: userId не найден", Toast.LENGTH_SHORT).show()
            Log.e("MainScreen", "Ошибка: userId не найден")
        }

        // Переход в аналитику
        val imageButton3: ImageButton = findViewById(R.id.imageButton3)
        imageButton3.setOnClickListener {
            val intent = Intent(this, BetweenScreen::class.java)
            startActivity(intent)
        }

        // Переход в настройки
        val imageButtonSettings: ImageButton = findViewById(R.id.imageButtonSettings)
        imageButtonSettings.setOnClickListener {
            val intent = Intent(this@MainScreen, SettingScreen::class.java)
            intent.putExtra("user", username)
            startActivity(intent)
        }

        // Переход в доходы
        val imageButton1: ImageButton = findViewById(R.id.imageButton1)
        imageButton1.setOnClickListener {
            val intent = Intent(this, RevenueScreen::class.java)
            intent.putExtra("userId", userId) // передаем userI
            startActivityForResult(intent, REQUEST_CODE_EXPENSEE)
        }

        // Переход в расходы
        val imageButton2: ImageButton = findViewById(R.id.imageButton2)
        imageButton2.setOnClickListener {
            val intent = Intent(this, ExpenseScreen::class.java)
            intent.putExtra("userId", userId) // передаем userId
            startActivityForResult(intent, REQUEST_CODE_EXPENSE)
        }
    }

    private fun loadBalance(userId: Long) {
        val userApi = RetrofitClient.api
        userApi.getBalance(userId).enqueue(object : Callback<Double> {
            override fun onResponse(call: Call<Double>, response: Response<Double>) {
                if (response.isSuccessful) {
                    val balance = response.body()
                    balanceTextView.text = String.format("%.2f ₽", balance ?: 0.0)
                    Log.d("MainScreen", "Баланс загружен: ${balance ?: 0.0}")
                } else {
                    Toast.makeText(this@MainScreen, "Ошибка при загрузке баланса", Toast.LENGTH_SHORT).show()
                    Log.e("MainScreen", "Ошибка при загрузке баланса: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Double>, t: Throwable) {
                Toast.makeText(this@MainScreen, "Ошибка: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("MainScreen", "Ошибка при загрузке баланса: ${t.message}", t)
            }
        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            val userId = data.getLongExtra("userId", -1)
            if (userId != -1L) {
                loadBalance(userId)  // Обновляем баланс после добавления транзакции
            }
        }
    }




}
