package com.example.domain

import com.example.domain.model.Expense
import com.example.domain.usecase.GetExpensesUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

import org.junit.Before
import org.junit.Test

class GetExpensesUseCaseTest {

    private lateinit var repository: FakeRepository
    private lateinit var useCase: GetExpensesUseCase


    @Before
    fun setup() {
        repository = FakeRepository()
        useCase = GetExpensesUseCase(repository)
    }

    @Test
    fun `invoke returns expenses`() = runTest {

        val expenses = listOf(
            Expense(
                id = "1",
                title = "Expense1",
                amount = 200.0,
                date = "2026-05-16"
            ),
            Expense(
                id = "2",
                title = "Expense2",
                amount = 54.25,
                date = "2026-05-16"
            )
        )

        for(item in expenses){
            repository.addExpense(expense = item)
        }

        val result = useCase().first()

        assertThat(result).isEqualTo(expenses)
    }
}