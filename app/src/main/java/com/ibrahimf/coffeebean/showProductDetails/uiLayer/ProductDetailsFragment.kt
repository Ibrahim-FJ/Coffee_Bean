package com.ibrahimf.coffeebean.showProductDetails.uiLayer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ibrahimf.coffeebean.R
import com.ibrahimf.coffeebean.addProduct.uiLayer.PhoneImagesListAdapter
import com.ibrahimf.coffeebean.databinding.FragmentProductDetailsBinding
import com.ibrahimf.coffeebean.camera.PhoneImage
import kotlinx.android.synthetic.main.fragment_product_details.*


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
            if (Firebase.auth.currentUser != null){
                val action = ProductDetailsFragmentDirections.actionProductDetailsFragmentToReserveOrderFragment(navigationArgs.sellerId, navigationArgs.prodructID)
                findNavController().navigate(action)
            }else{
                Toast.makeText(this.requireContext(), "please sign in", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.firebaseRegistrationFragment)
            }

        }

        location.setOnClickListener {

            val gmmIntentUri = Uri.parse("geo:0,0?q=${navigationArgs.latitude.toDouble()},${navigationArgs.longitude.toDouble()}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            startActivity(mapIntent)

        }
    }

    fun addAllImages(imagesList: List<String>){
        allImages.value = mutableListOf()
        imagesList.forEach {
            allImages.value?.add(PhoneImage(it))
        }
    }

}