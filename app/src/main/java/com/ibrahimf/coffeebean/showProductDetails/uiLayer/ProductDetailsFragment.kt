package com.ibrahimf.coffeebean.showProductDetails.uiLayer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ibrahimf.coffeebean.R
import com.ibrahimf.coffeebean.addProduct.ui.PhoneImagesListAdapter
import com.ibrahimf.coffeebean.databinding.FragmentProductDetailsBinding
import com.ibrahimf.coffeebean.userData.PhoneImage


class ProductDetailsFragment : Fragment() {
    var _binding: FragmentProductDetailsBinding? = null
    private val navigationArgs: ProductDetailsFragmentArgs by navArgs()
    var allImages = MutableLiveData<MutableList<PhoneImage>>(mutableListOf())


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return _binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding?.productTitleDetailsFragment?.text = navigationArgs.productTitle
        _binding?.productDetailsFragmentDetails?.text = navigationArgs.productDetails

        addAllImages(navigationArgs.imageList.toList())

        val adapter = PhoneImagesListAdapter(this.requireContext()) {}

        _binding?.imagesRecyclerView?.adapter = adapter

        allImages.observe(viewLifecycleOwner) {
            it.let {
                adapter.submitList(it)
            }
        }


        _binding?.reserveBtn?.setOnClickListener {
            val action = ProductDetailsFragmentDirections.actionProductDetailsFragmentToReserveOrderFragment(navigationArgs.sellerId)
            findNavController().navigate(action)
        }
    }


    fun addAllImages(imagesList: List<String>){
        imagesList.forEach {
            allImages.value?.add(PhoneImage(it))
        }
    }

}