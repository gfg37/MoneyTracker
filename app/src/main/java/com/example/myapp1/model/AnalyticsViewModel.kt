package com.example.myapp1.model


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapp1.model.TransactionHistoryItem

class AnalyticsViewModel : ViewModel() {

    // LiveData для истории транзакций
    private val _transactionHistory = MutableLiveData<List<TransactionHistoryItem>>()
    val transactionHistory: LiveData<List<TransactionHistoryItem>> get() = _transactionHistory

    init {
        // Загружаем начальные данные
        loadTransactionHistory()
    }

    // Метод для загрузки данных (в реальном приложении заменить на запрос к серверу)
    private fun loadTransactionHistory() {
        val transactions = listOf(
            TransactionHistoryItem(1500.0, "20/12/2024", "Аренда квартиры", 1L),
            TransactionHistoryItem(350.0, "18/12/2024", "Покупка продуктов", 2L),
            TransactionHistoryItem(500.0, "15/12/2024", "Поход в кафе", 4L),
            TransactionHistoryItem(2000.0, "10/12/2024", "Покупка техники", 3L)
        )
        _transactionHistory.value = transactions
    }

    // Метод для добавления новой транзакции
    fun addTransaction(transaction: TransactionHistoryItem) {
        val updatedList = _transactionHistory.value?.toMutableList() ?: mutableListOf()
        updatedList.add(transaction)
        _transactionHistory.value = updatedList
    }
}
