package com.ibrahimf.coffeebean.addProduct.domain

import com.ibrahimf.coffeebean.addProduct.data.ProductRepository
import com.ibrahimf.coffeebean.network.models.Product
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class AddProductUseCase(
    private val addProductRepository: ProductRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend operator fun invoke(product: Product) =

        addProductRepository.addProduct(product)


}