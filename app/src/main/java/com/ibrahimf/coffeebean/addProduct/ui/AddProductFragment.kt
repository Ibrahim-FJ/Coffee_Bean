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

    //  private val addProductViewModel: AddProductViewModel by activityViewModels()

    private val addProductViewModel: AddProductViewModel by activityViewModels {
        ViewModelFactory()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductBinding.inflate(inflater, container, false)
        val adapter = PhoneImagesListAdapter(this.requireContext()) {}

        binding?.imagesRecyclerViewAddFragment?.adapter = adapter

        addProductViewModel.allSelectedImages.observe(viewLifecycleOwner) {
            it.let {
                adapter.submitList(it)
            }
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding?.addImagesLayout?.setOnClickListener {
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

    private fun getDataFromUI() {
        binding.apply {
            if (isSignedIn) {
                addProductViewModel.addProduct(
                    Product(
                        title = product_title_edit_text.text.toString(),
                        details = product_details_edit_text.text.toString(),
                        20.2
                    )
                )
            } else {
                Toast.makeText(requireContext(), "Log in to add product", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null).
        if (Firebase.auth.currentUser != null) {
            isSignedIn = true
        } else {
            println("Not")

        }


    }

    fun getImageUri(): List<Uri>{
        val imageUriList = mutableListOf<Uri>()
        for (i in addProductViewModel.allSelectedImages.value!!){
            imageUriList.add(i.imageUri)
        }
        println(imageUriList)
        return imageUriList
    }



}