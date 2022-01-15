package com.ibrahimf.coffeebean.addProduct.domainLayer

import com.ibrahimf.coffeebean.addProduct.dataLayer.ProductRepository
import com.ibrahimf.coffeebean.network.models.Product
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


class AddProductUseCase(
    private val addProductRepository: ProductRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend operator fun invoke(product: Product) = addProductRepository.addProduct(product)
}