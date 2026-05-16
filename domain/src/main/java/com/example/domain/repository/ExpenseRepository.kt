package com.example.domain.repository

import com.example.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun getExpenses(): Flow<List<Expense>>
    suspend fun refreshExpenses()
    suspend fun addExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)
}