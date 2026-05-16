package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.local.ExpenseDao
import com.example.data.local.ExpenseDatabase
import com.example.data.remote.ExpenseApi
import com.example.data.repository.ExpenseRepositoryImpl
import com.example.domain.repository.ExpenseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val BASE_URL = "https://www.jsonkeeper.com/"

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideExpenseApi(): ExpenseApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExpenseApi::class.java)
    }


    @Provides
    @Singleton
    fun provideExpenseDatabase(@ApplicationContext context: Context): ExpenseDatabase {
        return Room.databaseBuilder(
            context,
            ExpenseDatabase::class.java,
            "expense_db"
        ).build()
    }


    @Provides
    fun provideExpenseDao(database: ExpenseDatabase): ExpenseDao{
        return database.expenseDao
    }


    @Provides
    @Singleton
    fun provideExpenseRepository(
        api: ExpenseApi,
        dao: ExpenseDao
    ): ExpenseRepository {
        return ExpenseRepositoryImpl(api, dao)
    }
}