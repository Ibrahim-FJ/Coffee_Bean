package com.ibrahimf.coffeebean.addProduct.domain

import com.ibrahimf.coffeebean.addProduct.data.ProductRepository

class AddImageToFirebaseStorageUseCase(private val addProductRepository: ProductRepository) {

    suspend operator fun invoke() = addProductRepository.addImageToFirebaseStorage()
}