package com.smartherd.deliciouschickenfoodapp.fargments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.smartherd.deliciouschickenfoodapp.R
import com.smartherd.deliciouschickenfoodapp.activities.CategoryMealActivity
import com.smartherd.deliciouschickenfoodapp.activities.MainActivity
import com.smartherd.deliciouschickenfoodapp.activities.MealActivity
import com.smartherd.deliciouschickenfoodapp.adapters.CategoriesAdapter
import com.smartherd.deliciouschickenfoodapp.adapters.MostPopularAdapter
import com.smartherd.deliciouschickenfoodapp.databinding.FragmentHomeBinding
import com.smartherd.deliciouschickenfoodapp.fargments.bottomsheet.MealBottomSheetFragment
import com.smartherd.deliciouschickenfoodapp.pojos.MealsByCategory
import com.smartherd.deliciouschickenfoodapp.pojos.Meal
import com.smartherd.deliciouschickenfoodapp.viewmodel.HomeViewModel


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel : HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularMealAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter


    companion object{
        const val MEAL_ID = "com.smartherd.deliciouschickenfoodapp.fargments.idMeal"
        const val MEAL_NAME = "com.smartherd.deliciouschickenfoodapp.fargments.nameMeal"
        const val MEAL_THUMB = "com.smartherd.deliciouschickenfoodapp.fargments.thumbMeal"
        const val CATEGORY_NAME = "com.smartherd.deliciouschickenfoodapp.fargments.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        popularMealAdapter = MostPopularAdapter()

    }

override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {

    binding = FragmentHomeBinding.inflate(layoutInflater)

    preparePopularMealRecyclerView()

    viewModel.getRandomMeal()
    observeRandomMeal()
    onRandomMealClick()


    viewModel.getPopularMeals()
    observePopularMealsLiveData()
    onPopularItemClicked()

    prepareCategoryRecyclerView()
    viewModel.getCategory()
    observeCategoryLivedata()
    onCategoryClick()

    onPopularItemLongClick()
    onSearchIconClick()

    return binding.root
}

    private fun onSearchIconClick() {
        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }


    private fun onPopularItemLongClick() {
        popularMealAdapter.onLongItemClick = {meal ->
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager,"Meal Info")
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = {category ->
            val intent = Intent(activity, CategoryMealActivity::class.java)
            intent.putExtra(CATEGORY_NAME,category.strCategory)
            Log.d("Category", "onCategoryClick: ${category.strCategory}")
            startActivity(intent)
        }
    }

    private fun prepareCategoryRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.recViewCategories.apply {
            layoutManager = GridLayoutManager(context,3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }

    }

    private fun observeCategoryLivedata() {
        viewModel.observeCategoryLiveData().observe(viewLifecycleOwner, Observer { categories ->
                categoriesAdapter.setCategories(categories)
        })
    }

    private fun onPopularItemClicked() {
        popularMealAdapter.onItemClick = {meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularMealRecyclerView() {
        binding.recViewMealPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularMealAdapter
        }
    }

    private fun observePopularMealsLiveData() {
        viewModel.observePopularMealLiveData().observe(viewLifecycleOwner) { mealList ->
            Log.d("HomeFragment1", mealList.toString())
                popularMealAdapter.setMeal(mealList as ArrayList<MealsByCategory>)
        }
    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            val intent = Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,randomMeal.idMeal)
            intent.putExtra(MEAL_NAME,randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB,randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observeRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner
        ) { meal ->
            Glide.with(this@HomeFragment)
                .load(meal.strMealThumb)
                .into(binding.imgRandomMeal)

            this.randomMeal = meal
        }
    }

}