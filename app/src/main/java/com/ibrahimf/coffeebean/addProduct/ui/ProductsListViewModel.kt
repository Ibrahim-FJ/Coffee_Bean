package com.ibrahimf.coffeebean.addProduct.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibrahimf.coffeebean.addProduct.domain.GetProductsUseCase
import com.ibrahimf.coffeebean.network.models.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductsListViewModel(private val getProductsUseCase: GetProductsUseCase): ViewModel() {
    private var _products = MutableStateFlow(MutableLiveData<MutableList<Product>>(mutableListOf()))
    val products: StateFlow<MutableLiveData<MutableList<Product>>> = _products.asStateFlow()

    var _allProducts = MutableLiveData<MutableList<Product>>(mutableListOf())


    fun getAllProducts(){

        viewModelScope.launch {
            getProductsUseCase.invoke().collect{product ->
                _products.value.value?.add(product)
                Log.e("productViewModel", "productViewModel: ${_allProducts.value}")

//                _products.update {
//                    it.copy(title = product.title, details = product.details, location = product.location, publishDate = product.publishDate, publisher = product.publisher)
//                }

            }

        }
    }
}