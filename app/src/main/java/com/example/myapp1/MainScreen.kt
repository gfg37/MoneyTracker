package com.example.myapp1

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText

class MainScreen : AppCompatActivity() {

    // private val categories = mutableListOf("1", "2", "3")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        // переход в аналитику
        val imageButton3: ImageButton = findViewById(R.id.imageButton3)
        imageButton3.setOnClickListener {
            val intent = Intent(this, BetweenScreen::class.java)
            startActivity(intent)
        }

        // переход в настройки
        val imageButtonSettings: ImageButton = findViewById(R.id.imageButtonSettings)
        imageButtonSettings.setOnClickListener {
            val intent = Intent(this, SettingScreen::class.java)
            startActivity(intent)
        }

        // переход в доходы
        val imageButton1: ImageButton = findViewById(R.id.imageButton1)
        imageButton1.setOnClickListener {
            val intent = Intent(this, RevenueScreen::class.java)
            startActivity(intent)
        }
        // переход в расходы
        val imageButton2: ImageButton = findViewById(R.id.imageButton2)
        imageButton2.setOnClickListener {
            val intent = Intent(this, ExpenseScreen::class.java)
            startActivity(intent)
        }

       /* val imageButton1: ImageButton = findViewById(R.id.imageButton1)
       imageButton1.setOnClickListener {
            // Создаем EditText для добавления новой категории
            val input = EditText(this)
            input.hint = "Введите категорию"

            // Создаем диалог с кастомным макетом или обычным функционалом
            val builder = AlertDialog.Builder(this, R.style.CustomPopupMenu)
            builder.setTitle("Выберите категорию:")
                .setItems(categories.toTypedArray()) { _, which ->
                    // Действие при выборе категории
                    Toast.makeText(this, "Вы выбрали: ${categories[which]}", Toast.LENGTH_SHORT)
                        .show()
                }
                .setView(input) // Добавляем EditText в диалог
                .setPositiveButton("Добавить") { _, _ ->
                    // Добавление новой категории в список
                    val newCategory = input.text.toString().trim()
                    if (newCategory.isNotEmpty()) {
                        categories.add(newCategory)
                        Toast.makeText(
                            this,
                            "Категория '$newCategory' добавлена.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(this, "Введите корректное значение.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                .setNegativeButton("Отмена") { dialog, _ ->
                    dialog.dismiss()
                }

            // Создаем и показываем диалог
            val dialog = builder.create()
            dialog.show()
            // Устанавливаем фиксированный размер окна
            dialog.window?.setLayout(800, WindowManager.LayoutParams.WRAP_CONTENT)
        }*/
    }
}