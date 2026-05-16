package com.example.data.remote

import retrofit2.http.GET

interface ExpenseApi {

    @GET("b/DYZJF")
    suspend fun getExpenses(): List<ExpenseDto>

}