package com.example.myapp1

import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat

class SettingScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_screen)

        // Получаем имя пользователя из MainActivity
        val username = intent.getStringExtra("user")

        // Устанавливаем текст в TextView
        val textView44 = findViewById<TextView>(R.id.textView44) // Укажите правильный ID
        textView44.text = username ?: "Неизвестный пользователь"


        val logoutButton = findViewById<Button>(R.id.button44) // Укажите правильный ID
        logoutButton.setOnClickListener {
            // Возвращаемся на экран входа
            val intent = Intent(this@SettingScreen, LoginScreen::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        findViewById<ImageButton>(R.id.imageButton7).setOnClickListener {
            finish() // Возврат на предыдущий экран
        }
        val notificationSwitch = findViewById<SwitchCompat>(R.id.switch1)

        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Открываем настройки уведомлений
                val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                startActivity(intent)


            }
        }

    }
}
