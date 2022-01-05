package com.ibrahimf.coffeebean.userProfile.uiLayer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ibrahimf.coffeebean.network.models.Product
import com.ibrahimf.coffeebean.userProfile.domainLayer.UserPostsUseCase
import com.ibrahimf.coffeebean.userProfile.domainLayer.UserProfileOrdersUseCase
import com.ibrahimf.coffeebean.userProfile.domainLayer.UserProfileRequestsUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val userProfileOrdersUseCase: UserProfileOrdersUseCase,
    private val userProfileReservationUseCase: UserProfileRequestsUseCase,
    private val userPostsUseCase: UserPostsUseCase
) : ViewModel() {




    private val _ordersStateFlow = MutableStateFlow<List<Product>>(emptyList())
    val ordersStateFlow: StateFlow<List<Product>> = _ordersStateFlow.asStateFlow()

    val productStatFlowToLiveData = ordersStateFlow.asLiveData()
    var _userOrders = MutableLiveData<List<Product>?>()
    var _userReservationRequest = MutableLiveData<List<Product>?>()
    var _userPosts = MutableLiveData<List<Product>?>()



    init {
        getUserOrders()
        getUserReservationRequest()
        getUserPosts()
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
}