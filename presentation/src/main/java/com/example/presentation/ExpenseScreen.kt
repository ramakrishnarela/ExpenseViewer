package com.example.presentation

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.Expense
import kotlinx.coroutines.flow.collectLatest

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseScreen(viewModel: ExpenseViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    var showAddDialog by rememberSaveable { mutableStateOf(false) }

    HandleUiEvents(
        viewModel = viewModel,
        onSaveSuccess = {
            showAddDialog = false
        }
    )

    HandleErrors(error = state.error)

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Expense Viewer") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showAddDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Expense")
            }
        }
    ) { paddingValues ->

        ExpenseContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = state,
            onDelete = viewModel::deleteExpense
        )

        if (showAddDialog) {
            AddExpenseDialog(
                title = state.title,
                amount = state.amount,
                titleError = state.titleError,
                amountError = state.amountError,
                onTitleChange = viewModel::onTitleChanged,
                onAmountChange = viewModel::onAmountChanged,
                onConfirm = viewModel::onAddExpense,
                onDismiss = { showAddDialog = false }
            )
        }
    }
}

@Composable
private fun HandleUiEvents(
    viewModel: ExpenseViewModel,
    onSaveSuccess: () -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is UiEvent.SaveExpenseSuccess -> {
                    onSaveSuccess()
                }
            }
        }
    }
}

@Composable
private fun HandleErrors(
    error: String?
) {
    val context = LocalContext.current

    LaunchedEffect(error) {
       error?.let{
           Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
       }
    }
}

@Composable
private fun ExpenseContent(
    modifier: Modifier = Modifier,
    state: ExpenseState,
    onDelete: (Expense) -> Unit
) {
    Box(modifier = modifier){
        when{
            state.isLoading && state.expenses.isEmpty() -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            state.expenses.isEmpty() -> {
                Text(
                    text = "No expenses. Tap + to add.",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(
                        items = state.expenses,
                        key = {it.id}
                    ){ expense ->
                        ExpenseItem(expense) {
                            onDelete(expense)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseItem(expense: Expense, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = expense.title, style = MaterialTheme.typography.titleMedium)
                Text(text = "₹${expense.amount}", style = MaterialTheme.typography.bodyMedium)
                Text(text = expense.date.take(10), style = MaterialTheme.typography.bodySmall)
            }
            Text(
                text = "Delete",
                modifier = Modifier.clickable{
                    onDelete()
                }
            )
        }
    }
}

@Composable
fun AddExpenseDialog(
    title :String,
    amount : String,
    titleError:String?,
    amountError :String?,
    onTitleChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Expense") },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = onTitleChange,
                    label = { Text("Title") },
                    isError = titleError != null,
                    supportingText = { titleError?.let { Text(it) } }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = amount,
                    onValueChange = onAmountChange,
                    label = { Text("Amount") },
                    isError = amountError != null,
                    supportingText = { amountError?.let { Text(it) } }
                )
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}