package uz.urinboydev.domproduct.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.urinboydev.domproduct.app.databinding.ItemCategoryBinding
import uz.urinboydev.domproduct.app.models.Category

class CategoryAdapter(
    private val categories: List<Category>,
    private val onCategoryClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size

    inner class CategoryViewHolder(
        private val binding: ItemCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.categoryName.text = category.name

            // Category icon based on name/slug
            val iconEmoji = when (category.slug) {
                "groceries", "oziq-ovqat" -> "🏪"
                "household", "uy-rozgor" -> "🏠"
                "womens-clothing", "ayollar-kiyimi" -> "👗"
                "mens-clothing", "erkaklar-kiyimi" -> "👔"
                "electronics" -> "📱"
                "books" -> "📚"
                else -> "📦"
            }

            binding.categoryIcon.text = iconEmoji

            // Click listener
            binding.root.setOnClickListener {
                onCategoryClick(category)
            }
        }
    }
}