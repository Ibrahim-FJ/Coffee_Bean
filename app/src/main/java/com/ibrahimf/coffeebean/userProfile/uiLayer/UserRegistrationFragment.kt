package com.ibrahimf.coffeebean.userProfile.uiLayer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.ibrahimf.coffeebean.R
import com.ibrahimf.coffeebean.addProduct.uiLayer.ProductViewModel
import com.ibrahimf.coffeebean.camera.PhoneImage
import com.ibrahimf.coffeebean.databinding.FragmentUserRegistrationBinding
import com.ibrahimf.coffeebean.userProfile.model.User
import com.ibrahimf.coffeebean.util.InputTypes
import com.ibrahimf.coffeebean.util.ViewModelFactory
import com.ibrahimf.coffeebean.util.isValid
import kotlinx.android.synthetic.main.fragment_user_registration.*


class UserRegistrationFragment : Fragment() {
    private var _binding: FragmentUserRegistrationBinding? = null
    private val binding get() = _binding
    var selectedImagesForUserProfile = MutableLiveData(mutableListOf(PhoneImage()))

    private val userProfileViewModel: UserProfileViewModel by activityViewModels {
        ViewModelFactory()
    }

    private val addProductViewModel: ProductViewModel by activityViewModels {
        ViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserRegistrationBinding.inflate(inflater, container, false)
       return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userProfileViewModel.getUser()

        userProfileViewModel._user.observe(viewLifecycleOwner, {
            binding?.userNameEditText?.setText(it?.userName)
            binding?.userLocationEditText?.setText(it?.userLocation)

            Glide.with(this@UserRegistrationFragment.requireContext())
                .load(it?.userImage)
                .circleCrop()
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .into(binding?.userImage!!)

        })

        binding?.apply {
            user_image.setOnClickListener {

               findNavController().navigate(R.id.action_userRegistrationFragment_to_phoneImagesFragment)

            }

            registerBtn.setOnClickListener {

                if (!formValidationCheck()){
                    userProfileViewModel.registerUser(userUiState())
                }

                if (!selectedImagesForUserProfile.value.isNullOrEmpty()){

                    val userName = user_name_edit_text.text.toString()
                    val userLocation = user_location_edit_text.text.toString()
                    val userImage = selectedImagesForUserProfile.value?.get(0)?.imageUri?:""

                    if (userName.isNotEmpty() && userLocation.isNotEmpty() && userImage.isNotEmpty()){

                        userProfileViewModel.addUser(User(userName = userName, userLocation = userLocation, userImage = userImage))
                        findNavController().navigateUp()

                    }else{
                        Toast.makeText(this@UserRegistrationFragment.requireContext(), "Complete the fields", Toast.LENGTH_SHORT).show()
                    }

                }else{
                    Toast.makeText(this@UserRegistrationFragment.requireContext(), "Complete the fields", Toast.LENGTH_SHORT).show()

                }

            }

            userProfileViewModel.uiState.observe(viewLifecycleOwner, {

                when(it.loadingStatus){
                    LOADING_STATUS.LOADING -> {
                        showLoading()
                    }

                    LOADING_STATUS.ERROR -> {
                        showError(it.errorMsg)
                    }

                    LOADING_STATUS.DONE -> {
                        showDoneContent()
                    }
                }

            })

        }

    }

    private fun showDoneContent() {
        binding?.loadingLayout?.visibility = View.GONE
        binding?.mainLayout?.visibility = View.VISIBLE
        binding?.errorLayout?.visibility = View.GONE
    }

    private fun showError(errorMsg: String) {
        binding?.loadingLayout?.visibility = View.GONE
        binding?.mainLayout?.visibility = View.GONE
        binding?.errorLayout?.visibility = View.VISIBLE
        binding?.errorMsg?.text = errorMsg

    }

    private fun showLoading() {
        binding?.loadingLayout?.visibility = View.VISIBLE
        binding?.mainLayout?.visibility = View.GONE
        binding?.errorLayout?.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        selectedImagesForUserProfile = addProductViewModel.allSelectedImages

    }

    override fun onDestroyView() {
        super.onDestroyView()


        addProductViewModel.allSelectedImages.value = mutableListOf()
        _binding = null

    }

    private fun formValidationCheck(): Boolean {
        var isValid = true

        if (binding?.userNameTextField!!.isValid(binding!!.userNameEditText, InputTypes.NAME))
            isValid = false

        if (binding?.userLocationTextField!!.isValid(binding!!.userLocationEditText, InputTypes.NAME))
            isValid = false

        return isValid
    }

    private fun userUiState(): UserProfileUiState{
        return UserProfileUiState(
            binding?.userNameEditText?.text.toString(),
            binding?.userLocationEditText?.text.toString()
        )
    }


}