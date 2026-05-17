package com.example.data.di;


import android.content.Context;

import androidx.room.Room;

import com.example.data.local.ExpenseDao;
import com.example.data.local.ExpenseDatabase;
import com.example.data.remote.ExpenseApi;
import com.example.data.repository.ExpenseRepositoryImpl;
import com.example.domain.repository.ExpenseRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class DataModule {
    public static final String BASE_URL = "https://www.jsonkeeper.com/";

    @Provides
    @Singleton
    public ExpenseApi provideExpenseApi() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ExpenseApi.class);
    }


    @Provides
    @Singleton
    public ExpenseDatabase provideExpenseDatabase(
            @ApplicationContext Context context
    ) {
        return Room.databaseBuilder(
                context,
                ExpenseDatabase.class,
        "expense_db" ).build();
    }

    @Provides
    public ExpenseDao provideExpenseDao(ExpenseDatabase database){
        return database.getExpenseDao();
    }


    @Provides
    @Singleton
    public ExpenseRepository provideExpenseRepository(
            ExpenseApi api,
            ExpenseDao dao
    ) {
        return new ExpenseRepositoryImpl(api, dao);
    }
}

