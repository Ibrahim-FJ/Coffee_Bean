package com.ibrahimf.coffeebean.userProfile.uiLayer

import android.util.Log
import androidx.lifecycle.*
import com.ibrahimf.coffeebean.userProfile.model.User
import com.ibrahimf.coffeebean.network.models.Product
import com.ibrahimf.coffeebean.userProfile.domainLayer.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val userProfileOrdersUseCase: UserProfileOrdersUseCase,
    private val userProfileReservationUseCase: UserProfileRequestsUseCase,
    private val userPostsUseCase: UserPostsUseCase,
    private val addUserUserCase: AddUserUserCase,
    private val getUserUseCase: GetUserUseCase,
    private val getBuyerInformationUserCase: GetBuyerInformationUserCase
) : ViewModel() {

    private val _ordersStateFlow = MutableStateFlow<List<Product>>(emptyList())
    val ordersStateFlow: StateFlow<List<Product>> = _ordersStateFlow.asStateFlow()

    val productStatFlowToLiveData = ordersStateFlow.asLiveData()
    val _userOrders = MutableLiveData<List<Product>?>()
    val userOrders = _userOrders
    val _userReservationRequest = MutableLiveData<List<OrderDetailsUiState>?>()
    val _userPosts = MutableLiveData<List<Product>?>()
    val _user = MutableLiveData<User>()

    private var _uiStatus = MutableStateFlow(UserRegistrationUiState())
    val uiState: LiveData<UserRegistrationUiState> = _uiStatus.asLiveData()


    fun getUserOrders() {
        viewModelScope.launch {
            userProfileOrdersUseCase.invoke().collect {
                _userOrders.value = it

            }
        }
    }

    fun getUserReservationRequest() {
        val list = mutableListOf<OrderDetailsUiState>()
        viewModelScope.launch {

            userProfileReservationUseCase.invoke().collect {
                it.forEach { order ->
                    viewModelScope.launch {
                        getBuyerInformationUserCase(order.buyer).collect {
                            val orderDetails = OrderDetailsUiState(
                                userImage = it.userImage,
                                name = it.userName,
                                phone = it.userPhone,
                                message = order.message,
                                quantity = order.quantity
                            )
                            list.add(orderDetails)

                            _userReservationRequest.value = list

                        }

                    }

                }

            }

        }

    }

    fun getUserPosts() {
        viewModelScope.launch {
            userPostsUseCase.invoke().collect {
                _userPosts.value = it

            }
        }
    }

    fun addUser(user: User) {
        viewModelScope.launch {
            addUserUserCase.invoke(user)
        }
    }

    fun getUser() {
        viewModelScope.launch {
            getUserUseCase.invoke().collect {
                _user.value = it

            }
        }
    }

    fun registerUser(userProfileUiState: UserProfileUiState) {
        viewModelScope.launch {

            _uiStatus.update { it.copy(loadingStatus = LOADING_STATUS.LOADING) }

            Log.e("TAG", "registerUser: $userProfileUiState")

        }
    }
}