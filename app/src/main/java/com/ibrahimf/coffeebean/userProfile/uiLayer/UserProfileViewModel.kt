package com.ibrahimf.coffeebean.userProfile.uiLayer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ibrahimf.coffeebean.userProfile.model.User
import com.ibrahimf.coffeebean.network.models.Product
import com.ibrahimf.coffeebean.userProfile.domainLayer.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val userProfileOrdersUseCase: UserProfileOrdersUseCase,
    private val userProfileReservationUseCase: UserProfileRequestsUseCase,
    private val userPostsUseCase: UserPostsUseCase,
    private val editProductUseCase: EditProductUseCase,
    private val addUserUserCase: AddUserUserCase,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    private val _ordersStateFlow = MutableStateFlow<List<Product>>(emptyList())
    val ordersStateFlow: StateFlow<List<Product>> = _ordersStateFlow.asStateFlow()

    val productStatFlowToLiveData = ordersStateFlow.asLiveData()
    var _userOrders = MutableLiveData<List<Product>?>()
    var _userReservationRequest = MutableLiveData<List<Product>?>()
    var _userPosts = MutableLiveData<List<Product>?>()
    var _user = MutableLiveData<User>()


    init {
        getUserOrders()
        getUserReservationRequest()
        getUserPosts()
        getUser()
    }

    fun getUserOrders() {
        viewModelScope.launch {
            userProfileOrdersUseCase.invoke().collect {
                _userOrders.value = it

            }
        }
    }

    fun getUserReservationRequest() {
        viewModelScope.launch {
            userProfileReservationUseCase.invoke().collect {
                _userReservationRequest.value = it

            }
        }
    }

    fun getUserPosts(){
        viewModelScope.launch {
            userPostsUseCase.invoke().collect {
                _userPosts.value = it

            }
        }
    }

    fun updateProduct(product: Product){
        viewModelScope.launch {
            editProductUseCase.invoke(product)
        }
    }

    fun addUser(user: User){
        viewModelScope.launch {
            addUserUserCase.invoke(user)
        }
    }

    fun getUser(){
        viewModelScope.launch {
            getUserUseCase.invoke().collect{
                _user.value = it

            }
        }
    }
}