package com.example.presentation


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Expense
import com.example.domain.usecase.AddExpenseUseCase
import com.example.domain.usecase.DeleteExpenseUseCase
import com.example.domain.usecase.GetExpensesUseCase
import com.example.domain.usecase.RefreshExpensesUseCase
import com.example.domain.usecase.ValidateUseCase
import com.example.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpensesUseCase,
    private val refreshExpensesUseCase: RefreshExpensesUseCase,
    private val addExpenseUseCase: AddExpenseUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase,
    private val validateUseCase: ValidateUseCase
) : ViewModel()
{
    private val _state = MutableStateFlow(ExpenseState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init{
        getExpensesUseCase().onEach { expenses ->
            _state.update { it.copy(expenses = expenses) }
        }
            .launchIn(viewModelScope)

        refresh()
    }

    private fun refresh(){
        refreshExpensesUseCase().onEach { result ->
            _state.update {
                it.copy(
                    isLoading = result is Result.Loading,
                    error = if (result is Result.Error) result.message else null
                )
            }
            if (result is Result.Error) {
                _eventFlow.emit(UiEvent.ShowToast(result.message ?: "Unknown Error"))
            }
        }
            .launchIn(viewModelScope)
    }

    fun onTitleChanged(title: String){
        _state.update { it.copy(title = title, titleError = null) }
    }

    fun onAmountChanged(amount: String){
        _state.update { it.copy(amount = amount, amountError = null) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onAddExpense(){
        val currentState = _state.value
        val amount = currentState.amount.toDoubleOrNull()
        val result = validateUseCase.invoke(currentState.title, amount)

        if(result is Result.Error){
            _state.update {
                it.copy(
                    titleError = if (currentState.title.isBlank()) result.message else null,
                    amountError = if (currentState.title.isNotBlank()) result.message else null
                )
            }
            return
        }

        viewModelScope.launch{
            val expense = Expense(
                id = java.util.UUID.randomUUID().toString(),
                title = currentState.title,
                amount = amount!!,
                date = java.time.OffsetDateTime.now().toString()
            )

            addExpenseUseCase(expense)
            clearDialogValues()
            _eventFlow.emit(UiEvent.SaveExpenseSuccess)
        }
    }

    fun clearDialogValues(){
        _state.update { it.copy(title = "", amount = "", titleError = null, amountError = null) }
    }


    fun deleteExpense(expense: Expense){
        viewModelScope.launch {
            try{
                deleteExpenseUseCase(expense)
            }catch(e: Exception){
                _eventFlow.emit(UiEvent.ShowToast(e.localizedMessage ?: "Could not delete"))
            }
        }
    }
}