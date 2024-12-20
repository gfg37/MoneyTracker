package com.example.myapp1.model

data class TransactionHistoryItem(
    val amount: Double,
    val date: String,
    val description: String?,
    val categoryId: Long
)