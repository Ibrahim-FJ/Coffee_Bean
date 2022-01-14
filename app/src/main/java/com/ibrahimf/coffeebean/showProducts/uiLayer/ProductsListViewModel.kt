package com.ibrahimf.coffeebean.showProducts.uiLayer

import androidx.lifecycle.*
import com.ibrahimf.coffeebean.showProducts.domainLayer.GetProductsUseCase
import com.ibrahimf.coffeebean.network.models.Product
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductsListViewModel(private val getProductsUseCase: GetProductsUseCase) : ViewModel() {

    private val _productsStateFlow = MutableStateFlow<List<Product>>(emptyList())
    val productsStateFlow: StateFlow<List<Product>> = _productsStateFlow.asStateFlow()


    var _products = MutableLiveData<List<Product>?>()
    val productStatFlowToLiveData = productsStateFlow.asLiveData()


    init {
        // getAllProducts()
        getAllProductsWithFlow()
    }

    fun getAllProducts() {

        viewModelScope.launch {
            getProductsUseCase.invoke().collect { products ->
                _products.value = products

            }

        }
    }

    fun getAllProductsWithFlow() {

        viewModelScope.launch {
            getProductsUseCase.invoke().collect { products ->
                //race condition
                //   _productsStateFlow.value=products

                _productsStateFlow.update { products }

            }

        }
    }

}