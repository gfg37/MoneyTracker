package com.example.myapp1.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TransactionViewModel : ViewModel() {
    private val _transactions = MutableLiveData<MutableList<TransactionHistoryItem>>(mutableListOf())
    val transactions: LiveData<MutableList<TransactionHistoryItem>> get() = _transactions

    fun addTransaction(transaction: TransactionHistoryItem) {
        _transactions.value?.add(transaction)
        _transactions.value = _transactions.value // Обновление LiveData
    }
}
