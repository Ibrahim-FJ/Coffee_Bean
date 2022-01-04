package com.ibrahimf.coffeebean.addProduct.ui

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ibrahimf.coffeebean.R
import com.ibrahimf.coffeebean.databinding.FragmentAddProductBinding
import com.ibrahimf.coffeebean.network.models.Product
import kotlinx.android.synthetic.main.fragment_add_product.*


class AddProductFragment : Fragment() {
    private var binding: FragmentAddProductBinding? = null
    var isSignedIn = false

    private val addProductViewModel: AddProductViewModel by activityViewModels {
        ViewModelFactory()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding?.imagesLayout?.setOnClickListener {
            // startActivity(Intent(this.requireContext(), CameraActivity::class.java))
            findNavController().navigate(R.id.action_addProductFragment_to_cameraFragment2)

        }


        val adapter = PhoneImagesListAdapter(this.requireContext()) {}
        binding?.imagesRecyclerViewAddFragment?.adapter = adapter
        addProductViewModel.allSelectedImages.observe(viewLifecycleOwner) {
            it.let {
                adapter.submitList(it)
            }
        }

        binding?.addProductButton?.setOnClickListener {
           getDataFromUI()
        }

    }

    // retrieve the data from the UI elements
    private fun getDataFromUI() {
        binding.apply {
            if (isSignedIn) {
                val productTitle = product_title_edit_text.text.toString()
                val productDetails = product_details_edit_text.text.toString()
                if (productTitle.isNotEmpty() && productDetails.isNotEmpty() && addProductViewModel.allSelectedImages.value?.isNotEmpty() == true){
                    addProductViewModel.addProduct(
                        Product(
                            title = product_title_edit_text.text.toString(),
                            details = product_details_edit_text.text.toString(),
                            imageUri = getImageUri(),
                            20.2
                        )
                    )
                    product_title_edit_text.error = null
                    product_details_edit_text.error = null
                    findNavController().navigate(R.id.action_addProductFragment_to_productListFragment)
                    addProductViewModel.allSelectedImages.value?.clear()

                }else{
                  product_title_edit_text.error = "Enter the product title"
                  product_details_edit_text.error = "Enter the product details"
                  Toast.makeText(requireContext(), "Add image", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(requireContext(), "Log in to add product", Toast.LENGTH_SHORT).show()
            }
        }
    }// end.......

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null).
        if (Firebase.auth.currentUser != null) {
            isSignedIn = true
        }

    }


    // function to get image Uri from the list in the addProductViewModel
    fun getImageUri(): List<String>{
        val imageUriList = mutableListOf<String>()
        for (i in addProductViewModel.allSelectedImages.value!!){
            imageUriList.add(i.imageUri)
        }
        return imageUriList
    }

}