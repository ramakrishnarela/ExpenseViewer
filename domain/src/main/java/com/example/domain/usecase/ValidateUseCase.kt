package com.example.domain.usecase

import com.example.domain.util.Result
import javax.inject.Inject

class ValidateUseCase @Inject constructor() {

    operator fun invoke(title: String, amount: Double?) : Result<Unit> {
        if(title.isBlank()){
            return Result.Error("Title cannot be blank")
        }

        amount?.let {
            if (it <= 0) {
                return Result.Error("Amount should be greater than 0")
            }
        } ?: return Result.Error("Amount is not valid")

        return Result.Success(Unit)
    }
}