package com.example.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ExpenseEntity.class}, version = 1)
public abstract class ExpenseDatabase extends RoomDatabase {
    public abstract ExpenseDao getExpenseDao();
}
