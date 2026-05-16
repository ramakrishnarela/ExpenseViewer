package com.example.domain.usecase

import com.example.domain.model.Expense
import com.example.domain.repository.ExpenseRepository
import javax.inject.Inject

class DeleteExpenseUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke (expense: Expense) = repository.deleteExpense(expense)
}