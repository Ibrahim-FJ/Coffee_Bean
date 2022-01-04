package com.ibrahimf.coffeebean.addProduct.util

import com.google.firebase.firestore.FirebaseFirestore
import com.ibrahimf.coffeebean.addProduct.data.ProductFireStoreDataSource
import com.ibrahimf.coffeebean.addProduct.data.ProductRepository
import com.ibrahimf.coffeebean.addProduct.domain.AddProductUseCase
import com.ibrahimf.coffeebean.showProducts.domainLayer.GetProductsUseCase
import com.ibrahimf.coffeebean.addProduct.data.ProductDataSource
import com.ibrahimf.coffeebean.reserveOrder.dataLayer.OrderRepository
import com.ibrahimf.coffeebean.reserveOrder.domainLayer.ReserveOrderUseCase


object ServiceLocator {

    fun provideProductRemoteDataSource(): ProductDataSource = ProductFireStoreDataSource(
        FirebaseFirestore.getInstance()
    )

    fun provideAppProductRepository(): ProductRepository =
        ProductRepository(provideProductRemoteDataSource())

    fun provideAddProductUseCase(): AddProductUseCase =
        AddProductUseCase(provideAppProductRepository())

    fun provideGetProductsUseCase(): GetProductsUseCase = GetProductsUseCase(
        provideAppProductRepository()
    )

    fun provideOrderRepository(): OrderRepository =
        OrderRepository(provideProductRemoteDataSource())

    fun provideReserveOrderUseCase(): ReserveOrderUseCase =
        ReserveOrderUseCase(provideOrderRepository())


}
