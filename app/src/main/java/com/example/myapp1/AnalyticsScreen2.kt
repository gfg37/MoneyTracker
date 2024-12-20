package com.example.myapp1

import TransactionHistoryAdapter
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp1.model.TransactionHistoryItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AnalyticsScreen2 : AppCompatActivity() {
    private lateinit var transactionHistoryRecyclerView2: RecyclerView
    private lateinit var transactionHistoryList: MutableList<TransactionHistoryItem>
    private lateinit var transactionHistoryAdapter: TransactionHistoryAdapter

    private fun loadTransactionHistory() {
        loadTransactionHistoryFromLocalStorage2()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics_screen2)

        // Инициализация RecyclerView
        transactionHistoryRecyclerView2 = findViewById(R.id.transactionHistoryRecyclerView2)
        transactionHistoryList = mutableListOf()

        // Инициализация адаптера
        transactionHistoryAdapter = TransactionHistoryAdapter(transactionHistoryList)
        transactionHistoryRecyclerView2.layoutManager = LinearLayoutManager(this)
        transactionHistoryRecyclerView2.adapter = transactionHistoryAdapter

        // Загрузка данных транзакций
        loadTransactionHistory()
    }

    // Метод для загрузки данных транзакций


    private fun loadTransactionHistoryFromLocalStorage2() {
        val sharedPreferences = getSharedPreferences("TransactionHistory2", Context.MODE_PRIVATE)
        val gson = Gson()
        val transactionsJson = sharedPreferences.getString("transactions", null)

        if (transactionsJson != null) {
            val type = object : TypeToken<MutableList<TransactionHistoryItem>>() {}.type
            val transactionListFromStorage: MutableList<TransactionHistoryItem> =
                gson.fromJson(transactionsJson, type)
            transactionHistoryList.clear()
            transactionHistoryList.addAll(transactionListFromStorage)
            transactionHistoryAdapter.notifyDataSetChanged()
        }
    }

}