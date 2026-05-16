package com.example.data.mapper

import com.example.data.local.ExpenseEntity
import com.example.data.remote.ExpenseDto
import com.example.domain.model.Expense

fun ExpenseDto.toDomain(): Expense {
    return Expense(
        id = id,
        title = title,
        amount = amount,
        date = date
    )
}

fun ExpenseEntity.toDomain(): Expense {
    return Expense(
        id = id,
        title = title,
        amount = amount,
        date = date
    )
}

fun Expense.toEntity(): ExpenseEntity {
    return ExpenseEntity(
        id = id,
        title = title,
        amount = amount,
        date = date
    )
}