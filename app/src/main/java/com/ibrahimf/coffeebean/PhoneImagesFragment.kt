package com.ibrahimf.coffeebean

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.ibrahimf.coffeebean.adapter.PhoneImagesListAdapter
import com.ibrahimf.coffeebean.data.PhoneImage
import com.ibrahimf.coffeebean.databinding.FragmentPhoneImagesBinding

class PhoneImagesFragment : Fragment() {
    private var binding: FragmentPhoneImagesBinding? = null
    var allImages: MutableList<PhoneImage> = mutableListOf()
    var allSelectedImages: MutableList<PhoneImage> = mutableListOf()



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

        val adapter = PhoneImagesListAdapter{image ->
            binding?.button?.visibility = View.VISIBLE

            binding?.button?.setOnClickListener {
                getAllSelected()
                println(allSelectedImages)

            }

        }
        binding?.imagesRecyclerView?.adapter = adapter
        adapter.submitList(allImages)


//            allImages.observe(viewLifecycleOwner){
//                it.let {
//                    adapter.submitList(it)
//                }
//            }


    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun queryImageStorage() {
        val imageProjection = arrayOf(
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media._ID
        )
        val imageSortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
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



                    allImages.add(PhoneImage(contentUri))
                    // add the URI to the list
                    // generate the thumbnail


                }
            } ?: kotlin.run {
                Log.e("TAG", "Cursor is null!")
            }
        }

    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            this.requireActivity(), it) == PackageManager.PERMISSION_GRANTED
    }



    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PhoneImagesFragment.REQUEST_CODE_PERMISSIONS) {
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
    }
}