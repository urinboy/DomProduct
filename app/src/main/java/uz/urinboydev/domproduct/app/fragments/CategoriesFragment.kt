package uz.urinboydev.domproduct.app.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uz.urinboydev.domproduct.app.R
import uz.urinboydev.domproduct.app.adapters.CategoryAdapter
import uz.urinboydev.domproduct.app.databinding.FragmentCategoriesBinding
import uz.urinboydev.domproduct.app.utils.Resource
import uz.urinboydev.domproduct.app.viewmodel.CategoryViewModel
import javax.inject.Inject

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var categoryAdapter: CategoryAdapter // CategoryAdapter ni inject qilish

    private val categoryViewModel: CategoryViewModel by viewModels()

    companion object {
        private const val TAG = "CategoriesFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadCategories()
    }

    private fun setupRecyclerView() {
        binding.categoriesRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2) // 2 ustunli grid
            adapter = categoryAdapter
        }
    }

    private fun loadCategories() {
        binding.progressBar.visibility = View.VISIBLE
        categoryViewModel.getCategories().observe(viewLifecycleOwner) {
            it?.let {
                when (it.status) {
                    Resource.Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        it.data?.let {
                            categoryAdapter.updateData(it)
                        }
                    }
                    Resource.Status.ERROR -> {
                        binding.progressBar.visibility = View.GONE
                        showMessage("Kategoriyalarni yuklashda xato: ${it.message}")
                        Log.e(TAG, "Failed to load categories: ${it.message}")
                    }
                    Resource.Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}