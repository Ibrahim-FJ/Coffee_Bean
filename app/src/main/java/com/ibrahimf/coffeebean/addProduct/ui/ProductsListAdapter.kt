package com.ibrahimf.coffeebean.addProduct.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibrahimf.coffeebean.databinding.PhoneImagesItemBinding
import com.ibrahimf.coffeebean.databinding.ProductListItemBinding
import com.ibrahimf.coffeebean.network.models.Product
import com.ibrahimf.coffeebean.userData.PhoneImage

class ProductsListAdapter (private val context: Context): ListAdapter<Product, ProductsListAdapter.ProductsViewHolder>(
    DiffCallback
) {


    class ProductsViewHolder(var binding: ProductListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {

            binding.apply {

                productTitle.text = product.title

            }

        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.title == newItem.title
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductsViewHolder {
        return ProductsViewHolder(
            ProductListItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val currentProduct = getItem(position)
        holder.bind(currentProduct)



    }

}