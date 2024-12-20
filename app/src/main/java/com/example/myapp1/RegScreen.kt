package com.example.myapp1


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp1.model.User
import com.example.myapp1.serv.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegScreen : AppCompatActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg_screen)

        usernameInput = findViewById(R.id.loginInput3)
        passwordInput = findViewById(R.id.passwordInput1)
        confirmPasswordInput = findViewById(R.id.passwordInput2)
        registerButton = findViewById(R.id.loginButton3)

        registerButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()

            if (password != confirmPassword) {
                Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
            } else {
                val user = User(username = username, password = password, role = "USER")
                registerUser(user)
            }
        }
    }

    // В классе RegScreen (регистрация)
    private fun registerUser(user: User) {
        RetrofitClient.api.register(user).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val registeredUser = response.body()
                    if (registeredUser != null) {
                        Toast.makeText(this@RegScreen, "Регистрация прошла успешно", Toast.LENGTH_SHORT).show()
                        // Сохраняем userId и переходим на экран логина
                        val intent = Intent(this@RegScreen, LoginScreen::class.java)
                        intent.putExtra("userId", registeredUser.id) // Передаем userId после регистрации
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Toast.makeText(this@RegScreen, "Ошибка регистрации", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@RegScreen, "Ошибка сети: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
