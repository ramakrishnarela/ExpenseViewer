package com.example.domain.usecase

import com.example.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.example.domain.util.Result

class RefreshExpensesUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    operator fun invoke() : Flow<Result<Unit>> = flow{
        emit(Result.Loading())
        try{
            repository.refreshExpenses()
            emit(Result.Success(Unit))

        }catch(e: Exception){
            emit(Result.Error(e.localizedMessage ?: "Error Occurred" ))
        }
    }
}