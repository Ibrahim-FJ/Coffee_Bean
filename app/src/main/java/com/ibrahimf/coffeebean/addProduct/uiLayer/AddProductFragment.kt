package com.ibrahimf.coffeebean.addProduct.uiLayer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
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
import com.ibrahimf.coffeebean.util.InputTypes
import com.ibrahimf.coffeebean.util.ViewModelFactory
import com.ibrahimf.coffeebean.util.isValid
import kotlinx.android.synthetic.main.fragment_add_product.*
import java.util.*


class AddProductFragment : Fragment(), OnMapReadyCallback {
    private var binding: FragmentAddProductBinding? = null
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

            Log.e("TAG", "onViewCreated:nn  ${formValidationCheck()}", )
            if (formValidationCheck()){
                Log.e("TAG", "onViewCreated:kkkk ", )
                addProductViewModel.addProduct(getDataFromUI())
                findNavController().navigate(R.id.action_addProductFragment_to_productListFragment)
                addProductViewModel.allSelectedImages.value?.clear()
            }
        }

        if (allSelectedImages.value?.isNotEmpty() == true){
            binding?.imagesTextview?.visibility = View.GONE
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
    private fun getDataFromUI(): Product {

        return Product(
            title = binding?.productTitleEditText?.text.toString(),
            details = binding?.productTitleEditText?.text.toString(),
            imageUri = getImageUri(),
            location = GeoPoint(latitude, longitude)
        )

    }// end.......



    private fun formValidationCheck(): Boolean {
        var allImages = mutableListOf<PhoneImage>()
        Log.e("TAG", "formValidationChe", )


        addProductViewModel.allSelectedImages.observe(viewLifecycleOwner, {
            Log.e("TAG", "formValidationCheck: $it", )
            allImages = it
        })
        var isValid = true

        if (binding?.productTitleEditText?.text?.isEmpty() == true){
            binding?.productTitleTextField?.error = this.requireContext().getString(R.string.not_empty_field)
            isValid = false

        }else{
            binding?.productTitleTextField?.error = null
        }

        if (binding?.productDetailsEditText?.text?.isEmpty() == true){
            binding?.productDetailsTextField?.error = this.requireContext().getString(R.string.not_empty_field)
            isValid = false

        }else{
            binding?.productDetailsTextField?.error = null
        }

        if (allImages.isNullOrEmpty()){
            Toast.makeText(this.requireContext(), "choose image", Toast.LENGTH_SHORT).show()
            isValid = false
        }
        if (latitude == 0.0 || longitude == 0.0){
            Toast.makeText(this.requireContext(), "add your location", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
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