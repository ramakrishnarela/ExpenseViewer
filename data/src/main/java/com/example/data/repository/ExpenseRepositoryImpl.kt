package com.example.data.repository

import com.example.data.local.ExpenseDao
import com.example.data.mapper.toDomain
import com.example.data.mapper.toEntity
import com.example.data.remote.ExpenseApi
import com.example.domain.model.Expense
import com.example.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val api: ExpenseApi,
    private val dao: ExpenseDao
): ExpenseRepository {
    override fun getExpenses(): Flow<List<Expense>> {
        return dao.getExpenses().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun refreshExpenses() {
        val remoteExpenses = api.getExpenses()
        dao.insertExpenses(remoteExpenses.map { it.toDomain().toEntity() })
    }

    override suspend fun addExpense(expense: Expense) {
        dao.insertExpense(expense.toEntity())
    }

    override suspend fun deleteExpense(expense: Expense) {
        dao.deleteExpense(expense.toEntity())
    }
}