package com.smartherd.deliciouschickenfoodapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.smartherd.deliciouschickenfoodapp.R
import com.smartherd.deliciouschickenfoodapp.adapters.CategoryMealsAdapter
import com.smartherd.deliciouschickenfoodapp.databinding.ActivityCategoryMealBinding
import com.smartherd.deliciouschickenfoodapp.fargments.HomeFragment
import com.smartherd.deliciouschickenfoodapp.viewmodel.CategoryMealsViewModel

class CategoryMealActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCategoryMealBinding
    private lateinit var categoryMvvm : CategoryMealsViewModel
    private lateinit var categoryMealsAdapter: CategoryMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealBinding.inflate(layoutInflater)
        categoryMvvm = ViewModelProviders.of(this)[CategoryMealsViewModel::class.java]
        enableEdgeToEdge()

        prepareRecyclerView()
        categoryMvvm.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)

        categoryMvvm.observeMealsByCategoryLiveData().observe(this) { categoryList ->
            binding.tvCategoryCount.text = categoryList.size.toString()
            categoryMealsAdapter.setMealCategory(categoryList)
        }

        setContentView(binding.root)
    }

    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = categoryMealsAdapter
        }
    }
}