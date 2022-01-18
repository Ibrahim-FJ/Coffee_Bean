package com.ibrahimf.coffeebean.addProduct.uiLayer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.ktx.Firebase
import com.ibrahimf.coffeebean.R
import com.ibrahimf.coffeebean.camera.PhoneImage
import com.ibrahimf.coffeebean.databinding.FragmentAddProductBinding
import com.ibrahimf.coffeebean.network.models.Product
import com.ibrahimf.coffeebean.util.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_add_product.*
import java.util.*


class AddProductFragment : Fragment(), OnMapReadyCallback {
    private var binding: FragmentAddProductBinding? = null
    var isSignedIn = false
    private val addProductViewModel: ProductViewModel by activityViewModels {
        ViewModelFactory()
    }
    private var latitude = 0.0
    private var longitude = 0.0

    var allSelectedImages = MutableLiveData<MutableList<PhoneImage>>()
    private var map: GoogleMap? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        val mapFragment = childFragmentManager
            .findFragmentById(R.id.myMap) as SupportMapFragment
        mapFragment.getMapAsync(this)


        allSelectedImages = addProductViewModel.allSelectedImages

        binding?.imagesLayout?.setOnClickListener {
        //     startActivity(Intent(this.requireActivity(), PhoneImagesActivity::class.java))
            findNavController().navigate(R.id.action_addProductFragment_to_phoneImagesFragment)

        }

        val adapter = PhoneImagesListAdapter(this.requireContext()) {}
        binding?.imagesRecyclerViewAddFragment?.adapter = adapter
        allSelectedImages.observe(viewLifecycleOwner) {
            it.let {
                adapter.submitList(it)
            }
        }

        binding?.addProductButton?.setOnClickListener {
            getDataFromUI()
        }


        if (allPermissionsGranted()) {
            map?.isMyLocationEnabled = true

        } else {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding?.myMap?.setOnClickListener {

            if (allPermissionsGranted()) {
                map?.isMyLocationEnabled = true

            } else {
                ActivityCompat.requestPermissions(
                    this.requireActivity(),
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            }

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
                            title = productTitle,
                            details = productDetails,
                            imageUri = getImageUri(),
                            location = GeoPoint(latitude, longitude)
                        )
                    )
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


    override fun onDestroyView() {
        super.onDestroyView()
        addProductViewModel.allSelectedImages.value = mutableListOf()
    }

    override fun onMapReady(googleMap: GoogleMap) {
       // googleMap.setOnMyLocationButtonClickListener(this)
        map = googleMap

        if (allPermissionsGranted()) {
            map?.isMyLocationEnabled = true

        } else {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        map?.addMarker(MarkerOptions().position(LatLng(24.852195517424427, 46.71341959387064)))
        map?.setOnMapLongClickListener {
            map?.clear()
            map?.addMarker(
                MarkerOptions()
                    .position(it)
            )

            latitude = it.latitude
            longitude = it.longitude

        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            this.requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                map?.isMyLocationEnabled = true

            } else {
                Toast.makeText(
                    this.requireContext(),
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                this.requireActivity().finish()
            }
        }
    }


    companion object {

        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    }



}