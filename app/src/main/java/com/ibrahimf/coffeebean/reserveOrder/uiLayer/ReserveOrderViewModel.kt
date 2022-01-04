package com.ibrahimf.coffeebean.reserveOrder.uiLayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibrahimf.coffeebean.reserveOrder.dataLayer.Order
import com.ibrahimf.coffeebean.reserveOrder.domainLayer.ReserveOrderUseCase
import kotlinx.coroutines.launch

class ReserveOrderViewModel(private val reserveOrderUseCase: ReserveOrderUseCase): ViewModel() {

    fun reserveOrder(order: Order){
        viewModelScope.launch {
            reserveOrderUseCase.invoke(order)
        }
    }

}