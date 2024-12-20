package com.example.myapp1

import TransactionHistoryAdapter
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp1.model.TransactionHistoryItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AnalyticsScreen : AppCompatActivity() {

    private lateinit var transactionHistoryRecyclerView: RecyclerView
    private lateinit var transactionHistoryList: MutableList<TransactionHistoryItem>
    private lateinit var transactionHistoryAdapter: TransactionHistoryAdapter

    private fun loadTransactionHistory() {
        loadTransactionHistoryFromLocalStorage()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analytics_screen)

        // Инициализация RecyclerView
        transactionHistoryRecyclerView = findViewById(R.id.transactionHistoryRecyclerView)
        transactionHistoryList = mutableListOf()

        // Инициализация адаптера
        transactionHistoryAdapter = TransactionHistoryAdapter(transactionHistoryList)
        transactionHistoryRecyclerView.layoutManager = LinearLayoutManager(this)
        transactionHistoryRecyclerView.adapter = transactionHistoryAdapter

        // Загрузка данных транзакций
        loadTransactionHistory()
    }

    // Метод для загрузки данных транзакций


    private fun loadTransactionHistoryFromLocalStorage() {
        val sharedPreferences = getSharedPreferences("TransactionHistory", Context.MODE_PRIVATE)
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
