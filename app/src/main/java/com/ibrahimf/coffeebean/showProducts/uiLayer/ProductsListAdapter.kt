package com.ibrahimf.coffeebean.showProducts.uiLayer

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibrahimf.coffeebean.R
import com.ibrahimf.coffeebean.addProduct.util.convertMilliSecondsToDate
import com.ibrahimf.coffeebean.databinding.ProductListItemBinding
import com.ibrahimf.coffeebean.network.models.Product

class ProductsListAdapter (private val context: Context, private val onClicked:(product: Product)-> Unit)
    : ListAdapter<Product, ProductsListAdapter.ProductsViewHolder>(
    DiffCallback
) {

    class ProductsViewHolder(var binding: ProductListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {

            binding.apply {


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
            return oldItem.imageUri == newItem.imageUri
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
        holder.binding.apply {
            if (currentProduct.imageUri[0].isNotEmpty()){
                Glide
                    .with(context)
                    .load(currentProduct.imageUri[0])
                    .placeholder(R.drawable.loading_animation)
                    .override(500, 500)
                    .centerCrop()
                    .into(productImage)

            }

            productTitle.text = currentProduct.title
            publishDate.text = convertMilliSecondsToDate(currentProduct.publishDate)

        }

        holder.binding.productItemCard.setOnClickListener {

            onClicked(currentProduct)

        }

    }

}