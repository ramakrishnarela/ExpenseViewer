package com.example.domain

import com.example.domain.model.Expense
import com.example.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeRepository : ExpenseRepository {

    private val expenses = MutableStateFlow<List<Expense>>(emptyList())

    override fun getExpenses(): Flow<List<Expense>> {
        return expenses
    }

    fun setExpenses(list: List<Expense>) {
        expenses.value = list
    }

    override suspend fun refreshExpenses() {

    }

    override suspend fun addExpense(expense: Expense) {
        expenses.value += expense
    }

    override suspend fun deleteExpense(expense: Expense) {
        expenses.value -= expense
    }
}