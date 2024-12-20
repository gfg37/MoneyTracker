package com.example.myapp1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp1.model.CategoryRequest
import com.example.myapp1.model.TransactionHistoryItem
import com.example.myapp1.model.TransactionRequest
import com.example.myapp1.model.UserRequest
import com.example.myapp1.serv.RetrofitClient
import com.example.myapp1.serv.UserApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class RevenueScreen : AppCompatActivity() {


    companion object {
        const val REQUEST_CODE_EXPENSEE = 1
    }

    private var userId: Long? = null
    private lateinit var amountEditText: EditText
    private lateinit var descriptionEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_revenue_screen)



        // Получение userId из Intent
        userId = intent.getLongExtra("userId", -1)
        Log.d("RevenueScreen", "Получен userId: $userId")
        if (userId == -1L) {
            Toast.makeText(this, "Ошибка: не удалось получить userId", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Инициализация элементов интерфейса
        amountEditText = findViewById(R.id.editTextCategory2)
        descriptionEditText = findViewById(R.id.descripEditText1)

        // Обработка кнопок категорий
        setupCategoryButtons()
    }

    private fun setupCategoryButtons() {
        val categoryButtons = mapOf(
            R.id.category2ImageButton1 to 9L, // ID категории "Переводы"
            R.id.category2ImageButton2 to 3L, // ID категории "Переводы"
            R.id.category2ImageButton3 to 10L, // ID категории "Бонусы"
            R.id.category2ImageButton4 to 11L  // ID категории "Остальное"
        )

        for ((buttonId, categoryId) in categoryButtons) {
            findViewById<ImageButton>(buttonId).setOnClickListener {
                handleTransactionCreation(categoryId)
            }
        }

        findViewById<ImageButton>(R.id.backButtonCategory6).setOnClickListener {
            finish() // Возврат на предыдущий экран
        }
    }

    private fun handleTransactionCreation(categoryId: Long) {
        val amount = amountEditText.text.toString().toDoubleOrNull()
        val description = descriptionEditText.text.toString()

        if (amount == null || amount <= 0) {
            Toast.makeText(this, "Введите корректную сумму", Toast.LENGTH_SHORT).show()
            return
        }

        if (userId == null) {
            Toast.makeText(this, "Ошибка: userId не установлен", Toast.LENGTH_SHORT).show()
            return
        }

        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        val transaction = TransactionRequest(
            amount = amount,
            description = description,
            dateTime = currentDateTime,
            category = CategoryRequest(categoryId),
            user = UserRequest(userId!!)
        )



        RetrofitClient.api.createTransaction(transaction).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RevenueScreen, "Транзакция добавлена!", Toast.LENGTH_SHORT).show()
                    amountEditText.text.clear()
                    descriptionEditText.text.clear()


                    val transactionItem2 = TransactionHistoryItem(
                        amount = amount,
                        date = currentDateTime,
                        description = description,
                        categoryId = categoryId
                    )

                    // Сохраняем транзакцию в локальной памяти
                    saveTransactionToLocalStorage2(transactionItem2)

                    // После успешной транзакции, обновляем баланс в MainScreen и завершаем активность
                    val resultIntent = Intent()
                    resultIntent.putExtra("userId", userId)  // Передаем userId обратно в MainScreen
                    setResult(RESULT_OK, resultIntent)  // Устанавливаем результат
                    finish()  // Завершаем текущую активность, чтобы вернуться на главный экран

                } else {
                    Log.e("RevenueScreen", "Ошибка сервера: ${response.code()} - ${response.errorBody()?.string()}")
                    Toast.makeText(
                        this@RevenueScreen,
                        "Ошибка при создании транзакции: код ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("RevenueScreen", "Ошибка сети: ${t.message}")
                Toast.makeText(this@RevenueScreen, "Ошибка сети: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun saveTransactionToLocalStorage2(transaction2: TransactionHistoryItem) {
        val sharedPreferences = getSharedPreferences("TransactionHistory2", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Получаем текущий список транзакций из локальной памяти
        val gson = Gson()
        val existingTransactionsJson = sharedPreferences.getString("transactions", null)
        val type = object : TypeToken<MutableList<TransactionHistoryItem>>() {}.type
        val transactionList: MutableList<TransactionHistoryItem> =
            gson.fromJson(existingTransactionsJson, type) ?: mutableListOf()

        // Добавляем новую транзакцию
        transactionList.add(transaction2)

        // Сохраняем обновленный список обратно в SharedPreferences
        val updatedTransactionsJson = gson.toJson(transactionList)
        editor.putString("transactions", updatedTransactionsJson)
        editor.apply()
    }


}