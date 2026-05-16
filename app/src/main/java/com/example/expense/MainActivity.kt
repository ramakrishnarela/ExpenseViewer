package com.example.expense

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expense.ui.theme.ExpenseTheme
import com.example.presentation.ExpenseScreen
import com.example.presentation.ExpenseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseTheme {
                val viewModel: ExpenseViewModel = hiltViewModel()
                ExpenseScreen(viewModel = viewModel)
            }
        }
    }
}
