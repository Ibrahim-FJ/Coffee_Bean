package com.ibrahimf.coffeebean.camera

import android.Manifest
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ibrahimf.coffeebean.CameraActivity
import com.ibrahimf.coffeebean.R
import com.ibrahimf.coffeebean.addProduct.ui.PhoneImagesListAdapter
import com.ibrahimf.coffeebean.databinding.FragmentPhoneImagesBinding
import com.ibrahimf.coffeebean.addProduct.ui.AddProductViewModel

class PhoneImagesFragment : Fragment() {
    private var binding: FragmentPhoneImagesBinding? = null
    private val addProductViewModel: AddProductViewModel by activityViewModels()
    var allImages: MutableList<PhoneImage> = mutableListOf()
    var allSelectedImages: MutableList<PhoneImage> = mutableListOf()

    var isSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhoneImagesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (allPermissionsGranted()) {
            queryImageStorage()
        } else {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        val adapter = PhoneImagesListAdapter(this.requireContext()) { image ->
            isSelected = true
//            binding?.saveSelectedImages?.visibility = View.VISIBLE
//            binding?.saveSelectedImages?.setOnClickListener {
//                getAllSelected()
//
//            }

        }


        binding?.imagesRecyclerView?.adapter = adapter

            addProductViewModel.allImages.observe(viewLifecycleOwner){
                it.let {
                    adapter.submitList(it)
                }
            }

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.selected_images_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.selected_images -> {
                getAllSelected()
                findNavController().navigateUp()
            }
            R.id.open_camera -> {
                startActivity(Intent(this.requireActivity(), CameraActivity::class.java))
                // findNavController().navigate(R.id.action_phoneImagesFragment_to_cameraFragment)
            }

        }

        return true
    }


    override fun onPrepareOptionsMenu(menu: Menu) {

        menu.findItem(R.id.selected_images)?.isVisible = true

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun queryImageStorage() {
        val imageProjection = arrayOf(
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media._ID
        )
        val imageSortOrder = "${MediaStore.Images.Media.DATE_TAKEN} ASC"
        val cursor = this.requireActivity().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            imageProjection,
            null,
            null,
            imageSortOrder
        )

        cursor.use {
            it?.let {
                val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val nameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
                val dateColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
                while (it.moveToNext()) {
                    val id = it.getLong(idColumn)
                    val name = it.getString(nameColumn)
                    val size = it.getString(sizeColumn)
                    val date = it.getString(dateColumn)
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                    allImages.add(PhoneImage(contentUri.toString()))


                   // imagesViewModel.allImages.value?.add(PhoneImage(contentUri))
                  //  imagesViewModel.setAllImages(PhoneImage(contentUri))
                 //   println(imagesViewModel.getAllImages()?.size)
                    // add the URI to the list
                    // generate the thumbnail

                }
            } ?: kotlin.run {
                Log.e("TAG", "Cursor is null!")
            }
        }

        addProductViewModel.allImages.value = allImages

    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            this.requireActivity(), it) == PackageManager.PERMISSION_GRANTED
    }



    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    queryImageStorage()
                }
            } else {
                Toast.makeText(this.requireActivity(),
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                this.requireActivity().finish()
            }
        }
    }


    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    fun getAllSelected(){
        for (i in allImages){
            if (i.isSelected){
                allSelectedImages.add(i)
            }
        }

        addProductViewModel.allSelectedImages.value = allSelectedImages
    }
}