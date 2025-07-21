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

            // Category icon - ID yoki nom asosida
            val iconEmoji = when {
                category.name.contains("Oziq-ovqat", ignoreCase = true) -> "🏪"
                category.name.contains("Ichimlik", ignoreCase = true) -> "🥤"
                category.name.contains("Meva", ignoreCase = true) -> "🍎"
                category.name.contains("Sabzavot", ignoreCase = true) -> "🥬"
                category.name.contains("Go'sht", ignoreCase = true) -> "🥩"
                category.name.contains("Sut", ignoreCase = true) -> "🥛"
                category.name.contains("Non", ignoreCase = true) -> "🍞"
                category.name.contains("Shirinlik", ignoreCase = true) -> "🍰"
                else -> when (category.id) {
                    1 -> "🏪"  // Oziq-ovqat mahsulotlari
                    2 -> "🥤"  // Ichimliklar
                    3 -> "🍎"  // Meva va sabzavotlar
                    4 -> "🥩"  // Go'sht va baliq
                    else -> "📦"  // Default
                }
            }

            binding.categoryIcon.text = iconEmoji

            // Click listener
            binding.root.setOnClickListener {
                onCategoryClick(category)
            }
        }
    }
}