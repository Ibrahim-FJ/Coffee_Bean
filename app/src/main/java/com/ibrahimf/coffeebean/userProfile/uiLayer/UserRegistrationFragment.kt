package com.ibrahimf.coffeebean.userProfile.uiLayer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.ibrahimf.coffeebean.CameraActivity
import com.ibrahimf.coffeebean.R
import com.ibrahimf.coffeebean.addProduct.ui.AddProductViewModel
import com.ibrahimf.coffeebean.addProduct.ui.ViewModelFactory
import com.ibrahimf.coffeebean.camera.PhoneImage
import com.ibrahimf.coffeebean.databinding.FragmentUserRegistrationBinding
import com.ibrahimf.coffeebean.userProfile.model.User
import kotlinx.android.synthetic.main.fragment_user_registration.*


class UserRegistrationFragment : Fragment() {
    private var binding: FragmentUserRegistrationBinding? = null
    var selectedImagesForUserProfile = MutableLiveData(mutableListOf(PhoneImage()))

    private val userProfileViewModel: UserProfileViewModel by activityViewModels {
        ViewModelFactory()
    }

    private val addProductViewModel: AddProductViewModel by activityViewModels {
        ViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserRegistrationBinding.inflate(inflater, container, false)
       return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

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

        }

        Log.e("TAG", "onViewCreated: image = ${addProductViewModel.allSelectedImages.value}")

        if (!selectedImagesForUserProfile.value.isNullOrEmpty()){
            Glide.with(this.requireContext())
                .load(selectedImagesForUserProfile.value?.get(0)?.imageUri)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_account_circle_24)
                .into(binding?.userImage!!)
        }

    }

    override fun onResume() {
        super.onResume()
        selectedImagesForUserProfile = addProductViewModel.allSelectedImages


    }

    override fun onDestroyView() {
        super.onDestroyView()

        addProductViewModel.allSelectedImages.value = mutableListOf()

    }
}