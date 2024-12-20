package com.example.myapp1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp1.model.User
import com.example.myapp1.serv.AuthenticationRequest
import com.example.myapp1.serv.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginScreen : AppCompatActivity() {


    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)


        usernameInput = findViewById(R.id.loginInput2)
        passwordInput = findViewById(R.id.passwordInput2)
        loginButton = findViewById(R.id.loginButton)

        val userApi = RetrofitClient.api // Получение API из RetrofitClient

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = AuthenticationRequest(username, password)

            // Шаг 1: аутентификация пользователя
            userApi.authenticate(request).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        if (user != null) {
                            Log.d("LoginScreen", "userId: ${user.id}")
                            Toast.makeText(this@LoginScreen, "Добро пожаловать, ${user.username}", Toast.LENGTH_SHORT).show()

                            // Шаг 2: Получаем userId по имени
                            getUserIdByUsername(user.username) { userId ->
                                if (userId != null) {
                                    // Передаем userId в MainScreen
                                    val intent = Intent(this@LoginScreen, MainScreen::class.java)
                                    intent.putExtra("userId", userId)
                                    intent.putExtra("user", username)// Передаем userId в MainScreen
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this@LoginScreen, "Не удалось получить userId", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(this@LoginScreen, "Ошибка входа", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e("LoginScreen", "Ошибка: ${t.message}")
                    Toast.makeText(this@LoginScreen, "Ошибка сети", Toast.LENGTH_SHORT).show()
                }
            })
        }

        val registerText: TextView = findViewById(R.id.registerText)
        registerText.setOnClickListener {
            val intent = Intent(this, RegScreen::class.java)
            startActivity(intent)
        }
    }

    // Метод для получения userId по имени пользователя
    private fun getUserIdByUsername(username: String, callback: (Long?) -> Unit) {
        val userApi = RetrofitClient.api
        userApi.getUserByUsername(username).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        callback(user.id) // Отправляем полученный userId в callback
                    } else {
                        callback(null) // Если user не найден, возвращаем null
                    }
                } else {
                    callback(null) // В случае ошибки на сервере
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("LoginScreen", "Ошибка получения userId: ${t.message}")
                callback(null)
            }
        })
    }
}
