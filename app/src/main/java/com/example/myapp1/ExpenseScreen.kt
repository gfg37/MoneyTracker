package com.example.myapp1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapp1.model.CategoryRequest
import com.example.myapp1.model.TransactionHistoryItem
import com.example.myapp1.model.TransactionRequest
import com.example.myapp1.model.TransactionViewModel
import com.example.myapp1.model.UserRequest
import com.example.myapp1.serv.RetrofitClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ExpenseScreen : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_EXPENSE = 1
    }
    private var userId: Long? = null
    private lateinit var amountEditText: EditText
    private lateinit var descriptionEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_screen)

        // Получение userId из Intent
        userId = intent.getLongExtra("userId", -1)
        Log.d("ExpenseScreen", "Получен userId: $userId")
        if (userId == -1L) {
            Toast.makeText(this, "Ошибка: не удалось получить userId", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Инициализация элементов интерфейса
        amountEditText = findViewById(R.id.editTextCategory)
        descriptionEditText = findViewById(R.id.descripEditText2)

        // Обработка кнопок категорий
        setupCategoryButtons()
    }

    private fun setupCategoryButtons() {
        val categoryButtons = mapOf(
            R.id.categoryImageButton1 to 1L, // ID категории "Продукты"
            R.id.categoryImageButton2 to 4L, // ID категории "Одежда"
            R.id.categoryImageButton3 to 3L, // ID категории "Переводы"
            R.id.categoryImageButton4 to 2L,  // ID категории "Аптека"
            R.id.categoryImageButton5 to 6L, // счета и налоги
            R.id.categoryImageButton6 to 7L, // развлечения
            R.id.categoryImageButton7 to 8L, // транспорт
            R.id.categoryImageButton8 to 5L, // рестораны
            R.id.categoryImageButton9 to 11L // остальное
        )

        for ((buttonId, categoryId) in categoryButtons) {
            findViewById<ImageButton>(buttonId).setOnClickListener {
                handleTransactionCreation(categoryId)
            }
        }

        findViewById<ImageButton>(R.id.backButtonCategory5).setOnClickListener {
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
        val currentDateTime2 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))


        // Превращаем сумму в отрицательную для расходов
        val signedAmount = -amount

        val transaction = TransactionRequest(
            amount = signedAmount,
            description = description,
            dateTime = currentDateTime,
            category = CategoryRequest(categoryId),
            user = UserRequest(userId!!)
        )

        RetrofitClient.api.createTransaction(transaction).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ExpenseScreen, "Транзакция добавлена!", Toast.LENGTH_SHORT).show()
                    amountEditText.text.clear()
                    descriptionEditText.text.clear()



                    val transactionItem = TransactionHistoryItem(
                        amount = signedAmount,
                        date = currentDateTime,
                        description = description,
                        categoryId = categoryId
                    )

                    // Сохраняем транзакцию в локальной памяти
                    saveTransactionToLocalStorage(transactionItem)





                    val resultIntent = Intent()
                    resultIntent.putExtra("userId", userId)  // Передаем userId обратно в MainScreen
                    setResult(RESULT_OK, resultIntent)  // Устанавливаем результат
                    finish()  // Завершаем текущую активность, чтобы вернуться на главный экран

                } else {
                    Log.e("ExpenseScreen", "Ошибка сервера: ${response.code()} - ${response.errorBody()?.string()}")
                    Toast.makeText(
                        this@ExpenseScreen,
                        "Ошибка при создании транзакции: код ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("ExpenseScreen", "Ошибка сети: ${t.message}")
                Toast.makeText(this@ExpenseScreen, "Ошибка сети: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })




    }
    private fun saveTransactionToLocalStorage(transaction: TransactionHistoryItem) {
        val sharedPreferences = getSharedPreferences("TransactionHistory", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Получаем текущий список транзакций из локальной памяти
        val gson = Gson()
        val existingTransactionsJson = sharedPreferences.getString("transactions", null)
        val type = object : TypeToken<MutableList<TransactionHistoryItem>>() {}.type
        val transactionList: MutableList<TransactionHistoryItem> =
            gson.fromJson(existingTransactionsJson, type) ?: mutableListOf()

        // Добавляем новую транзакцию
        transactionList.add(transaction)

        // Сохраняем обновленный список обратно в SharedPreferences
        val updatedTransactionsJson = gson.toJson(transactionList)
        editor.putString("transactions", updatedTransactionsJson)
        editor.apply()
    }


}
