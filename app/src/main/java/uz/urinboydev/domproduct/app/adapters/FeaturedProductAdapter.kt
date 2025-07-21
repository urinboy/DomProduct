package uz.urinboydev.domproduct.app.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.urinboydev.domproduct.app.R
import uz.urinboydev.domproduct.app.constants.ApiConstants
import uz.urinboydev.domproduct.app.databinding.ItemFeaturedProductBinding
import uz.urinboydev.domproduct.app.fragments.ProductAction
import uz.urinboydev.domproduct.app.models.Product
import java.text.NumberFormat
import java.util.*

class FeaturedProductAdapter(
    private val products: List<Product>,
    private val onProductAction: (Product, ProductAction) -> Unit
) : RecyclerView.Adapter<FeaturedProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemFeaturedProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    inner class ProductViewHolder(
        private val binding: ItemFeaturedProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            // Product name
            binding.productName.text = product.name

            // Product price
            val currentPrice = product.getCurrentPrice()
            binding.productPrice.text = formatPrice(currentPrice)

            // Original price (if on sale)
            if (product.hasDiscount()) {
                binding.originalPrice.visibility = View.VISIBLE
                binding.originalPrice.text = formatPrice(product.price)
                binding.originalPrice.paintFlags = binding.originalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

                // Discount badge
                binding.discountBadge.visibility = View.VISIBLE
                binding.discountBadge.text = "-${product.getDiscountPercentage()}%"
            } else {
                binding.originalPrice.visibility = View.GONE
                binding.discountBadge.visibility = View.GONE
            }

            // Product image
            loadProductImage(product.image)

            // Rating - NULL SAFETY FIX
            val avgRating = product.averageRating ?: 0.0
            val reviewCount = product.reviewsCount ?: 0

            if (avgRating > 0) {
                binding.productRating.visibility = View.VISIBLE
                binding.productRating.text = "$avgRating ⭐ ($reviewCount)"
            } else {
                binding.productRating.visibility = View.GONE
            }

            // Stock status
            if (!product.isInStock()) {
                binding.addToCartButton.text = "Tugagan"
                binding.addToCartButton.isEnabled = false
                binding.addToCartButton.alpha = 0.5f
            } else {
                binding.addToCartButton.text = "Savatga"
                binding.addToCartButton.isEnabled = true
                binding.addToCartButton.alpha = 1.0f
            }

            // Click listeners
            binding.root.setOnClickListener {
                onProductAction(product, ProductAction.CLICK)
            }

            binding.addToCartButton.setOnClickListener {
                if (product.isInStock()) {
                    onProductAction(product, ProductAction.ADD_TO_CART)
                    showAddedFeedback()
                }
            }
        }

        private fun loadProductImage(imageUrl: String?) {
            try {
                when {
                    !imageUrl.isNullOrEmpty() -> {
                        // To'liq URL yoki nisbiy URL ekanligini tekshirish
                        val fullUrl = if (imageUrl.startsWith("http")) {
                            imageUrl
                        } else {
                            "${ApiConstants.BASE_URL}$imageUrl"
                        }

                        Glide.with(binding.productImage.context)
                            .load(fullUrl)
                            .placeholder(R.drawable.placeholder_product)
                            .error(R.drawable.placeholder_product)
                            .centerCrop()
                            .into(binding.productImage)
                    }
                    else -> {
                        binding.productImage.setImageResource(R.drawable.placeholder_product)
                    }
                }
            } catch (e: Exception) {
                binding.productImage.setImageResource(R.drawable.placeholder_product)
            }
        }

        private fun formatPrice(price: Double): String {
            val formatter = NumberFormat.getNumberInstance(Locale("uz", "UZ"))
            return "${formatter.format(price.toInt())} SO'M"
        }

        private fun showAddedFeedback() {
            // Show visual feedback that item was added
            val originalText = binding.addToCartButton.text
            binding.addToCartButton.text = "Qo'shildi ✓"
            binding.addToCartButton.setBackgroundColor(binding.root.context.getColor(R.color.success_color))

            // Reset after 1 second
            binding.addToCartButton.postDelayed({
                binding.addToCartButton.text = originalText
                binding.addToCartButton.setBackgroundResource(R.drawable.button_small_primary)
            }, 1000)
        }
    }
}