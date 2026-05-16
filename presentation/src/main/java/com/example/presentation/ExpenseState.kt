package com.example.presentation

import com.example.domain.model.Expense

data class ExpenseState(
    val expenses: List<Expense> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,

    val title: String = "",
    val amount: String = "",
    val titleError: String? = null,
    val amountError: String? = null
)


sealed class DialogEvent{
    data class TitleChanged(val title: String) : DialogEvent()
    data class AmountChanged(val amount: String) : DialogEvent()
    object Submit : DialogEvent()
}


sealed class UiEvent{
    data class ShowToast(val message: String) : UiEvent()
    object SaveExpenseSuccess : UiEvent()
}