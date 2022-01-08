package com.ibrahimf.coffeebean.userProfile.uiLayer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ibrahimf.coffeebean.R
import com.ibrahimf.coffeebean.addProduct.ui.AddProductViewModel
import com.ibrahimf.coffeebean.addProduct.ui.PhoneImagesListAdapter
import com.ibrahimf.coffeebean.addProduct.ui.ViewModelFactory
import com.ibrahimf.coffeebean.databinding.FragmentEditProductBinding
import com.ibrahimf.coffeebean.network.models.Product
import com.ibrahimf.coffeebean.camera.PhoneImage
import kotlinx.android.synthetic.main.fragment_add_product.*


class EditProductFragment : Fragment() {
    private var binding: FragmentEditProductBinding? = null
    private val navigationArgs: EditProductFragmentArgs by  navArgs()
    var allImages = MutableLiveData<MutableList<PhoneImage>>(mutableListOf())

    private val userProfileViewModel: UserProfileViewModel? by activityViewModels {
        ViewModelFactory()
    }

    private val addProductViewModel: AddProductViewModel? by activityViewModels {
        ViewModelFactory()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProductBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        addAllImages(navigationArgs.imagesList.toList())
        bindToUI()

    }

    fun bindToUI(){
        val adapter = PhoneImagesListAdapter(this.requireContext()) {}

        binding?.imagesRecyclerViewEditFragment?.adapter = adapter

        binding?.productTitleEditText?.setText(navigationArgs.productTitle)
        binding?.productDetailsEditText?.setText(navigationArgs.productDetails)

       // Log.e("TAG", "bindToUIddd: ${navigationArgs.imagesList.toList()}", )


        addProductViewModel?.allSelectedImages?.observe(viewLifecycleOwner) {
            it.let {
                Log.e("TAG", "bindToUI: $it", )
                adapter.submitList(it)
            }
        }

        binding?.editProductButton?.setOnClickListener {

            binding.apply {

                val productTitle = product_title_edit_text.text.toString()
                val productDetails = product_details_edit_text.text.toString()
                if (productTitle.isNotEmpty() && productDetails.isNotEmpty() && addProductViewModel?.allSelectedImages?.value?.isNotEmpty() == true){
                    userProfileViewModel?.updateProduct(
                        Product(
                            title = productTitle,
                            details = productDetails,
                            imageUri = getImageUri(),
                            location = 20.25
                        )
                    )
                    findNavController().navigate(R.id.action_addProductFragment_to_productListFragment)
                    addProductViewModel?.allSelectedImages?.value?.clear()

                }else{
                    product_title_edit_text.error = "Enter the product title"
                    product_details_edit_text.error = "Enter the product details"
                    Toast.makeText(requireContext(), "Add image", Toast.LENGTH_SHORT).show()
                }
            }

            userProfileViewModel?.updateProduct(Product())
            Toast.makeText(this.requireContext(), "edit", Toast.LENGTH_SHORT).show()
        }


        binding?.imagesLayout?.setOnClickListener {
            findNavController().navigate(R.id.action_editProductFragment_to_cameraFragment)

        }
    }

    fun addAllImages(imagesList: List<String>){
        Log.e("TAG", "all Imagesgggggggggg: ${imagesList[0]}")

        imagesList.forEach {
            addProductViewModel?.allSelectedImages?.value?.add(PhoneImage(it))
            Log.e("TAG", "all Images: ${addProductViewModel?.allSelectedImages?.value}")

        }
    }

    fun getImageUri(): List<String>{
        val imageUriList = mutableListOf<String>()
        for (i in addProductViewModel?.allSelectedImages?.value!!){
            imageUriList.add(i.imageUri)
        }
        return imageUriList
    }

}