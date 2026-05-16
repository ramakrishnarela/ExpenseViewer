package com.example.domain

import com.example.domain.usecase.ValidateUseCase
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import com.example.domain.util.Result

class ValidateUseCaseTest {

    private lateinit var validateUseCase: ValidateUseCase

    @Before
    fun setup() {
        validateUseCase = ValidateUseCase()
    }

    @Test
    fun `returns error when title is blank`() {

        val result = validateUseCase("", 100.0)

        assertThat(result).isInstanceOf(Result.Error::class.java)

        val error = result as Result.Error

        assertThat(error.message).isEqualTo("Title cannot be blank")

    }

    @Test
    fun `returns error when amount is null`() {

        val result = validateUseCase(
            title = "Expense1",
            amount = null
        )

        assertThat(result).isInstanceOf(Result.Error::class.java)

        val error = result as Result.Error

        assertThat(error.message).isEqualTo("Amount is not valid")
    }

    @Test
    fun `returns success when input is valid`() {

        val result = validateUseCase(
            title = "Expense1",
            amount = 200.0
        )

        assertThat(result).isInstanceOf(Result.Success::class.java)
    }

}