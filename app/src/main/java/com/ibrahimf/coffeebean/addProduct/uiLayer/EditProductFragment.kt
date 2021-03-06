package com.ibrahimf.coffeebean.addProduct.uiLayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ibrahimf.coffeebean.databinding.FragmentEditProductBinding
import com.ibrahimf.coffeebean.camera.PhoneImage
import com.ibrahimf.coffeebean.userProfile.uiLayer.UserProfileViewModel
import com.ibrahimf.coffeebean.util.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_add_product.*


class EditProductFragment : Fragment() {
    private var binding: FragmentEditProductBinding? = null
    private val navigationArgs: EditProductFragmentArgs by  navArgs()
    var allImages = MutableLiveData<MutableList<PhoneImage>>(mutableListOf())

    private val productsViewModel: ProductViewModel? by activityViewModels {
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
        binding?.productTitle?.text = navigationArgs.productTitle
        binding?.productDetails?.text = navigationArgs.productDetails

        addAllImages(navigationArgs.imagesList.toList())

        val adapter = PhoneImagesListAdapter(this.requireContext()) {}

        binding?.imagesRecyclerView?.adapter = adapter

        allImages.observe(viewLifecycleOwner) {
            it.let {
                adapter.submitList(it)
            }
        }

        binding?.deletePostBtn?.setOnClickListener {
            productsViewModel?.deletePost(navigationArgs.productID)
            findNavController().navigateUp()
        }

    }

    fun addAllImages(imagesList: List<String>){
        imagesList.forEach {
            allImages.value?.add(PhoneImage(it))
        }
    }


}