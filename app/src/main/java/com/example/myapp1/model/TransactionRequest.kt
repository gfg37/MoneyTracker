package com.example.myapp1.model

data class TransactionRequest(
    val amount: Double,
    val description: String?,
    val dateTime: String,
    val category: CategoryRequest,
    val user: UserRequest
)

data class CategoryRequest(
    val id: Long
)

data class UserRequest(
    val id: Long
)
