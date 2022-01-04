package com.ibrahimf.coffeebean.addProduct.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibrahimf.coffeebean.userData.PhoneImage
import com.ibrahimf.coffeebean.databinding.PhoneImagesItemBinding

class PhoneImagesListAdapter (private val context: Context, private val onItemClicked: (phoneImage: PhoneImage) -> Unit): ListAdapter<PhoneImage, PhoneImagesListAdapter.ImagesViewHolder>(
    DiffCallback
) {

    class ImagesViewHolder(var binding: PhoneImagesItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(phoneImage: PhoneImage) {


            binding.apply {

            //    imageView.setImageURI(phoneImage.imageUri)

            }

        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<PhoneImage>() {
        override fun areItemsTheSame(oldItem: PhoneImage, newItem: PhoneImage): Boolean {
            return oldItem.imageUri == newItem.imageUri
        }

        override fun areContentsTheSame(oldItem: PhoneImage, newItem: PhoneImage): Boolean {
            return oldItem.imageUri == newItem.imageUri
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImagesViewHolder {
        return ImagesViewHolder(
            PhoneImagesItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        val currentImage = getItem(position)
        holder.bind(currentImage)
        holder.binding.apply {
            imageCard.setOnLongClickListener {
                imageCard.isChecked = !imageCard.isChecked
                if (imageCard.isChecked) {
                    currentImage.isSelected = true
                    onItemClicked(currentImage)
                } else {
                    currentImage.isSelected = false
                }
                true
            }

            Glide.with(context)
                .load(currentImage.imageUri)
                .centerCrop()
                .into(imageView)


        }



    }

}